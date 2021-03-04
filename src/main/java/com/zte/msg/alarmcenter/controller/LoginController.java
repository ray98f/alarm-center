package com.zte.msg.alarmcenter.controller;

import com.zte.msg.alarmcenter.dto.DataResponse;
import com.zte.msg.alarmcenter.dto.req.LoginReqDTO;
import com.zte.msg.alarmcenter.dto.req.UserReqDTO;
import com.zte.msg.alarmcenter.dto.res.MenuResDTO;
import com.zte.msg.alarmcenter.entity.ChangeShifts;
import com.zte.msg.alarmcenter.service.ChangeShiftsService;
import com.zte.msg.alarmcenter.service.MenuService;
import com.zte.msg.alarmcenter.service.UserService;
import com.zte.msg.alarmcenter.utils.TokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zte.msg.alarmcenter.utils.TokenUtil.createSimpleToken;


/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2020/12/28 9:52
 */
@Slf4j
@RestController
@RequestMapping
@Api(tags = "登录")
@Validated
public class LoginController {

    @Resource
    private UserService userService;

    @Resource
    private MenuService menuService;

    @Resource
    private ChangeShiftsService changeShiftsService;

    /**
     * 管理平台登录
     *
     * @param loginReqDTO 用户信息
     * @return DataResponse
     * @throws Exception Comm
     */
    @PostMapping("/login")
    @ApiOperation(value = "管理平台登录")
    public DataResponse<Map<String, Object>> login(@RequestBody @Valid LoginReqDTO loginReqDTO) throws Exception {
        UserReqDTO userInfo = userService.selectUserInfo(loginReqDTO);
        String token = createSimpleToken(userInfo);
        log.info("{} Token返回成功", userInfo.getUserName());
        ChangeShifts changeShifts = new ChangeShifts();
        changeShifts.setType(2);
        changeShifts.setUserName(userInfo.getUserName());
        changeShiftsService.addChangeShifts(changeShifts);
        Map<String, Object> data = new HashMap<>(16);
        data.put("token", token);
        log.info("登陆成功");
        return DataResponse.of(data);
    }

    /**
     * 管理平台登出
     *
     * @param <T>
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value = "管理平台登出")
    public <T> DataResponse<T> exit() {
        String userName = TokenUtil.getCurrentUserName();
        ChangeShifts changeShifts = new ChangeShifts();
        changeShifts.setType(1);
        changeShifts.setUserName(userName);
        changeShiftsService.addChangeShifts(changeShifts);
        log.info("{} 登出成功", userName);
        return DataResponse.success();
    }

    /**
     * 动态菜单获取
     *
     * @return
     */
    @GetMapping("/menu")
    @ApiOperation(value = "动态菜单获取")
    public DataResponse<List<MenuResDTO>> menu() {
        List<MenuResDTO> menuResDTOList = menuService.listLoginMenu();
        return DataResponse.of(menuResDTOList);
    }
}
