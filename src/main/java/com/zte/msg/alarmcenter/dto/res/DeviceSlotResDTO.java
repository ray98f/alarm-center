package com.zte.msg.alarmcenter.dto.res;

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
 * @date 2021/2/22 15:41
 */
@Data
@ApiModel
public class DeviceSlotResDTO {

    @ApiModelProperty(value = "id")
    @NotBlank(message = "32000006")
    private Long id;

    @ApiModelProperty(value = "槽位名称")
    @NotBlank(message = "32000006")
    private String slotName;

    @ApiModelProperty(value = "槽位编号")
    @NotNull(message = "32000006")
    private String slotCode;

    @ApiModelProperty(value = "设备编码")
    @NotBlank(message = "32000006")
    private String deviceCode;

    @ApiModelProperty(value = "设备位置")
    @NotNull(message = "32000006")
    private String positionName;

    @ApiModelProperty(value = "所属系统")
    @NotBlank(message = "32000006")
    private String systemName;

    @ApiModelProperty(value = "设备名称")
    @NotBlank(message = "32000006")
    private String deviceName;

}
