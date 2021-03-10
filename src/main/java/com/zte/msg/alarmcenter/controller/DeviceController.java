package com.zte.msg.alarmcenter.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.zte.msg.alarmcenter.dto.DataResponse;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.PageResponse;
import com.zte.msg.alarmcenter.dto.SimpleTokenInfo;
import com.zte.msg.alarmcenter.dto.req.DeviceReqDTO;
import com.zte.msg.alarmcenter.dto.req.DeviceReqModifyDTO;
import com.zte.msg.alarmcenter.dto.res.DeviceResDTO;
import com.zte.msg.alarmcenter.service.DeviceService;
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
@RequestMapping("/device")
@Api(tags = "设备管理")
@Validated
public class DeviceController {

    @Autowired
    private DeviceService myDevice;

    @PostMapping("/import")
    @ApiOperation(value = "批量导入设备列表")
    public DataResponse<T> importDevice(@RequestParam MultipartFile deviceFile, ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        SimpleTokenInfo tokenInfo = (SimpleTokenInfo) httpRequest.getAttribute("tokenInfo");
        myDevice.importDevice(deviceFile, tokenInfo == null ? null : tokenInfo.getUserId());
        return DataResponse.success();
    }

    @GetMapping("/export")
    @ApiOperation(value = "批量导出设备列表")
    public DataResponse<T> exportDevice(@RequestParam(required = false) @ApiParam(value = "设备名称模糊查询") String name,
                                        @RequestParam(required = false) @ApiParam(value = "设备编号查询") String deviceCode,
                                        @RequestParam(required = false) @ApiParam(value = "系统id") Long systemId,
                                        @RequestParam(required = false) @ApiParam(value = "位置id") Long positionId,
                                        HttpServletResponse response) {
        myDevice.exportDevice(name, deviceCode, systemId, positionId, response);
        return DataResponse.success();
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增设备")
    public DataResponse<Void> addDevice(@RequestBody DeviceReqDTO deviceReqDTO, ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        SimpleTokenInfo tokenInfo = (SimpleTokenInfo) httpRequest.getAttribute("tokenInfo");
        myDevice.addDevice(deviceReqDTO, tokenInfo == null ? null : tokenInfo.getUserId());
        return DataResponse.success();
    }

    @PutMapping("/upData/{id}")
    @ApiOperation(value = "修改设备")
    public DataResponse<Void> modifyDevice(@PathVariable("id") Long id, @RequestBody DeviceReqModifyDTO reqModifyDTO, ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        SimpleTokenInfo tokenInfo = (SimpleTokenInfo) httpRequest.getAttribute("tokenInfo");
        myDevice.modifyDevice(reqModifyDTO, id, tokenInfo == null ? null : tokenInfo.getUserId());
        return DataResponse.success();
    }

    @GetMapping("/list")
    @ApiOperation(value = "设备分页查询")
    public PageResponse<DeviceResDTO> getDevices(@RequestParam("page") Long page,@RequestParam("size") Long size,
                                                 @RequestParam(required = false) @ApiParam(value = "设备名称模糊查询") String name,
                                                 @RequestParam(required = false) @ApiParam(value = "设备编号查询") String deviceCode,
                                                 @RequestParam(required = false) @ApiParam(value = "系统id") Long systemId,
                                                 @RequestParam(required = false) @ApiParam(value = "位置id") Long positionId) {
        Page<DeviceResDTO> dtoPage = myDevice.getDevices(name, deviceCode, systemId, positionId, page,size);
        return PageResponse.of(dtoPage, page, size);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除设备")
    public DataResponse<T> deleteDevice(@PathVariable("id") Long id) {
        myDevice.deleteDevice(id);
        return DataResponse.success();
    }
}
