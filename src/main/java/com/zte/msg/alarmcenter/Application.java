package com.zte.msg.alarmcenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author ct
 */
@EnableSwagger2
@SpringBootApplication
@EnableConfigurationProperties
@EnableScheduling
@MapperScan("com/zte/msg/alarmcenter/mapper")
@EntityScan("com/zte/msg/alarmcenter/entity")
@EnableAsync
@ServletComponentScan("com.zte.msg.alarmcenter.config.filter")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
