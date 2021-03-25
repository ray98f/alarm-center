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
public class Position extends BaseEntity {

    private Long pid;

    private Integer positionCode;

    private String name;

    private String icon;

    private String topographic;

    private Integer type;

    private String coordinate;
}
