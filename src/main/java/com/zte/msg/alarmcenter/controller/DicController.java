package com.zte.msg.alarmcenter.controller;

import com.zte.msg.alarmcenter.annotation.PermissionCheck;
import com.zte.msg.alarmcenter.dto.DataResponse;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.PageResponse;
import com.zte.msg.alarmcenter.dto.req.DicDataReqDTO;
import com.zte.msg.alarmcenter.dto.req.DicDataUpdateReqDTO;
import com.zte.msg.alarmcenter.dto.req.DicReqDTO;
import com.zte.msg.alarmcenter.dto.req.DicUpdateReqDTO;
import com.zte.msg.alarmcenter.dto.res.DicDataResDTO;
import com.zte.msg.alarmcenter.dto.res.DicResDTO;
import com.zte.msg.alarmcenter.service.DicDataService;
import com.zte.msg.alarmcenter.service.DicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/1/19 9:57
 */
@RestController
@Validated
@Api(tags = "字典管理")
@RequestMapping("/dic")
public class DicController {

    @Resource
    private DicService dicService;

    @Resource
    private DicDataService dicDataService;

    @GetMapping("/page")
    @PermissionCheck(permissionName = "system:dic-type:list")
    @ApiOperation(value = "【字典管理】- 分页查询字典类型列表")
    public PageResponse<DicResDTO> getDics(@RequestParam(required = false) @ApiParam(value = "名称模糊查询字段") String name,
                                           @RequestParam(required = false) @ApiParam(value = "字典类型") String type,
                                           @RequestParam(required = false) @ApiParam(value = "字典状态：0-禁用，1-启用") Integer isEnable,
                                           @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(dicService.getDics(pageReqDTO.of(), name, type, isEnable));
    }

    @PostMapping
    @PermissionCheck(permissionName = "system:dic-type:add")
    @ApiOperation(value = "【字典管理】- 新增字典类型")
    public <T> DataResponse<T> addDic(@RequestBody @Valid DicReqDTO reqDTO) {

        dicService.addDic(reqDTO);
        return DataResponse.success();
    }

    @PutMapping
    @ApiOperation(value = "【字典管理】- 修改字典类型")
    @PermissionCheck(permissionName = "system:dic-type:modify")
    public <T> DataResponse<T> updateDic(@RequestBody @Valid DicUpdateReqDTO reqDTO) {

        dicService.updateDic(reqDTO);
        return DataResponse.success();
    }

    @DeleteMapping
    @ApiOperation(value = "【字典管理】- 删除字典类型")
    @PermissionCheck(permissionName = "system:dic-type:remove")
    public <T> DataResponse<T> deleteDic(@RequestBody Long[] dicIds) {
        dicService.deleteDic(dicIds);
        return DataResponse.success();
    }

    @GetMapping("/data")
    @ApiOperation(value = "【字典数据】- 分页查询")
    @PermissionCheck(permissionName = "system:dic-data:list")
    public PageResponse<DicDataResDTO> getDicDataByPage(@Valid PageReqDTO pageReqDTO,
                                                        @RequestParam(required = false) @ApiParam(value = "字典名称，下拉框显示名称，实际传type") String type,
                                                        @RequestParam(required = false) @ApiParam(value = "字典标签") String value,
                                                        @RequestParam(required = false) @ApiParam(value = "状态") Integer isEnable) {

        return PageResponse.of(dicDataService.getDicDataByPage(pageReqDTO.of(), type, value, isEnable));
    }

    @PostMapping("/data")
    @ApiOperation(value = "【字典数据】- 新增字典数据")
    @PermissionCheck(permissionName = "system:dic-data:add")
    public <T> DataResponse<T> addDicData(@RequestBody @Valid DicDataReqDTO reqDTO) {

        dicDataService.addDicData(reqDTO);
        return DataResponse.success();
    }

    @PutMapping("/data")
    @ApiOperation(value = "【字典数据】- 更新字典数据")
    @PermissionCheck(permissionName = "system:dic-data:modify")
    public <T> DataResponse<T> updateDicData(@RequestBody @Valid DicDataUpdateReqDTO reqDTO) {
        dicDataService.updateDicData(reqDTO);
        return DataResponse.success();
    }

    @DeleteMapping("/data")
    @ApiOperation(value = "【字典数据】- 删除字典数据")
    @PermissionCheck(permissionName = "system:dic-data:remove")
    public <T> DataResponse<T> deleteDicData(@RequestBody Long[] dicIds) {
        dicDataService.deleteDicData(dicIds);
        return DataResponse.success();
    }
}
