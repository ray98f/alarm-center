package com.zte.msg.alarmcenter.dto.res;

import lombok.Data;

@Data
public class AlarmAbnormalResDTO {

    private Long id;
    private String alarmTime;
    private Integer systemCode;
    private String systemName;
    private String lineCode;
    private String lineName;
    private String positionCode;
    private String positionName;
    private String deviceCode;
    private String deviceName;
    private String slotCode;
    private String slotName;
    private String alarmCode;
    private String alarmName;
    private String ip;
    private String createdAt;
    private String errorContent;
}
