package com.zte.msg.alarmcenter.controller;

import com.zte.msg.alarmcenter.dto.DataResponse;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.PageResponse;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
                                                                 @ApiParam("开始时间") Timestamp startTime,
                                                             @RequestParam(required = false)
                                                                 @ApiParam("结束时间") Timestamp endTime,
                                                             @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(alarmStatisticsService.totalAlarmData(systemId, siteId, alarmReason, startTime, endTime, pageReqDTO));
    }

    /**
     * 按线路统计
     *
     * @param siteIds
     * @param alarmLevels
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/line/statistics")
    @ApiOperation(value = "按线路统计")
    public DataResponse<List<StatisticsByAnyResDTO>> statisticsByLine(@RequestParam(required = false)
                                                                          @ApiParam("站点") List<Long> siteIds,
                                                                      @RequestParam(required = false)
                                                                          @ApiParam("告警级别") List<Integer> alarmLevels,
                                                                      @RequestParam(required = false)
                                                                          @ApiParam("开始时间") Timestamp startTime,
                                                                      @RequestParam(required = false)
                                                                          @ApiParam("结束时间") Timestamp endTime) {
        return DataResponse.of(alarmStatisticsService.statisticsByLine(siteIds, alarmLevels, startTime, endTime));
    }

    /**
     * 按系统统计
     *
     * @param systemIds
     * @param alarmLevels
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/system/statistics")
    @ApiOperation(value = "按系统统计")
    public DataResponse<List<StatisticsByAnyResDTO>> statisticsBySystem(@RequestParam(required = false)
                                                                            @ApiParam("系统") List<Long> systemIds,
                                                                        @RequestParam(required = false)
                                                                            @ApiParam("告警级别") List<Integer> alarmLevels,
                                                                        @RequestParam(required = false)
                                                                            @ApiParam("开始时间") Timestamp startTime,
                                                                        @RequestParam(required = false)
                                                                            @ApiParam("结束时间") Timestamp endTime) {
        return DataResponse.of(alarmStatisticsService.statisticsBySystem(systemIds, alarmLevels, startTime, endTime));
    }

    @GetMapping("/level/statistics")
    @ApiOperation(value = "按告警级别统计")
    public DataResponse<List<StatisticsByAnyResDTO>> statisticsByAlarmLevel(@RequestParam(required = false)
                                                                                @ApiParam("告警级别") List<Integer> alarmLevels,
                                                                            @RequestParam(required = false)
                                                                                @ApiParam("开始时间") Timestamp startTime,
                                                                            @RequestParam(required = false)
                                                                                @ApiParam("结束时间") Timestamp endTime) {
        return DataResponse.success();
    }

    @GetMapping("/line/trend")
    @ApiOperation(value = "线路告警趋势")
    public DataResponse<List<AnyAlarmTrendResDTO>> lineAlarmTrend(@RequestParam(required = false)
                                                                      @ApiParam("站点") Long siteId,
                                                                  @RequestParam(required = false)
                                                                      @ApiParam("告警级别") List<Integer> alarmLevels,
                                                                  @RequestParam(required = false)
                                                                      @ApiParam("统计周期") Integer statisticsCycle,
                                                                  @RequestParam(required = false)
                                                                      @ApiParam("开始时间") Timestamp startTime,
                                                                  @RequestParam(required = false)
                                                                      @ApiParam("结束时间") Timestamp endTime) {
        return DataResponse.success();
    }

    @GetMapping("/level/trend")
    @ApiOperation(value = "级别告警趋势")
    public DataResponse<List<AnyAlarmTrendResDTO>> levelAlarmTrend(@RequestParam(required = false)
                                                                       @ApiParam("告警级别") List<Integer> alarmLevels,
                                                                   @RequestParam(required = false)
                                                                       @ApiParam("统计周期") Integer statisticsCycle,
                                                                   @RequestParam(required = false)
                                                                       @ApiParam("开始时间") Timestamp startTime,
                                                                   @RequestParam(required = false)
                                                                       @ApiParam("结束时间") Timestamp endTime) {
        return DataResponse.success();
    }

    @GetMapping("/system/trend")
    @ApiOperation(value = "系统告警趋势")
    public DataResponse<List<AnyAlarmTrendResDTO>> systemAlarmTrend(@RequestParam(required = false)
                                                                        @ApiParam("系统") List<Long> systemIds,
                                                                    @RequestParam(required = false)
                                                                        @ApiParam("告警级别") List<Integer> alarmLevels,
                                                                    @RequestParam(required = false)
                                                                        @ApiParam("统计周期") Integer statisticsCycle,
                                                                    @RequestParam(required = false)
                                                                        @ApiParam("开始时间") Timestamp startTime,
                                                                    @RequestParam(required = false)
                                                                        @ApiParam("结束时间") Timestamp endTime) {
        return DataResponse.success();
    }

    @GetMapping("/resolution/efficiency")
    @ApiOperation(value = "告警解决效率")
    public DataResponse<List<AlarmResolutionEfficiencyResDTO>> alarmResolutionEfficiency(@RequestParam(required = false)
                                                                                             @ApiParam("告警级别") List<Integer> alarmLevels,
                                                                                         @RequestParam(required = false)
                                                                                             @ApiParam("统计周期") Integer statisticsCycle,
                                                                                         @RequestParam(required = false)
                                                                                             @ApiParam("开始时间") Timestamp startTime,
                                                                                         @RequestParam(required = false)
                                                                                             @ApiParam("结束时间") Timestamp endTime) {
        return DataResponse.success();
    }
}
