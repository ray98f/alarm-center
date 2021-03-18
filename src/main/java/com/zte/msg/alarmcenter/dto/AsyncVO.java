package com.zte.msg.alarmcenter.dto;

import com.zte.msg.alarmcenter.dto.req.AlarmCodeSyncReqDTO;
import com.zte.msg.alarmcenter.dto.req.DeviceSyncReqDTO;
import com.zte.msg.alarmcenter.dto.req.SlotSyncReqDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@ApiModel
@Data
public class AsyncVO {

    @ApiModelProperty(value = "全量告警码同步信息")
    private List<AlarmCodeSyncReqDTO> alarmCodeSyncReqDTOList;

    @ApiModelProperty(value = "全量设备同步信息")
    private List<DeviceSyncReqDTO> deviceSyncReqDTOList;

    @ApiModelProperty(value = "全量设备槽位同步信息")
    private List<SlotSyncReqDTO> slotSyncReqDTOList;
}
