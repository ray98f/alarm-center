package com.zte.msg.alarmcenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.req.DeviceReqDTO;
import com.zte.msg.alarmcenter.dto.req.DeviceReqModifyDTO;
import com.zte.msg.alarmcenter.dto.res.DeviceResDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface DeviceService {

    void exportDevice(String name, String deviceCode, Long systemId, Long positionId, HttpServletResponse response);

    void importDevice(MultipartFile deviceFile,String userId);

    void addDevice(DeviceReqDTO deviceReqDTO, String userId);

    void modifyDevice(DeviceReqModifyDTO reqModifyDTO, Long id, String userId);

    Page<DeviceResDTO> getDevices(String name, String deviceCode, Long systemId, Long positionId, Long page,Long size);

    void deleteDevice(Long id);
}
