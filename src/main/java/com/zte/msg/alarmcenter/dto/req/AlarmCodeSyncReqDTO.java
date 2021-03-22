package com.zte.msg.alarmcenter.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author frp
 */
@ApiModel
@Data
public class AlarmCodeSyncReqDTO implements Serializable {

    @ApiModelProperty(value = "客户端系统内id")
    private Long id;

    @ApiModelProperty(value = "系统编号")
    private Long systemId;

    @ApiModelProperty(value = "线路编号")
    private Long lineId;

    @ApiModelProperty(value = "告警码")
    private String alarmCode;

    @ApiModelProperty(value = "告警名称")
    private String alarmName;

    @ApiModelProperty(value = "告警原因")
    private String alarmReason;

    @ApiModelProperty(value = "告警级别")
    private Integer alarmLevel;

    @ApiModelProperty(value = "处理意见")
    private String handlingOpinions;

    @ApiModelProperty(value = "删除标识 （0 未删除 1 已删除）")
    private Integer isDeleted;

}
