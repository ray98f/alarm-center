package com.zte.msg.alarmcenter.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.req.AnyAlarmTrendReqDTO;
import com.zte.msg.alarmcenter.dto.req.StatisticsByAnyReqDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmResolutionEfficiencyResDTO;
import com.zte.msg.alarmcenter.dto.res.AnyAlarmTrendResDTO;
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
     * @param statisticsByAnyReqDTO
     * @return
     */
    @Override
    public List<StatisticsByAnyResDTO> statisticsByLine(StatisticsByAnyReqDTO statisticsByAnyReqDTO) {
        List<StatisticsByAnyResDTO> list = alarmStatisticsMapper.statisticsByLine(statisticsByAnyReqDTO);
        if (null == list || list.isEmpty()) {
            log.warn("告警记录为空");
        }
        return list;
    }

    /**
     * 按系统统计
     * @param statisticsByAnyReqDTO
     * @return
     */
    @Override
    public List<StatisticsByAnyResDTO> statisticsBySystem(StatisticsByAnyReqDTO statisticsByAnyReqDTO) {
        List<StatisticsByAnyResDTO> list = alarmStatisticsMapper.statisticsBySystem(statisticsByAnyReqDTO);
        if (null == list || list.isEmpty()) {
            log.warn("告警记录为空");
        }
        return list;
    }

    /**
     * 按告警级别统计
     * @param statisticsByAnyReqDTO
     * @return
     */
    @Override
    public List<StatisticsByAnyResDTO> statisticsByAlarmLevel(StatisticsByAnyReqDTO statisticsByAnyReqDTO) {
        List<StatisticsByAnyResDTO> list = alarmStatisticsMapper.statisticsByAlarmLevel(statisticsByAnyReqDTO);
        if (null == list || list.isEmpty()) {
            log.warn("告警记录为空");
        }
        return list;
    }

    /**
     * 线路告警趋势
     * @param anyAlarmTrendReqDTO
     * @return
     */
    @Override
    public List<AnyAlarmTrendResDTO> lineAlarmTrend(AnyAlarmTrendReqDTO anyAlarmTrendReqDTO) {
        List<AnyAlarmTrendResDTO> list = alarmStatisticsMapper.lineAlarmTrend(anyAlarmTrendReqDTO);
        if (null == list || list.isEmpty()) {
            log.warn("告警记录为空");
        }
        return list;
    }

    /**
     * 级别告警趋势
     * @param anyAlarmTrendReqDTO
     * @return
     */
    @Override
    public List<AnyAlarmTrendResDTO> levelAlarmTrend(AnyAlarmTrendReqDTO anyAlarmTrendReqDTO) {
        List<AnyAlarmTrendResDTO> list = alarmStatisticsMapper.levelAlarmTrend(anyAlarmTrendReqDTO);
        if (null == list || list.isEmpty()) {
            log.warn("告警记录为空");
        }
        return list;
    }

    /**
     * 系统告警趋势
     * @param anyAlarmTrendReqDTO
     * @return
     */
    @Override
    public List<AnyAlarmTrendResDTO> systemAlarmTrend(AnyAlarmTrendReqDTO anyAlarmTrendReqDTO) {
        List<AnyAlarmTrendResDTO> list = alarmStatisticsMapper.systemAlarmTrend(anyAlarmTrendReqDTO);
        if (null == list || list.isEmpty()) {
            log.warn("告警记录为空");
        }
        return list;
    }

    /**
     * 告警解决效率
     * @param anyAlarmTrendReqDTO
     * @return
     */
    @Override
    public List<AlarmResolutionEfficiencyResDTO> alarmResolutionEfficiency(AnyAlarmTrendReqDTO anyAlarmTrendReqDTO) {
        List<AlarmResolutionEfficiencyResDTO> list = alarmStatisticsMapper.alarmResolutionEfficiency(anyAlarmTrendReqDTO);
        if (null == list || list.isEmpty()) {
            log.warn("告警记录为空");
        }
        return list;
    }

}
