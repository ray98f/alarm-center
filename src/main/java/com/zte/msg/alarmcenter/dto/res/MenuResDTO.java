package com.zte.msg.alarmcenter.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author frp
 */
@Data
@ApiModel
public class MenuResDTO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "是否为目录")
    private Integer isCatalog;

    @ApiModelProperty(value = "目录id")
    private Long catalogId;

    @ApiModelProperty(value = "是否为菜单")
    private Integer isMenu;

    @ApiModelProperty(value = "菜单id")
    private Long menuId;

    @ApiModelProperty(value = "目录名称")
    private String menuName;

    @ApiModelProperty(value = "目录类型")
    private Integer menuType;

    @ApiModelProperty(value = "目录排序")
    private String menuSort;

    @ApiModelProperty(value = "目录value")
    private String menuValue;

    @ApiModelProperty(value = "权限标识")
    private String roleIdentify;

    @ApiModelProperty(value = "是否显示(0显示,1隐藏)")
    private Integer isShow;

    @ApiModelProperty(value = "操作时间")
    private Timestamp updatedAt;

    @ApiModelProperty(value = "菜单信息")
    private List<MenuInfo> children;

    @Data
    public static class MenuInfo{

        @ApiModelProperty(value = "主键id")
        private Long id;

        @ApiModelProperty(value = "是否为目录")
        private Integer isCatalog;

        @ApiModelProperty(value = "目录id")
        private Long catalogId;

        @ApiModelProperty(value = "是否为菜单")
        private Integer isMenu;

        @ApiModelProperty(value = "菜单id")
        private Long menuId;

        @ApiModelProperty(value = "菜单名称")
        private String menuName;

        @ApiModelProperty(value = "菜单类型")
        private Integer menuType;

        @ApiModelProperty(value = "菜单排序")
        private String menuSort;

        @ApiModelProperty(value = "目录value")
        private String menuValue;

        @ApiModelProperty(value = "权限标识")
        private String roleIdentify;

        @ApiModelProperty(value = "是否显示(0显示,1隐藏)")
        private Integer isShow;

        @ApiModelProperty(value = "操作时间")
        private Timestamp updatedAt;

        @ApiModelProperty(value = "按钮信息")
        private List<ButtonInfo> children;

        @Data
        public static class ButtonInfo{

            @ApiModelProperty(value = "主键id")
            private Long id;

            @ApiModelProperty(value = "是否为目录")
            private Integer isCatalog;

            @ApiModelProperty(value = "目录id")
            private Long catalogId;

            @ApiModelProperty(value = "是否为菜单")
            private Integer isMenu;

            @ApiModelProperty(value = "菜单id")
            private Long menuId;

            @ApiModelProperty(value = "按钮名称")
            private String menuName;

            @ApiModelProperty(value = "按钮类型")
            private Integer menuType;

            @ApiModelProperty(value = "按钮排序")
            private String menuSort;

            @ApiModelProperty(value = "目录value")
            private String menuValue;

            @ApiModelProperty(value = "权限标识")
            private String roleIdentify;

            @ApiModelProperty(value = "是否显示(0显示,1隐藏)")
            private Integer isShow;

            @ApiModelProperty(value = "操作时间")
            private Timestamp updatedAt;
        }
    }
}
