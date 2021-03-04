package com.zte.msg.alarmcenter.controller;

import com.zte.msg.alarmcenter.annotation.LogMaker;
import com.zte.msg.alarmcenter.annotation.PermissionCheck;
import com.zte.msg.alarmcenter.dto.DataResponse;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.PageResponse;
import com.zte.msg.alarmcenter.dto.req.RoleReqDTO;
import com.zte.msg.alarmcenter.entity.Role;
import com.zte.msg.alarmcenter.service.RoleService;
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
 * @date 2021/2/20 13:42
 */
@Slf4j
@RestController
@RequestMapping("/role")
@Api(tags = "角色管理")
@Validated
public class RoleController {

    @Resource
    private RoleService roleService;

    /**
     * 获取所有角色信息
     *
     * @return List<Role>
     */
    @GetMapping("/all")
    @ApiOperation(value = "获取所有角色信息")
    public DataResponse<List<Role>> listAllRole() {
        return DataResponse.of(roleService.listAllRole());
    }

    /**
     * 分页获取角色信息
     * @param status
     * @param roleName
     * @param pageReqDTO
     * @return
     */
    @PermissionCheck(permissionName = {"system:role:list"})
//    @LogMaker(value = "分页获取角色信息")
    @GetMapping
    @ApiOperation(value = "分页获取角色信息")
    public PageResponse<Role> listRole(@RequestParam(required = false)
                                           @ApiParam("状态") Integer status,
                                       @RequestParam(required = false)
                                           @ApiParam("角色名称") String roleName,
                                       @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(roleService.listRole(status, roleName, pageReqDTO));
    }

    /**
     * 删除角色
     *
     * @param ids 角色信息id
     * @return <T>
     */
    @PermissionCheck(permissionName = {"system:role:remove"})
    @DeleteMapping
    @ApiOperation(value = "删除角色")
    public <T> DataResponse<T> deleteRole(@Valid @RequestBody List<Long> ids) {
        roleService.deleteRole(ids);
        return DataResponse.success();
    }

    /**
     * 新增角色
     *
     * @param roleReqDTO 新增角色信息
     * @return <T>
     */
    @PermissionCheck(permissionName = {"system:role:add"})
    @PutMapping
    @ApiOperation(value = "新增角色")
    public <T> DataResponse<T> insertRole(@Valid @RequestBody RoleReqDTO roleReqDTO){
        roleService.insertRole(roleReqDTO);
        return DataResponse.success();
    }

    /**
     * 修改角色
     *
     * @param roleReqDTO 修改角色信息
     * @return <T>
     */
    @PermissionCheck(permissionName = {"system:role:modify"})
    @PostMapping
    @ApiOperation(value = "修改角色")
    public <T> DataResponse<T> updateRole(@Valid @RequestBody RoleReqDTO roleReqDTO){
        roleService.updateRole(roleReqDTO);
        return DataResponse.success();
    }
}