package com.zte.msg.alarmcenter.utils;

import com.zte.msg.alarmcenter.config.RabbitMqConfig;
import com.zte.msg.alarmcenter.dto.AsyncVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author frp
 */
@Slf4j
@Component
public class AsyncSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public boolean send(String message) throws Exception {
        if (StringUtils.isEmpty(message)) {
            return false;
        }
        rabbitTemplate.convertAndSend(RabbitMqConfig.STRING_QUEUE, message);
        log.info(String.format("HelloSender发送字符串消息结果：%s", true));
        return true;
    }

    public boolean send(AsyncVO asyncVO) throws Exception {
        rabbitTemplate.convertAndSend(RabbitMqConfig.ASYNC_QUEUE, asyncVO);
        log.info(String.format("HelloSender发送对象消息结果：%s", true));
        return true;
    }
}
