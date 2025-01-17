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
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
@Api(tags = "点表数据同步接口")
@Validated
public class SynchronizeController {

    @Resource
    private SynchronizeService synchronizeService;

    @Autowired
    AsyncSender asyncSender;

    /**
     * 全量告警码同步
     *
     * @param alarmCodeSyncReqDTOList
     * @return
     */
    @PostMapping("/alarmCodesSync")
    @ApiOperation(value = "全量告警码同步")
    public DataResponse<T> alarmCodesSync(@RequestBody @Valid List<AlarmCodeSyncReqDTO> alarmCodeSyncReqDTOList) throws Exception {
        AsyncVO asyncVO = new AsyncVO();
        asyncVO.setAlarmCodeSyncReqDTOList(alarmCodeSyncReqDTOList);
        asyncSender.send(asyncVO);
        return DataResponse.success();
    }

    /**
     * 告警码变更
     *
     * @param alarmCodeSyncReqDTO
     * @return
     */
    @PostMapping("/alarmCodeSync")
    @ApiOperation(value = "告警码变更")
    public DataResponse<T> alarmCodeSync(@RequestBody @Valid AlarmCodeSyncReqDTO alarmCodeSyncReqDTO) {
        synchronizeService.alarmCodeSync(alarmCodeSyncReqDTO);
        return DataResponse.success();
    }

    /**
     * 全量设备同步
     *
     * @param deviceSyncReqDTOList
     * @return
     */
    @PostMapping("/devicesSync")
    @ApiOperation(value = "全量设备同步")
    public DataResponse<T> devicesSync(@RequestBody @Valid List<DeviceSyncReqDTO> deviceSyncReqDTOList) throws Exception {
        AsyncVO asyncVO = new AsyncVO();
        asyncVO.setDeviceSyncReqDTOList(deviceSyncReqDTOList);
        asyncSender.send(asyncVO);
        return DataResponse.success();
    }

    /**
     * 设备变更
     *
     * @param deviceSyncReqDTO
     * @return
     */
    @PostMapping("/deviceSync")
    @ApiOperation(value = "设备变更")
    public DataResponse<T> deviceSync(@RequestBody @Valid DeviceSyncReqDTO deviceSyncReqDTO) {
        synchronizeService.deviceSync(deviceSyncReqDTO);
        return DataResponse.success();
    }

    /**
     * 全量设备槽位同步
     *
     * @param slotSyncReqDTOList
     * @return
     */
    @PostMapping("/slotsSync")
    @ApiOperation(value = "全量设备槽位同步")
    public DataResponse<T> slotsSync(@RequestBody @Valid List<SlotSyncReqDTO> slotSyncReqDTOList) throws Exception {
        AsyncVO asyncVO = new AsyncVO();
        asyncVO.setSlotSyncReqDTOList(slotSyncReqDTOList);
        asyncSender.send(asyncVO);
        return DataResponse.success();
    }

    /**
     * 设备槽位变更
     *
     * @param slotSyncReqDTO
     * @return
     */
    @PostMapping("/slotSync")
    @ApiOperation(value = "设备槽位变更")
    public DataResponse<T> slotSync(@RequestBody @Valid SlotSyncReqDTO slotSyncReqDTO) {
        synchronizeService.slotSync(slotSyncReqDTO);
        return DataResponse.success();
    }
}
