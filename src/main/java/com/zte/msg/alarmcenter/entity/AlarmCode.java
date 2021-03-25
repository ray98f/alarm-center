package com.zte.msg.alarmcenter.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

/**
 * @author frp
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class AlarmCode extends BaseEntity {

    @ApiModelProperty(value = "第三方内告警码id")
    private Long exeId;

    @ApiModelProperty(value = "系统id")
    private Long systemId;

    @ApiModelProperty(value = "位置id")
    private Long positionId;

    @ApiModelProperty(value = "告警码")
    private Integer code;

    @ApiModelProperty(value = "告警名称")
    private String name;

    @ApiModelProperty(value = "告警原因")
    private String reason;

    @ApiModelProperty(value = "告警级别")
    private Integer levelId;

    @ApiModelProperty(value = "处理意见")
    private String handlingOpinions;

    @ApiModelProperty(value = "是否锁定")
    private Integer isLocked;

}
