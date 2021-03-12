package com.zte.msg.alarmcenter.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@ApiModel
@Data
public class HomeDeviceSituationResDTO {

    @ApiModelProperty(value = "设备总数")
    private Long totalNum;

    @ApiModelProperty(value = "正常设备数")
    private Long normalNum;

    @ApiModelProperty(value = "异常设备数")
    private Long abnormalNum;

    @ApiModelProperty(value = "正常设备占有率")
    private Double normalRate;

    public void setNormalRate(Long totalNum, Long normalNum) {
        this.normalRate = (double) (normalNum / totalNum) * 100;
    }
}
