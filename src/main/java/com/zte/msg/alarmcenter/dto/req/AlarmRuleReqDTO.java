package com.zte.msg.alarmcenter.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

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

    @ApiModelProperty(value = "子系统id集合")
    @NotNull(message = "32000006")
    private List<Integer> systemIds;

    @ApiModelProperty(value = "车站id集合")
    @NotNull(message = "32000006")
    private List<Integer> positionIds;

    @ApiModelProperty(value = "设备id集合")
    @NotNull(message = "32000006")
    private List<Integer> deviceIds;

    @ApiModelProperty(value = "告警码id集合")
    @NotNull(message = "32000006")
    private List<Integer> alarmIds;

    @ApiModelProperty(value = "规则状态")
    @NotNull(message = "32000006")
    private Integer isEnable;

    @ApiModelProperty(value = "系统id")
    private Integer systemId;

    @ApiModelProperty(value = "位置id")
    private Integer positionId;

    @ApiModelProperty(value = "设备id")
    private Integer deviceId;

    @ApiModelProperty(value = "告警码id")
    private Integer alarmId;

    @ApiModelProperty(value = "用户")
    private String userId;

    @ApiModelProperty(value = "id")
    private Long id;
}
