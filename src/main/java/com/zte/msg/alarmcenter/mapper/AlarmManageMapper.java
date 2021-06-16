package com.zte.msg.alarmcenter.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.req.AlarmHistoryReqDTO;
import com.zte.msg.alarmcenter.dto.res.*;
import com.zte.msg.alarmcenter.entity.AlarmHistory;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface AlarmManageMapper {

    /**
     * 告警历史-查询
     *
     * @param page
     * @param subsystemId
     * @param siteId
     * @param alarmLevel
     * @param alarmCode
     * @param startTime
     * @param endTime
     * @return
     */
    Page<AlarmHistoryResDTO> pageAlarmHistory(Page<AlarmHistoryResDTO> page, Long subsystemId, Long siteId, Integer alarmLevel, Integer alarmCode, Timestamp startTime, Timestamp endTime);

    /**
     * 告警历史-导出
     *
     * @param subsystemId
     * @param siteId
     * @param alarmLevel
     * @param alarmCode
     * @param startTime
     * @param endTime
     * @return
     */
    List<AlarmHistoryResDTO> exportAlarmHistory(Long subsystemId, Long siteId, Integer alarmLevel, Integer alarmCode, Timestamp startTime, Timestamp endTime);


    /**
     * 添加备注
     *
     * @param alarmRemark
     * @param id
     * @return
     */
    int editRemark(String alarmRemark, Long id);

    /**
     * 批量新增告警记录
     *
     * @param alarmHistories
     * @return
     */
    int editAlarmHistory(List<AlarmHistory> alarmHistories);

    /**
     * 同步告警记录
     *
     * @param alarmHistories
     * @return
     */
    int syncAlarmHistory(List<AlarmHistory> alarmHistories);

    /**
     * 编辑告警记录附加信息
     *
     * @param alarmHistory
     * @return
     */
    int editAlarmMessage(AlarmHistory alarmHistory);

    /**
     * 同步告警记录附加信息
     *
     * @param alarmId
     * @param alarmMessages
     * @return
     */
    int syncAlarmMessage(Long alarmId, List<AlarmHistoryReqDTO.AlarmMessage> alarmMessages);

    /**
     * 获取告警记录id
     *
     * @param alarmHistory
     * @return
     */
    Long getAlarmHistoryId(AlarmHistory alarmHistory);

    /**
     * 获取告警记等级
     *
     * @param alarmHistory
     * @return
     */
    Integer getUpdateLevel(AlarmHistory alarmHistory);

    /**
     * 修改延迟参数
     */
    void updateDelayAlarmHistory();

    /**
     * 告警升级修改告警等级参数
     *
     * @param alarmHistory
     */
    void updateFrequencyAlarmHistory(AlarmHistory alarmHistory);

    /**
     * 告警升级修改告警等级参数
     *
     * @return
     */
    void updateExperienceAlarmHistory();

    /**
     * 同步恢复告警
     *
     * @return
     */
    void updateSyncAlarmHistory(List<AlarmHistoryResDTO> alarmHistoryResDTOList);
}