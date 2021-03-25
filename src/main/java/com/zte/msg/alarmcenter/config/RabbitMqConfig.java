package com.zte.msg.alarmcenter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;

/**
 * @author frp
 */
@Configuration
public class RabbitMqConfig {

    public static final String ASYNC_QUEUE = "ASYNC_QUEUE";
    public static final String STRING_QUEUE = "STRING_QUEUE";
    public static final String ALARM_QUEUE = "ALARM_QUEUE";
    public static final String SYNC_ALARM_QUEUE = "SYNC_ALARM_QUEUE";

    /**
     * 声明接收字符串的队列 默认
     *
     * @return
     */
    @Bean
    public Queue stringQueue() {
        return QueueBuilder.durable(STRING_QUEUE)
                .build();
    }

    /**
     * 声明接收对象的队列 支持持久化
     *
     * @return
     */
    @Bean
    public Queue goodsQueue() {
        return QueueBuilder.durable(ASYNC_QUEUE).build();
    }

    @Bean
    public Queue aaa() {
        return QueueBuilder.durable(SYNC_ALARM_QUEUE).build();
    }

}
