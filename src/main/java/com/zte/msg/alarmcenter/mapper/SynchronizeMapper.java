package com.zte.msg.alarmcenter.mapper;

import com.zte.msg.alarmcenter.dto.req.AlarmCodeSyncReqDTO;
import com.zte.msg.alarmcenter.dto.req.DeviceSyncReqDTO;
import com.zte.msg.alarmcenter.dto.req.SlotSyncReqDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface SynchronizeMapper {

    /**
     * 全量告警码同步
     *
     * @param alarmCodeSyncReqDTOList
     * @return
     */
    int alarmCodesSync(List<AlarmCodeSyncReqDTO> alarmCodeSyncReqDTOList);

    /**
     * 告警码变更
     *
     * @param alarmCodeSyncReqDTO
     * @return
     */
    int alarmCodeSync(AlarmCodeSyncReqDTO alarmCodeSyncReqDTO);

    /**
     * 全量设备同步
     *
     * @param deviceSyncReqDTOList
     * @return
     */
    int devicesSync(List<DeviceSyncReqDTO> deviceSyncReqDTOList);

    /**
     * 设备变更
     *
     * @param deviceSyncReqDTO
     * @return
     */
    int deviceSync(DeviceSyncReqDTO deviceSyncReqDTO);

    /**
     * 全量设备槽位同步
     *
     * @param slotSyncReqDTOList
     * @return
     */
    int slotsSync(List<SlotSyncReqDTO> slotSyncReqDTOList);

    /**
     * 设备槽位变更
     *
     * @param slotSyncReqDTO
     * @return
     */
    int slotSync(SlotSyncReqDTO slotSyncReqDTO);
}
