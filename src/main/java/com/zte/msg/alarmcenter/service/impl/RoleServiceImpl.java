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
    public Page<Role> listRole(Integer status,String roleName,PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPage().intValue(), pageReqDTO.getSize().intValue());
        return roleMapper.listRole(pageReqDTO.of(), status,roleName);
    }

    @Override
    public void deleteRole(List<Long> ids) {
        if (null == ids || ids.isEmpty()) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
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
        int insertRole = roleMapper.insertRole(role);
        if (insertRole <= 0) {
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
        int updateRole = roleMapper.updateRole(role);
        if (updateRole <= 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
        log.info("修改角色成功");
    }
}
