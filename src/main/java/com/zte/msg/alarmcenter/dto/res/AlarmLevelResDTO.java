package com.zte.msg.alarmcenter.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class AlarmLevelResDTO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "告警编号")
    private Long code;

    @ApiModelProperty(value = "告警名称")
    private Long name;

    @ApiModelProperty(value = "告警色号")
    private Long color;

    @ApiModelProperty(value = "创建时间")
    private Long createdAt;

    @ApiModelProperty(value = "更新时间")
    private Long updatedAt;

    @ApiModelProperty(value = "创建人")
    private Long createdBy;

    @ApiModelProperty(value = "更新人")
    private Long updatedBy;

}
