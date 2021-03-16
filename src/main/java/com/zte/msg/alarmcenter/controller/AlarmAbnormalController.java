package com.zte.msg.alarmcenter.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.PageResponse;
import com.zte.msg.alarmcenter.dto.res.AlarmAbnormalResDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmCodeResDTO;
import com.zte.msg.alarmcenter.service.AlarmAbnormalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alarmAbnormal")
@Api(tags = "告警异常")
@Validated
public class AlarmAbnormalController {

    @Autowired
    private AlarmAbnormalService alarmAbnormalService;

    @GetMapping("/list")
    @ApiOperation(value = "告警异常分页查询")
    public PageResponse<AlarmAbnormalResDTO> getAlarmAbnormal(@RequestParam("page") Long page, @RequestParam("size") Long size,
                                                              @RequestParam(required = false) @ApiParam(value = "起始时间") String startTime,
                                                              @RequestParam(required = false) @ApiParam(value = "结束时间") String endTime,
                                                              @RequestParam(required = false) @ApiParam(value = "系统编号") Long systemCode) {
        Page<AlarmAbnormalResDTO> dtoPage = alarmAbnormalService.getAlarmAbnormal(startTime, endTime, systemCode, page,size);
        return PageResponse.of(dtoPage, page, size);
    }

}
