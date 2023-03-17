package com.zte.msg.alarmcenter.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@ApiModel
@Data
public class HomeSubsystemSituationResDTO {

    @ApiModelProperty(value = "系统id")
    private Long subsystemId;

    @ApiModelProperty(value = "系统code")
    private Integer subsystemCode;

    @ApiModelProperty(value = "系统名称")
    private String subsystemName;

    @ApiModelProperty(value = "是否在线")
    private Integer isOnline;

    @ApiModelProperty(value = "紧急告警数")
    private Long emergencyAlarmNum;

    @ApiModelProperty(value = "严重告警数")
    private Long seriousAlarmNum;

    @ApiModelProperty(value = "一般告警数")
    private Long generalAlarmNum;
}
