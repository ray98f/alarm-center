package com.zte.msg.alarmcenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.req.AlarmRuleReqDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmRuleDetailsResDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmRuleResDTO;

public interface AlarmRuleService {
    void addAlarmRule(AlarmRuleReqDTO alarmRuleReqDTO, String userId);

    Page<AlarmRuleResDTO> getAlarmRule(String name, Integer isEnable, Integer type, Long page, Long size);

    void modifyAlarmRule(AlarmRuleReqDTO alarmRuleReqDTO, Long id, String userId);

    AlarmRuleDetailsResDTO lookOverAlarmRuleDetails(String id);
}
