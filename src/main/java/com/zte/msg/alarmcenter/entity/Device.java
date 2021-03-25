package com.zte.msg.alarmcenter.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
public class Device extends BaseEntity {

    private String name;

    private Long exeId;

    private Long systemId;

    private Long positionId;

    private Long stationId;

    private Integer deviceCode;

    private String brand;

    private String serialNum;

    private String description;

    private String manufacturer;

    private Integer isNormal;

    private Integer isLocked;

}
