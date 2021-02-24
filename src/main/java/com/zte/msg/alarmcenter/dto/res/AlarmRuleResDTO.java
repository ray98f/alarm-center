package com.zte.msg.alarmcenter.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/2/23 10:28
 */
@Data
@ApiModel
public class AlarmRuleResDTO {

    private Long id;

    @ApiModelProperty(value = "告警规则名称")
    private String name;

    @ApiModelProperty(value = "规则类型")
    private Integer type;

    @ApiModelProperty(value = "规则状态")
    private Integer isEnable;

    private String updatedBy;

    private Timestamp updatedAt;
}
