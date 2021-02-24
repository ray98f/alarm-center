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
 * @date 2021/2/23 10:14
 */
@Data
@ApiModel
public class AlarmRuleReqDTO {

    @ApiModelProperty(value = "告警规则名称")
    @NotBlank(message = "32000006")
    private String name;

    @ApiModelProperty(value = "规则类型")
    @NotNull(message = "32000006")
    private Integer type;

    @ApiModelProperty(value = "规则状态")
    @NotNull(message = "32000006")
    private Integer isEnable;
}
