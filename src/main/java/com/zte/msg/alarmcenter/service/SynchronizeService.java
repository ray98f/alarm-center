package com.zte.msg.alarmcenter.service;

import com.zte.msg.alarmcenter.dto.req.AlarmCodeSyncReqDTO;
import com.zte.msg.alarmcenter.dto.req.DeviceSyncReqDTO;
import com.zte.msg.alarmcenter.dto.req.SlotSyncReqDTO;

import java.util.List;

/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2021/3/15 10:58
 */
public interface SynchronizeService {

    /**
     * 全量告警码同步
     *
     * @param alarmCodeSyncReqDTOList
     */
    void alarmCodesSync(List<AlarmCodeSyncReqDTO> alarmCodeSyncReqDTOList);

    /**
     * 告警码变更
     *
     * @param alarmCodeSyncReqDTO
     */
    void alarmCodeSync(AlarmCodeSyncReqDTO alarmCodeSyncReqDTO);

    /**
     * 全量设备同步
     *
     * @param deviceSyncReqDTOList
     */
    void devicesSync(List<DeviceSyncReqDTO> deviceSyncReqDTOList);

    /**
     * 设备变更
     *
     * @param deviceSyncReqDTO
     */
    void deviceSync(DeviceSyncReqDTO deviceSyncReqDTO);

    /**
     * 全量设备槽位同步
     *
     * @param slotSyncReqDTOList
     */
    void slotsSync(List<SlotSyncReqDTO> slotSyncReqDTOList);

    /**
     * 设备槽位变更
     *
     * @param slotSyncReqDTO
     */
    void slotSync(SlotSyncReqDTO slotSyncReqDTO);
}
