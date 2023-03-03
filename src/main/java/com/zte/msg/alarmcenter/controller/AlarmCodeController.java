package com.zte.msg.alarmcenter.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.annotation.LogMaker;
import com.zte.msg.alarmcenter.dto.DataResponse;
import com.zte.msg.alarmcenter.dto.PageResponse;
import com.zte.msg.alarmcenter.dto.SimpleTokenInfo;
import com.zte.msg.alarmcenter.dto.req.AlarmCodeReqDTO;
import com.zte.msg.alarmcenter.dto.req.DeviceReqDTO;
import com.zte.msg.alarmcenter.dto.req.DeviceReqModifyDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmCodeResDTO;
import com.zte.msg.alarmcenter.dto.res.DeviceResDTO;
import com.zte.msg.alarmcenter.service.AlarmCodeService;
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

@RestController
@RequestMapping("/alarmCode")
@Api(tags = "告警码")
@Validated
public class AlarmCodeController {

        @Autowired
        private AlarmCodeService alarmCodeService;

        @PostMapping("/import")
        @ApiOperation(value = "批量导入设备列表")
        @LogMaker(value = "批量导入设备列表")
        public DataResponse<T> importAlarmCode(@RequestParam MultipartFile file, ServletRequest request) {
                HttpServletRequest httpRequest = (HttpServletRequest) request;
                SimpleTokenInfo tokenInfo = (SimpleTokenInfo) httpRequest.getAttribute("tokenInfo");
                alarmCodeService.importDevice(file, tokenInfo == null ? null : tokenInfo.getUserName());
                return DataResponse.success();
        }

        @GetMapping("/export")
        @ApiOperation(value = "批量导出设备列表")
        @LogMaker(value = "批量导出设备列表")
        public DataResponse<T> exportAlarmCode(@RequestParam(required = false) @ApiParam(value = "告警码") Long alarmCode,
                                               @RequestParam(required = false) @ApiParam(value = "告警名称") String alarmName,
                                               @RequestParam(required = false) @ApiParam(value = "系统id") Long systemId,
                                               @RequestParam(required = false) @ApiParam(value = "告警级别") Long alarmLevelId,
                                               HttpServletResponse response) {
                alarmCodeService.exportDevice(alarmCode, alarmName, systemId, alarmLevelId, response);
                return DataResponse.success();
        }

        @PostMapping("/add")
        @ApiOperation(value = "新增告警码")
        @LogMaker(value = "新增告警码")
        public DataResponse<Void> addAlarmCode(@RequestBody AlarmCodeReqDTO alarmCodeReqDTO, ServletRequest request) {
                HttpServletRequest httpRequest = (HttpServletRequest) request;
                SimpleTokenInfo tokenInfo = (SimpleTokenInfo) httpRequest.getAttribute("tokenInfo");
                alarmCodeService.addAlarmCode(alarmCodeReqDTO, tokenInfo == null ? null : tokenInfo.getUserName());
                return DataResponse.success();
        }

        @PutMapping("/upData/{id}")
        @ApiOperation(value = "修改告警码")
        @LogMaker(value = "修改告警码")
        public DataResponse<Void> modifyAlarmCode(@PathVariable("id") Long id, @RequestBody AlarmCodeReqDTO alarmCodeReqDTO, ServletRequest request) {
                HttpServletRequest httpRequest = (HttpServletRequest) request;
                SimpleTokenInfo tokenInfo = (SimpleTokenInfo) httpRequest.getAttribute("tokenInfo");
                alarmCodeService.modifyAlarmCode(alarmCodeReqDTO, id, tokenInfo == null ? null : tokenInfo.getUserName());
                return DataResponse.success();
        }

        @GetMapping("/list")
        @ApiOperation(value = "告警码分页查询")
        public PageResponse<AlarmCodeResDTO> getAlarmCode(@RequestParam("page") Long page, @RequestParam("size") Long size,
                                                          @RequestParam(required = false) @ApiParam(value = "告警码") Long alarmCode,
                                                          @RequestParam(required = false) @ApiParam(value = "告警名称") String alarmName,
                                                          @RequestParam(required = false) @ApiParam(value = "系统id") Long systemId,
                                                          @RequestParam(required = false) @ApiParam(value = "告警级别") Long alarmLevelId) {
                Page<AlarmCodeResDTO> dtoPage = alarmCodeService.getAlarmCode(alarmCode, alarmName, systemId, alarmLevelId, page, size);
                return PageResponse.of(dtoPage, page, size);
        }

        @DeleteMapping("/delete/{id}")
        @ApiOperation(value = "删除告警码")
        @LogMaker(value = "删除告警码")
        public DataResponse<T> deleteAlarmCode(@PathVariable("id") Long id) {
                alarmCodeService.deleteAlarmCode(id);
                return DataResponse.success();
        }

}
