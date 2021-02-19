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
public class Menu extends BaseEntity {

    @ApiModelProperty(value = "是否为目录")
    private Integer isCatalog;

    @ApiModelProperty(value = "目录id")
    private Long catalogId;

    @ApiModelProperty(value = "是否为菜单")
    private Integer isMenu;

    @ApiModelProperty(value = "菜单id")
    private Long menuId;

    @ApiModelProperty(value = "名称")
    private String menuName;

    @ApiModelProperty(value = "类型")
    private Integer menuType;

    @ApiModelProperty(value = "图标")
    private String menuIcon;

    @ApiModelProperty(value = "排序")
    private Integer menuSort;

    @ApiModelProperty(value = "路由地址")
    private String menuUrl;

    @ApiModelProperty(value = "权限标识")
    private String roleIdentify;

    @ApiModelProperty(value = "组件地址")
    private String componentPath;

    @ApiModelProperty(value = "是否使用")
    private Integer isUse;

    @ApiModelProperty(value = "是否显示")
    private Integer isShow;
}
