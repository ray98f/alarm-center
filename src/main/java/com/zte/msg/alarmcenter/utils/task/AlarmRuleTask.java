package com.zte.msg.alarmcenter.utils.task;

import com.alibaba.fastjson.JSON;
import com.zte.msg.alarmcenter.dto.res.AlarmRuleDataResDTO;
import com.zte.msg.alarmcenter.dto.res.RedisUpdateFrequencyResDTO;
import com.zte.msg.alarmcenter.entity.AlarmHistory;
import com.zte.msg.alarmcenter.mapper.AlarmManageMapper;
import com.zte.msg.alarmcenter.mapper.ChildSystemMapper;
import com.zte.msg.alarmcenter.utils.SerializeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author frp
 */
@Component
@Slf4j
public class AlarmRuleTask {

    @Autowired
    private AlarmManageMapper alarmManageMapper;

    @Autowired
    private ChildSystemMapper childSystemMapper;

    /**
     * 告警延迟规则
     */
    @Scheduled(cron = "30 */1 * * * ?")
    @Async
    public void alarmDelayRuleTask() {
        log.info("-----------  告警延迟规则修改开始  -----------");
        alarmManageMapper.updateDelayAlarmHistory();
        log.info("-----------  告警延迟规则修改结束  -----------");
    }

    /**
     * 告警经历时间升级规则
     */
    @Scheduled(cron = "30 */1 * * * ?")
    @Async
    public void alarmUpdateExperienceRuleTask() {
        log.info("-----------  告警经历时间升级规则  -----------");
        alarmManageMapper.updateExperienceAlarmHistory();
        log.info("-----------  告警经历时间升级规则  -----------");
    }

    /**
     * 修改系统在线状态
     */
    @Scheduled(cron = "*/1 * * * * ?")
    @Async
    public void heartbeatTask() {
        childSystemMapper.offline();
    }
}
