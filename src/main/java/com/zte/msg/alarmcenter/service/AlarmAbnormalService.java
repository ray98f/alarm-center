package com.zte.msg.alarmcenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmAbnormalResDTO;

import java.sql.Timestamp;

public interface AlarmAbnormalService {
    Page<AlarmAbnormalResDTO> getAlarmAbnormal(Timestamp startTime, Timestamp endTime, Long systemCode, PageReqDTO pageReqDTO);
}
