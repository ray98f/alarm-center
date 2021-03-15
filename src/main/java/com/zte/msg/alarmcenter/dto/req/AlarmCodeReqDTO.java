package com.zte.msg.alarmcenter.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/2/23 10:38
 */
@Data
@ApiModel
public class AlarmCodeReqDTO {

    @ApiModelProperty(value = "告警码")
    private Long alarmCode;

    @ApiModelProperty(value = "告警名称")
    private String alarmName;

    @ApiModelProperty(value = "系统id")
    private Long systemId;

    @ApiModelProperty(value = "告警级别")
    private Long alarmLevelId;

    @ApiModelProperty(value = "线路编号")
    private Long positionId;

    @ApiModelProperty(value = "告警原因")
    private String reason;

    @ApiModelProperty(value = "处理意见")
    private String handlingOpinions;
}
