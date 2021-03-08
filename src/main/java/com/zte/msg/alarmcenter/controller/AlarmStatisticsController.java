package com.zte.msg.alarmcenter.controller;

import com.zte.msg.alarmcenter.dto.DataResponse;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.PageResponse;
import com.zte.msg.alarmcenter.dto.req.AnyAlarmTrendReqDTO;
import com.zte.msg.alarmcenter.dto.req.StatisticsByAnyReqDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmResolutionEfficiencyResDTO;
import com.zte.msg.alarmcenter.dto.res.AnyAlarmTrendResDTO;
import com.zte.msg.alarmcenter.dto.res.StatisticsByAnyResDTO;
import com.zte.msg.alarmcenter.dto.res.TotalAlarmDataResDTO;
import com.zte.msg.alarmcenter.service.AlarmStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.List;

/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2021/2/24 9:01
 */
@Slf4j
@RestController
@RequestMapping("/statistics")
@Api(tags = "告警统计")
@Validated
public class AlarmStatisticsController {

    @Resource
    private AlarmStatisticsService alarmStatisticsService;

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
    @GetMapping("/total")
    @ApiOperation(value = "告警数据总计")
    public PageResponse<TotalAlarmDataResDTO> totalAlarmData(@RequestParam(required = false)
                                                                 @ApiParam("系统") Long systemId,
                                                             @RequestParam(required = false)
                                                                 @ApiParam("站点") Long siteId,
                                                             @RequestParam(required = false)
                                                                 @ApiParam("告警原因") String alarmReason,
                                                             @RequestParam(required = false)
                                                                 @ApiParam("开始时间") String startTime,
                                                             @RequestParam(required = false)
                                                                 @ApiParam("结束时间") String endTime,
                                                             @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(alarmStatisticsService.totalAlarmData(systemId, siteId, alarmReason, startTime, endTime, pageReqDTO));
    }

    /**
     * 按线路统计
     * @param statisticsByAnyReqDTO
     * @return
     */
    @PostMapping("/line")
    @ApiOperation(value = "按线路统计")
    public DataResponse<List<StatisticsByAnyResDTO>> statisticsByLine(@RequestBody @Valid StatisticsByAnyReqDTO statisticsByAnyReqDTO) {
        return DataResponse.of(alarmStatisticsService.statisticsByLine(statisticsByAnyReqDTO));
    }

    /**
     * 按系统统计
     * @param statisticsByAnyReqDTO
     * @return
     */
    @PostMapping("/system")
    @ApiOperation(value = "按系统统计")
    public DataResponse<List<StatisticsByAnyResDTO>> statisticsBySystem(@RequestBody @Valid StatisticsByAnyReqDTO statisticsByAnyReqDTO) {
        return DataResponse.of(alarmStatisticsService.statisticsBySystem(statisticsByAnyReqDTO));
    }

    /**
     * 按告警级别统计
     * @param statisticsByAnyReqDTO
     * @return
     */
    @PostMapping("/level")
    @ApiOperation(value = "按告警级别统计")
    public DataResponse<List<StatisticsByAnyResDTO>> statisticsByAlarmLevel(@RequestBody @Valid StatisticsByAnyReqDTO statisticsByAnyReqDTO) {
        return DataResponse.of(alarmStatisticsService.statisticsByAlarmLevel(statisticsByAnyReqDTO));
    }

    /**
     * 线路告警趋势
     * @param anyAlarmTrendReqDTO
     * @return
     */
    @PostMapping("/line/trend")
    @ApiOperation(value = "线路告警趋势")
    public DataResponse<List<AnyAlarmTrendResDTO>> lineAlarmTrend(@RequestBody @Valid AnyAlarmTrendReqDTO anyAlarmTrendReqDTO) {
        return DataResponse.of(alarmStatisticsService.lineAlarmTrend(anyAlarmTrendReqDTO));
    }

    /**
     * 级别告警趋势
     * @param anyAlarmTrendReqDTO
     * @return
     */
    @PostMapping("/level/trend")
    @ApiOperation(value = "级别告警趋势")
    public DataResponse<List<AnyAlarmTrendResDTO>> levelAlarmTrend(@RequestBody @Valid AnyAlarmTrendReqDTO anyAlarmTrendReqDTO) {
        return DataResponse.of(alarmStatisticsService.levelAlarmTrend(anyAlarmTrendReqDTO));
    }

    /**
     * 系统告警趋势
     * @param anyAlarmTrendReqDTO
     * @return
     */
    @PostMapping("/system/trend")
    @ApiOperation(value = "系统告警趋势")
    public DataResponse<List<AnyAlarmTrendResDTO>> systemAlarmTrend(@RequestBody @Valid AnyAlarmTrendReqDTO anyAlarmTrendReqDTO) {
        return DataResponse.of(alarmStatisticsService.systemAlarmTrend(anyAlarmTrendReqDTO));
    }

    /**
     * 告警解决效率
     * @param anyAlarmTrendReqDTO
     * @return
     */
    @PostMapping("/efficiency")
    @ApiOperation(value = "告警解决效率")
    public DataResponse<List<AlarmResolutionEfficiencyResDTO>> alarmResolutionEfficiency(@RequestBody @Valid AnyAlarmTrendReqDTO anyAlarmTrendReqDTO) {
        return DataResponse.of(alarmStatisticsService.alarmResolutionEfficiency(anyAlarmTrendReqDTO));
    }
}
