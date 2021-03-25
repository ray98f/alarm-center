package com.zte.msg.alarmcenter.controller;

import com.zte.msg.alarmcenter.annotation.PermissionCheck;
import com.zte.msg.alarmcenter.dto.DataResponse;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.PageResponse;
import com.zte.msg.alarmcenter.dto.req.PasswordReqDTO;
import com.zte.msg.alarmcenter.dto.req.UserReqDTO;
import com.zte.msg.alarmcenter.entity.User;
import com.zte.msg.alarmcenter.service.UserService;
import com.zte.msg.alarmcenter.utils.AsyncSender;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2021/2/19 10:42
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Api(tags = "用户")
@Validated
public class UserController {

    @Resource
    private UserService userService;

    @Autowired
    AsyncSender asyncSender;

    /**
     * 新增管理平台登录用户
     *
     * @param userReqDTO 用户信息
     * @return DataResponse
     */
    @PermissionCheck(permissionName = {"system:user:add"})
    @PutMapping
    @ApiOperation(value = "新增管理平台登录用户")
    public <T> DataResponse<T> createUser(@RequestBody @Valid UserReqDTO userReqDTO) {
        userService.insertUser(userReqDTO);
        return DataResponse.success();
    }

    /**
     * 修改密码
     *
     * @param passwordReqDTO 密码信息
     * @return DataResponse
     */
    @PostMapping("/change")
    @ApiOperation(value = "修改密码")
    public <T> DataResponse<T> changePwd(@RequestBody @Valid PasswordReqDTO passwordReqDTO) {
        userService.changePwd(passwordReqDTO);
        return DataResponse.success();
    }

    /**
     * 重置密码
     *
     * @param id 用户id
     * @return DataResponse
     */
    @PermissionCheck(permissionName = {"system:user:reset-psw"})
    @GetMapping("/change")
    @ApiOperation(value = "重置密码")
    public <T> DataResponse<T> resetPwd(@Valid @RequestParam @NotNull(message = "32000006") Integer id) {
        userService.resetPwd(id);
        return DataResponse.success();
    }

    /**
     * 修改用户信息
     *
     * @param userReqDTO 用户修改信息
     * @return <T>
     */
    @PermissionCheck(permissionName = {"system:user:modify"})
    @PostMapping
    @ApiOperation(value = "修改用户信息")
    public <T> DataResponse<T> edit(@RequestBody @Valid UserReqDTO userReqDTO) {
        userService.editUser(userReqDTO);
        return DataResponse.success();
    }

    /**
     * 用户删除
     *
     * @param ids 用户id
     * @return DataResponse
     */
    @PermissionCheck(permissionName = {"system:user:remove"})
    @DeleteMapping
    @ApiOperation(value = "删除用户")
    public <T> DataResponse<T> deleteUser(@Valid @RequestBody List<Integer> ids) {
        userService.deleteUser(ids);
        return DataResponse.success();
    }

    /**
     * 获取所有用户
     */
    @GetMapping("/all")
    @ApiOperation(value = "获取所有用户")
    public DataResponse<List<User>> listAllUser(){
        return DataResponse.of(userService.listAllUser());
    }

    /**
     * 分页查询用户列表
     * @param status
     * @param userRealName
     * @param pageReqDTO
     * @return
     */
    @PermissionCheck(permissionName = {"system:user:list"})
    @GetMapping
    @ApiOperation(value = "分页查询用户列表")
    public PageResponse<User> listUser(@RequestParam(required = false)
                                           @ApiParam("状态") Integer status,
                                       @RequestParam(required = false)
                                           @ApiParam("姓名") String userRealName,
                                       @Valid PageReqDTO pageReqDTO){
        return PageResponse.of(userService.listUser(status, userRealName, pageReqDTO));
    }

    @GetMapping("/test")
    public <T> DataResponse<T> test() throws Exception {
        asyncSender.test();
        return DataResponse.success();
    }
}