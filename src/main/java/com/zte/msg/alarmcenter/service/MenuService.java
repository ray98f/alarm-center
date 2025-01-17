package com.zte.msg.alarmcenter.service;

import com.zte.msg.alarmcenter.dto.req.MenuReqDTO;
import com.zte.msg.alarmcenter.dto.res.MenuResDTO;

import java.util.List;

/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2021/1/18 17:03
 */
public interface MenuService {

    /**
     * 登录动态菜单列表获取
     * @return
     */
    List<MenuResDTO> listLoginMenu();

    /**
     * 根据权限获取菜单列表
     * @param roleIds
     * @return
     */
    List<MenuResDTO> listMenu(List<Long> roleIds);

    /**
     * 获取菜单列表
     * @param menuReqDTO
     * @return
     */
    List<MenuResDTO> listMenu(MenuReqDTO menuReqDTO);
}
