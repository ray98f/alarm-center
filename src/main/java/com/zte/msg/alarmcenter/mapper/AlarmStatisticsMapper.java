package com.zte.msg.alarmcenter.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.req.AnyAlarmTrendReqDTO;
import com.zte.msg.alarmcenter.dto.req.StatisticsByAnyReqDTO;
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
     * 告警解决效率
     * @param anyAlarmTrendReqDTO
     * @return
     */
    List<AlarmResolutionEfficiencyResDTO> alarmResolutionEfficiency(AnyAlarmTrendReqDTO anyAlarmTrendReqDTO);

}
