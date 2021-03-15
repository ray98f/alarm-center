package com.zte.msg.alarmcenter.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/1/19 14:55
 */
@ApiModel
@Data
public class DicReqDTO {

    @ApiModelProperty(value = "名称")
    @NotNull(message = "32000006")
    private String name;

    @ApiModelProperty(value = "类型")
    @NotNull(message = "32000006")
    private String type;

    @ApiModelProperty(value = "启用状态：0-禁用，1-启用")
    private Integer isEnable;

    @ApiModelProperty(value = "描述")
    private String description;
}
