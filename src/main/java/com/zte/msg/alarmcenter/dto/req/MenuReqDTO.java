package com.zte.msg.alarmcenter.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class MenuReqDTO {

    @ApiModelProperty(value = "名称")
    private String menuName;

    @ApiModelProperty(value = "是否使用")
    private Integer isUse;
}
