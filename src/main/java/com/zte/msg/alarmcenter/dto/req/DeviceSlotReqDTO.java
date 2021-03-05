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
 * @date 2021/2/22 15:41
 */
@Data
@ApiModel
public class DeviceSlotReqDTO {

    @ApiModelProperty(value = "设备id")
    @NotBlank(message = "32000006")
    private String deviceId;

    @ApiModelProperty(value = "槽位名称")
    @NotBlank(message = "32000006")
    private String slotName;

    @ApiModelProperty(value = "槽位编号")
    @NotNull(message = "32000006")
    private String slotCode;

    @ApiModelProperty(value = "设备编号")
    @NotBlank(message = "32000006")
    private String deviceCode;

    @ApiModelProperty(value = "车站编号")
    @NotBlank(message = "32000006")
    private String stationCode;

    @ApiModelProperty(value = "线路编号")
    @NotBlank(message = "32000006")
    private String LineCode;

    @ApiModelProperty(value = "系统编号")
    @NotBlank(message = "32000006")
    private String systemCode;


}
