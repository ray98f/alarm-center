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
    @ApiModelProperty(value = "操作人员id")
    private Long userId;

    @ApiModelProperty(value = "操作时间")
    private Timestamp operationTime;

    @ApiModelProperty(value = "操作类型")
    private Long operationType;

    @ApiModelProperty(value = "主机IP")
    private String hostIp;

    @ApiModelProperty(value = "描述")
    private String describe;
}
