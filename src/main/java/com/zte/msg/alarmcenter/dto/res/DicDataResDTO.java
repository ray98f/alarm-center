package com.zte.msg.alarmcenter.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/1/19 14:54
 */
@Data
@ApiModel
public class DicDataResDTO {

    private Long id;

    @ApiModelProperty(value = "字典键值")
    private Integer key;

    @ApiModelProperty(value = "字典标签")
    private String value;

    @ApiModelProperty(value = "字典类型")
    private String type;

    private Integer order;

    @ApiModelProperty(value = "备注")
    private String description;

    @ApiModelProperty(value = "状态:0-禁用，1-启用")
    private Integer isEnable;

    @ApiModelProperty(value = "操作时间")
    private Timestamp updatedAt;
}
