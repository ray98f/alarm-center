package com.zte.msg.alarmcenter.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author frp
 */
@ApiModel
@Data
public class DeviceSyncReqDTO implements Serializable {

    @ApiModelProperty(value = "客户端系统内id")
    @NotBlank(message = "32000006")
    private String id;

    @ApiModelProperty(value = "系统编号")
    @NotNull(message = "32000006")
    private Long systemId;

    @ApiModelProperty(value = "线路编号")
    @NotNull(message = "32000006")
    private Long lineId;

    @ApiModelProperty(value = "车站编号")
    @NotNull(message = "32000006")
    private Long stationId;

    @ApiModelProperty(value = "设备编号")
    @NotBlank(message = "32000006")
    private String deviceId;

    @ApiModelProperty(value = "设备名称")
    @NotBlank(message = "32000006")
    private String deviceName;

    @ApiModelProperty(value = "设备厂商")
    private String manufacturer;

    @ApiModelProperty(value = "规格型号")
    private String model;

    @ApiModelProperty(value = "删除标识 （0 未删除 1 已删除）")
    @NotNull(message = "32000006")
    private Integer isDeleted;

}
