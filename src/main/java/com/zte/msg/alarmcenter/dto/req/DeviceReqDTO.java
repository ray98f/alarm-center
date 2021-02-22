package com.zte.msg.alarmcenter.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/2/22 14:01
 */
@Data
@ApiModel
public class DeviceReqDTO {

    @ApiModelProperty(value = "设备名称")
    @NotBlank(message = "32000006")
    private String name;

    @ApiModelProperty(value = "子系统id")
    @NotNull(message = "32000006")
    private Long systemId;

    @ApiModelProperty(value = "位置id")
    @NotNull(message = "32000006")
    private Long positionId;

    @ApiModelProperty(value = "设备编码")
    @NotBlank(message = "32000006")
    private String deviceCode;

    @ApiModelProperty(value = "")
    @NotBlank(message = "32000006")
    private String brand;

    @ApiModelProperty(value = "设备描述")
    private String description;

}
