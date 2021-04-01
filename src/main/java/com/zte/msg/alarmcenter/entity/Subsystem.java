package com.zte.msg.alarmcenter.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2021/3/24 10:36
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Subsystem extends BaseEntity {
    private Long pid;

    private String name;

    private Integer sid;

    private String serverIp;

    private Integer serverPort;

    private String icon;

    private Integer isOnline;

    private Timestamp onlineTime;
}
