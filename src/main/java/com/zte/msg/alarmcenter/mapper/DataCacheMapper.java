package com.zte.msg.alarmcenter.mapper;

import com.zte.msg.alarmcenter.dto.res.AlarmRuleDataResDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmRuleDetailsResDTO;
import com.zte.msg.alarmcenter.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface DataCacheMapper {

    /**
     * 全量获取告警码表
     *
     * @return
     */
    List<AlarmCode> selectAlarmCode();

    /**
     * 增量获取告警码表
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<AlarmCode> selectAlarmCodeByTime(Timestamp startTime, Timestamp endTime);

    /**
     * 全量获取告警规则
     *
     * @return
     */
    List<AlarmRuleDataResDTO> selectAlarmRule();

    /**
     * 增量获取告警规则
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<AlarmRuleDataResDTO> selectAlarmRuleByTime(Timestamp startTime, Timestamp endTime);

    /**
     * 全量获取设备
     *
     * @return
     */
    List<Device> selectDevice();

    /**
     * 增量获取设备
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<Device> selectDeviceByTime(Timestamp startTime, Timestamp endTime);

    /**
     * 全量获取设备槽位
     *
     * @return
     */
    List<DeviceSlot> selectDeviceSlot();

    /**
     * 增量获取设备槽位
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<DeviceSlot> selectDeviceSlotByTime(Timestamp startTime, Timestamp endTime);

    /**
     * 全量获取系统
     *
     * @return
     */
    List<Subsystem> selectSubsystem();

    /**
     * 增量获取系统
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<Subsystem> selectSubsystemByTime(Timestamp startTime, Timestamp endTime);

    /**
     * 全量获取线路与站点
     *
     * @return
     */
    List<Position> selectPosition();

    /**
     * 增量获取线路与站点
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<Position> selectPositionByTime(Timestamp startTime, Timestamp endTime);

    /**
     * 全量获取告警记录（延迟告警）
     *
     * @return
     */
    List<AlarmHistory> selectDelayAlarmHistory();

    /**
     * 增量获取告警记录（延迟告警）
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<AlarmHistory> selectDelayAlarmHistoryByTime(Timestamp startTime, Timestamp endTime);

    /**
     * 全量获取告警记录（频次升级）
     *
     * @return
     */
    List<AlarmHistory> selectUpdateFrequencyAlarmHistory();

    /**
     * 增量获取告警记录（频次升级）
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<AlarmHistory> selectUpdateFrequencyAlarmHistoryByTime(Timestamp startTime, Timestamp endTime);

    /**
     * 全量获取告警记录（历时升级）
     *
     * @return
     */
    List<AlarmHistory> selectUpdateExperienceAlarmHistory();

    /**
     * 增量获取告警记录（历时升级）
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<AlarmHistory> selectUpdateExperienceAlarmHistoryByTime(Timestamp startTime, Timestamp endTime);

}
