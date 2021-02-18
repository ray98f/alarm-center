package com.zte.msg.alarmcenter.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.event.ConsumerStoppedEvent;
import org.springframework.kafka.support.Acknowledgment;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2020/4/26 16:26
 */
@Slf4j
public abstract class BaseConsumer implements ApplicationListener<ConsumerStoppedEvent> {


    @Resource(name = "kafkaConsumerExecutor")
    private ThreadPoolExecutor consumeExecutor;

    private volatile boolean isClosePoolExecutor = false;

    /**
     * 收到spring-kafka 关闭Consumer的通知
     *
     * @param event 关闭Consumer 事件
     */
    @Override
    public void onApplicationEvent(ConsumerStoppedEvent event) {

        isClosePoolExecutor = true;
        closeConsumeExecutorService();

    }

    private void closeConsumeExecutorService() {

        if (!consumeExecutor.isShutdown()) {
            shutdownGracefully(consumeExecutor, 120, TimeUnit.SECONDS);
            log.info("consumeExecutor stopped");

        }

    }

    public static void shutdownGracefully(ExecutorService executor, long timeout, TimeUnit timeUnit) {
        // 禁止提交新任务。
        executor.shutdown();
        try {
            // 等待一段时间，以终止现有任务。
            if (!executor.awaitTermination(timeout, timeUnit)) {
                executor.shutdownNow();
                // 请稍等片刻，以使任务响应被取消的情况。
                if (!executor.awaitTermination(timeout, timeUnit)) {
                    log.warn(String.format("%s didn't terminate!", executor));
                }
            }
        } catch (InterruptedException ie) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    @PreDestroy
    public void doClose() {
        if (!isClosePoolExecutor) {
            closeConsumeExecutorService();
        }
    }

    @KafkaListener(topics = "${spring.kafka.consumer.topic}", containerFactory = "kafkaListenerContainerFactory")
    public void onMessage(List<String> msgList, Acknowledgment ack) {

        CountDownLatch countDownLatch = new CountDownLatch(msgList.size());

        for (String message : msgList) {
            submitConsumeTask(message, countDownLatch);
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error("countDownLatch exception ", e);
        }

        // 本次批量消费完,手动提交
        ack.acknowledge();
        log.info("finish commit offset");

    }

    private void submitConsumeTask(String message, CountDownLatch countDownLatch) {
        consumeExecutor.submit(() -> {
            try {
                onDealMessage(message);
            } catch (Exception ex) {
                log.error("on DealMessage exception:", ex);
            } finally {
                countDownLatch.countDown();
            }
        });
    }

    /**
     * 子类实现该抽象方法处理具体消息的业务逻辑
     *
     * @param message kafka的消息
     */
    protected abstract void onDealMessage(String message);

}
