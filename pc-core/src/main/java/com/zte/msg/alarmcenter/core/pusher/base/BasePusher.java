package com.zte.msg.alarmcenter.core.pusher.base;

import com.alibaba.fastjson.JSONObject;
import com.zte.msg.alarmcenter.core.javac.CodeJavac;
import com.zte.msg.alarmcenter.core.warn.WarnHandler;
import com.zte.msg.alarmcenter.enums.PushMethods;
import com.zte.msg.alarmcenter.utils.MapUtils;
import com.zte.msg.pushcenter.pcscript.PcScript;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2020/12/11 8:33
 */
@Slf4j
@Service
public abstract class BasePusher {

    @Resource
    private WarnHandler warnHandler;

    @Resource
    protected AppService appService;

    @Resource
    protected ProviderMapper providerMapper;

    @Resource
    protected HistoryService historyService;

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.consumer.topic}")
    private String kafkaTopic;

    @Resource(name = "asyncPushExecutor")
    protected ThreadPoolTaskExecutor pushExecutor;

    @Resource(name = "asyncResponseExecutor")
    protected ThreadPoolTaskExecutor resExecutor;

    @Resource
    protected RestTemplate restTemplate;

    @Resource
    protected CodeJavac scriptManager;

    /**
     * pushMethods作为外层map的key
     * <p>
     * 短信：templateId作为内层map的key，priority作为最内层嵌套map的key,
     * <p>
     * 郵件：providerId作为内部两层嵌套map的key
     * <p>
     * 微信：templateId作为内部两层嵌套的key
     * <p>
     * APP: providerId作为内部两层嵌套map的key
     */
    protected Map<PushMethods, Map<Long, TreeMap<Integer, Config>>> configMap = new HashMap<>();


    /**
     * 提交推送
     *
     * @param message message
     */
    public final void submit(Message message) {
        kafkaTemplate.send(kafkaTopic, JSONObject.toJSONString(message));
    }

    /**
     * 推送
     *
     * @param message message
     */
    public abstract void push(Message message);

    /**
     * 回调
     *
     * @param message message
     */
    protected void response(Message message, PcScript.Res res) {
        if (!message.getIsCallBack()) {
            return;
        }
        log.info("==========start sms push response==========");
        try {
            restTemplate.exchange(message.getCallBackUrl(), HttpMethod.POST, new HttpEntity<>(res), String.class);
        } catch (Exception e) {
            log.error("Error!!! An exception occurred when response.");
        }
    }

    /**
     * 存库
     *
     * @param message message
     * @param res     res
     */
    abstract protected void persist(Message message, PcScript.Res res);

    /**
     * 配置及模版初始化
     */
    @PostConstruct
    abstract protected void init();

    protected void warn() {
        warnHandler.submitWarn(Instant.now().plus(TimeUnit.HOURS.toMillis(8)));
    }

    protected Map<Long, TreeMap<Integer, Config>> getConfig(PushMethods pushMethods) {
        return configMap.get(pushMethods);
    }

    protected Map<String, Object> getParamMap(Object... objects) {
        Map<String, Object> paramMap = new HashMap<>(16);
        for (Object object : objects) {
            if (object instanceof Map) {
                paramMap.putAll((Map<String, Object>) object);
            } else {
                paramMap.putAll(MapUtils.objectToMap(object));
            }
        }
        return paramMap;
    }

    protected int getDelay(Message message) {
        return (int) (System.currentTimeMillis() - message.getRequestTime());
    }

}
