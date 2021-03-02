package com.zte.msg.alarmcenter.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.res.AlarmResolutionEfficiencyResDTO;
import com.zte.msg.alarmcenter.dto.res.AnyAlarmTrendResDTO;
import com.zte.msg.alarmcenter.dto.res.StatisticsByAnyResDTO;
import com.zte.msg.alarmcenter.dto.res.TotalAlarmDataResDTO;
import com.zte.msg.alarmcenter.entity.OperationLog;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

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
     *
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
     *
     * @param siteIds
     * @param alarmLevels
     * @param startTime
     * @param endTime
     * @return
     */
    List<StatisticsByAnyResDTO> statisticsByLine(List<Long> siteIds, List<Integer> alarmLevels, Timestamp startTime, Timestamp endTime);

    /**
     * 按系统统计
     *
     * @param systemIds
     * @param alarmLevels
     * @param startTime
     * @param endTime
     * @return
     */
    List<StatisticsByAnyResDTO> statisticsBySystem(List<Long> systemIds, List<Integer> alarmLevels, Timestamp startTime, Timestamp endTime);

    /**
     * 按告警级别统计
     *
     * @param alarmLevels
     * @param startTime
     * @param endTime
     * @return
     */
    List<StatisticsByAnyResDTO> statisticsByAlarmLevel(List<Integer> alarmLevels, Timestamp startTime, Timestamp endTime);

    /**
     * 线路告警趋势
     *
     * @param siteId
     * @param alarmLevels
     * @param statisticsCycle
     * @param startTime
     * @param endTime
     * @return
     */
    List<AnyAlarmTrendResDTO> lineAlarmTrend(Long siteId, List<Integer> alarmLevels, Integer statisticsCycle, Timestamp startTime, Timestamp endTime);

    /**
     * 级别告警趋势
     *
     * @param alarmLevels
     * @param statisticsCycle
     * @param startTime
     * @param endTime
     * @return
     */
    List<AnyAlarmTrendResDTO> levelAlarmTrend(List<Integer> alarmLevels, Integer statisticsCycle, Timestamp startTime, Timestamp endTime);

    /**
     * 系统告警趋势
     *
     * @param systemId
     * @param alarmLevels
     * @param statisticsCycle
     * @param startTime
     * @param endTime
     * @return
     */
    List<AnyAlarmTrendResDTO> systemAlarmTrend(Long systemId, List<Integer> alarmLevels, Integer statisticsCycle, Timestamp startTime, Timestamp endTime);

    /**
     * 告警解决效率
     *
     * @param alarmLevels
     * @param statisticsCycle
     * @param startTime
     * @param endTime
     * @return
     */
    List<AlarmResolutionEfficiencyResDTO> alarmResolutionEfficiency(List<Integer> alarmLevels, Integer statisticsCycle, Timestamp startTime, Timestamp endTime);

}
