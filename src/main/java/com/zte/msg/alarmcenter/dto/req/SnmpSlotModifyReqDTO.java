package com.zte.msg.alarmcenter.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/2/24 10:07
 */
@Data
@ApiModel
public class SnmpSlotModifyReqDTO {

    @ApiModelProperty(value = "SNMP槽位名称")
    private String name;

    @ApiModelProperty(value = "槽位id")
    private Long slotId;

    @ApiModelProperty(value = "子系统id")
    private Long systemId;

    @ApiModelProperty(value = "位置id")
    private Long positionId;

}


