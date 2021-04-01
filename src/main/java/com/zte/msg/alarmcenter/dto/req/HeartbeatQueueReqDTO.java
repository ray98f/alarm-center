package com.zte.msg.alarmcenter.dto.req;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author frp
 */
@Data
@ApiModel
public class HeartbeatQueueReqDTO implements Serializable {

    private Integer lineCode;

    private Integer systemCode;

    private LocalDateTime time;

}
