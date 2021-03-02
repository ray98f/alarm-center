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
public class AlarmHistory extends BaseEntity{

    @ApiModelProperty(value = "子系统id")
    private Long subsystemId;

    @ApiModelProperty(value = "告警等级")
    private Integer alarmLevel;

    @ApiModelProperty(value = "站点")
    private Long siteId;

    @ApiModelProperty(value = "设备")
    private String equipment;

    @ApiModelProperty(value = "槽位")
    private String slotPosition;

    @ApiModelProperty(value = "告警码")
    private String alarmCode;

    @ApiModelProperty(value = "告警名称")
    private String alarmName;

    @ApiModelProperty(value = "告警原因")
    private String alarmReason;

    @ApiModelProperty(value = "第一次告警时间")
    private Timestamp firstTime;

    @ApiModelProperty(value = "最后一次告警时间")
    private Timestamp finalTime;

    @ApiModelProperty(value = "告警状态")
    private String alarmState;

    @ApiModelProperty(value = "告警恢复时间")
    private Timestamp recoveryTime;

    @ApiModelProperty(value = "告警备注")
    private String alarmRemark;

}
