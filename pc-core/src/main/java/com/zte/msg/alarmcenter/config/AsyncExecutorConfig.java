package com.zte.msg.alarmcenter.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2020/12/11 15:16
 */
@Slf4j
@Configuration
public class AsyncExecutorConfig {

    @Resource
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @Value("${async.push.pool-size.core}")
    private int pushCore;

    @Value("${async.push.pool-size.max}")
    private int pushMax;

    @Value("${spring.kafka.consumer.thread.min}")
    private int consumerThreadMin;

    @Value("${spring.kafka.consumer.thread.max}")
    private int consumerThreadMax;

    @SuppressWarnings("AlibabaThreadShouldSetName")
    @Bean(name = "kafkaConsumerExecutor")
    public ThreadPoolExecutor kafkaConsumerExecutor() {
        return new ThreadPoolExecutor(
                consumerThreadMin,
                consumerThreadMax,
                1000 * 60,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(100)
                , (r, executor) -> kafkaListenerEndpointRegistry.getAllListenerContainers().forEach(o -> {
            o.stop();
            log.info("Kafka consumer queue is full, stop consume 10s.....");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            o.start();
        }));
    }


    @Bean(name = "asyncPushExecutor")
    public ThreadPoolTaskExecutor asyncPushExecutor() {
        return buildExecutor(pushCore, pushMax, 2000, "asyncPushExecutor-", null);
    }

    @Bean(name = "asyncResponseExecutor")
    public ThreadPoolTaskExecutor asyncResponseExecutor() {
        return buildExecutor(20, 20, 2000, "asyncResponseExecutor-", null);
    }

    @Bean(name = "asyncWarnExecutor")
    public ThreadPoolTaskExecutor asyncWarnExecutor() {
        return buildExecutor(5, 10, 500, "asyncWarnExecutor-", null);
    }


    private ThreadPoolTaskExecutor buildExecutor(int corePoolSize,
                                                 int maxPoolSize,
                                                 int queueCapacity,
                                                 String name, RejectedExecutionHandler rejectedExecutionHandler) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        if (Objects.nonNull(rejectedExecutionHandler)) {
            executor.setRejectedExecutionHandler(rejectedExecutionHandler);
        }
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(30);
        executor.setThreadNamePrefix(name);
        executor.setAllowCoreThreadTimeOut(true);
        executor.initialize();
        return executor;
    }

}
