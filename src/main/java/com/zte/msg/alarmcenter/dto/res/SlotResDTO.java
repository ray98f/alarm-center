package com.zte.msg.alarmcenter.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/2/23 9:14
 */
@Data
@ApiModel
public class SlotResDTO {

    @ApiModelProperty(value = "槽位编码")
    private String slotCode;

    @ApiModelProperty(value = "槽位名称")
    private String slotName;


}
