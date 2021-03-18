package com.zte.msg.alarmcenter.dto.res;

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
    private String createdAt;
    private String createdBy;
    private String updatedAt;
    private String updatedBy;

}
