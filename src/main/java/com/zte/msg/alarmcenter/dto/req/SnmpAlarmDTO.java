package com.zte.msg.alarmcenter.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/2/24 10:16
 */
@Data
@ApiModel
public class SnmpAlarmDTO {

    /**
     * 线路编号
     */
    private int lineCode;
    /**
     * 系统编号
     */
    private int systemCode;
    /**
     * 是否清除告警 false 正常告警  true 消除告警
     */
    private boolean cleared;
    /**
     * 告警位置名称
     */
    private String alarmManagedObjectInstanceName;
    /**
     * 告警特殊原因
     */
    private String alarmSpecificProblem;
    /**
     * 告警码
     */
    private String emsAlarmCode;
    /**
     * 网元类型
     */
    private String alarmNetype;

    /**
     * 告警时间
     */
    private Timestamp alarmTime;

    /**
     * 附加信息
     */
    private List<AlarmHistoryReqDTO.AlarmMessage> messages;

}
