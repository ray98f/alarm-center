package com.zte.msg.alarmcenter.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmAbnormalResDTO;
import com.zte.msg.alarmcenter.mapper.AlarmAbnormalMapper;
import com.zte.msg.alarmcenter.service.AlarmAbnormalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class AlarmAbnormalServiceImpl implements AlarmAbnormalService {

    @Autowired
    private AlarmAbnormalMapper alarmAbnormalMapper;

    @Override
    public Page<AlarmAbnormalResDTO> getAlarmAbnormal(Timestamp startTime, Timestamp endTime, Long systemCode, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPage().intValue(), pageReqDTO.getSize().intValue());
        return alarmAbnormalMapper.getAlarmAbnormal(pageReqDTO.of(), startTime, endTime, systemCode);
    }
}
