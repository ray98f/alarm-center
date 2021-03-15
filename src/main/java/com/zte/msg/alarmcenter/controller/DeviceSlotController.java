package com.zte.msg.alarmcenter.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.DataResponse;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.PageResponse;
import com.zte.msg.alarmcenter.dto.SimpleTokenInfo;
import com.zte.msg.alarmcenter.dto.req.DeviceReqDTO;
import com.zte.msg.alarmcenter.dto.req.DeviceReqModifyDTO;
import com.zte.msg.alarmcenter.dto.req.DeviceSlotReqDTO;
import com.zte.msg.alarmcenter.dto.res.DeviceResDTO;
import com.zte.msg.alarmcenter.dto.res.DeviceSlotResDTO;
import com.zte.msg.alarmcenter.service.DeviceService;
import com.zte.msg.alarmcenter.service.DeviceSlotService;
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

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/2/22 10:52
 */
@RestController
@RequestMapping("/deviceSlot")
@Api(tags = "设备槽位管理")
@Validated
public class DeviceSlotController {

    @Autowired
    private DeviceSlotService mDeviceSlotService;

    @PostMapping("/import")
    @ApiOperation(value = "批量导入设备槽位")
    public DataResponse<T> importDevice(@RequestParam MultipartFile file, ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        SimpleTokenInfo tokenInfo = (SimpleTokenInfo) httpRequest.getAttribute("tokenInfo");
        mDeviceSlotService.importDevice(file, tokenInfo == null ? null : tokenInfo.getUserId());
        return DataResponse.success();
    }

    @GetMapping("/export")
    @ApiOperation(value = "批量导出设备槽位")
    public DataResponse<T> exportDevice(@RequestParam(required = false) @ApiParam(value = "槽位名称模糊查询") String slotName,
                                        @RequestParam(required = false) @ApiParam(value = "设备名称模糊查询") String deviceName,
                                        @RequestParam(required = false) @ApiParam(value = "设备编号id") String deviceCode,
                                        @RequestParam(required = false) @ApiParam(value = "所属系统id") Long systemId,
                                        @RequestParam(required = false) @ApiParam(value = "设备位置id") Long positionId,
                                        HttpServletResponse response) {
        mDeviceSlotService.exportDevice(slotName, deviceName, deviceCode, systemId, positionId, response);
        return DataResponse.success();
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增设备槽位")
    public DataResponse<Void> addDeviceSlot(@RequestBody DeviceSlotReqDTO deviceSlotReqDTO, ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        SimpleTokenInfo tokenInfo = (SimpleTokenInfo) httpRequest.getAttribute("tokenInfo");
        mDeviceSlotService.addDeviceSlot(deviceSlotReqDTO, tokenInfo == null ? null : tokenInfo.getUserId());
        return DataResponse.success();
    }

    @PutMapping("/upData/{id}")
    @ApiOperation(value = "修改设备槽位")
    public DataResponse<Void> modifyDevice(@PathVariable("id") Long id, @RequestBody DeviceSlotReqDTO deviceSlotReqDTO, ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        SimpleTokenInfo tokenInfo = (SimpleTokenInfo) httpRequest.getAttribute("tokenInfo");
        mDeviceSlotService.modifyDevice(id, deviceSlotReqDTO, tokenInfo == null ? null : tokenInfo.getUserId());
        return DataResponse.success();
    }

    @GetMapping("/list")
    @ApiOperation(value = "设备分页查询")
    public PageResponse<DeviceSlotResDTO> getDevicesSlot(@RequestParam("page") Long page, @RequestParam("size") Long size,
                                                         @RequestParam(required = false) @ApiParam(value = "槽位名称模糊查询") String slotName,
                                                         @RequestParam(required = false) @ApiParam(value = "设备名称模糊查询") String deviceName,
                                                         @RequestParam(required = false) @ApiParam(value = "设备编号id") String deviceCode,
                                                         @RequestParam(required = false) @ApiParam(value = "所属系统id") Long systemId,
                                                         @RequestParam(required = false) @ApiParam(value = "设备位置id") Long positionId) {
        Page<DeviceSlotResDTO> dtoPage = mDeviceSlotService.getDevicesSlot(slotName, deviceName, deviceCode, systemId, positionId, page, size);
        return PageResponse.of(dtoPage, page, size);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除设备槽位")
    public DataResponse<T> deleteDevice(@PathVariable("id") Long id) {
        mDeviceSlotService.deleteDevice(id);
        return DataResponse.success();
    }
}
