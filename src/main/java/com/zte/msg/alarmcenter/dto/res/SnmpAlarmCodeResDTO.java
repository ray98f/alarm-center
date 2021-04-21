package com.zte.msg.alarmcenter.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/2/24 10:17
 */
@Data
@ApiModel
public class SnmpAlarmCodeResDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "系统编号")
    private String systemCode;

    @ApiModelProperty(value = "线路编号")
    private String positionCode;

    @ApiModelProperty(value = "系统id")
    private String systemId;

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

    @ApiModelProperty(value = "系统名称")
    private String systemName;

    @ApiModelProperty(value = "线路名称")
    private String positionName;

}
