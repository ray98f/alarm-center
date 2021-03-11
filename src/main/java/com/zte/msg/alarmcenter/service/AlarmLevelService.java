package com.zte.msg.alarmcenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.req.AlarmLevelReqDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmLevelResDTO;

public interface AlarmLevelService {
    Page<AlarmLevelResDTO> getAlarmLevelList(Long page, Long size);

    void modifyAlarmLevel(Long id, AlarmLevelReqDTO alarmLevelReqDTO, String userId);
}
