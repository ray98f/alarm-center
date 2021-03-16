package com.zte.msg.alarmcenter.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.res.AlarmAbnormalResDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmCodeResDTO;
import com.zte.msg.alarmcenter.mapper.AlarmAbnormalMapper;
import com.zte.msg.alarmcenter.service.AlarmAbnormalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlarmAbnormalServiceImpl implements AlarmAbnormalService {

    @Autowired
    private AlarmAbnormalMapper alarmAbnormalMapper;

    @Override
    public Page<AlarmAbnormalResDTO> getAlarmAbnormal(String startTime, String endTime, Long systemCode, Long page, Long size) {
        List<AlarmAbnormalResDTO> abnormalReqDTOList = null;
        int count = alarmAbnormalMapper.getAlarmAbnormalCount(startTime, endTime, systemCode);
        Page<AlarmAbnormalResDTO> pageBean = new Page<>();
        pageBean.setCurrent(page).setPages(size).setTotal(count);
        if (count > 0) {
            page = (page - 1) * size;
            abnormalReqDTOList = alarmAbnormalMapper.getAlarmAbnormal(startTime, endTime, systemCode, page, size);
            pageBean.setRecords(abnormalReqDTOList);
        }
        return pageBean;
    }
}
