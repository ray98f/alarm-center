package com.zte.msg.alarmcenter.controller;

import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.PageResponse;
import com.zte.msg.alarmcenter.entity.OperationLog;
import com.zte.msg.alarmcenter.entity.SystemParameter;
import com.zte.msg.alarmcenter.service.OperationLogService;
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

/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2021/2/24 9:01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/log")
@Api(tags = "操作日志")
@Validated
public class OperationLogController {

    @Resource
    private OperationLogService operationLogService;

    /**
     * 获取全部系统参数列表
     *
     * @return List<SystemParameter>
     */
    @GetMapping
    @ApiOperation(value = "获取全部系统参数列表")
    public PageResponse<OperationLog> pageSystemParam(@RequestParam(required = false)
                                                         @ApiParam("操作员") String userName,
                                                      @RequestParam(required = false)
                                                         @ApiParam("操作类型") Integer operationType,
                                                      @RequestParam(required = false)
                                                         @ApiParam("开始时间") Timestamp startTime,
                                                      @RequestParam(required = false)
                                                         @ApiParam("结束时间") Timestamp endTime,
                                                      @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(operationLogService.listOperationLog(userName, operationType, startTime, endTime, pageReqDTO));
    }
}
