package com.zte.msg.alarmcenter.config;


import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author frp
 */
@Configuration
public class RedissonAutoConfiguration {

    @Value("${redisson.address}")
    private String addressUrl;
    @Value("${redisson.password}")
    private String password;

    /**
     * 单机模式配置
     *
     * @return
     */
    @Bean
    public RedissonClient getRedisson() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(addressUrl).setPassword(password)
                .setReconnectionTimeout(10000)
                .setRetryInterval(5000)
                .setTimeout(10000)
                .setConnectTimeout(10000);
        return Redisson.create(config);
    }
}
