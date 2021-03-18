package com.zte.msg.alarmcenter.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@ApiModel
@Data
public class SlotSyncReqDTO {

    @ApiModelProperty(value = "客户端系统内id")
    private Long id;

    @ApiModelProperty(value = "系统编号")
    private Long systemId;

    @ApiModelProperty(value = "线路编号")
    private Long lineId;

    @ApiModelProperty(value = "车站编号")
    private Long stationId;

    @ApiModelProperty(value = "设备编号")
    private Long deviceId;

    @ApiModelProperty(value = "槽位编号")
    private String slotId;

    @ApiModelProperty(value = "槽位名称")
    private String slotName;

    @ApiModelProperty(value = "删除标识 （0 未删除 1 已删除）")
    private Integer isDeleted;

}
