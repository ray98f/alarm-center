package com.zte.msg.alarmcenter.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.DataResponse;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.PageResponse;
import com.zte.msg.alarmcenter.dto.SimpleTokenInfo;
import com.zte.msg.alarmcenter.dto.req.DeviceReqDTO;
import com.zte.msg.alarmcenter.dto.req.DeviceReqModifyDTO;
import com.zte.msg.alarmcenter.dto.req.SnmpAlarmCodeReqDTO;
import com.zte.msg.alarmcenter.dto.req.SnmpSlotModifyReqDTO;
import com.zte.msg.alarmcenter.dto.res.DeviceResDTO;
import com.zte.msg.alarmcenter.dto.res.SnmpAlarmCodeResDTO;
import com.zte.msg.alarmcenter.dto.res.SnmpSlotResDTO;
import com.zte.msg.alarmcenter.service.SnmpService;
import com.zte.msg.alarmcenter.utils.SendTrapUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/2/22 10:52
 */
@RestController
@RequestMapping("/snmp")
@Api(tags = "SNMP配置")
@Validated
public class SnmpController {

    @Autowired
    private SnmpService mySlotService;

    @PostMapping("/slot/import")
    @ApiOperation(value = "批量导入SNMP槽位")
    public DataResponse<T> importSnmpSlot(@RequestParam MultipartFile file, ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        SimpleTokenInfo tokenInfo = (SimpleTokenInfo) httpRequest.getAttribute("tokenInfo");
        mySlotService.importDevice(file, tokenInfo == null ? null : tokenInfo.getUserId());
        return DataResponse.success();
    }

    @GetMapping("/slot/export")
    @ApiOperation(value = "批量导出SNMP槽位")
    public DataResponse<T> exportSnmpSlot(@RequestParam(required = false) @ApiParam(value = "SNMP槽位") String snmpSlotName,
                                          @RequestParam(required = false) @ApiParam(value = "系统id") Long systemId,
                                          @RequestParam(required = false) @ApiParam(value = "设备编号查询") Long siteId,
                                          HttpServletResponse response) {
        mySlotService.exportDevice(snmpSlotName, systemId, siteId, response);
        return DataResponse.success();
    }

    @PostMapping("/slot/add")
    @ApiOperation(value = "新增SNMP槽位")
    public DataResponse<Void> addSnmpSlot(@RequestBody SnmpSlotModifyReqDTO slotModifyReqDTO, ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        SimpleTokenInfo tokenInfo = (SimpleTokenInfo) httpRequest.getAttribute("tokenInfo");
        mySlotService.addSnmpSlot(slotModifyReqDTO, tokenInfo == null ? null : tokenInfo.getUserId());
        return DataResponse.success();
    }

    @PutMapping("/slot/upData/{id}")
    @ApiOperation(value = "修改SNMP槽位")
    public DataResponse<Void> modifySnmpSlot(@PathVariable("id") Long id, @RequestBody SnmpSlotModifyReqDTO slotModifyReqDTO, ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        SimpleTokenInfo tokenInfo = (SimpleTokenInfo) httpRequest.getAttribute("tokenInfo");
        mySlotService.modifySnmpSlot(slotModifyReqDTO, id, tokenInfo == null ? null : tokenInfo.getUserId());
        return DataResponse.success();
    }

    @GetMapping("/slot/list")
    @ApiOperation(value = "SNMP槽位查询")
    public PageResponse<SnmpSlotResDTO> getSnmpSlot(@RequestParam("page") Long page, @RequestParam("size") Long size,
                                                    @RequestParam(required = false) @ApiParam(value = "SNMP槽位") String snmpSlotName,
                                                    @RequestParam(required = false) @ApiParam(value = "系统id") Long systemId,
                                                    @RequestParam(required = false) @ApiParam(value = "设备编号查询") Long positionId) {
        Page<SnmpSlotResDTO> dtoPage = mySlotService.getSnmpSlot(snmpSlotName, systemId, positionId, page, size);
        return PageResponse.of(dtoPage, page, size);
    }

    @DeleteMapping("/slot/delete/{id}")
    @ApiOperation(value = "删除SNMP槽位")
    public DataResponse<T> deleteSnmpSlot(@PathVariable("id") Long id) {
        mySlotService.deleteSnmpSlot(id);
        return DataResponse.success();
    }


    @PostMapping("/code/import")
    @ApiOperation(value = "批量导入SNMP告警码")
    public DataResponse<T> importSnmpAlarmCode(@RequestParam MultipartFile file, ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        SimpleTokenInfo tokenInfo = (SimpleTokenInfo) httpRequest.getAttribute("tokenInfo");
        mySlotService.importSnmpAlarmCode(file, tokenInfo == null ? null : tokenInfo.getUserId());
        return DataResponse.success();
    }

    @GetMapping("/code/export")
    @ApiOperation(value = "批量导出SNMP告警码")
    public DataResponse<T> exportSnmpAlarmCode(@RequestParam(required = false) @ApiParam(value = "SNMP告警码") String alarmCode,
                                               @RequestParam(required = false) @ApiParam(value = "系统id") Long systemId,
                                               HttpServletResponse response) {
        mySlotService.exportSnmpAlarmCode(alarmCode, systemId, response);
        return DataResponse.success();
    }


    @PostMapping("/code/add")
    @ApiOperation(value = "新增SNMP告警码")
    public DataResponse<Void> addSSnmpAlarmCode(@RequestBody SnmpAlarmCodeReqDTO snmpAlarmCode, ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        SimpleTokenInfo tokenInfo = (SimpleTokenInfo) httpRequest.getAttribute("tokenInfo");
        mySlotService.addSSnmpAlarmCode(snmpAlarmCode, tokenInfo == null ? null : tokenInfo.getUserId());
        return DataResponse.success();
    }

    @PutMapping("/code/upData/{id}")
    @ApiOperation(value = "修改SNMP告警码")
    public DataResponse<Void> modifySnmpAlarmCode(@PathVariable("id") Long id, @RequestBody SnmpAlarmCodeReqDTO snmpAlarmCode, ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        SimpleTokenInfo tokenInfo = (SimpleTokenInfo) httpRequest.getAttribute("tokenInfo");
        mySlotService.modifySnmpAlarmCode(snmpAlarmCode, id, tokenInfo == null ? null : tokenInfo.getUserId());
        return DataResponse.success();
    }

    @GetMapping("/code/list")
    @ApiOperation(value = "设备分页查询")
    public PageResponse<SnmpAlarmCodeResDTO> getSnmpAlarmCode(@RequestParam("page") Long page, @RequestParam("size") Long size,
                                                              @RequestParam(required = false) @ApiParam(value = "告警码") Long systemId,
                                                              @RequestParam(required = false) @ApiParam(value = "设备编号查询") String code) {
        Page<SnmpAlarmCodeResDTO> dtoPage = mySlotService.getSnmpAlarmCode(code, systemId, page, size);
        return PageResponse.of(dtoPage, page, size);
    }

    @DeleteMapping("/code/delete/{id}")
    @ApiOperation(value = "删除SNMP告警码")
    public DataResponse<T> deleteSnmpAlarmCode(@PathVariable("id") Long id) {
        mySlotService.deleteSnmpAlarmCode(id);
        return DataResponse.success();
    }

}
