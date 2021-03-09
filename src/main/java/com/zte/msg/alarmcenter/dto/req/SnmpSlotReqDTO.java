package com.zte.msg.alarmcenter.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/2/23 14:24
 */
@Data
@ApiModel
public class SnmpSlotReqDTO {

    @ApiModelProperty(value = "SNMP槽位名称")
    private String snmpSlotName;

    @ApiModelProperty(value = "子系统id")
    private Long systemId;

    @ApiModelProperty(value = "线路编号")
    private String LineCode;

    @ApiModelProperty(value = "站点编号")
    private String siteCode;

    @ApiModelProperty(value = "设备编号")
    private String deviceCode;

    @ApiModelProperty(value = "槽位编号")
    private String slotCode;

    @ApiModelProperty(value = "位置id")
    private String slotName;

}
