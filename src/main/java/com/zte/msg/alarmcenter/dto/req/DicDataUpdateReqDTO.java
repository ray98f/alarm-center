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
 * @date 2021/1/19 16:02
 */
@Data
@ApiModel
public class DicDataUpdateReqDTO {

    private Long id;

    @ApiModelProperty(value = "字典键值")
    @NotNull(message = "32000006")
    private Integer key;

    @ApiModelProperty(value = "字典标签")
    @NotNull(message = "32000006")
    private String value;

    @ApiModelProperty(value = "字典类型")
    private String type;

    @ApiModelProperty(value = "备注")
    private String description;

    @ApiModelProperty(value = "字典排序")
    @NotNull(message = "32000006")
    private Integer order;

    @ApiModelProperty(value = "状态：0-禁用，1-启用")
    private Integer isEnable;

}
