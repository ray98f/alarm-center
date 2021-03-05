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
public class OperationLog extends BaseEntity {

    @ApiModelProperty(value = "操作人")
    private String userName;

    @ApiModelProperty(value = "操作时间")
    private Timestamp operationTime;

    @ApiModelProperty(value = "操作类型")
    private String operationType;

    @ApiModelProperty(value = "用时")
    private Long useTime;

    @ApiModelProperty(value = "描述")
    private String params;

    @ApiModelProperty(value = "主机IP")
    private String hostIp;
}
