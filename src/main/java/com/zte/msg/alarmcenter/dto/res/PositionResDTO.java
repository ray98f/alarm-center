package com.zte.msg.alarmcenter.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/2/19 14:16
 */
@Data
@ApiModel
public class PositionResDTO {

    @ApiModelProperty(value = "节点id")
    private Long id;

    @ApiModelProperty(value = "父节点id")
    private Long pId;

    @ApiModelProperty(value = "编号")
    private String code;

    @ApiModelProperty(value = "节点名称")
    private String name;

    @ApiModelProperty(value = "位置图标")
    private String icon;

    @ApiModelProperty(value = "地形图")
    private String topographic;

    @ApiModelProperty(value = "节点类型，0-线路，1-站点")
    private Integer type;

    @ApiModelProperty(value = "控制区")
    private String controlArea;

    @ApiModelProperty(value = "数据子集")
    private List<PositionResDTO> children;
}
