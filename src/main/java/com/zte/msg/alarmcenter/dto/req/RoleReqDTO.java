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
public class RoleReqDTO extends PageReqDTO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "权限字符")
    private String roleStr;

    @ApiModelProperty(value = "排序")
    private Integer roleSort;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "菜单ids")
    private List<Long> menuIds;

    @ApiModelProperty(value = "新建人")
    private String createdBy;
}
