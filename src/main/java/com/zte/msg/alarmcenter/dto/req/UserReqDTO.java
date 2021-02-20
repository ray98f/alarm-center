package com.zte.msg.alarmcenter.dto.req;

import com.zte.msg.alarmcenter.dto.PageReqDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author frp
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class UserReqDTO extends PageReqDTO {

    @ApiModelProperty(value = "用户id")
    private Long id;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "用户密码")
    private String password;

    @ApiModelProperty(value = "姓名")
    private String userRealName;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String mail;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "用户角色id")
    private List<Long> roleIds;

    @ApiModelProperty(value = "备注")
    private String remark;
}
