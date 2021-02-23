package com.zte.msg.alarmcenter.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

/**
 * @author frp
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class ChangeShifts extends BaseEntity{
    @ApiModelProperty(value = "交接班类型")
    private Integer type;

    @ApiModelProperty(value = "记录人")
    private Long noteTaker;

    @ApiModelProperty(value = "联系电话")
    private String tel;

    @ApiModelProperty(value = "备注情况")
    private String remark;
}
