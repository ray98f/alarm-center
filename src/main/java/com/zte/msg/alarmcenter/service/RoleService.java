package com.zte.msg.alarmcenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.req.RoleReqDTO;
import com.zte.msg.alarmcenter.entity.Role;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2021/2/21 16:14
 */
public interface RoleService {

    /**
     * 获取角色列表
     * @return
     */
    List<Role> listAllRole();

    /**
     * 查询角色列表
     * @param status
     * @param roleName
     * @param pageReqDTO
     * @return
     */
    Page<Role> listRole(Integer status,String roleName,PageReqDTO pageReqDTO);

    /**
     * 删除角色
     * @param ids
     */
    void deleteRole(List<Long> ids);

    /**
     * 新增角色
     * @param role
     */
    void insertRole(RoleReqDTO role);

    /**
     * 修改角色
     * @param role
     */
    void updateRole(RoleReqDTO role);

    /**
     * 获取角色对应菜单ids
     * @param roleId
     * @return
     */
    List<Long> selectMenuIds(Long roleId);
}
