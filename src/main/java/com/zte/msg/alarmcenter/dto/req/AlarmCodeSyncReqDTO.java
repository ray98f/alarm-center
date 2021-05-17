package com.zte.msg.alarmcenter.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author frp
 */
@ApiModel
@Data
public class AlarmCodeSyncReqDTO implements Serializable {

    @ApiModelProperty(value = "客户端系统内id")
    @NotBlank(message = "32000006")
    private String id;

    @ApiModelProperty(value = "系统编号")
    @NotNull(message = "32000006")
    private Long systemId;

    @ApiModelProperty(value = "线路编号")
    @NotNull(message = "32000006")
    private Long lineId;

    @ApiModelProperty(value = "告警码")
    @NotBlank(message = "32000006")
    private String alarmCode;

    @ApiModelProperty(value = "告警名称")
    @NotBlank(message = "32000006")
    private String alarmName;

    @ApiModelProperty(value = "告警原因")
    @NotBlank(message = "32000006")
    private String alarmReason;

    @ApiModelProperty(value = "告警级别")
    @NotNull(message = "32000006")
    private Integer alarmLevel;

    @ApiModelProperty(value = "处理意见")
    private String handlingOpinions;

    @ApiModelProperty(value = "删除标识 （0 未删除 1 已删除）")
    @NotNull(message = "32000006")
    private Integer isDeleted;

}
