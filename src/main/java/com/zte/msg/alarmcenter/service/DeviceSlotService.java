package com.zte.msg.alarmcenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.req.DeviceSlotReqDTO;
import com.zte.msg.alarmcenter.dto.res.DeviceSlotResDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface DeviceSlotService {
    void importDevice(MultipartFile deviceFile, String userId);

    void exportDevice(String slotName, String deviceName, String deviceCode, Long systemId, Long positionId, HttpServletResponse response);

    void deleteDevice(Long id);

    void addDeviceSlot(DeviceSlotReqDTO deviceSlotReqDTO,String userId);

    void modifyDevice(Long id, DeviceSlotReqDTO deviceSlotReqDTO, String userId);

    Page<DeviceSlotResDTO> getDevicesSlot(String slotName, String deviceName, String deviceCode, Long systemId, Long positionId, PageReqDTO page);
}
