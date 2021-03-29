package com.zte.msg.alarmcenter.utils.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author frp
 */
@Component
@Slf4j
public class AlarmRuleTask {
    @Scheduled(cron = "0 */1 * * * ? ")
    public void aa() {

    }
}
