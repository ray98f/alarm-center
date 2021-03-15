package com.zte.msg.alarmcenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.req.AlarmCodeReqDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmCodeResDTO;
import com.zte.msg.alarmcenter.dto.res.DeviceResDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface AlarmCodeService {
    void importDevice(MultipartFile file, String userId);

    void exportDevice(Long alarmCode, String alarmName, Long systemId, Long alarmLevelId, HttpServletResponse response);

    void addAlarmCode(AlarmCodeReqDTO alarmCodeReqDTO, String userId);

    void modifyAlarmCode(AlarmCodeReqDTO alarmCodeReqDTO, Long id, String userId);

    Page<AlarmCodeResDTO> getAlarmCode(Long alarmCode, String alarmName, Long systemId, Long alarmLevelId, Long page, Long size);

    void deleteAlarmCode(Long id);
}
