package com.zte.msg.alarmcenter.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程池
 * <p>
 * （1）判断核心线程数是否已满，核心线程数大小和corePoolSize参数有关，未满则创建线程执行任务
 * （2）若核心线程池已满，判断队列是否满，队列是否满和workQueue参数有关，若未满则加入队列中
 * （3）若队列已满，判断线程池是否已满，线程池是否已满和maximumPoolSize参数有关，若未满创建线程执行任务
 * （4）若线程池已满，则采用拒绝策略处理无法执执行的任务，拒绝策略和handler参数有关
 *
 * @author frp
 */
@Configuration
@EnableAsync
public class AsyncExecutorConfig {

    @Bean(name = "commonAsyncPool")
    public Executor commonAsyncPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 线程池创建时候初始化的线程数
        executor.setCorePoolSize(30);
        // 线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        executor.setMaxPoolSize(150);
        // 用来缓冲执行任务的队列
        executor.setQueueCapacity(50);
        // 允许线程的空闲时间60秒：当超过了核心线程之外的线程,在空闲时间到达之后会被销毁
        executor.setKeepAliveSeconds(60);
        // 线程池名的前缀：设置好了之后可以方便我们定位处理任务所在的线程池
        executor.setThreadNamePrefix("serverCommonAsyncPool-");
        // 线程池对拒绝任务的处理策略：这里采用了CallerRunsPolicy策略，当线程池没有处理能力的时候，
        // 该策略会直接在 execute 方法的调用线程中运行被拒绝的任务；如果执行程序已关闭，则会丢弃该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}