package com.zte.msg.alarmcenter.controller;

import com.zte.msg.alarmcenter.dto.DataResponse;
import com.zte.msg.alarmcenter.dto.req.ChildSystemConfigReqDTO;
import com.zte.msg.alarmcenter.dto.res.ChildSystemConfigResDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/child-system")
@Api(tags = "子系统管理")
@Validated
public class ChildSystemController {

    @DeleteMapping
    @ApiOperation(value = "子系统-删除")
    public <T> DataResponse<T> removeChildSystem(@RequestParam Long id) {
        return DataResponse.success();
    }

    @GetMapping("/export")
    @ApiOperation(value = "子系统-导出列表")
    public <T> DataResponse<T> export() {
        return DataResponse.success();
    }

    @GetMapping("/list")
    @ApiOperation(value = "子系统列表")
    public DataResponse<List<ChildSystemConfigResDTO>> getChildSystemConfigs() {

        return null;
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public <T> DataResponse<T> modifyChildSystemConfig(@Valid ChildSystemConfigReqDTO reqDTO) {

        return DataResponse.success();
    }

}
