package com.zte.msg.alarmcenter.entity;

import com.zte.msg.alarmcenter.dto.req.AlarmHistoryReqDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author frp
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class AlarmHistory extends BaseEntity implements Serializable {

    private Long id;

    @ApiModelProperty(value = "子系统id")
    private Long subsystemId;

    @ApiModelProperty(value = "子系统编码")
    private Integer subsystemCode;

    @ApiModelProperty(value = "子系统名称")
    private String subsystemName;

    @ApiModelProperty(value = "线路id")
    private Long lineId;

    @ApiModelProperty(value = "线路编码")
    private Integer lineCode;

    @ApiModelProperty(value = "线路名称")
    private String lineName;

    @ApiModelProperty(value = "站点id")
    private Long siteId;

    @ApiModelProperty(value = "站点码")
    private Integer siteCode;

    @ApiModelProperty(value = "站点名称")
    private String siteName;

    @ApiModelProperty(value = "设备id")
    private Long deviceId;

    @ApiModelProperty(value = "设备编码")
    private Integer deviceCode;

    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "槽位id")
    private Long slotId;

    @ApiModelProperty(value = "槽位编码")
    private Integer slotCode;

    @ApiModelProperty(value = "槽位名称")
    private String slotName;

    @ApiModelProperty(value = "告警等级")
    private Integer alarmLevel;

    @ApiModelProperty(value = "告警码id")
    private Long alarmCode;

    @ApiModelProperty(value = "告警码")
    private Integer alarmCodeId;

    @ApiModelProperty(value = "告警名称")
    private String alarmName;

    @ApiModelProperty(value = "告警原因")
    private String alarmReason;

    @ApiModelProperty(value = "第一次告警时间")
    private Timestamp firstTime;

    @ApiModelProperty(value = "最后一次告警时间")
    private Timestamp finalTime;

    @ApiModelProperty(value = "告警次数")
    private Integer frequency;

    @ApiModelProperty(value = "告警音量")
    private String alarmVolume;

    @ApiModelProperty(value = "告警升级")
    private Integer isUpgrade;

    @ApiModelProperty(value = "是否静音")
    private Integer isMute;

    @ApiModelProperty(value = "告警状态(0待处理;1延迟告警;2手动确认;3自动确认;4手动过滤;5自动过滤;6手动清除;7自动清除)")
    private Integer alarmState;

    @ApiModelProperty(value = "告警恢复时间")
    private Timestamp recoveryTime;

    @ApiModelProperty(value = "告警延迟时间")
    private Timestamp delayTime;

    @ApiModelProperty(value = "告警升级频次")
    private Integer alarmFrequency;

    @ApiModelProperty(value = "告警升级频次单位时间")
    private Long frequencyTime;

    @ApiModelProperty(value = "告警升级经历时间")
    private Long experienceTime;

    @ApiModelProperty(value = "告警升级等级")
    private Integer alarmUpdateLevel;

    @ApiModelProperty(value = "告警备注")
    private String alarmRemark;

    @ApiModelProperty(value = "是否响铃")
    private Integer isRing;

    @ApiModelProperty(value = "告警附加信息")
    private List<AlarmHistoryReqDTO.AlarmMessage> alarmMessageList;

    @ApiModelProperty("是否为告警恢复")
    private Boolean isRecovery = false;

}
