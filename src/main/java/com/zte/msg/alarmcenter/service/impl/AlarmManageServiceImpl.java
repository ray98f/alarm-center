package com.zte.msg.alarmcenter.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.req.AnyAlarmTrendReqDTO;
import com.zte.msg.alarmcenter.dto.req.StatisticsByAnyReqDTO;
import com.zte.msg.alarmcenter.dto.res.*;
import com.zte.msg.alarmcenter.mapper.AlarmManageMapper;
import com.zte.msg.alarmcenter.mapper.AlarmStatisticsMapper;
import com.zte.msg.alarmcenter.service.AlarmManageService;
import com.zte.msg.alarmcenter.service.AlarmStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2021/2/24 9:15
 */
@Service
@Slf4j
public class AlarmManageServiceImpl implements AlarmManageService {

    @Autowired
    private AlarmManageMapper alarmManageMapper;

    /**
     * 告警历史-查询
     *
     * @param subsystemId
     * @param siteId
     * @param alarmLevel
     * @param alarmState
     * @param startTime
     * @param endTime
     * @param pageReqDTO
     * @return
     */
    @Override
    public Page<AlarmHistoryResDTO> pageAlarmHistory(Long subsystemId, Long siteId, Integer alarmLevel, Integer alarmState, Timestamp startTime, Timestamp endTime, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPage().intValue(), pageReqDTO.getSize().intValue());
        return alarmManageMapper.pageAlarmHistory(pageReqDTO.of(), subsystemId, siteId, alarmLevel, alarmState, startTime, endTime);
    }

}
