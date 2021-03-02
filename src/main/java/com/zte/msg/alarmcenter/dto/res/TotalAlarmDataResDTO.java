package com.zte.msg.alarmcenter.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@ApiModel
@Data
public class TotalAlarmDataResDTO {

    @ApiModelProperty(value = "子系统id")
    private Long subsystemId;

    @ApiModelProperty(value = "子系统名称")
    private String subsystemName;

    @ApiModelProperty(value = "站点id")
    private Long siteId;

    @ApiModelProperty(value = "站点名称")
    private String siteName;

    @ApiModelProperty(value = "告警名称")
    private String alarmName;

    @ApiModelProperty(value = "告警原因")
    private String alarmReason;

    @ApiModelProperty(value = "告警数据量")
    private Long alarmNum;
}
