package com.zte.msg.alarmcenter.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.req.RoleReqDTO;
import com.zte.msg.alarmcenter.entity.Role;
import com.zte.msg.alarmcenter.enums.ErrorCode;
import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.mapper.RoleMapper;
import com.zte.msg.alarmcenter.service.RoleService;
import com.zte.msg.alarmcenter.utils.Constants;
import com.zte.msg.alarmcenter.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2021/2/20 14:26
 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> listAllRole() {
        List<Role> list = roleMapper.listAllRole();
        if (null == list || list.isEmpty()) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        return list;
    }

    @Override
    public Page<Role> listRole(Integer status, String roleName, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPage().intValue(), pageReqDTO.getSize().intValue());
        if (roleName != null && roleName.contains(Constants.PERCENT_SIGN)) {
            roleName = "尼玛死了";
        }
        return roleMapper.listRole(pageReqDTO.of(), status, roleName);
    }

    @Override
    public void deleteRole(List<Long> ids) {
        if (null == ids || ids.isEmpty()) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        for (Long id : ids) {
            int count = roleMapper.selectRoleUse(id);
            if (count > 0) {
                throw new CommonException(ErrorCode.ROLE_USE_CANT_DELETE);
            }
        }
        int deleteRole = roleMapper.deleteRole(ids);
        if (deleteRole >= 0) {
            log.info("角色删除成功");
        } else {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    @Override
    public void insertRole(RoleReqDTO role) {
        if (Objects.isNull(role)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        role.setCreatedBy(TokenUtil.getCurrentUserName());
        Long id = roleMapper.selectRoleIsExist(role);
        if (!Objects.isNull(id)) {
            throw new CommonException(ErrorCode.ROLE_EXIST);
        }
        int insertRole = roleMapper.insertRole(role);
        if (insertRole <= 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
        if (null == role.getMenuIds() || role.getMenuIds().isEmpty()) {
            log.warn("没有需要添加的角色权限信息");
            return;
        }
        int insertRoleMenu = roleMapper.insertRoleMenu(role.getId(), role.getMenuIds(), role.getCreatedBy());
        if (insertRoleMenu <= 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
        log.info("新增角色成功");
    }

    @Override
    public void updateRole(RoleReqDTO role) {
        if (Objects.isNull(role)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        role.setCreatedBy(TokenUtil.getCurrentUserName());
        Long id = roleMapper.selectRoleIsExist(role);
        if (!Objects.isNull(id)) {
            throw new CommonException(ErrorCode.ROLE_EXIST);
        }
        int updateRole = roleMapper.updateRole(role);
        if (updateRole <= 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
        roleMapper.deleteRoleMenus(role.getId());
        if (null == role.getMenuIds() || role.getMenuIds().isEmpty()) {
            log.warn("没有需要修改的角色权限信息");
            return;
        }
        int insertRoleMenu = roleMapper.insertRoleMenu(role.getId(), role.getMenuIds(), role.getCreatedBy());
        if (insertRoleMenu <= 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
        log.info("修改角色成功");
    }

    @Override
    public List<Long> selectMenuIds(Long roleId) {
        if (null == roleId || roleId < 0) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        List<Long> list = roleMapper.selectRoleMenuIds(roleId);
        if (null == list || list.isEmpty()) {
            log.warn("该角色无菜单信息");
            return null;
        } else {
            log.info("角色菜单权限返回成功");
            return list;
        }
    }
}
