package com.zte.msg.alarmcenter.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@ApiModel
@Data
public class AnyAlarmTrendResDTO {

    @ApiModelProperty(value = "日期")
    private String statisticsDate;

    @ApiModelProperty(value = "告警级别")
    private Integer alarmLevel;

    @ApiModelProperty(value = "告警数据量")
    private Long alarmNum;

    @ApiModelProperty(value = "严重告警数量")
    private Long seriousAlarmNum;

    @ApiModelProperty(value = "紧急告警数量")
    private Long emergencyAlarmNum;

    @ApiModelProperty(value = "一般告警数量")
    private Long generalAlarmNum;
}
