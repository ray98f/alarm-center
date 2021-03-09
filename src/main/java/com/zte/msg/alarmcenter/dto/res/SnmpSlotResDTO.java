package com.zte.msg.alarmcenter.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/2/24 9:16
 */
@Data
@ApiModel
public class SnmpSlotResDTO {

    private Long id;

    @ApiModelProperty(value = "snmp槽位名称")
    private String snmpSlotName;

    @ApiModelProperty(value = "系统id")
    private String systemId;

    @ApiModelProperty(value = "线路编号")
    private String lineCode;

    @ApiModelProperty(value = "站点编号")
    private String siteCode;

    @ApiModelProperty(value = "设备编号")
    private String deviceCode;

    @ApiModelProperty(value = "槽位编号")
    private String slotCode;

    @ApiModelProperty(value = "槽位名称")
    private String slotName;

    @ApiModelProperty(value = "系统名称")
    private String systemName;

    @ApiModelProperty(value = "站点名称")
    private String siteName;

    @ApiModelProperty(value = "设备名称")
    private String deviceName;
}
