package com.zte.msg.alarmcenter.dto.res;

import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
public class AlarmRuleDataResDTO {
    private Long id;
    private Long type;
    private String name;
    private Long isEnable;
    private Long delayTime;
    private Integer frequency;
    private Long frequencyTime;
    private Long experienceTime;
    private Long msgConfigId;
    private Integer isDeleted;
    private List<Long> systemIds;
    private List<Long> positionIds;
    private List<Long> deviceIds;
    private List<Long> alarmIds;
}
