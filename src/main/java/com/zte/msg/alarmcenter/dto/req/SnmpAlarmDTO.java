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
 * @date 2021/2/24 10:16
 */
@Data
@ApiModel
public class SnmpAlarmDTO {

    @ApiModelProperty(value = "系统名称")
    @NotNull(message = "32000006")
    private Long systemId;

    @ApiModelProperty(value = "线路id")
    private String positionId;

    @ApiModelProperty(value = "告警码")
    private String code;

    @ApiModelProperty(value = "网元类型")
    private String elementType;

    @ApiModelProperty(value = "SNMP码")
    private String snmpCode;

    @ApiModelProperty(value = "告警原因")
    private String reason;

}
