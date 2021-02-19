package com.zte.msg.alarmcenter.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/2/18 17:28
 */
@Data
@ApiModel
public class ChildSystemReqDTO {

    @NotNull(message = "32000006")
    private Long id;

    @ApiModelProperty(value = "子系统名称")
    @NotBlank(message = "32000006")
    private String name;

    @ApiModelProperty(value = "子系统简称")
    @NotBlank(message = "32000006")
    private String abbreviation;
}
