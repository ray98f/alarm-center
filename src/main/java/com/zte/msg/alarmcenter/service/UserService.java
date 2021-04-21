package com.zte.msg.alarmcenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.req.LoginReqDTO;
import com.zte.msg.alarmcenter.dto.req.PasswordReqDTO;
import com.zte.msg.alarmcenter.dto.req.UserReqDTO;
import com.zte.msg.alarmcenter.entity.User;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2021/2/20 9:37
 */
public interface UserService extends IService<User> {

    /**
     * 获取用户信息
     *
     * @param loginReqDTO
     * @return
     */
    UserReqDTO selectUserInfo(LoginReqDTO loginReqDTO);

    /**
     * 新增用户
     *
     * @param userReqDTO
     */
    void insertUser(UserReqDTO userReqDTO);

    /**
     * 修改密码
     *
     * @param passwordReqDTO
     */
    void changePwd(PasswordReqDTO passwordReqDTO);

    /**
     * 重置密码
     *
     * @param id
     */
    void resetPwd(Integer id);

    /**
     * 编辑用户
     *
     * @param userReqDTO
     */
    void editUser(UserReqDTO userReqDTO);

    /**
     * 删除用户
     *
     * @param ids
     * @param userId
     */
    void deleteUser(List<Integer> ids, String userId);

    /**
     * 获取所有用户列表
     *
     * @return
     */
    List<User> listAllUser();

    /**
     * 查询用户列表
     *
     * @param status
     * @param userRealName
     * @param pageReqDTO
     * @return
     */
    Page<User> listUser(Integer status, String userRealName, PageReqDTO pageReqDTO);
}
