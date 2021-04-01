package com.zte.msg.alarmcenter.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@ApiModel
@Data
public class HomeStationSituationResDTO {

    @ApiModelProperty(value = "线路id")
    private Long lineId;

    @ApiModelProperty(value = "线路名称")
    private String lineName;

    @ApiModelProperty(value = "站点信息")
    private List<Station> stationList;

    @Data
    public static class Station {

        @ApiModelProperty(value = "线路id")
        private Long lineId;

        @ApiModelProperty(value = "车站id")
        private Long stationId;

        @ApiModelProperty(value = "车站名称")
        private String stationName;

        @ApiModelProperty(value = "车站状态")
        private Integer stationStatus;

        @ApiModelProperty(value = "车站坐标")
        private String coordinate;

        @ApiModelProperty(value = "紧急告警数")
        private Long emergencyAlarmNum;

        @ApiModelProperty(value = "严重告警数")
        private Long seriousAlarmNum;

        @ApiModelProperty(value = "一般告警数")
        private Long generalAlarmNum;

        @ApiModelProperty(value = "系统信息")
        private List<Subsystem> subsystemList;

        @Data
        public static class Subsystem {
            @ApiModelProperty(value = "系统id")
            private Long subsystemId;

            @ApiModelProperty(value = "系统名称")
            private String subsystemName;

            @ApiModelProperty(value = "紧急告警数")
            private Long sysEmergencyAlarmNum;

            @ApiModelProperty(value = "严重告警数")
            private Long sysSeriousAlarmNum;

            @ApiModelProperty(value = "一般告警数")
            private Long sysGeneralAlarmNum;
        }
    }
}
