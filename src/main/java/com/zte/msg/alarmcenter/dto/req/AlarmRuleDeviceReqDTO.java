package com.zte.msg.alarmcenter.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@ApiModel
@Data
public class AlarmRuleDeviceReqDTO {

    @ApiModelProperty(value = "子系统id列表")
    private List<Long> systemIds;

    @ApiModelProperty(value = "车站id列表")
    private List<Long> stationIds;
}
