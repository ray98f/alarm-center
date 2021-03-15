package com.zte.msg.alarmcenter.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.DataResponse;
import com.zte.msg.alarmcenter.dto.PageResponse;
import com.zte.msg.alarmcenter.dto.SimpleTokenInfo;
import com.zte.msg.alarmcenter.dto.req.AlarmLevelReqDTO;
import com.zte.msg.alarmcenter.dto.req.AlarmRuleReqDTO;
import com.zte.msg.alarmcenter.dto.req.DeviceReqDTO;
import com.zte.msg.alarmcenter.dto.req.DeviceReqModifyDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmLevelResDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmRuleDetailsResDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmRuleResDTO;
import com.zte.msg.alarmcenter.dto.res.DeviceResDTO;
import com.zte.msg.alarmcenter.service.AlarmLevelService;
import com.zte.msg.alarmcenter.service.AlarmRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/2/18 17:14
 */
@RestController
@RequestMapping("/alarmRule")
@Api(tags = "告警规则")
@Validated
public class AlarmRuleController {

    @Autowired
    private AlarmRuleService alarmRuleService;

    @PostMapping("/add")
    @ApiOperation(value = "新增告警规则")
    public DataResponse<Void> addAlarmRule(@RequestBody AlarmRuleReqDTO alarmRuleReqDTO, ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        SimpleTokenInfo tokenInfo = (SimpleTokenInfo) httpRequest.getAttribute("tokenInfo");
        alarmRuleService.addAlarmRule(alarmRuleReqDTO, tokenInfo.getUserId());
        return DataResponse.success();
    }

    @GetMapping("/list")
    @ApiOperation(value = "告警规则分页查询")
    public PageResponse<AlarmRuleResDTO> getAlarmRule(@RequestParam("page") Long page, @RequestParam("size") Long size,
                                                      @RequestParam(value = "isEnable",required = false) Integer isEnable,
                                                      @RequestParam(value = "name",required = false) String name,
                                                      @RequestParam(value = "type",required = false) Integer type) {
        Page<AlarmRuleResDTO> dtoPage = alarmRuleService.getAlarmRule(name, isEnable, type, page, size);
        return PageResponse.of(dtoPage, page, size);
    }

    @PutMapping("/upData/{id}")
    @ApiOperation(value = "修改告警规则")
    public DataResponse<Void> modifyAlarmRule(@PathVariable("id") Long id, @RequestBody AlarmRuleReqDTO alarmRuleReqDTO, ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        SimpleTokenInfo tokenInfo = (SimpleTokenInfo) httpRequest.getAttribute("tokenInfo");
        alarmRuleService.modifyAlarmRule(alarmRuleReqDTO, id, tokenInfo.getUserId());
        return DataResponse.success();
    }

    @GetMapping("/lookOver/{id}")
    @ApiOperation(value = "查看告警规则详情")
    public DataResponse<AlarmRuleDetailsResDTO> lookOverAlarmRuleDetails(@PathVariable("id") String id) {
        AlarmRuleDetailsResDTO dtoPage = alarmRuleService.lookOverAlarmRuleDetails(id);
        return DataResponse.of(dtoPage);
    }

}
