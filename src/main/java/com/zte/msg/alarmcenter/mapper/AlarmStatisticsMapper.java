package com.zte.msg.alarmcenter.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.res.StatisticsByAnyResDTO;
import com.zte.msg.alarmcenter.dto.res.TotalAlarmDataResDTO;
import com.zte.msg.alarmcenter.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface AlarmStatisticsMapper {

    /**
     * 告警数据总计
     * @param page
     * @param systemId
     * @param siteId
     * @param alarmReason
     * @param startTime
     * @param endTime
     * @return
     */
    Page<TotalAlarmDataResDTO> totalAlarmData(Page<TotalAlarmDataResDTO> page, Long systemId, Long siteId, String alarmReason, Timestamp startTime, Timestamp endTime);

    /**
     * 按线路统计
     * @param siteIds
     * @param alarmLevels
     * @param startTime
     * @param endTime
     * @return
     */
    List<StatisticsByAnyResDTO> statisticsByLine(List<Long> siteIds, List<Integer> alarmLevels, Timestamp startTime, Timestamp endTime);

    /**
     * 按系统统计
     * @param systemIds
     * @param alarmLevels
     * @param startTime
     * @param endTime
     * @return
     */
    List<StatisticsByAnyResDTO> statisticsBySystem(List<Long> systemIds, List<Integer> alarmLevels, Timestamp startTime, Timestamp endTime);

}
