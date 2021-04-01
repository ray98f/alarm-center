package com.zte.msg.alarmcenter.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author frp
 */
@Data
@ApiModel
public class AlarmHistoryReqDTO implements Serializable {

    @ApiModelProperty(value = "系统")
    private Integer system;

    @ApiModelProperty(value = "线路")
    private Integer line;

    @ApiModelProperty(value = "车站")
    private Integer station;

    @ApiModelProperty(value = "设备")
    private Integer device;

    @ApiModelProperty(value = "槽位")
    private Integer slot;

    @ApiModelProperty(value = "故障码")
    private Integer alarmCode;

    @ApiModelProperty(value = "报警时间")
    private Timestamp alarmTime;

    @ApiModelProperty(value = "故障恢复标识 true 恢复 false 故障")
    private Boolean recovery;

    @ApiModelProperty(value = "告警附加信息")
    private List<AlarmMessage> alarmMessageList;

    @Data
    public static class AlarmMessage implements Serializable{

        @ApiModelProperty(value = "标题")
        private String title;

        @ApiModelProperty(value = "内容")
        private String content;
    }

}
