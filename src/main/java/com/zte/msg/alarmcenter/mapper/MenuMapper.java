package com.zte.msg.alarmcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zte.msg.alarmcenter.dto.req.MenuReqDTO;
import com.zte.msg.alarmcenter.dto.res.MenuResDTO;
import com.zte.msg.alarmcenter.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 获取目录列表
     *
     * @param menuReqDTO 目录列表查询条件
     * @param menuIds    权限内的菜单ids
     * @return List<MenuResDTO>
     */
    List<MenuResDTO> listCatalog(MenuReqDTO menuReqDTO, List<Long> menuIds);

    /**
     * 获取菜单列表
     *
     * @param catalogId  目录ids
     * @param menuReqDTO 目录列表查询条件
     * @param menuIds    权限内的菜单ids
     * @return List<MenuResDTO.MenuInfo>
     */
    List<MenuResDTO.MenuInfo> listMenu(Long catalogId, MenuReqDTO menuReqDTO, List<Long> menuIds);

    /**
     * 获取按钮列表
     *
     * @param menuId     菜单id
     * @param menuReqDTO 菜单请求值
     * @param menuIds    权限内的菜单ids
     * @return List<MenuResDTO.MenuInfo.ButtonInfo>
     */
    List<MenuResDTO.MenuInfo.ButtonInfo> listButton(Long menuId, MenuReqDTO menuReqDTO, List<Long> menuIds);

    /**
     * 获取按钮权限标识
     *
     * @param menuId  菜单id
     * @param menuIds 权限内的菜单ids
     * @return String
     */
    String listButtonRoleIdentify(Long menuId, List<Long> menuIds);

    /**
     * 初始化一级菜单
     *
     * @return
     */
    List<Long> selectNormalMenuIds();

}