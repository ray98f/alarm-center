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
public class ChangeShifts extends BaseEntity {
    @ApiModelProperty(value = "交接班类型（1为交班，2为接班）")
    private Integer type;

    @ApiModelProperty(value = "记录人")
    private String userName;
}
