package com.zte.msg.alarmcenter.controller;

import com.zte.msg.alarmcenter.dto.DataResponse;
import com.zte.msg.alarmcenter.dto.res.*;
import com.zte.msg.alarmcenter.service.HomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
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

    /**
     * 项目情况查询
     *
     * @return
     */
    @GetMapping("/proj")
    @ApiOperation(value = "项目情况查询")
    public DataResponse<HomeProjSituationResDTO> projectSituation() {
        return DataResponse.of(homeService.projectSituation());
    }

    /**
     * 线路情况查询
     *
     * @return
     */
    @GetMapping("/line")
    @ApiOperation(value = "线路情况查询")
    public DataResponse<HomeLineSituationResDTO> lineSituation() {
        return DataResponse.of(homeService.lineSituation());
    }

    /**
     * 设备情况查询
     *
     * @return
     */
    @GetMapping("/device")
    @ApiOperation(value = "设备情况查询")
    public DataResponse<HomeDeviceSituationResDTO> deviceSituation() {
        return DataResponse.of(homeService.deviceSituation());
    }

    /**
     * 告警处置情况
     *
     * @return
     */
    @GetMapping("/alarm-handle")
    @ApiOperation(value = "告警处置情况")
    public DataResponse<Map<String, Object>> alarmHandleSituation() {
        return DataResponse.of(homeService.alarmHandleSituation());
    }

    /**
     * 系统状况查询
     *
     * @return
     */
    @GetMapping("/subsystem")
    @ApiOperation(value = "系统状况查询")
    public DataResponse<HomeSubsystemSituationResDTO> subsystemSituation() {
        return DataResponse.of(homeService.subsystemSituation());
    }

    /**
     * 首页告警消息
     *
     * @return
     */
    @GetMapping("/alarm")
    @ApiOperation(value = "首页告警消息")
    public DataResponse<List<AlarmHistoryResDTO>> selectAlarmHistory() {
        return DataResponse.of(homeService.selectAlarmHistory());
    }

    /**
     * 首页告警消息导出
     *
     * @return
     */
    @GetMapping("/alarm/export")
    @ApiOperation(value = "首页告警消息导出")
    public <T> DataResponse<T> exportAlarmHistory(HttpServletResponse response) {
        homeService.exportAlarmHistory(response);
        return DataResponse.success();
    }

    /**
     * 静音
     *
     * @param ids
     * @param <T>
     * @return
     */
    @PostMapping("/alarm/mute")
    @ApiOperation(value = "静音")
    public <T> DataResponse<T> mute(@Valid @RequestBody List<Integer> ids) {
        homeService.mute(ids);
        return DataResponse.success();
    }

    /**
     * 解除静音
     *
     * @param ids
     * @param <T>
     * @return
     */
    @PostMapping("/alarm/unmute")
    @ApiOperation(value = "解除静音")
    public <T> DataResponse<T> unmute(@Valid @RequestBody List<Integer> ids) {
        homeService.unmute(ids);
        return DataResponse.success();
    }

    /**
     * 调节音量
     *
     * @param alarmVolume
     * @param ids
     * @param <T>
     * @return
     */
    @PostMapping("/alarm/volume")
    @ApiOperation(value = "调节音量")
    public <T> DataResponse<T> adjustVolume(@RequestParam(required = false)
                                            @ApiParam("音量大小") String alarmVolume,
                                            @Valid @RequestBody List<Integer> ids) {
        homeService.adjustVolume(ids, alarmVolume);
        return DataResponse.success();
    }

    /**
     * 清除告警
     *
     * @param ids
     * @param <T>
     * @return
     */
    @PostMapping("/alarm/clear")
    @ApiOperation(value = "清除告警")
    public <T> DataResponse<T> clearAlarm(@Valid @RequestBody List<Integer> ids) {
        homeService.clearAlarm(ids);
        return DataResponse.success();
    }

    /**
     * 确认告警
     *
     * @param ids
     * @param <T>
     * @return
     */
    @PostMapping("/alarm/confirm")
    @ApiOperation(value = "确认告警")
    public <T> DataResponse<T> confirmAlarm(@Valid @RequestBody List<Integer> ids) {
        homeService.confirmAlarm(ids);
        return DataResponse.success();
    }

    /**
     * 过滤告警
     *
     * @param ids
     * @param <T>
     * @return
     */
    @PostMapping("/alarm/filter")
    @ApiOperation(value = "过滤告警")
    public <T> DataResponse<T> filterAlarm(@Valid @RequestBody List<Integer> ids) {
        homeService.filterAlarm(ids);
        return DataResponse.success();
    }

    /**
     * 恢复告警
     *
     * @param ids
     * @param <T>
     * @return
     */
    @PostMapping("/alarm/recovery")
    @ApiOperation(value = "恢复告警")
    public <T> DataResponse<T> recoveryAlarm(@Valid @RequestBody List<Integer> ids) {
        homeService.recoveryAlarm(ids);
        return DataResponse.success();
    }

}
