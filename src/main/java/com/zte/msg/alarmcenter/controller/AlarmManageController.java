package com.zte.msg.alarmcenter.controller;

import com.zte.msg.alarmcenter.dto.DataResponse;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.PageResponse;
import com.zte.msg.alarmcenter.dto.req.*;
import com.zte.msg.alarmcenter.dto.res.AlarmRuleResDTO;
import com.zte.msg.alarmcenter.dto.res.SlotResDTO;
import com.zte.msg.alarmcenter.dto.res.SnmpAlarmCodeResDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/2/23 9:10
 */
@RequestMapping("/alarm")
@Api(tags = "子系统管理")
@Validated
@RestController
public class AlarmManageController {

    @GetMapping("/slot")
    @ApiOperation(value = "设备槽位-分页查询")
    public PageResponse<SlotResDTO> getSlot(@Valid PageReqDTO pageReqDTO,
                                            @RequestParam @ApiParam(value = "设备id") Long deviceId,
                                            @RequestParam(required = false) @ApiParam(value = "设备名称模糊查询") String deviceName) {
        return null;
    }

    @PostMapping("/slot")
    @ApiOperation(value = "设备槽位-新增槽位")
    public <T> DataResponse<T> addSlot(@RequestBody SlotReqDTO slotReqDTO) {

        return DataResponse.success();
    }

    @PutMapping("/slot")
    @ApiOperation(value = "设备槽位-修改槽位")
    public <T> DataResponse<T> modifySlot(@RequestBody SlotReqDTO slotReqDTO) {

        return DataResponse.success();
    }

    @DeleteMapping("/slot")
    @ApiOperation(value = "设备槽位-修改槽位")
    public <T> DataResponse<T> modifySlot(@RequestParam Long slotId) {

        return DataResponse.success();
    }

    @PostMapping("/slot/import")
    @ApiOperation(value = "设备槽位-导入")
    public <T> DataResponse<T> importSlots(MultipartFile json) {
        return DataResponse.success();
    }

    @GetMapping("/slot/export")
    @ApiOperation(value = "设备槽位-导出")
    public <T> DataResponse<T> exportSlots(@RequestParam(required = false) @ApiParam("槽位名称模糊查询") String slotName) {
        return DataResponse.success();
    }

    @PostMapping("/rule")
    @ApiOperation(value = "告警规则-新增")
    public <T> DataResponse<T> addAlarmRule(@RequestBody @Valid AlarmRuleReqDTO alarmRuleReqDTO) {
        return DataResponse.success();
    }

    @GetMapping("/rule/page")
    @ApiOperation(value = "告警规则-分页查询")
    public PageResponse<AlarmRuleResDTO> getAlarmRules(@Valid PageReqDTO page,
                                                       @RequestParam(required = false) @ApiParam("告警名称模糊查询") String name,
                                                       @RequestParam(required = false) @ApiParam("告警规则类型") Integer type,
                                                       @RequestParam(required = false) @ApiParam("告警规则状态") Integer isEnable) {
        return null;
    }

    @GetMapping("/rule")
    @ApiOperation(value = "告警规则-查看")
    public DataResponse<AlarmRuleResDTO> getAlarmRule(@RequestParam @ApiParam("告警规则id") Long id) {

        return null;
    }

    @GetMapping("/level")
    @ApiOperation(value = "告警级别-分页查询")
    public PageResponse<AlarmRuleResDTO> getAlarmLevels(@Valid PageReqDTO page) {
        return null;
    }

    @PostMapping("/level")
    @ApiOperation(value = "告警级别-新增")
    public <T> DataResponse<T> addAlarmLevel(@RequestBody AlarmLevelReqDTO reqDTO) {

        return DataResponse.success();
    }

    @PutMapping("/level")
    @ApiOperation(value = "告警级别-修改")
    public <T> DataResponse<T> modifyAlarmLevel(@RequestBody AlarmLevelUpdateReqDTO reqDTO) {

        return DataResponse.success();
    }

    @DeleteMapping("/level")
    @ApiOperation(value = "告警级别-删除")
    public <T> DataResponse<T> removeAlarmLevel(@RequestParam Long id) {

        return DataResponse.success();
    }

    @PostMapping("/snmp/slot")
    @ApiOperation("SNMP转换配置-新增SNMP槽位")
    public <T> DataResponse<T> addSnmpSlot(@RequestBody SnmpSlotReqDTO snmpSlotReqDTO) {
        return DataResponse.success();
    }

    @PostMapping("/snmp/slot/import")
    @ApiOperation("SNMP转换配置-导入")
    public <T> DataResponse<T> importSnmpSlot(MultipartFile file) {
        return DataResponse.success();
    }

    @GetMapping("/snmp/slot/export")
    @ApiOperation("SNMP转换配置-导出")
    public <T> DataResponse<T> exportSnmpSlot(@RequestParam(required = false) @ApiParam("SNMP槽位名称模糊查询") String name,
                                              @RequestParam(required = false) @ApiParam("系统") Long systemId,
                                              @RequestParam(required = false) @ApiParam("站点") Long positionId) {
        return DataResponse.success();
    }

    @GetMapping("/snmp/slot")
    @ApiOperation("SNMP转换配置-分页查询")
    public <T> DataResponse<T> getSnmpSlots(@Valid PageReqDTO pageReqDTO,
                                            @RequestParam(required = false) @ApiParam("SNMP槽位名称模糊查询") String name,
                                            @RequestParam(required = false) @ApiParam("系统") Long systemId,
                                            @RequestParam(required = false) @ApiParam("站点") Long positionId) {
        return DataResponse.success();
    }

    @PutMapping("/snmp/slot")
    @ApiOperation("SNMP转换配置-修改")
    public <T> DataResponse<T> modifySnmpSlot(@RequestBody SnmpSlotModifyReqDTO reqDTO) {

        return DataResponse.success();
    }

    @DeleteMapping("/snmp/slot")
    @ApiOperation("SNMP转换配置-删除")
    public <T> DataResponse<T> removeSnmpSlot(@RequestParam Long id) {
        return DataResponse.success();
    }

    @GetMapping("/snmp/code")
    @ApiOperation("SNMP告警码-分页查询")
    public PageResponse<SnmpAlarmCodeResDTO> getSnmpAlarmCodes(@RequestParam(required = false) @ApiParam("告警码模糊查询") String code,
                                                               @RequestParam(required = false) @ApiParam("所属系统") Long systemId) {

        return null;
    }

    @PostMapping("/snmp/code")
    @ApiOperation("SNMP告警码-新增SNMP告警码")
    public <T> DataResponse<T> addSnmpCode(@RequestBody @Valid SnmpAlarmCodeReqDTO snmpAlarmCodeReqDTO) {

        return DataResponse.success();
    }

    @PostMapping("/snmp/code/import")
    @ApiOperation("SNMP告警码-导入")
    public <T> DataResponse<T> importSnmpCode(MultipartFile file) {

        return DataResponse.success();
    }

    @GetMapping("/snmp/code/export")
    @ApiOperation("SNMP告警码-导出")
    public <T> DataResponse<T> exportSnmpCode(MultipartFile file) {
        return DataResponse.success();
    }

    @DeleteMapping("/snmp/code")
    @ApiOperation("SNMP告警码-删除")
    public <T> DataResponse<T> deleteSnmpCode(@RequestParam @ApiParam(value = "告警码id", required = true) Long id) {
        return DataResponse.success();
    }

}
