package com.zte.msg.alarmcenter.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author frp
 */
@ApiModel
public class RedisUpdateFrequencyResDTO {

    @ApiModelProperty(value = "截止时间")
    private Long time;

    @ApiModelProperty(value = "当前告警次数")
    private Integer frequency;

    public RedisUpdateFrequencyResDTO(Long time, Integer frequency) {
        this.time = time;
        this.frequency = frequency;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "RedisUpdateFrequencyResDTO{" +
                "time=" + time +
                ", frequency=" + frequency +
                '}';
    }
}
