package com.zte.msg.alarmcenter.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2021/3/24 10:36
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceSlot extends BaseEntity {

    private Long id;

    private String name;

    private Long exeId;

    private Long systemId;

    private Long positionId;

    private Long stationId;

    private Long deviceId;

    private Integer slotCode;

    private Integer isLocked;

}
