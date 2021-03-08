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
public class AnyAlarmTrendReqDTO {
    @ApiModelProperty(value = "系统")
    private Long systemId;

    @ApiModelProperty(value = "站点")
    private Long siteId;

    @ApiModelProperty(value = "告警级别")
    private List<Integer> alarmLevels;

    @ApiModelProperty(value = "统计周期")
    private Integer statisticsCycle;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;
}
