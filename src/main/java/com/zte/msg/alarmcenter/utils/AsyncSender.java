package com.zte.msg.alarmcenter.utils;

import com.alibaba.fastjson.JSON;
import com.zte.msg.alarmcenter.config.RabbitMqConfig;
import com.zte.msg.alarmcenter.dto.AsyncVO;
import com.zte.msg.alarmcenter.dto.req.AlarmHistoryReqDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author frp
 */
@Slf4j
@Component
public class AsyncSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void sendTts(String message) {
        if (StringUtils.isEmpty(message)) {
            return;
        }
        rabbitTemplate.convertAndSend(RabbitMqConfig.TTS, message);
    }

    public void send(AsyncVO asyncVO) {
        if (Objects.isNull(asyncVO)) {
            return;
        }
        rabbitTemplate.convertAndSend(RabbitMqConfig.ASYNC_QUEUE, asyncVO);
    }

    public void refresh() {
        rabbitTemplate.convertAndSend(RabbitMqConfig.REFRESH_QUEUE);
    }
}
