package com.zte.msg.alarmcenter.dto.res;

import lombok.Data;

import java.util.List;

@Data
public class AlarmRuleDetailsResDTO {

    private Long id;
    private Long type;
    private String name;
    private Long isEnable;
    private List<String> systemIds;
    private List<String> positionIds;
    private List<String> deviceIds;
    private List<String> alarmIds;
    private String createdAt;
    private String createdBy;
    private String updatedAt;
    private String updatedBy;
}
