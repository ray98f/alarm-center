package com.zte.msg.alarmcenter.controller;

import com.zte.msg.alarmcenter.dto.AsyncVO;
import com.zte.msg.alarmcenter.dto.DataResponse;
import com.zte.msg.alarmcenter.dto.req.AlarmCodeSyncReqDTO;
import com.zte.msg.alarmcenter.dto.req.DeviceSyncReqDTO;
import com.zte.msg.alarmcenter.dto.req.SlotSyncReqDTO;
import com.zte.msg.alarmcenter.service.SynchronizeService;
import com.zte.msg.alarmcenter.utils.AsyncSender;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2021/2/24 9:01
 */
@Slf4j
@RestController
@RequestMapping
@Api(tags = "数据同步")
@Validated
public class SynchronizeController {

    @Resource
    private SynchronizeService synchronizeService;

    /**
     * 全量告警码同步
     *
     * @param alarmCodeSyncReqDTOList
     * @param <T>
     * @return
     */
    @PostMapping("/alarmCodesSync")
    @ApiOperation(value = "全量告警码同步")
    public <T> DataResponse<T> alarmCodesSync(@RequestBody @Valid List<AlarmCodeSyncReqDTO> alarmCodeSyncReqDTOList) throws Exception {
//        synchronizeService.alarmCodesSync(alarmCodeSyncReqDTOList);
        AsyncSender asyncSender = new AsyncSender();
        AsyncVO asyncVO = new AsyncVO();
        asyncVO.setAlarmCodeSyncReqDTOList(alarmCodeSyncReqDTOList);
        asyncSender.send(asyncVO);
        return DataResponse.success();
    }

    /**
     * 告警码变更
     *
     * @param alarmCodeSyncReqDTO
     * @param <T>
     * @return
     */
    @PostMapping("/alarmCodeSync")
    @ApiOperation(value = "告警码变更")
    public <T> DataResponse<T> alarmCodeSync(@RequestBody @Valid AlarmCodeSyncReqDTO alarmCodeSyncReqDTO) {
        synchronizeService.alarmCodeSync(alarmCodeSyncReqDTO);
        return DataResponse.success();
    }

    /**
     * 全量设备同步
     *
     * @param deviceSyncReqDTOList
     * @param <T>
     * @return
     */
    @PostMapping("/devicesSync")
    @ApiOperation(value = "全量设备同步")
    public <T> DataResponse<T> devicesSync(@RequestBody @Valid List<DeviceSyncReqDTO> deviceSyncReqDTOList) throws Exception {
//        synchronizeService.devicesSync(deviceSyncReqDTOList);
        AsyncSender asyncSender = new AsyncSender();
        AsyncVO asyncVO = new AsyncVO();
        asyncVO.setDeviceSyncReqDTOList(deviceSyncReqDTOList);
        asyncSender.send(asyncVO);
        return DataResponse.success();
    }

    /**
     * 设备变更
     *
     * @param deviceSyncReqDTO
     * @param <T>
     * @return
     */
    @PostMapping("/deviceSync")
    @ApiOperation(value = "设备变更")
    public <T> DataResponse<T> deviceSync(@RequestBody @Valid DeviceSyncReqDTO deviceSyncReqDTO) {
        synchronizeService.deviceSync(deviceSyncReqDTO);
        return DataResponse.success();
    }

    /**
     * 全量设备槽位同步
     *
     * @param slotSyncReqDTOList
     * @param <T>
     * @return
     */
    @PostMapping("/slotsSync")
    @ApiOperation(value = "全量设备槽位同步")
    public <T> DataResponse<T> slotsSync(@RequestBody @Valid List<SlotSyncReqDTO> slotSyncReqDTOList) throws Exception {
//        synchronizeService.slotsSync(slotSyncReqDTOList);
        AsyncSender asyncSender = new AsyncSender();
        AsyncVO asyncVO = new AsyncVO();
        asyncVO.setSlotSyncReqDTOList(slotSyncReqDTOList);
        asyncSender.send(asyncVO);
        return DataResponse.success();
    }

    /**
     * 设备槽位变更
     *
     * @param slotSyncReqDTO
     * @param <T>
     * @return
     */
    @PostMapping("/slotSync")
    @ApiOperation(value = "设备槽位变更")
    public <T> DataResponse<T> slotSync(@RequestBody @Valid SlotSyncReqDTO slotSyncReqDTO) {
        synchronizeService.slotSync(slotSyncReqDTO);
        return DataResponse.success();
    }
}
