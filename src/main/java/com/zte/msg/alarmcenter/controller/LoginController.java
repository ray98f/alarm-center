package com.zte.msg.alarmcenter.controller;

import com.zte.msg.alarmcenter.annotation.LogMaker;
import com.zte.msg.alarmcenter.dto.DataResponse;
import com.zte.msg.alarmcenter.dto.req.LoginReqDTO;
import com.zte.msg.alarmcenter.dto.req.UserReqDTO;
import com.zte.msg.alarmcenter.dto.res.HomeMapPathResDTO;
import com.zte.msg.alarmcenter.dto.res.MenuResDTO;
import com.zte.msg.alarmcenter.entity.ChangeShifts;
import com.zte.msg.alarmcenter.service.ChangeShiftsService;
import com.zte.msg.alarmcenter.service.HomeService;
import com.zte.msg.alarmcenter.service.MenuService;
import com.zte.msg.alarmcenter.service.UserService;
import com.zte.msg.alarmcenter.utils.TokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @Resource
    private HomeService homeService;

    /**
     * 管理平台登录
     *
     * @param loginReqDTO 用户信息
     * @return DataResponse
     * @throws Exception Comm
     */
    @PostMapping("/login")
    @ApiOperation(value = "管理平台登录")
    @LogMaker(value = "管理平台登录")
    public DataResponse<Map<String, Object>> login(@RequestBody @Valid LoginReqDTO loginReqDTO) throws Exception {
        UserReqDTO userInfo = userService.selectUserInfo(loginReqDTO);
        String token = createSimpleToken(userInfo);
        Map<String, Object> data = new HashMap<>(16);
        data.put("token", token);
        data.put("userName", userInfo.getUserName());
        data.put("userRealName", userInfo.getUserRealName());
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
    @LogMaker(value = "管理平台登出")
    public <T> DataResponse<T> exit() {
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

    /**
     * 交接班
     *
     * @param loginReqDTO
     * @return
     * @throws Exception
     */
    @PostMapping("/handover")
    @ApiOperation(value = "交接班")
    public DataResponse<Map<String, Object>> handover(@RequestBody @Valid LoginReqDTO loginReqDTO,
                                                      @RequestParam(required = false)
                                                      @ApiParam("remark") String remark) throws Exception {
        UserReqDTO userInfo = userService.selectUserInfo(loginReqDTO);
        ChangeShifts changeShifts = new ChangeShifts();
        changeShifts.setByUserName(TokenUtil.getCurrentUserName());
        changeShifts.setToUserName(userInfo.getUserName());
        changeShifts.setRemark(remark);
        changeShiftsService.addChangeShifts(changeShifts);
        Map<String, Object> data = new HashMap<>(16);
        String token = createSimpleToken(userInfo);
        data.put("token", token);
        data.put("userName", userInfo.getUserName());
        data.put("userRealName", userInfo.getUserRealName());
        return DataResponse.of(data);
    }

    /**
     * 获取首页地图地址
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/map")
    @ApiOperation(value = "获取首页地图地址")
    public DataResponse<List<HomeMapPathResDTO>> homeMapPath() throws Exception {
        return DataResponse.of(homeService.getHomeMapPath());
    }
}
