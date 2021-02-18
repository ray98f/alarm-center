package com.zte.msg.alarmcenter.core.pusher;

import com.alibaba.fastjson.JSONObject;
import com.zte.msg.alarmcenter.core.pusher.base.BasePusher;
import com.zte.msg.alarmcenter.core.pusher.base.Config;
import com.zte.msg.alarmcenter.core.pusher.base.Message;
import com.zte.msg.alarmcenter.core.pusher.msg.SmsMessage;
import com.zte.msg.alarmcenter.enums.ErrorCode;
import com.zte.msg.alarmcenter.enums.PushMethods;
import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.utils.Constants;
import com.zte.msg.alarmcenter.utils.PatternUtils;
import com.zte.msg.pushcenter.pcscript.ParamConstants;
import com.zte.msg.pushcenter.pcscript.PcScript;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2020/12/11 9:14
 */
@Service
@Slf4j
public class SmsPusher extends BasePusher {

    @Resource
    private AppService appService;

    @Resource
    private SmsTemplateRelationMapper smsTemplateRelationMapper;

    @Override
    public void init() {
        configMap.put(PushMethods.SMS, new HashMap<>(16));
        List<SmsConfigModel> configDetails = smsTemplateRelationMapper.selectAllSmsConfigForInit();
        buildAndFlush(configDetails);
    }

    private void buildAndFlush(List<SmsConfigModel> smsConfigModels) {
        smsConfigModels.forEach(o -> {
            SmsConfig smsConfig = new SmsConfig();
            BeanUtils.copyProperties(o, smsConfig);
            smsConfig.setParams(PatternUtils.getParams(smsConfig.getContent()));
            smsConfig.setConfig(JSONObject.parseObject(o.getConfig()));
            flushConfig(smsConfig);
            log.info(smsConfig.toString());
        });
        log.info("==========update sms config completed, update count {} ==========", smsConfigModels.size());
    }

    private void flushConfig(SmsConfig o) {
        TreeMap<Integer, Config> treeMap = super.getConfig(PushMethods.SMS).get(o.getSmsTemplateId());
        if (Objects.isNull(treeMap)) {
            treeMap = new TreeMap<>();
        }
        treeMap.put(o.getPriority(), o);
        super.getConfig(PushMethods.SMS).put(o.getSmsTemplateId(), treeMap);
    }

    @Override
    public void push(Message message) {
        SmsMessage smsMessage = (SmsMessage) message;
        SmsConfig smsConfig = getConfig(smsMessage.getTemplateId());
        smsMessage.build(smsConfig);
        String[] varsKeySet = smsMessage.getVars().keySet().toArray(new String[]{});
        if (varsKeySet.length != smsConfig.getParams().size()) {
            log.error("模版: {} 参数列表数量不匹配，短信发送失败..", smsMessage.getTemplateId());
            throw new CommonException(ErrorCode.SMS_TEMPLATE_PARAMS_NOT_MATCH);
        }
        for (int i = 0; i < smsConfig.getParams().size(); i++) {
            smsMessage.getVars().put(smsConfig.getParams().get(i), smsMessage.getVars().remove(varsKeySet[i]));
        }
        Map<String, Object> paramMap = getParamMap(smsConfig, smsMessage, smsConfig.getConfig().getInnerMap());
        Class<?> scriptClass = scriptManager.getScriptClass(smsConfig.getScriptTag());
        List<PcScript.Res> resList = new ArrayList<>(smsMessage.getPhoneNum().length);
        CompletableFuture.supplyAsync(() -> {
            for (int i = 0; i < smsMessage.getPhoneNum().length; i++) {
                smsMessage.setIndex(i);
                String phoneNum = smsMessage.getPhoneNum()[i];
                paramMap.put(ParamConstants.PHONE_NUM, phoneNum);
                PcScript.Res res = null;
                try {
                    log.info("success find script class : name {}, obj :{}", smsConfig.getScriptTag(), scriptClass.getName());
                    Method execute = scriptClass.getMethod("execute", Map.class);
                    Object o = scriptClass.newInstance();
                    res = (PcScript.Res) execute.invoke(o, paramMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                resList.add(res);
                persist(smsMessage, res);
            }
            return resList;
        }, pushExecutor).exceptionally(e -> {
            log.error("Error while send a sms message: {}", e.getMessage());
            e.printStackTrace();
            resList.add(new PcScript.Res(-1, e.getMessage()));
            return resList;
        }).thenAcceptAsync(list -> list.forEach(o -> {
            if (o.getCode() != Constants.SUCCESS && message.getIsWarn()) {
                warn();
            }
            if (message.getIsCallBack()) {
                response(message, o);
            }
        }), resExecutor);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    protected void persist(Message message, PcScript.Res res) {
        try {
            SmsMessage smsMessage = (SmsMessage) message;
            smsMessage.setDelay(getDelay(message));
            smsMessage.setAppName(appService.getAppName(smsMessage.getAppId()));
            SmsInfo smsInfo = new SmsInfo(smsMessage, res);
            historyService.addHistorySms(smsInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void flushConfig(Provider provider) {
        flushConfig(provider, false);
    }

    public void flushConfig(Provider provider, boolean remove) {
        flushConfig(Collections.singletonList(provider), remove);
    }

    public void flushConfig(List<Provider> providers) {
        flushConfig(providers, false);
    }

    public void flushConfig(List<Provider> providers, boolean remove) {
        List<SmsConfigModel> smsConfigForFlush = smsTemplateRelationMapper.selectSmsConfigForFlushByProviderIds(providers
                .stream().map(Provider::getId).collect(Collectors.toList()));
        if (!remove) {
            buildAndFlush(smsConfigForFlush);
        } else {
            removeConfig(smsConfigForFlush.stream()
                    .map(SmsConfigModel::getSmsTemplateId)
                    .toArray(Long[]::new));
            log.info("========== delete sms config completed, delete count: {} ==========", providers.size());
        }
    }

    public void flushConfig(Long templateId) {
        flushConfig(templateId, false);
    }

    public void flushConfig(Long templateId, boolean remove) {
        flushConfig(new Long[]{templateId}, remove);
    }

    public void flushConfig(Long[] templateIds) {
        flushConfig(templateIds, false);
    }

    public void flushConfig(Long[] templateIds, boolean remove) {
        if (!remove) {
            List<SmsConfigModel> smsConfigForFlush = smsTemplateRelationMapper.selectSmsConfigForFlush(Arrays.asList(templateIds));
            buildAndFlush(smsConfigForFlush);
        } else {
            removeConfig(templateIds);
        }
    }

    private void removeConfig(Long[] templateIds) {
        for (Long templateId : templateIds) {
            configMap.get(PushMethods.SMS).remove(templateId);
        }
    }

    @Data
    public static class SmsConfig implements Config {

        private long smsTemplateId;

        private long providerTemplateId;

        private int priority;

        private int templateStatus;

        private String code;

        private String sign;

        private String content;

        private List<String> params;

        private int platformTemplateStatus;

        private String providerName;

        private String scriptTag;

        private JSONObject config;

    }

    public SmsConfig getConfig(Long templateId) {
        return (SmsConfig) super.getConfig(PushMethods.SMS).get(templateId).lastEntry().getValue();
    }

}

