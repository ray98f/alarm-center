package com.zte.msg.alarmcenter.utils.task;

import com.zte.msg.alarmcenter.entity.AlarmHistory;
import com.zte.msg.alarmcenter.mapper.AlarmManageMapper;
import com.zte.msg.alarmcenter.mapper.ChildSystemMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Map;

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
    @Scheduled(cron = "30 */1 * * * ? ")
    @Async
    public void alarmDelayRuleTask() {
        log.info("-----------  告警延迟规则修改开始  -----------");
        Timestamp now = new Timestamp(System.currentTimeMillis());
        for (Map.Entry<Long, AlarmHistory> m : DataCacheTask.alarmDelayHistoryData.entrySet()) {
            if (m.getValue().getDelayTime().before(now)) {
                alarmManageMapper.updateDelayHistory(m.getValue().getId());
            }
        }
        log.info("-----------  告警延迟规则修改结束  -----------");
    }

    /**
     * 告警升级规则
     */
    @Scheduled(cron = "30 */1 * * * ? ")
    @Async
    public void alarmUpdateRuleTask() {
        log.info("-----------  告警升级规则修改开始  -----------");
        System.out.println(DataCacheTask.alarmUpdateFrequencyHistoryData.size());
        System.out.println(DataCacheTask.alarmUpdateExperienceHistoryData.size());
        log.info("-----------  告警升级规则修改结束  -----------");
    }

    /**
     * 修改系统在线状态
     */
    @Scheduled(cron = "*/1 * * * * ? ")
    @Async
    public void heartbeatTask() {
        childSystemMapper.offline();
    }
}
