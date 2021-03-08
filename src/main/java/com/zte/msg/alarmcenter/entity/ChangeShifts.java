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

    @ApiModelProperty(value = "交班人")
    private String byUserName;

    @ApiModelProperty(value = "接班人")
    private String toUserName;
}
