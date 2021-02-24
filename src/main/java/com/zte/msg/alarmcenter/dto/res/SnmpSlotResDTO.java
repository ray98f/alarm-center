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

    @ApiModelProperty(value = "槽位名称")
    private String name;

    @ApiModelProperty(value = "snmp槽位")
    private String SnmpSlot;

    @ApiModelProperty(value = "系统名称")
    private String systemName;

    @ApiModelProperty(value = "站点")
    private String position;

    @ApiModelProperty(value = "设备")
    private String deviceCode;

    @ApiModelProperty(value = "槽位")
    private String slotCode;

    @ApiModelProperty(value = "槽位名称")
    private String slotName;

}
