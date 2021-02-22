package com.zte.msg.alarmcenter.controller;

import com.zte.msg.alarmcenter.dto.DataResponse;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.PageResponse;
import com.zte.msg.alarmcenter.dto.req.DeviceReqDTO;
import com.zte.msg.alarmcenter.dto.req.DeviceReqModifyDTO;
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
 * @date 2021/2/22 10:52
 */
@RestController
@RequestMapping("/device")
@Api(tags = "设备管理")
@Validated
public class DeviceController {

    @PostMapping("/import")
    @ApiOperation(value = "批量导入设备列表")
    public <T> DataResponse<T> importDevice(@RequestParam MultipartFile deviceFile) {
        return DataResponse.success();
    }

    @GetMapping("/export")
    @ApiOperation(value = "批量导出设备列表")
    public <T> DataResponse<T> exportDevice(@RequestParam(required = false) @ApiParam(value = "设备名称模糊查询") String name,
                                            @RequestParam(required = false) @ApiParam(value = "设备编号查询") String deviceCode,
                                            @RequestParam(required = false) @ApiParam(value = "系统id") Long systemId,
                                            @RequestParam(required = false) @ApiParam(value = "位置id") Long positionId) {
        return DataResponse.success();
    }

    @PostMapping
    @ApiOperation(value = "新增设备")
    public <T> DataResponse<T> addDevice(@RequestBody DeviceReqDTO deviceReqDTO) {
        return DataResponse.success();
    }

    @PutMapping
    @ApiOperation(value = "修改设备")
    public <T> DataResponse<T> modifyDevice(@RequestBody DeviceReqModifyDTO reqModifyDTO) {

        return DataResponse.success();
    }

    @GetMapping
    @ApiOperation(value = "设备分页查询")
    public PageResponse<DeviceReqDTO> getDevices(@Valid PageReqDTO page,
                                                 @RequestParam(required = false) @ApiParam(value = "设备名称模糊查询") String name,
                                                 @RequestParam(required = false) @ApiParam(value = "设备编号查询") String deviceCode,
                                                 @RequestParam(required = false) @ApiParam(value = "系统id") Long systemId,
                                                 @RequestParam(required = false) @ApiParam(value = "位置id") Long positionId) {

        return null;
    }

}
