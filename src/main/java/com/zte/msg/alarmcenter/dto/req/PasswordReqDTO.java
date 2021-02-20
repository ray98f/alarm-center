package com.zte.msg.alarmcenter.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class PasswordReqDTO {

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "旧密码")
    private String oldPwd;

    @ApiModelProperty(value = "新密码")
    private String newPwd;
}
