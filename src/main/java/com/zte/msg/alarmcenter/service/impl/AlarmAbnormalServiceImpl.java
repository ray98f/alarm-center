package com.zte.msg.alarmcenter.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zte.msg.alarmcenter.dto.res.AlarmAbnormalResDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmCodeResDTO;
import com.zte.msg.alarmcenter.mapper.AlarmAbnormalMapper;
import com.zte.msg.alarmcenter.service.AlarmAbnormalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AlarmAbnormalServiceImpl implements AlarmAbnormalService {

    @Autowired
    private AlarmAbnormalMapper alarmAbnormalMapper;

    @Override
    public Page<AlarmAbnormalResDTO> getAlarmAbnormal(String startTime, String endTime, Long systemCode, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        return alarmAbnormalMapper.getAlarmAbnormal(new Page<>(page, size), startTime, endTime, systemCode);
    }
}
