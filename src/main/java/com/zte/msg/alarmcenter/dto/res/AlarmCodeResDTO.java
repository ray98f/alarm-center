package com.zte.msg.alarmcenter.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class AlarmCodeResDTO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "线路id")
    private Long positionId;

    @ApiModelProperty(value = "线路名称")
    private String positionName;

    @ApiModelProperty(value = "线路编号")
    private Integer positionCode;

    @ApiModelProperty(value = "系统id")
    private Long systemId;

    @ApiModelProperty(value = "系统名称")
    private String systemName;

    @ApiModelProperty(value = "系统编号")
    private Integer systemCode;

    @ApiModelProperty(value = "告警码")
    private String alarmCode;

    @ApiModelProperty(value = "告警名称")
    private String alarmName;

    @ApiModelProperty(value = "告警原因")
    private String reason;

    @ApiModelProperty(value = "告警级别id")
    private Long alarmLevelId;

    @ApiModelProperty(value = "告警级别名称")
    private String alarmLevelName;

    @ApiModelProperty(value = "处理意见")
    private String handlingOpinions;

}
