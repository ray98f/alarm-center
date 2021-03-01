package com.zte.msg.alarmcenter.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.res.StatisticsByAnyResDTO;
import com.zte.msg.alarmcenter.dto.res.TotalAlarmDataResDTO;
import com.zte.msg.alarmcenter.enums.ErrorCode;
import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.mapper.AlarmStatisticsMapper;
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
public class AlarmStatisticsServiceImpl implements AlarmStatisticsService {

    @Autowired
    private AlarmStatisticsMapper alarmStatisticsMapper;

    /**
     * 告警数据总计
     *
     * @param systemId
     * @param siteId
     * @param alarmReason
     * @param startTime
     * @param endTime
     * @param pageReqDTO
     * @return
     */
    @Override
    public Page<TotalAlarmDataResDTO> totalAlarmData(Long systemId, Long siteId, String alarmReason, Timestamp startTime, Timestamp endTime, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPage().intValue(), pageReqDTO.getSize().intValue());
        return alarmStatisticsMapper.totalAlarmData(pageReqDTO.of(), systemId, siteId, alarmReason, startTime, endTime);
    }

    /**
     * 按线路统计
     *
     * @param siteIds
     * @param alarmLevels
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<StatisticsByAnyResDTO> statisticsByLine(List<Long> siteIds, List<Integer> alarmLevels, Timestamp startTime, Timestamp endTime) {
        List<StatisticsByAnyResDTO> list = alarmStatisticsMapper.statisticsByLine(siteIds, alarmLevels, startTime, endTime);
        if (null == list || list.isEmpty()) {
            log.warn("告警记录为空");
        }
        return list;
    }

    /**
     * 按系统统计
     *
     * @param systemIds
     * @param alarmLevels
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<StatisticsByAnyResDTO> statisticsBySystem(List<Long> systemIds, List<Integer> alarmLevels, Timestamp startTime, Timestamp endTime) {
        List<StatisticsByAnyResDTO> list = alarmStatisticsMapper.statisticsBySystem(systemIds, alarmLevels, startTime, endTime);
        if (null == list || list.isEmpty()) {
            log.warn("告警记录为空");
        }
        return list;
    }

}
