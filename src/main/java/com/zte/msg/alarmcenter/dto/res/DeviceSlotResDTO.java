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
    private Long id;

    @ApiModelProperty(value = "槽位名称")
    private String slotName;

    @ApiModelProperty(value = "槽位编号")
    private String slotCode;

    @ApiModelProperty(value = "设备编码")
    private String deviceCode;

    @ApiModelProperty(value = "设备位置")
    private String positionName;

    @ApiModelProperty(value = "所属系统")
    private String systemName;

    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "设备id")
    private String deviceId;

    @ApiModelProperty(value = "系统id")
    private Long systemId;

    @ApiModelProperty(value = "站点id")
    private Long positionId;

    @ApiModelProperty(value = "系统编号")
    private Integer systemCode;

    @ApiModelProperty(value = "站点编号")
    private Integer positionCode;

    @ApiModelProperty(value = "线路编号")
    private Integer lineCode;

    @ApiModelProperty(value = "线路名称")
    private String lineName;

}
