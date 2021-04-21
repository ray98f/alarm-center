package com.zte.msg.alarmcenter.dto.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AlarmRuleDetailsResDTO {

    private Long id;
    private Long type;
    private String name;
    private Long isEnable;
    private List<DataIdAndNameResDTO> systemIds;
    private List<DataIdAndNameResDTO> positionIds;
    private List<DataIdAndNameResDTO> deviceIds;
    private List<DataIdAndNameResDTO> alarmIds;

    @ApiModelProperty(value = "告警延迟规则告警延迟时间")
    private Long delayTime;

    @ApiModelProperty(value = "告警升级规则单位时间内告警次数")
    private Integer frequency;

    @ApiModelProperty(value = "告警升级规则告警单位时间")
    private Long frequencyTime;

    @ApiModelProperty(value = "告警升级规则告警经历时间")
    private Long experienceTime;

    @ApiModelProperty(value = "告警前转规则信息id")
    private Long msgConfigId;

    @ApiModelProperty(value = "告警前转规则信息类型")
    private Integer msgType;

    @ApiModelProperty(value = "告警前转规则手机")
    private String msgPhone;

    @ApiModelProperty(value = "告警前转规则邮箱")
    private String msgEmail;

    @ApiModelProperty(value = "告警前转规则告警箱")
    private String msgBox;
    private String createdAt;
    private String createdBy;
    private String updatedAt;
    private String updatedBy;

}
