package com.zte.msg.alarmcenter.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/2/23 9:54
 */
@Data
@ApiModel
public class SlotReqDTO {

    @ApiModelProperty(value = "设备id")
    @NotNull(message = "32000006")
    private Long deviceId;

    @NotNull(message = "32000006")
    @ApiModelProperty(value = "槽位编码")
    private String slotCode;

    @ApiModelProperty(value = "槽位名称")
    private String name;
}
