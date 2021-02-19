package com.zte.msg.alarmcenter.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author frp
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class RoleMenu extends BaseEntity{

    @ApiModelProperty(value = "角色名称")
    private Long roleId;

    @ApiModelProperty(value = "权限字符")
    private Long menuId;

}