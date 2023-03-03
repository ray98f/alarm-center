package com.zte.msg.alarmcenter.controller;

import com.zte.msg.alarmcenter.annotation.LogMaker;
import com.zte.msg.alarmcenter.dto.DataResponse;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.PageResponse;
import com.zte.msg.alarmcenter.dto.res.AlarmHistoryResDTO;
import com.zte.msg.alarmcenter.service.AlarmManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Map;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/2/23 9:10
 */
@RequestMapping("/alarm")
@Api(tags = "告警管理")
@Validated
@RestController
public class AlarmManageController {

    @Resource
    private AlarmManageService alarmManageService;

    @GetMapping("/history")
    @ApiOperation("告警历史-查询")
    public PageResponse<AlarmHistoryResDTO> pageAlarmHistory(@RequestParam(required = false) @ApiParam("所属子系统") Long subsystemId,
                                                             @RequestParam(required = false) @ApiParam("站点") Long siteId,
                                                             @RequestParam(required = false) @ApiParam("告警级别") Integer alarmLevel,
                                                             @RequestParam(required = false) @ApiParam("告警码") Integer alarmCode,
                                                             @RequestParam(required = false) @ApiParam("开始时间") Timestamp startTime,
                                                             @RequestParam(required = false) @ApiParam("结束时间") Timestamp endTime,
                                                             @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(alarmManageService.pageAlarmHistory(subsystemId, siteId, alarmLevel, alarmCode, startTime, endTime, pageReqDTO));
    }

    @GetMapping("/history/export")
    @ApiOperation("告警历史-导出")
    @LogMaker(value = "告警历史-导出")
    public DataResponse<T> exportAlarmHistory(@RequestParam(required = false) @ApiParam("所属子系统") Long subsystemId,
                                              @RequestParam(required = false) @ApiParam("站点") Long siteId,
                                              @RequestParam(required = false) @ApiParam("站点") Integer alarmLevel,
                                              @RequestParam(required = false) @ApiParam("告警码") Integer alarmCode,
                                              @RequestParam(required = false) @ApiParam("开始时间") Timestamp startTime,
                                              @RequestParam(required = false) @ApiParam("结束时间") Timestamp endTime,
                                              HttpServletResponse response) {
        alarmManageService.exportAlarmHistory(subsystemId, siteId, alarmLevel, alarmCode, startTime, endTime, response);
        return DataResponse.success();
    }

    @PostMapping("/history/{id}")
    @ApiOperation("告警历史-添加备注")
    @LogMaker(value = "告警历史-添加备注")
    public DataResponse<T> editRemark(@ApiParam(value = "告警历史id", required = true) @PathVariable(value = "id") Long id,
                                      @RequestBody Map<String, Object> map) {
        String alarmRemark = (String) map.get("alarmRemark");
        alarmManageService.editRemark(alarmRemark, id);
        return DataResponse.success();
    }

}
