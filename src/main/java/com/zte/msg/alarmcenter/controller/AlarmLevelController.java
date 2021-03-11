package com.zte.msg.alarmcenter.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.DataResponse;
import com.zte.msg.alarmcenter.dto.PageResponse;
import com.zte.msg.alarmcenter.dto.SimpleTokenInfo;
import com.zte.msg.alarmcenter.dto.req.AlarmLevelReqDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmLevelResDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmRuleResDTO;
import com.zte.msg.alarmcenter.service.AlarmLevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/2/18 17:14
 */
@RestController
@RequestMapping("/alarmLevel")
@Api(tags = "告警级别")
@Validated
public class AlarmLevelController {

    @Autowired
    private AlarmLevelService alarmLevelService;

    @GetMapping("/list")
    @ApiOperation(value = "告警级别列表")
    public PageResponse<AlarmLevelResDTO> getAlarmLevelList(@RequestParam("page") Long page, @RequestParam("size") Long size) {
        Page<AlarmLevelResDTO> alarmLevelList = alarmLevelService.getAlarmLevelList(page, size);
        return PageResponse.of(alarmLevelList, page, size);
    }



    @PutMapping("/upData/{id}")
    @ApiOperation(value = "修改")
    public DataResponse<Void> modifyAlarmLevel(@PathVariable("id") Long id,
                                                      @RequestBody AlarmLevelReqDTO alarmLevelReqDTO,
                                                      ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        SimpleTokenInfo tokenInfo = (SimpleTokenInfo) httpRequest.getAttribute("tokenInfo");
        alarmLevelService.modifyAlarmLevel(id, alarmLevelReqDTO,tokenInfo.getUserId());
        return DataResponse.success();
    }

}
