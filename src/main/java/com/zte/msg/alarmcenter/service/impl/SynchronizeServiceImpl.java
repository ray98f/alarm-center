package com.zte.msg.alarmcenter.service.impl;

import com.zte.msg.alarmcenter.dto.req.AlarmCodeSyncReqDTO;
import com.zte.msg.alarmcenter.dto.req.DeviceSyncReqDTO;
import com.zte.msg.alarmcenter.dto.req.SlotSyncReqDTO;
import com.zte.msg.alarmcenter.enums.ErrorCode;
import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.mapper.SynchronizeMapper;
import com.zte.msg.alarmcenter.service.SynchronizeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2021/2/24 9:15
 */
@Service
@Slf4j
public class SynchronizeServiceImpl implements SynchronizeService {

    @Autowired
    private SynchronizeMapper synchronizeMapper;

    /**
     * 全量告警码同步
     *
     * @param alarmCodeSyncReqDTOList
     * @return
     */
    @Override
    @Async("commonAsyncPool")
    public void alarmCodesSync(List<AlarmCodeSyncReqDTO> alarmCodeSyncReqDTOList) {
        if (null == alarmCodeSyncReqDTOList || alarmCodeSyncReqDTOList.isEmpty()) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        int result = synchronizeMapper.alarmCodesSync(alarmCodeSyncReqDTOList);
        if (result < 0) {
            throw new CommonException(ErrorCode.SYNC_ERROR);
        }
    }

    /**
     * 告警码变更
     *
     * @param alarmCodeSyncReqDTO
     */
    @Override
    public void alarmCodeSync(AlarmCodeSyncReqDTO alarmCodeSyncReqDTO) {
        if (Objects.isNull(alarmCodeSyncReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        int result = synchronizeMapper.alarmCodeSync(alarmCodeSyncReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.SYNC_ERROR);
        }
    }

    /**
     * 全量设备同步
     *
     * @param deviceSyncReqDTOList
     */
    @Override
    @Async("commonAsyncPool")
    public void devicesSync(List<DeviceSyncReqDTO> deviceSyncReqDTOList) {
        if (null == deviceSyncReqDTOList || deviceSyncReqDTOList.isEmpty()) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        int result = synchronizeMapper.devicesSync(deviceSyncReqDTOList);
        if (result < 0) {
            throw new CommonException(ErrorCode.SYNC_ERROR);
        }
    }

    /**
     * 设备变更
     *
     * @param deviceSyncReqDTO
     */
    @Override
    public void deviceSync(DeviceSyncReqDTO deviceSyncReqDTO) {
        if (Objects.isNull(deviceSyncReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        int result = synchronizeMapper.deviceSync(deviceSyncReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.SYNC_ERROR);
        }
    }

    /**
     * 全量设备槽位同步
     *
     * @param slotSyncReqDTOList
     */
    @Override
    @Async("commonAsyncPool")
    public void slotsSync(List<SlotSyncReqDTO> slotSyncReqDTOList) {
        if (null == slotSyncReqDTOList || slotSyncReqDTOList.isEmpty()) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        int result = synchronizeMapper.slotsSync(slotSyncReqDTOList);
        if (result < 0) {
            throw new CommonException(ErrorCode.SYNC_ERROR);
        }
    }

    /**
     * 设备槽位变更
     *
     * @param slotSyncReqDTO
     */
    @Override
    public void slotSync(SlotSyncReqDTO slotSyncReqDTO) {
        if (Objects.isNull(slotSyncReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        int result = synchronizeMapper.slotSync(slotSyncReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.SYNC_ERROR);
        }
    }
}
