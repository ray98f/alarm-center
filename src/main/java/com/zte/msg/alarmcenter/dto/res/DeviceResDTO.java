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
public class DeviceResDTO {

    @ApiModelProperty(value = "id")
    @NotBlank(message = "32000006")
    private Long id;

    @ApiModelProperty(value = "设备名称")
    @NotBlank(message = "32000006")
    private String name;

    @ApiModelProperty(value = "子系统id")
    private Long systemId;

    @ApiModelProperty(value = "子系统编号")
    private Integer systemCode;

    @ApiModelProperty(value = "子系统名称")
    @NotNull(message = "32000006")
    private String systemName;

    @ApiModelProperty(value = "位置id")
    private Long positionId;

    @ApiModelProperty(value = "位置编号")
    private Integer positionCode;

    @ApiModelProperty(value = "位置名称")
    @NotNull(message = "32000006")
    private String positionName;

    @ApiModelProperty(value = "设备编码")
    @NotBlank(message = "32000006")
    private String deviceCode;

    @ApiModelProperty(value = "")
    @NotBlank(message = "32000006")
    private String brand;

    @ApiModelProperty(value = "")
    @NotBlank(message = "32000006")
    private String serialNum;

    @ApiModelProperty(value = "设备描述")
    private String description;
}
