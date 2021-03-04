package com.zte.msg.alarmcenter.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/2/22 10:02
 */
@Data
@ApiModel
public class PositionReqDTO {

    @ApiModelProperty(value = "位置名称")
    @NotBlank(message = "32000006")
    private String name;

    @ApiModelProperty(value = "上级节点id，不传则表示新增根节点")
    private Long pId;

    @ApiModelProperty(value = "位置图标")
    private String icon;

    @ApiModelProperty(value = "地形图")
    private String topographic;

    @ApiModelProperty(value = "位置编码")
    private String positionCode;

    @ApiModelProperty(value = "位置类型，0-地图，1-线路，3-车站")
    private Integer type;

    @ApiModelProperty(value = "坐标")
    private String coordinate;

    @ApiModelProperty(value = "人员id")
    private String userId;
}
