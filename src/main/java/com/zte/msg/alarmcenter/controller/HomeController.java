package com.zte.msg.alarmcenter.controller;

import com.zte.msg.alarmcenter.dto.DataResponse;
import com.zte.msg.alarmcenter.dto.res.*;
import com.zte.msg.alarmcenter.service.HomeService;
import com.zte.msg.alarmcenter.utils.AsyncSender;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2021/3/9 9:52
 */
@Slf4j
@RestController
@RequestMapping("/home")
@Api(tags = "首页")
@Validated
public class HomeController {

    @Resource
    private HomeService homeService;

    @Autowired
    AsyncSender asyncSender;


    /**
     * 告警情况查询
     *
     * @return
     */
    @GetMapping("/status")
    @ApiOperation(value = "告警情况查询")
    public DataResponse<HomeAlarmStatusSituationResDTO> statusSituation() {
        return DataResponse.of(homeService.alarmStatusSituation());
    }

    /**
     * 系统状况查询
     *
     * @return
     */
    @GetMapping("/subsystem")
    @ApiOperation(value = "系统状况查询")
    public DataResponse<List<HomeSubsystemSituationResDTO>> subsystemSituation() {
        return DataResponse.of(homeService.subsystemSituation());
    }

    /**
     * 车站状况查询
     *
     * @return
     */
    @GetMapping("/station")
    @ApiOperation(value = "车站状况查询")
    public DataResponse<List<HomeStationSituationResDTO>> stationSituation() {
        return DataResponse.of(homeService.stationSituation());
    }

    /**
     * 首页告警消息
     *
     * @return
     */
    @GetMapping("/alarm")
    @ApiOperation(value = "首页告警消息")
    public DataResponse<List<AlarmHistoryResDTO>> selectAlarmHistory(@RequestParam(required = false) @ApiParam("返回数据数量") Long size,
                                                                     @RequestParam(required = false) @ApiParam("系统id") Long subsystemId,
                                                                     @RequestParam(required = false) @ApiParam("站点id") Long siteId) {
        return DataResponse.of(homeService.selectAlarmHistory(size, subsystemId, siteId));
    }

    /**
     * 首页告警消息导出
     *
     * @return
     */
    @GetMapping("/alarm/export")
    @ApiOperation(value = "首页告警消息导出")
    public DataResponse<T> exportAlarmHistory(@RequestParam(required = false) @ApiParam("系统id") Long subsystemId,
                                              @RequestParam(required = false) @ApiParam("站点id") Long siteId) {
        homeService.exportAlarmHistory(subsystemId, siteId);
        return DataResponse.success();
    }

    /**
     * 静音
     *
     * @param ids
     * @return
     */
    @PostMapping("/alarm/mute")
    @ApiOperation(value = "静音")
    public DataResponse<T> mute(@Valid @RequestBody List<Integer> ids) {
        homeService.mute(ids);
        return DataResponse.success();
    }

    /**
     * 解除静音
     *
     * @param ids
     * @return
     */
    @PostMapping("/alarm/unmute")
    @ApiOperation(value = "解除静音")
    public DataResponse<T> unmute(@Valid @RequestBody List<Integer> ids) {
        homeService.unmute(ids);
        return DataResponse.success();
    }

    /**
     * 调节音量
     *
     * @param alarmVolume
     * @param ids
     * @return
     */
    @PostMapping("/alarm/volume")
    @ApiOperation(value = "调节音量")
    public DataResponse<T> adjustVolume(@RequestParam(required = false)
                                            @ApiParam("音量大小") String alarmVolume,
                                        @Valid @RequestBody List<Integer> ids) {
        homeService.adjustVolume(ids, alarmVolume);
        return DataResponse.success();
    }

    /**
     * 清除告警
     *
     * @param ids
     * @return
     */
    @PostMapping("/alarm/clear")
    @ApiOperation(value = "清除告警")
    public DataResponse<T> clearAlarm(@Valid @RequestBody List<Integer> ids) {
        homeService.clearAlarm(ids);
        return DataResponse.success();
    }

    /**
     * 确认告警
     *
     * @param ids
     * @return
     */
    @PostMapping("/alarm/confirm")
    @ApiOperation(value = "确认告警")
    public DataResponse<T> confirmAlarm(@Valid @RequestBody List<Integer> ids) {
        homeService.confirmAlarm(ids);
        return DataResponse.success();
    }

    /**
     * 过滤告警
     *
     * @param ids
     * @return
     */
    @PostMapping("/alarm/filter")
    @ApiOperation(value = "过滤告警")
    public DataResponse<T> filterAlarm(@Valid @RequestBody List<Integer> ids) {
        homeService.filterAlarm(ids);
        return DataResponse.success();
    }

    /**
     * 恢复告警
     *
     * @param ids
     * @return
     */
    @PostMapping("/alarm/recovery")
    @ApiOperation(value = "恢复告警")
    public DataResponse<T> recoveryAlarm(@Valid @RequestBody List<Integer> ids) {
        homeService.recoveryAlarm(ids);
        return DataResponse.success();
    }

    /**
     * 告警记录刷新
     *
     * @return
     */
    @GetMapping("/alarm/refresh")
    @ApiOperation(value = "告警记录刷新")
    public DataResponse<T> refreshAlarm() throws Exception {
        asyncSender.refresh();
        return DataResponse.success();
    }

    /**
     * 修改响铃
     *
     * @return
     */
    @PostMapping("/alarm/ring")
    @ApiOperation(value = "修改响铃")
    public DataResponse<T> alarmRing(@RequestBody Map<String, Object> map) throws Exception {
        Integer isRing = (Integer) map.get("isRing");
        List<Long> ids = (List<Long>) map.get("ids");
        homeService.updateIsRing(isRing, ids);
        return DataResponse.success();
    }
}
