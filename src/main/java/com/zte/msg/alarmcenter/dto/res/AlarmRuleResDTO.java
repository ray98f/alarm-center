package com.zte.msg.alarmcenter.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/2/23 10:28
 */
@Data
@ApiModel
public class AlarmRuleResDTO {

    private Long id;

    @ApiModelProperty(value = "告警规则名称")
    private String name;

    @ApiModelProperty(value = "规则类型")
    private Integer type;

    @ApiModelProperty(value = "规则状态")
    private Integer isEnable;

    @ApiModelProperty(value = "告警延迟规则告警延迟时间")
    private Long delayTime;

    @ApiModelProperty(value = "告警升级规则单位时间内告警次数")
    private Integer frequency;

    @ApiModelProperty(value = "告警升级规则告警单位时间")
    private Long frequencyTime;

    @ApiModelProperty(value = "告警升级规则告警经历时间")
    private Long experienceTime;

    @ApiModelProperty(value = "告警前转规则信息类型")
    private Integer msgType;

    @ApiModelProperty(value = "告警前转规则手机")
    private String msgPhone;

    @ApiModelProperty(value = "告警前转规则邮箱")
    private String msgEmail;

    @ApiModelProperty(value = "告警前转规则告警箱")
    private String msgBox;

    @ApiModelProperty(value = "创建时间")
    private String createdAt;

    @ApiModelProperty(value = "创建人")
    private String createdBy;

    @ApiModelProperty(value = "修改时间")
    private String updatedBy;

    @ApiModelProperty(value = "修改人")
    private String updatedAt;
}
