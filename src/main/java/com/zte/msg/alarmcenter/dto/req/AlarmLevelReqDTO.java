package com.zte.msg.alarmcenter.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/2/23 10:38
 */
@Data
@ApiModel
public class AlarmLevelReqDTO {

    @ApiModelProperty(value = "告警级别名称")
    @NotBlank(message = "32000006")
    private String name;

    @NotNull(message = "32000006")
    @ApiModelProperty(value = "告警级别颜色")
    private String color;

    private String updatedBy;

    private Timestamp updatedAt;
}
