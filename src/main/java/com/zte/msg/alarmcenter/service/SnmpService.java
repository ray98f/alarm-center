package com.zte.msg.alarmcenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.req.SnmpAlarmCodeReqDTO;
import com.zte.msg.alarmcenter.dto.req.SnmpSlotModifyReqDTO;
import com.zte.msg.alarmcenter.dto.res.SnmpAlarmCodeResDTO;
import com.zte.msg.alarmcenter.dto.res.SnmpSlotResDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface SnmpService {

    void importDevice(MultipartFile file, String userId);

    void exportDevice(String snmpSlotName, Long systemId, Long siteId, HttpServletResponse response);

    void addSnmpSlot(SnmpSlotModifyReqDTO slotModifyReqDTO, String userId);

    void modifySnmpSlot(SnmpSlotModifyReqDTO slotModifyReqDTO, Long id, String userId);

    void deleteSnmpSlot(Long id);

    Page<SnmpSlotResDTO> getSnmpSlot(String snmpSlotName, Long systemId, Long siteId, PageReqDTO page);

    void importSnmpAlarmCode(MultipartFile file, String userId);

    void exportSnmpAlarmCode(String alarmCode, Long systemId, HttpServletResponse response);

    void addSSnmpAlarmCode(SnmpAlarmCodeReqDTO snmpAlarmCode, String userId);

    void modifySnmpAlarmCode(SnmpAlarmCodeReqDTO snmpAlarmCode, Long id, String userId);

    Page<SnmpAlarmCodeResDTO> getSnmpAlarmCode(String code, Long systemId, PageReqDTO page);

    void deleteSnmpAlarmCode(Long id);
}
