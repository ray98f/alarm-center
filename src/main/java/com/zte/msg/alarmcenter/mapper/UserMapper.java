package com.zte.msg.alarmcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.req.PasswordReqDTO;
import com.zte.msg.alarmcenter.dto.req.UserReqDTO;
import com.zte.msg.alarmcenter.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 获取用户信息
     *
     * @param userName
     * @return
     */
    UserReqDTO selectUserInfo(String userName);

    /**
     * 获取用户权限id
     *
     * @param userId
     * @return
     */
    List<Long> selectUserRoles(Long userId);

    /**
     * 新增用户
     *
     * @param user
     * @param doName
     * @return
     */
    int insertUser(UserReqDTO user, String doName);

    /**
     * 新增用户权限
     *
     * @param userId
     * @param roleIds
     * @param doName
     * @return
     */
    int insertUserRole(Long userId, List<Long> roleIds, String doName);

    /**
     * 修改密码
     *
     * @param passwordReqDTO
     * @param updateBy
     * @return
     */
    int changePwd(PasswordReqDTO passwordReqDTO, String updateBy);

    /**
     * 重置密码
     *
     * @param password
     * @param updateBy
     * @param id
     * @return
     */
    int resetPwd(String password, String updateBy, Integer id);

    /**
     * 修改用户
     *
     * @param userReqDTO
     * @param updateBy
     * @return
     */
    int editUser(UserReqDTO userReqDTO, String updateBy);

    /**
     * 删除用户的所有角色
     * @param userId
     */
    void deleteUserRole(Long userId);

    /**
     * 删除用户
     *
     * @param ids
     * @return
     */
    int deleteUser(List<Integer> ids);

    /**
     * 获取所有用户列表
     *
     * @return
     */
    List<User> listAllUser();

    /**
     * 查询用户列表
     *
     * @param page
     * @param userReqDTO
     * @return
     */
    Page<User> listUser(Page<User> page, UserReqDTO userReqDTO);
}
