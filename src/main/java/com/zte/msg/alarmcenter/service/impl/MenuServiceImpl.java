package com.zte.msg.alarmcenter.service.impl;

import com.zte.msg.alarmcenter.dto.res.MenuResDTO;
import com.zte.msg.alarmcenter.mapper.MenuMapper;
import com.zte.msg.alarmcenter.mapper.RoleMapper;
import com.zte.msg.alarmcenter.mapper.UserMapper;
import com.zte.msg.alarmcenter.service.MenuService;
import com.zte.msg.alarmcenter.service.OperationLogService;
import com.zte.msg.alarmcenter.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2021/2/24 9:15
 */
@Service
@Slf4j
public class MenuServiceImpl implements MenuService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Resource
    private MenuService menuService;

    /**
     * 登录动态菜单列表获取
     *
     * @return
     */
    @Override
    public List<MenuResDTO> listLoginMenu() {
        Long id = userMapper.selectUserId(TokenUtil.getCurrentUserName());
        List<Long> userRoles = userMapper.selectUserRoles(id);
        return new ArrayList<>(menuService.listMenu(userRoles));
    }

    @Override
    public List<MenuResDTO> listMenu(List<Long> roleIds) {
        List<MenuResDTO> list;
        List<MenuResDTO.MenuInfo> menuInfoList;
        List<Long> menuIds = roleMapper.selectMenuIds(roleIds);
        list = menuMapper.listCatalog(null, menuIds);
        if (list.isEmpty()) {
            log.warn("根目录无下级");
            list = null;
        } else {
            for (MenuResDTO menuResDTO : list) {
                menuInfoList = menuMapper.listMenu(menuResDTO.getId(), null, menuIds);
                if (menuInfoList.isEmpty()) {
                    log.warn(menuResDTO.getId() + "目录无下级");
                    menuInfoList = null;
                } else {
                    for (MenuResDTO.MenuInfo menuInfo : menuInfoList) {
                        String str = menuMapper.listButtonRoleIdentify(menuInfo.getId(), menuIds);
                        if (StringUtils.isNotBlank(str)) {
                            menuInfo.setRoleIdentify(str);
                        }
                    }
                }
                menuResDTO.setChildren(menuInfoList);
            }
        }
        return list;
    }

}
