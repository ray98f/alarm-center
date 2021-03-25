package com.zte.msg.alarmcenter.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.req.AlarmLevelReqDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmLevelResDTO;
import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.mapper.AlarmLevelMapper;
import com.zte.msg.alarmcenter.service.AlarmLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlarmLevelServiceImpl implements AlarmLevelService {

    @Autowired
    private AlarmLevelMapper alarmLevelMapper;

    @Override
    public Page<AlarmLevelResDTO> getAlarmLevelList(Long page, Long size) {
        List<AlarmLevelResDTO> deviceReqDTOList = null;
        int count = alarmLevelMapper.getAlarmLevelCount();
        Page<AlarmLevelResDTO> pageBean = new Page<>();
        pageBean.setCurrent(page).setPages(size).setTotal(count);
        if (count > 0) {
            page = (page-1)*size;
            deviceReqDTOList = alarmLevelMapper.getAlarmLevelList(page,size);
            pageBean.setRecords(deviceReqDTOList);
        }
        return pageBean;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyAlarmLevel(Long id, AlarmLevelReqDTO alarmLevelReqDTO, String userId) {
        int integer = alarmLevelMapper.modifyAlarmLevel(id,alarmLevelReqDTO,userId);
        if (integer == 0) {
            throw new CommonException(4000, "编辑失败！");
        }
    }
}
