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
 * @date 2021/1/19 13:35
 */
@Data
@ApiModel
public class DicResDTO {

    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "状态：0-停用，1-启用")
    private Integer isEnable;

    @ApiModelProperty(value = "备注")
    private String description;

    @ApiModelProperty(value = "操作时间")
    private Timestamp updatedAt;
}
