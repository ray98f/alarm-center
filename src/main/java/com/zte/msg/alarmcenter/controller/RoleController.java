package com.zte.msg.alarmcenter.controller;

import com.zte.msg.alarmcenter.annotation.LogMaker;
import com.zte.msg.alarmcenter.annotation.PermissionCheck;
import com.zte.msg.alarmcenter.dto.DataResponse;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.PageResponse;
import com.zte.msg.alarmcenter.dto.req.MenuReqDTO;
import com.zte.msg.alarmcenter.dto.req.RoleReqDTO;
import com.zte.msg.alarmcenter.dto.res.MenuResDTO;
import com.zte.msg.alarmcenter.entity.Role;
import com.zte.msg.alarmcenter.service.MenuService;
import com.zte.msg.alarmcenter.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Resource
    private MenuService menuService;

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
    @GetMapping
    @ApiOperation(value = "分页获取角色信息")
    public PageResponse<Role> listRole(@RequestParam(required = false) @ApiParam("状态") Integer status,
                                       @RequestParam(required = false) @ApiParam("角色名称") String roleName,
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
    @LogMaker(value = "删除角色")
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
    @LogMaker(value = "新增角色")
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
    @LogMaker(value = "修改角色")
    public <T> DataResponse<T> updateRole(@Valid @RequestBody RoleReqDTO roleReqDTO){
        roleService.updateRole(roleReqDTO);
        return DataResponse.success();
    }

    /**
     * 查询菜单信息
     *
     * @param menuReqDTO 查询信息
     * @return List<MenuResDTO>
     */
    @PermissionCheck(permissionName = {"system:tab:list"})
    @GetMapping("/menu")
    @ApiOperation(value = "查询菜单信息")
    public DataResponse<List<MenuResDTO>> listMenu(@Valid MenuReqDTO menuReqDTO) {
        return DataResponse.of(menuService.listMenu(menuReqDTO));
    }

    /**
     * 获取角色对应菜单id
     *
     * @param roleId 角色id
     * @return Map<String, Object>
     */
    @GetMapping("/{roleId}")
    @ApiOperation(value = "获取角色对应菜单id")
    public DataResponse<Map<String, Object>> detailRoleMenu(@PathVariable Long roleId) {
        Map<String, Object> data = new HashMap<>(16);
        data.put("menuIds", roleService.selectMenuIds(roleId));
        return DataResponse.of(data);
    }
}