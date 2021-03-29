package com.zte.msg.alarmcenter.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@ApiModel
@Data
public class HomeAlarmStatusSituationResDTO {

    @ApiModelProperty(value = "告警总数")
    private Long totalAlarmNum;

    @ApiModelProperty(value = "紧急告警总数")
    private Long emergencyAlarmNum;

    @ApiModelProperty(value = "严重告警总数")
    private Long seriousAlarmNum;

    @ApiModelProperty(value = "一般告警总数")
    private Long generalAlarmNum;
}
