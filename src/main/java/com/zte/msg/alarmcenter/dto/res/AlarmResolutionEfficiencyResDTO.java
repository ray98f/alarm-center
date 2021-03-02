package com.zte.msg.alarmcenter.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author frp
 */
@ApiModel
@Data
public class AlarmResolutionEfficiencyResDTO {

    @ApiModelProperty(value = "日期")
    private String statisticsDate;

    @ApiModelProperty(value = "告警级别")
    private Integer alarmLevel;

    @ApiModelProperty(value = "处置时间")
    private Long disposalTime;
}
