package com.zte.msg.alarmcenter.controller;

import com.zte.msg.alarmcenter.dto.DataResponse;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.PageResponse;
import com.zte.msg.alarmcenter.dto.req.PositionReqDTO;
import com.zte.msg.alarmcenter.dto.res.PositionResDTO;
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

    @PostMapping
    @ApiOperation(value = "新增位置")
    public <T> DataResponse<T> addPosition(@Valid PositionReqDTO reqDTO,
                                           @RequestParam(required = false) @ApiParam(value = "位置图标") MultipartFile icon,
                                           @RequestParam(required = false) @ApiParam(value = "地形图") MultipartFile topographic) {

        return DataResponse.success();
    }

    @PutMapping
    @ApiOperation(value = "修改位置")
    public <T> DataResponse<T> modifyPosition(@RequestParam Long id,
                                              @Valid PositionReqDTO reqDTO,
                                              @RequestParam(required = false) @ApiParam(value = "位置图标") MultipartFile icon,
                                              @RequestParam(required = false) @ApiParam(value = "地形图") MultipartFile topographic) {

        return DataResponse.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除位置")
    public <T> DataResponse<T> deletePosition(@RequestParam Long id) {

        return DataResponse.success();
    }



//    @GetMapping("/export")
//    @ApiOperation(value = "线路站点信息-列表导出")
//    public <T> DataResponse<T> exportLinePosition() {
//
//        return DataResponse.success();
//    }
//
//    @GetMapping("/{pId}/export")
//    @ApiOperation(value = "位置信息-列表导出")
//    public <T> DataResponse<T> exportPosition(@PathVariable @ApiParam(value = "线路id") Long pId) {
//        return DataResponse.success();
//    }

}
