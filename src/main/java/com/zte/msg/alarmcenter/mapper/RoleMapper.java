package com.zte.msg.alarmcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.req.RoleReqDTO;
import com.zte.msg.alarmcenter.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 获取所有角色
     *
     * @return
     */
    List<Role> listAllRole();

    /**
     * 查询角色列表
     *
     * @param page
     * @param roleReqDTO
     * @return
     */
    Page<Role> listRole(Page<Role> page, RoleReqDTO roleReqDTO);

    /**
     * 删除角色
     *
     * @param ids
     * @return
     */
    int deleteRole(List<Long> ids);

    /**
     * 新增角色
     *
     * @param role
     * @return
     */
    int insertRole(RoleReqDTO role);

    /**
     * 修改角色
     *
     * @param role
     * @return
     */
    int updateRole(RoleReqDTO role);

    /**
     * 搜索角色对应菜单ids
     *
     * @param roleIds
     * @return
     */
    List<Long> selectMenuIds(List<Long> roleIds);

}
