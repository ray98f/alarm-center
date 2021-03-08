package com.zte.msg.alarmcenter.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.DataResponse;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.PageResponse;
import com.zte.msg.alarmcenter.dto.req.*;
import com.zte.msg.alarmcenter.dto.res.AlarmHistoryResDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmRuleResDTO;
import com.zte.msg.alarmcenter.dto.res.SlotResDTO;
import com.zte.msg.alarmcenter.dto.res.SnmpAlarmCodeResDTO;
import com.zte.msg.alarmcenter.entity.AlarmHistory;
import com.zte.msg.alarmcenter.service.AlarmManageService;
import com.zte.msg.alarmcenter.service.AlarmStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.List;

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
    public PageResponse<AlarmHistoryResDTO> pageAlarmHistory(@RequestParam(required = false)
                                                                 @ApiParam("所属子系统") Long subsystemId,
                                                             @RequestParam(required = false)
                                                                 @ApiParam("站点") Long siteId,
                                                             @RequestParam(required = false)
                                                                 @ApiParam("站点") Integer alarmLevel,
                                                             @RequestParam(required = false)
                                                                 @ApiParam("告警码") Integer alarmCode,
                                                             @RequestParam(required = false)
                                                                 @ApiParam("开始时间") Timestamp startTime,
                                                             @RequestParam(required = false)
                                                                 @ApiParam("结束时间") Timestamp endTime,
                                                             @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(alarmManageService.pageAlarmHistory(subsystemId, siteId, alarmLevel, alarmCode, startTime, endTime, pageReqDTO));
    }

}
