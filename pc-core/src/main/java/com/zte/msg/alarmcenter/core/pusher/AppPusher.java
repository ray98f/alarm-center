package com.zte.msg.alarmcenter.core.pusher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zte.msg.alarmcenter.core.pusher.base.BasePusher;
import com.zte.msg.alarmcenter.core.pusher.base.Config;
import com.zte.msg.alarmcenter.core.pusher.base.Message;
import com.zte.msg.alarmcenter.core.pusher.msg.AppMessage;
import com.zte.msg.alarmcenter.enums.PushMethods;
import com.zte.msg.alarmcenter.utils.Constants;
import com.zte.msg.pushcenter.pcscript.PcScript;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2020/12/11 9:35
 */
@Slf4j
@Component
public class AppPusher extends BasePusher {

    @Override
    protected void init() {
        configMap.put(PushMethods.APP, new HashMap<>(16));
        List<Provider> providers = providerMapper.selectList(new QueryWrapper<Provider>().eq("type", PushMethods.APP.value()));
        flushConfig(providers);
    }

    @Override
    public void push(Message message) {
        AppMessage appMessage = (AppMessage) message;
        CompletableFuture.supplyAsync(() -> {
            AppConfig config = getConfig(appMessage.getProviderId());
            appMessage.setTransmitTime(new Timestamp(System.currentTimeMillis()));
            appMessage.setProviderName(config.getProviderName());
            Map<String, Object> paramMap = getParamMap(appMessage,
                    JSON.parseObject(config.getConfig(), new TypeReference<HashMap<String, Object>>() {
                    }));
            Class<?> scriptClass = scriptManager.getScriptClass(config.getScriptTag());
            Method execute;
            PcScript.Res res = null;
            try {
                execute = scriptClass.getMethod("execute", Map.class);
                Object o = scriptClass.newInstance();
                res = (PcScript.Res) execute.invoke(o, paramMap);
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return res;
        }).exceptionally(e -> {
            log.error("Error while send a sms message: {}", e.getMessage());
            e.printStackTrace();
            return new PcScript.Res(-1, e.getMessage());
        }).thenAcceptAsync(o -> {
            if (o.getCode() != Constants.SUCCESS && message.getIsWarn()) {
                warn();
            }
            if (message.getIsCallBack()) {
                response(message, o);
            }
            persist(appMessage, o);
        }, resExecutor);
    }

    @Override
    protected void persist(Message message, PcScript.Res res) {
        AppMessage appMessage = (AppMessage) message;
        appMessage.setAppName(appService.getAppName(appMessage.getAppId()));

        appMessage.setDelay(getDelay(message));

        ApplicationInfo applicationInfo = new ApplicationInfo(appMessage, res);
        historyService.addApplicationInfo(applicationInfo);
    }

    public void flushConfig(List<Provider> providers) {
        flushConfig(providers, false);
    }

    public void flushConfig(List<Provider> providers, boolean remove) {
        providers.forEach(o -> {
            flushConfig(o, remove);
            if (!remove) {
                log.info("========== update app config completed, update count: {} ==========", providers.size());
            } else {
                log.info("========== delete app config completed, delete count: {} ==========", providers.size());
            }
        });
    }

    public void flushConfig(Provider provider, boolean remove) {
        if (!remove) {
            TreeMap<Integer, Config> treeMap = configMap.get(PushMethods.APP).get(provider.getId());
            if (Objects.isNull(treeMap)) {
                treeMap = new TreeMap<>();
            }
            treeMap.put(provider.getId().intValue(), new AppConfig(provider));
            configMap.get(PushMethods.APP).put(provider.getId(), treeMap);
        } else {
            configMap.get(PushMethods.APP).remove(provider.getId());
        }
    }

    @Data
    private static class AppConfig implements Config {

        private Long providerId;

        private String providerName;

        private String scriptTag;

        private String config;

        public AppConfig(Provider provider) {
            this.scriptTag = provider.getScriptTag();
            this.config = provider.getConfig();
            this.providerId = provider.getId();
            this.providerName = provider.getProviderName();
        }
    }

    private AppConfig getConfig(Long providerId) {
        return (AppConfig) super.getConfig(PushMethods.APP).get(providerId).lastEntry().getValue();
    }

}
