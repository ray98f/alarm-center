package com.zte.msg.alarmcenter.core.warn;

import com.zte.msg.alarmcenter.core.pusher.SmsPusher;
import com.zte.msg.alarmcenter.core.pusher.msg.MailMessage;
import com.zte.msg.alarmcenter.core.pusher.msg.SmsMessage;
import com.zte.msg.alarmcenter.core.pusher.msg.WeChatMessage;
import com.zte.msg.alarmcenter.utils.Constants;
import com.zte.msg.alarmcenter.utils.DateUtils;
import com.zte.msg.alarmcenter.utils.PatternUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.Instant;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/1/20 10:37
 */
@Service
@Slf4j
public class WarnHandler {

    @Resource(name = "asyncWarnExecutor")
    private ThreadPoolTaskExecutor warnExecutor;

    private static final int VAR_COUNT = 3;

    @Resource
    private EarlyWarnService earlyWarnService;

    @Resource
    private EarlyWarnInfoService earlyWarnInfoService;

    @Resource
    private UserService userService;

    private EarlyWarnConfigModel warnConfig = null;

    private long statTime;

    private long endTime;

    private int warnCount;

    @PostConstruct
    public void init() {
        EarlyWarnConfig earlyWarnConfig = earlyWarnService.selectEarlyWarnConfig();
        if (Objects.isNull(earlyWarnConfig)) {
            return;
        }
        List<Long> userIds = Arrays.stream(earlyWarnConfig.getUserIds()
                .split(Constants.COMMA_EN)).map(Long::parseLong).collect(Collectors.toList());
        warnConfig = new EarlyWarnConfigModel();
        BeanUtils.copyProperties(earlyWarnConfig, warnConfig);
        warnConfig.setUsers(userService.listUser(userIds));
        warnConfig.setAlarmCycle(earlyWarnConfig.getAlarmCycle() * Constants.PER_MINUTE_MILLS);
        warnConfig.setAlarmInterval(earlyWarnConfig.getAlarmInterval() * Constants.PER_MINUTE_MILLS);
        statTime = Instant.now().getMillis();
        endTime = earlyWarnConfig.getAlarmCycle() + statTime;
        warnCount = 0;
    }

    private long lastWarnTime = 0;

    @Resource
    private PushCenterService pushCenterService;

    public void doWarn(Instant now, Reference reference) {
        pushSms(reference);
        pushMail();
        pushWechat();
        persist(now);
    }

    private void pushWechat() {
        String openIds = warnConfig.getOpenIds();
        if (StringUtils.isBlank(warnConfig.getOpenIds()) || Objects.isNull(warnConfig.getWechatProviderId())) {
            return;
        }
        for (String openId : openIds.split(Constants.COMMA_EN)) {
            WeChatMessageReqDTO reqDTO = new WeChatMessageReqDTO();
            reqDTO.setOpenId(openId);
            reqDTO.setProviderId(warnConfig.getWechatProviderId());
            reqDTO.setTemplateId(warnConfig.getWechatTemplateId());
            reqDTO.setData(warnConfig.getWechatData());
            reqDTO.setMessageId(UUID.randomUUID().toString());
            reqDTO.setAppId(warnConfig.getAppId());
            reqDTO.setIsCallBack(false);

            WeChatMessage weChatMessage = new WeChatMessage().build(reqDTO);

            weChatMessage.setIsWarn(true);
            pushCenterService.pushWechat(weChatMessage);
        }
    }

    private void pushMail() {
        if (StringUtils.isAllBlank(warnConfig.getMailBody(), warnConfig.getMailTitle())) {
            return;
        }
        MailMessageReqDTO reqDTO = new MailMessageReqDTO();
        reqDTO.setTo(warnConfig.getUsers().stream().map(User::getMail).toArray(String[]::new));
        reqDTO.setSubject(warnConfig.getMailTitle());
        reqDTO.setProviderId(warnConfig.getMailProviderId());
        reqDTO.setContent(warnConfig.getMailBody());
        reqDTO.setMessageId(UUID.randomUUID().toString());
        reqDTO.setAppId(warnConfig.getAppId());
        reqDTO.setIsCallBack(false);

        MailMessage message = new MailMessage().build(reqDTO);

        message.setIsWarn(true);
        pushCenterService.pushMail(message);
    }

    private void pushSms(Reference reference) {
        String[] phoneNums = warnConfig.getUsers().stream().map(User::getPhone).toArray(String[]::new);
        Map<String, String> var = new HashMap<>(8);
        SmsPusher.SmsConfig smsConfig = pushCenterService.getSmsConfig(warnConfig.getSmsTemplateId());
        List<String> params = smsConfig.getParams();
        if (params.size() < VAR_COUNT) {
            log.error("预警配置模板参数数量与规定不符");
        }
        var.put(params.get(0), DateUtils.formatDate(new Date(reference.getStatTime())));
        var.put(params.get(1), DateUtils.formatDate(new Date(reference.getEndTime())));
        var.put(params.get(2), String.valueOf(reference.getWarnCount()));

        SmsMessageReqDTO reqDTO = new SmsMessageReqDTO();
        reqDTO.setVars(var);
        reqDTO.setPhoneNum(phoneNums);
        reqDTO.setTemplateId(warnConfig.getSmsTemplateId());
        reqDTO.setMessageId(UUID.randomUUID().toString());
        reqDTO.setAppId(warnConfig.getAppId());
        reqDTO.setIsCallBack(false);
        SmsMessage smsMessage = new SmsMessage().build(reqDTO);

        smsMessage.setIsWarn(true);

        pushCenterService.pushSms(smsMessage);
    }

    public synchronized void warnMatch(Instant now) {
        long nowMillis = now.getMillis();
        while (nowMillis > endTime) {
            statTime = endTime;
            endTime += warnConfig.getAlarmCycle();
        }
        warnCount++;
        if (warnCount >= warnConfig.getThreshold() &&
                (nowMillis - lastWarnTime) > warnConfig.getAlarmInterval()) {
            CompletableFuture.supplyAsync(() -> {
                Reference reference = new Reference(statTime, endTime, warnCount);
                doWarn(now, reference);
                return null;
            }, warnExecutor).thenAccept(o -> log.info("finish a warn task !"));

            lastWarnTime = nowMillis;
            warnCount = 0;
        }
    }

    public void submitWarn(Instant now) {
        if (Objects.isNull(warnConfig)) {
            log.warn("Cannot set up an invalid early-warn config ! Early-warn will be cancelled !");
            return;
        }
        log.info("Receive a push fail warn ....");
        warnMatch(now);
    }

    public void updateWarnConfig() {
        init();
    }

    private void persist(Instant now) {
        EarlyWarnInfo earlyWarnInfo = new EarlyWarnInfo();
        earlyWarnInfo.setTime(new Timestamp(now.getMillis()));
        earlyWarnInfo.setReason(String.format("消息平台%s分钟内，推送失败次数超过%s次", warnConfig.getAlarmCycle() / 1000 / 60,
                warnConfig.getThreshold()));
        earlyWarnInfo.setContent(PatternUtils.buildContent(pushCenterService.getSmsConfig(warnConfig.getSmsTemplateId()).getContent(),
                DateUtils.formatDate(new Date(statTime)), DateUtils.formatDate(new Date(endTime)), warnConfig.getThreshold()));
        earlyWarnInfo.setDisposer(StringUtils.join(warnConfig.getUsers().stream().map(User::getUserName).toArray(), Constants.COMMA_EN));
        earlyWarnInfoService.save(earlyWarnInfo);
    }

    @Data
    private static class Reference {

        private long statTime;

        private long endTime;

        private int warnCount;

        public Reference(long statTime, long endTime, int warnCount) {
            this.statTime = statTime;
            this.endTime = endTime;
            this.warnCount = warnCount;
        }
    }
}
