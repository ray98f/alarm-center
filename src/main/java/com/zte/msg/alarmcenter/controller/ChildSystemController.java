package com.zte.msg.alarmcenter.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.annotation.LogMaker;
import com.zte.msg.alarmcenter.dto.DataResponse;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.PageResponse;
import com.zte.msg.alarmcenter.dto.SimpleTokenInfo;
import com.zte.msg.alarmcenter.dto.req.ChildSystemConfigReqDTO;
import com.zte.msg.alarmcenter.dto.res.ChildSystemConfigResDTO;
import com.zte.msg.alarmcenter.service.ChildSystemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/2/18 17:14
 */
@RestController
@RequestMapping("/subsystem")
@Api(tags = "子系统管理")
@Validated
public class ChildSystemController {

    @Autowired
    private ChildSystemService myChildSystemService;

    @GetMapping("/list")
    @ApiOperation(value = "子系统列表")
    public PageResponse<ChildSystemConfigResDTO> getChildSystemConfigs(@RequestParam("page") Long page, @RequestParam("size") Long size) {
        Page<ChildSystemConfigResDTO> childSystemConfigs = myChildSystemService.getChildSystemConfigs(page, size);
        return PageResponse.of(childSystemConfigs, page, size);
    }

    @PostMapping("/add")
    @ApiOperation(value = "子系统-新增")
    @LogMaker(value = "子系统-新增")
    public DataResponse<Void> addChildSystemConfigs(@RequestBody ChildSystemConfigReqDTO childSystemConfigReqDTO,
                                                    ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        SimpleTokenInfo tokenInfo = (SimpleTokenInfo) httpRequest.getAttribute("tokenInfo");
        childSystemConfigReqDTO.setUserId(tokenInfo == null ? null : tokenInfo.getUserName());
        myChildSystemService.addChildSystemConfigs(childSystemConfigReqDTO);
        return DataResponse.success();
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "子系统-删除")
    @LogMaker(value = "子系统-删除")
    public DataResponse<Void> removeChildSystem(@PathVariable("id") Long id) {
        myChildSystemService.removeChildSystem(id);
        return DataResponse.success();
    }

    @PutMapping("/upData/{id}")
    @ApiOperation(value = "子系统-修改")
    @LogMaker(value = "子系统-修改")
    public DataResponse<Void> modifyChildSystemConfig(@PathVariable("id") Long id,
                                                      @RequestBody ChildSystemConfigReqDTO childSystemConfigReqDTO,
                                                      ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        SimpleTokenInfo tokenInfo = (SimpleTokenInfo) httpRequest.getAttribute("tokenInfo");
        childSystemConfigReqDTO.setUserId(tokenInfo == null ? null : tokenInfo.getUserName());
        myChildSystemService.modifyChildSystemConfig(id, childSystemConfigReqDTO);
        return DataResponse.success();
    }
}
