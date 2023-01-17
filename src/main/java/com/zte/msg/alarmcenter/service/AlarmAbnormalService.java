package com.zte.msg.alarmcenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.res.AlarmAbnormalResDTO;

public interface AlarmAbnormalService {
    Page<AlarmAbnormalResDTO> getAlarmAbnormal(String startTime, String endTime, Long systemCode, Integer page, Integer size);
}
