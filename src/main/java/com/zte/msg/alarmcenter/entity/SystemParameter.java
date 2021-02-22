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
public class SystemParameter extends BaseEntity {
    @ApiModelProperty(value = "参数")
    private String parameter;

    @ApiModelProperty(value = "参数描述")
    private String parameterDescribe;

    @ApiModelProperty(value = "参数值")
    private String value;

    @ApiModelProperty(value = "参数值描述")
    private String valueDescribe;
}
