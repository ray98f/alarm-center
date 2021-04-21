package com.zte.msg.alarmcenter.dto.res;

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
public class AlarmHistoryResDTO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "子系统id")
    private Long subsystemId;

    @ApiModelProperty(value = "子系统名称")
    private String subsystemName;

    @ApiModelProperty(value = "子系统Code")
    private Integer subsystemCode;

    @ApiModelProperty(value = "告警等级")
    private Integer alarmLevel;

    @ApiModelProperty(value = "线路id")
    private Long lineId;

    @ApiModelProperty(value = "线路名称")
    private String lineName;

    @ApiModelProperty(value = "线路Code")
    private Integer lineCode;

    @ApiModelProperty(value = "站点id")
    private Long siteId;

    @ApiModelProperty(value = "站点名称")
    private String siteName;

    @ApiModelProperty(value = "设备id")
    private Long deviceId;

    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "槽位id")
    private Long slotId;

    @ApiModelProperty(value = "槽位名称")
    private String slotPosition;

    @ApiModelProperty(value = "告警码")
    private String alarmCode;

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

    @ApiModelProperty(value = "是否响铃")
    private Integer isRing;

    @ApiModelProperty(value = "告警状态(1待处理;2手动确认;3自动确认;4已清除;5手动过滤;6自动过滤)")
    private Integer alarmState;

    @ApiModelProperty(value = "告警恢复时间")
    private Timestamp recoveryTime;

    @ApiModelProperty(value = "告警备注")
    private String alarmRemark;

    @ApiModelProperty(value = "告警记录附加信息")
    private List<AlarmMessageResDTO> alarmMessageResDTOList;

    @Data
    public static class AlarmMessageResDTO {

        @ApiModelProperty(value = "标题")
        private String title;

        @ApiModelProperty(value = "内容")
        private String content;
    }
}
