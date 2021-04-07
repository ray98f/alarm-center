package com.zte.msg.alarmcenter.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.DataResponse;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.PageResponse;
import com.zte.msg.alarmcenter.entity.MsgConfig;
import com.zte.msg.alarmcenter.entity.MsgPushHistory;
import com.zte.msg.alarmcenter.service.MsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.List;

/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2021/4/7 8:56
 */
@RestController
@RequestMapping("/msg")
@Api(tags = "消息推送信息")
@Validated
public class MsgController {

    @Autowired
    public MsgService msgService;

    @GetMapping("/config")
    @ApiOperation(value = "获取告警推送配置列表")
    public PageResponse<MsgConfig> pageMsgConfig(@RequestParam(required = false)
                                                     @ApiParam("名称") String name,
                                                 @RequestParam(required = false)
                                                     @ApiParam("推送类型") Integer type,
                                                 @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(msgService.pageMsgConfig(name, type, pageReqDTO));
    }

    @PostMapping("/config")
    @ApiOperation(value = "修改告警推送配置列表")
    public DataResponse<T> editMsgConfig(@RequestBody @Valid MsgConfig msgConfig) {
        msgService.editMsgConfig(msgConfig);
        return DataResponse.success();
    }

    @DeleteMapping("/config")
    @ApiOperation(value = "删除告警推送配置列表")
    public DataResponse<T> deleteMsgConfig(@Valid @RequestBody List<Long> ids) {
        msgService.deleteMsgConfig(ids);
        return DataResponse.success();
    }

    @PutMapping("/config")
    @ApiOperation(value = "新增告警推送配置列表")
    public DataResponse<T> insertMsgConfig(@RequestBody @Valid MsgConfig msgConfig) {
        msgService.insertMsgConfig(msgConfig);
        return DataResponse.success();
    }

    @GetMapping("/history")
    @ApiOperation(value = "获取告警记录推送历史")
    public PageResponse<MsgPushHistory> pageMsgHistory(@Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(msgService.pageMsgHistory(pageReqDTO));
    }
}
