package com.zte.msg.alarmcenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.req.AlarmRuleDeviceReqDTO;
import com.zte.msg.alarmcenter.dto.req.AlarmRuleReqDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmCodeResDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmRuleDetailsResDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmRuleResDTO;
import com.zte.msg.alarmcenter.dto.res.DeviceResDTO;

import java.util.List;

public interface AlarmRuleService {
    void addAlarmRule(AlarmRuleReqDTO alarmRuleReqDTO, String userId);

    Page<AlarmRuleResDTO> getAlarmRule(String name, Integer isEnable, Integer type, Long page, Long size);

    void modifyAlarmRule(AlarmRuleReqDTO alarmRuleReqDTO, Long id, String userId);

    AlarmRuleDetailsResDTO lookOverAlarmRuleDetails(String id);

    void deleteAlarmRule(Long id);

    /**
     * 获取设备下拉列表
     *
     * @param alarmRuleDeviceReqDTO
     * @return
     */
    List<DeviceResDTO> getDevices(AlarmRuleDeviceReqDTO alarmRuleDeviceReqDTO);

    /**
     * 获取告警码下拉列表
     *
     * @param systemIds
     * @return
     */
    List<AlarmCodeResDTO> getAlarmCodes(List<Long> systemIds);
}
