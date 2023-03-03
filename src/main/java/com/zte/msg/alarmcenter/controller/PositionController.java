package com.zte.msg.alarmcenter.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.annotation.LogMaker;
import com.zte.msg.alarmcenter.dto.DataResponse;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.PageResponse;
import com.zte.msg.alarmcenter.dto.SimpleTokenInfo;
import com.zte.msg.alarmcenter.dto.req.PositionReqDTO;
import com.zte.msg.alarmcenter.dto.res.PositionResDTO;
import com.zte.msg.alarmcenter.service.PositionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private PositionService myPositionService;

    @GetMapping("/list")
    @ApiOperation(value = "位置信息-分页查询")
    public PageResponse<PositionResDTO> getPositions(@RequestParam("page") Long page,@RequestParam("size") Long size) {
        Page<PositionResDTO> resPage = myPositionService.getPositions(page,size);
        return PageResponse.of(resPage, page, size);
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增位置")
    @LogMaker(value = "新增位置")
    public DataResponse<Void> addPosition(@RequestBody PositionReqDTO reqDTO, ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        SimpleTokenInfo tokenInfo = (SimpleTokenInfo) httpRequest.getAttribute("tokenInfo");
        reqDTO.setUserId(tokenInfo==null?null:tokenInfo.getUserName());
        myPositionService.addPosition(reqDTO);
        return DataResponse.success();
    }

    @PutMapping("/upData/{id}")
    @ApiOperation(value = "修改位置")
    @LogMaker(value = "修改位置")
    public DataResponse<Void> modifyPosition(@PathVariable("id") Long id,
                                             @RequestBody PositionReqDTO reqDTO,
                                             ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        SimpleTokenInfo tokenInfo = (SimpleTokenInfo) httpRequest.getAttribute("tokenInfo");
        reqDTO.setUserId(tokenInfo==null?null:tokenInfo.getUserName());
        myPositionService.modifyPosition(id, reqDTO);
        return DataResponse.success();
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除位置")
    @LogMaker(value = "删除位置")
    public DataResponse<Void> deletePosition(@PathVariable("id") Long id) {
        myPositionService.deletePosition(id);
        return DataResponse.success();
    }

}
