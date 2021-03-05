package com.zte.msg.alarmcenter.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author frp
 */
@Data
@ApiModel
public class StatisticsByAnyReqDTO {

    @ApiModelProperty(value = "系统")
    private List<Long> systemIds;

    @ApiModelProperty(value = "站点")
    private List<Long> siteIds;

    @ApiModelProperty(value = "告警级别")
    private List<Integer> alarmLevels;

    @ApiModelProperty(value = "开始时间")
    private Timestamp startTime;

    @ApiModelProperty(value = "结束时间")
    private Timestamp endTime;
}
