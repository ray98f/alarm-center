package com.zte.msg.alarmcenter.controller;

import com.zte.msg.alarmcenter.dto.DataResponse;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.PageResponse;
import com.zte.msg.alarmcenter.dto.res.PositionResDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/2/19 14:14
 */
@RestController
@RequestMapping("/position")
@Api(tags = "位置管理")
@Validated
public class PositionController {

    @GetMapping("/list")
    @ApiOperation(value = "获取线路站点信息列表")
    public PageResponse<PositionResDTO> getLinePositions(@Valid PageReqDTO pageReqDTO) {

        return null;
    }

    @GetMapping("/{pId}")
    @ApiOperation(value = "位置信息-分页查询")
    public PageResponse<PositionResDTO> getPositions(@Valid PageReqDTO pageReqDTO,
                                                     @PathVariable @ApiParam(value = "线路id") Long pId,
                                                     @RequestParam(required = false) @ApiParam(value = "位置名称模糊查询") String name) {

        return null;
    }

    @GetMapping("/export")
    @ApiOperation(value = "线路站点信息-列表导出")
    public <T> DataResponse<T> exportLinePosition() {

        return DataResponse.success();
    }

    @GetMapping("/{pId}/export")
    @ApiOperation(value = "位置信息-列表导出")
    public <T> DataResponse<T> exportPosition(@PathVariable @ApiParam(value = "线路id") Long pId) {
        return DataResponse.success();
    }

}
