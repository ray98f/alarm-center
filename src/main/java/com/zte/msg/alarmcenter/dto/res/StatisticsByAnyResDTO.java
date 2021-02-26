package com.zte.msg.alarmcenter.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@ApiModel
@Data
public class StatisticsByAnyResDTO {

    @ApiModelProperty(value = "子系统id")
    private Long childSystemId;

    @ApiModelProperty(value = "子系统名称")
    private String childSystemName;

    @ApiModelProperty(value = "站点id")
    private Long siteId;

    @ApiModelProperty(value = "站点名称")
    private String siteName;

    @ApiModelProperty(value = "告警级别")
    private Integer alarmLevel;

    @ApiModelProperty(value = "告警数据量")
    private Long alarmNum;
}
