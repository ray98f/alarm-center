package com.zte.msg.alarmcenter.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.res.*;
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
}
