package com.zte.msg.alarmcenter.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/2/18 17:17
 */
@ApiModel
@Data
public class ChildSystemPageResDTO {

    private Long id;

    @ApiModelProperty(value = "子系统名称")
    private String name;

    @ApiModelProperty(value = "子系统简称")
    private String abbreviation;

}
