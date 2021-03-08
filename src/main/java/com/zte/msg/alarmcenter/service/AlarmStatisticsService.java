package com.zte.msg.alarmcenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.req.AnyAlarmTrendReqDTO;
import com.zte.msg.alarmcenter.dto.req.StatisticsByAnyReqDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmResolutionEfficiencyResDTO;
import com.zte.msg.alarmcenter.dto.res.AnyAlarmTrendResDTO;
import com.zte.msg.alarmcenter.dto.res.StatisticsByAnyResDTO;
import com.zte.msg.alarmcenter.dto.res.TotalAlarmDataResDTO;

import java.sql.Timestamp;
import java.util.List;

/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2021/2/24 9:01
 */
public interface AlarmStatisticsService {
    /**
     * 告警数据总计
     *
     * @param systemId
     * @param siteId
     * @param alarmReason
     * @param startTime
     * @param endTime
     * @param pageReqDTO
     * @return
     */
    Page<TotalAlarmDataResDTO> totalAlarmData(Long systemId, Long siteId, String alarmReason, String startTime, String endTime, PageReqDTO pageReqDTO);

    /**
     * 按线路统计
     * @param statisticsByAnyReqDTO
     * @return
     */
    List<StatisticsByAnyResDTO> statisticsByLine(StatisticsByAnyReqDTO statisticsByAnyReqDTO);

    /**
     * 按系统统计
     * @param statisticsByAnyReqDTO
     * @return
     */
    List<StatisticsByAnyResDTO> statisticsBySystem(StatisticsByAnyReqDTO statisticsByAnyReqDTO);

    /**
     * 按告警级别统计
     * @param statisticsByAnyReqDTO
     * @return
     */
    List<StatisticsByAnyResDTO> statisticsByAlarmLevel(StatisticsByAnyReqDTO statisticsByAnyReqDTO);

    /**
     * 线路告警趋势
     * @param anyAlarmTrendReqDTO
     * @return
     */
    List<AnyAlarmTrendResDTO> lineAlarmTrend(AnyAlarmTrendReqDTO anyAlarmTrendReqDTO);

    /**
     * 级别告警趋势
     * @param anyAlarmTrendReqDTO
     * @return
     */
    List<AnyAlarmTrendResDTO> levelAlarmTrend(AnyAlarmTrendReqDTO anyAlarmTrendReqDTO);

    /**
     * 系统告警趋势
     * @param anyAlarmTrendReqDTO
     * @return
     */
    List<AnyAlarmTrendResDTO> systemAlarmTrend(AnyAlarmTrendReqDTO anyAlarmTrendReqDTO);

    /**
     *
     * @param anyAlarmTrendReqDTO
     * @return
     */
    List<AlarmResolutionEfficiencyResDTO> alarmResolutionEfficiency(AnyAlarmTrendReqDTO anyAlarmTrendReqDTO);

}
