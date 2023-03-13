package com.zte.msg.alarmcenter.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SnmpDeviceSlot extends BaseEntity {

    private Integer system;

    private Integer line;

    private Integer station;

    private Integer device;

    private Integer slot;

    private String snmpName;

}
