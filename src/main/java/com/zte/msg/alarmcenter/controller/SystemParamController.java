package com.zte.msg.alarmcenter.controller;

import com.zte.msg.alarmcenter.dto.DataResponse;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.PageResponse;
import com.zte.msg.alarmcenter.entity.SystemParameter;
import com.zte.msg.alarmcenter.service.SystemParamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2021/2/22 9:07
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/system")
@Api(tags = "系统参数管理")
@Validated
public class SystemParamController {

    @Resource
    private SystemParamService systemParamService;

    /**
     * 获取全部系统参数列表
     *
     * @return List<SystemParameter>
     */
    @GetMapping
    @ApiOperation(value = "获取全部系统参数列表")
    public PageResponse<SystemParameter> pageSystemParam(@RequestParam(required = false)
                                                         @ApiParam("参数名模糊查询") String parameter,
                                                         @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(systemParamService.listSystemParam(parameter, pageReqDTO));
    }

    /**
     * 获取全部系统参数列表
     *
     * @return List<SystemParameter>
     */
    @GetMapping("/all")
    @ApiOperation(value = "获取全部系统参数列表")
    public DataResponse<List<SystemParameter>> listAllSystemParam() {
        return DataResponse.of(systemParamService.listAllSystemParam());
    }

    /**
     * 批量删除系统参数
     * @param ids
     * @return <T>
     */
    @DeleteMapping
    @ApiOperation(value = "批量删除系统参数")
    public <T> DataResponse<T> deleteSystemParam(@Valid @RequestBody List<Long> ids) {
        systemParamService.deleteSystemParam(ids);
        return DataResponse.success();
    }

    /**
     * 修改系统参数
     * @param systemParameter
     * @return <T>
     */
    @PostMapping
    @ApiOperation(value = "批量删除系统参数")
    public <T> DataResponse<T> updateSystemParam(@Valid @RequestBody SystemParameter systemParameter) {
        systemParamService.updateSystemParam(systemParameter);
        return DataResponse.success();
    }

    /**
     * 新增系统参数
     * @param systemParameter
     * @return <T>
     */
    @PutMapping
    @ApiOperation(value = "批量删除系统参数")
    public <T> DataResponse<T> insertSystemParam(@Valid @RequestBody SystemParameter systemParameter) {
        systemParamService.insertSystemParam(systemParameter);
        return DataResponse.success();
    }

}

