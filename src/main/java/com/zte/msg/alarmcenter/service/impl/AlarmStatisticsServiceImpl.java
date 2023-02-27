package com.zte.msg.alarmcenter.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.req.AnyAlarmTrendReqDTO;
import com.zte.msg.alarmcenter.dto.req.StatisticsByAnyReqDTO;
import com.zte.msg.alarmcenter.dto.res.*;
import com.zte.msg.alarmcenter.enums.ErrorCode;
import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.mapper.AlarmStatisticsMapper;
import com.zte.msg.alarmcenter.service.AlarmStatisticsService;
import com.zte.msg.alarmcenter.utils.Constants;
import com.zte.msg.alarmcenter.utils.ExcelPortUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
    public Page<TotalAlarmDataResDTO> totalAlarmData(Long systemId, Long siteId, String alarmReason, String startTime, String endTime, PageReqDTO pageReqDTO) {
        if (alarmReason.contains(Constants.PERCENT_SIGN)) {
            alarmReason = "Prohibit input";
        }
        PageHelper.startPage(pageReqDTO.getPage().intValue(), pageReqDTO.getSize().intValue());
        return alarmStatisticsMapper.totalAlarmData(pageReqDTO.of(), systemId, siteId, alarmReason, startTime, endTime);
    }

    /**
     * 导出告警数据
     *
     * @param systemId
     * @param siteId
     * @param alarmReason
     * @param startTime
     * @param endTime
     * @param response
     */
    @Override
    public void totalAlarmData(Long systemId, Long siteId, String alarmReason, String startTime, String endTime, HttpServletResponse response) {
        List<String> listName = Arrays.asList("系统", "站点", "告警名称", "告警原因", "告警数据量");
        List<TotalAlarmDataResDTO> totalAlarmData = alarmStatisticsMapper.exportTotalAlarmData(systemId, siteId, alarmReason, startTime, endTime);
        List<Map<String, String>> list = new ArrayList<>();
        if (null != totalAlarmData) {
            for (TotalAlarmDataResDTO totalAlarmDataResDTO : totalAlarmData) {
                Map<String, String> map = new HashMap<>(16);
                map.put("系统", totalAlarmDataResDTO.getSubsystemName());
                map.put("站点", totalAlarmDataResDTO.getSiteName());
                map.put("告警名称", totalAlarmDataResDTO.getAlarmName());
                map.put("告警原因", totalAlarmDataResDTO.getAlarmReason());
                map.put("告警数据量", totalAlarmDataResDTO.getAlarmNum().toString());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("告警数据总计", listName, list, null, response);
    }


    /**
     * 按线路统计
     *
     * @param statisticsByAnyReqDTO
     * @return
     */
    @Override
    public List<StatisticsByAnyResDTO> statisticsByLine(StatisticsByAnyReqDTO statisticsByAnyReqDTO) {
        List<StatisticsByAnyResDTO> statisticsByAnyResDTOList = alarmStatisticsMapper.statisticsByLine(statisticsByAnyReqDTO);
        if (null == statisticsByAnyResDTOList || statisticsByAnyResDTOList.isEmpty()) {
            log.warn("告警记录为空");
            return null;
        }
        List<StatisticsByAnyResDTO> list = new ArrayList<>();
        List<String> names = new ArrayList<>(new LinkedHashSet<>(statisticsByAnyResDTOList.stream().map(StatisticsByAnyResDTO::getSiteName).collect(Collectors.toList())));
        for (String name : names) {
            StatisticsByAnyResDTO data = new StatisticsByAnyResDTO();
            data.setSiteName(name);
            data.setEmergencyAlarmNum(0L);
            data.setGeneralAlarmNum(0L);
            data.setSeriousAlarmNum(0L);
            for (StatisticsByAnyResDTO statisticsByAnyResDTO : statisticsByAnyResDTOList) {
                if (statisticsByAnyResDTO.getSiteName().equals(name)) {
                    if (statisticsByAnyResDTO.getAlarmLevel() == null) {
                        continue;
                    }
                    if (1 == statisticsByAnyResDTO.getAlarmLevel()) {
                        data.setEmergencyAlarmNum(statisticsByAnyResDTO.getAlarmNum());
                    } else if (2 == statisticsByAnyResDTO.getAlarmLevel()) {
                        data.setSeriousAlarmNum(statisticsByAnyResDTO.getAlarmNum());
                    } else if (3 == statisticsByAnyResDTO.getAlarmLevel()) {
                        data.setGeneralAlarmNum(statisticsByAnyResDTO.getAlarmNum());
                    }
                }
            }
            list.add(data);
        }
        return list;
    }

    /**
     * 按系统统计
     *
     * @param statisticsByAnyReqDTO
     * @return
     */
    @Override
    public List<StatisticsByAnyResDTO> statisticsBySystem(StatisticsByAnyReqDTO statisticsByAnyReqDTO) {
        List<StatisticsByAnyResDTO> statisticsByAnyResDTOList = alarmStatisticsMapper.statisticsBySystem(statisticsByAnyReqDTO);
        if (null == statisticsByAnyResDTOList || statisticsByAnyResDTOList.isEmpty()) {
            log.warn("告警记录为空");
            return null;
        }
        List<StatisticsByAnyResDTO> list = new ArrayList<>();
        List<String> names = new ArrayList<>(new LinkedHashSet<>(statisticsByAnyResDTOList.stream().map(StatisticsByAnyResDTO::getSubsystemName).collect(Collectors.toList())));
        for (String name : names) {
            StatisticsByAnyResDTO data = new StatisticsByAnyResDTO();
            data.setSiteName(name);
            data.setEmergencyAlarmNum(0L);
            data.setGeneralAlarmNum(0L);
            data.setSeriousAlarmNum(0L);
            for (StatisticsByAnyResDTO statisticsByAnyResDTO : statisticsByAnyResDTOList) {
                if (statisticsByAnyResDTO.getSubsystemName().equals(name)) {
                    if (statisticsByAnyResDTO.getAlarmLevel() == null) {
                        continue;
                    }
                    if (1 == statisticsByAnyResDTO.getAlarmLevel()) {
                        data.setEmergencyAlarmNum(statisticsByAnyResDTO.getAlarmNum());
                    } else if (2 == statisticsByAnyResDTO.getAlarmLevel()) {
                        data.setSeriousAlarmNum(statisticsByAnyResDTO.getAlarmNum());
                    } else if (3 == statisticsByAnyResDTO.getAlarmLevel()) {
                        data.setGeneralAlarmNum(statisticsByAnyResDTO.getAlarmNum());
                    }
                }
            }
            list.add(data);
        }
        return list;
    }

    /**
     * 按告警级别统计
     *
     * @param statisticsByAnyReqDTO
     * @return
     */
    @Override
    public List<StatisticsByAnyResDTO> statisticsByAlarmLevel(StatisticsByAnyReqDTO statisticsByAnyReqDTO) {
        List<StatisticsByAnyResDTO> statisticsByAnyResDTOList = alarmStatisticsMapper.statisticsByAlarmLevel(statisticsByAnyReqDTO);
        if (null == statisticsByAnyResDTOList || statisticsByAnyResDTOList.isEmpty()) {
            log.warn("告警记录为空");
            return null;
        }
        List<StatisticsByAnyResDTO> list = new ArrayList<>();
        StatisticsByAnyResDTO data = new StatisticsByAnyResDTO();
        data.setEmergencyAlarmNum(0L);
        data.setGeneralAlarmNum(0L);
        data.setSeriousAlarmNum(0L);
        for (StatisticsByAnyResDTO statisticsByAnyResDTO : statisticsByAnyResDTOList) {
            if (1 == statisticsByAnyResDTO.getAlarmLevel()) {
                data.setEmergencyAlarmNum(statisticsByAnyResDTO.getAlarmNum());
            } else if (2 == statisticsByAnyResDTO.getAlarmLevel()) {
                data.setSeriousAlarmNum(statisticsByAnyResDTO.getAlarmNum());
            } else if (3 == statisticsByAnyResDTO.getAlarmLevel()) {
                data.setGeneralAlarmNum(statisticsByAnyResDTO.getAlarmNum());
            }
        }
        list.add(data);
        return list;
    }

    /**
     * 线路告警趋势
     *
     * @param anyAlarmTrendReqDTO
     * @return
     */
    @Override
    public List<AnyAlarmTrendResDTO> lineAlarmTrend(AnyAlarmTrendReqDTO anyAlarmTrendReqDTO) {
        List<AnyAlarmTrendResDTO> anyAlarmTrendResDTOList = alarmStatisticsMapper.lineAlarmTrend(anyAlarmTrendReqDTO);
        if (null == anyAlarmTrendResDTOList || anyAlarmTrendResDTOList.isEmpty()) {
            log.warn("告警记录为空");
            return null;
        }
        List<AnyAlarmTrendResDTO> list = new ArrayList<>();
        List<String> names = new ArrayList<>(new LinkedHashSet<>(anyAlarmTrendResDTOList.stream().map(AnyAlarmTrendResDTO::getStatisticsDate).collect(Collectors.toList())));
        names.remove(null);
        for (String name : names) {
            AnyAlarmTrendResDTO data = new AnyAlarmTrendResDTO();
            data.setStatisticsDate(name);
            data.setEmergencyAlarmNum(0L);
            data.setGeneralAlarmNum(0L);
            data.setSeriousAlarmNum(0L);
            for (AnyAlarmTrendResDTO anyAlarmTrendResDTO : anyAlarmTrendResDTOList) {
                if (anyAlarmTrendResDTO.getStatisticsDate() == null) {
                    continue;
                }
                if (anyAlarmTrendResDTO.getStatisticsDate().equals(name)) {
                    if (anyAlarmTrendResDTO.getAlarmLevel() == null) {
                        continue;
                    }
                    if (1 == anyAlarmTrendResDTO.getAlarmLevel()) {
                        data.setEmergencyAlarmNum(anyAlarmTrendResDTO.getAlarmNum());
                    } else if (2 == anyAlarmTrendResDTO.getAlarmLevel()) {
                        data.setSeriousAlarmNum(anyAlarmTrendResDTO.getAlarmNum());
                    } else if (3 == anyAlarmTrendResDTO.getAlarmLevel()) {
                        data.setGeneralAlarmNum(anyAlarmTrendResDTO.getAlarmNum());
                    }
                }
            }
            list.add(data);
        }
        return list;
    }

    /**
     * 级别告警趋势
     *
     * @param anyAlarmTrendReqDTO
     * @return
     */
    @Override
    public List<AnyAlarmTrendResDTO> levelAlarmTrend(AnyAlarmTrendReqDTO anyAlarmTrendReqDTO) {
        List<AnyAlarmTrendResDTO> anyAlarmTrendResDTOList = alarmStatisticsMapper.levelAlarmTrend(anyAlarmTrendReqDTO);
        if (null == anyAlarmTrendResDTOList || anyAlarmTrendResDTOList.isEmpty()) {
            log.warn("告警记录为空");
            return null;
        }
        List<AnyAlarmTrendResDTO> list = new ArrayList<>();
        List<String> names = new ArrayList<>(new LinkedHashSet<>(anyAlarmTrendResDTOList.stream().map(AnyAlarmTrendResDTO::getStatisticsDate).collect(Collectors.toList())));
        for (String name : names) {
            AnyAlarmTrendResDTO data = new AnyAlarmTrendResDTO();
            data.setStatisticsDate(name);
            data.setEmergencyAlarmNum(0L);
            data.setGeneralAlarmNum(0L);
            data.setSeriousAlarmNum(0L);
            for (AnyAlarmTrendResDTO anyAlarmTrendResDTO : anyAlarmTrendResDTOList) {
                if (anyAlarmTrendResDTO.getStatisticsDate().equals(name)) {
                    if (1 == anyAlarmTrendResDTO.getAlarmLevel()) {
                        data.setEmergencyAlarmNum(anyAlarmTrendResDTO.getAlarmNum());
                    } else if (2 == anyAlarmTrendResDTO.getAlarmLevel()) {
                        data.setSeriousAlarmNum(anyAlarmTrendResDTO.getAlarmNum());
                    } else if (3 == anyAlarmTrendResDTO.getAlarmLevel()) {
                        data.setGeneralAlarmNum(anyAlarmTrendResDTO.getAlarmNum());
                    }
                }
            }
            list.add(data);
        }
        return list;
    }

    /**
     * 系统告警趋势
     *
     * @param anyAlarmTrendReqDTO
     * @return
     */
    @Override
    public List<AnyAlarmTrendResDTO> systemAlarmTrend(AnyAlarmTrendReqDTO anyAlarmTrendReqDTO) {
        List<AnyAlarmTrendResDTO> anyAlarmTrendResDTOList = alarmStatisticsMapper.systemAlarmTrend(anyAlarmTrendReqDTO);
        if (null == anyAlarmTrendResDTOList || anyAlarmTrendResDTOList.isEmpty()) {
            log.warn("告警记录为空");
            return null;
        }
        List<AnyAlarmTrendResDTO> list = new ArrayList<>();
        List<String> names = new ArrayList<>(new LinkedHashSet<>(anyAlarmTrendResDTOList.stream().map(AnyAlarmTrendResDTO::getStatisticsDate).collect(Collectors.toList())));
        names.remove(null);
        for (String name : names) {
            AnyAlarmTrendResDTO data = new AnyAlarmTrendResDTO();
            data.setStatisticsDate(name);
            data.setEmergencyAlarmNum(0L);
            data.setGeneralAlarmNum(0L);
            data.setSeriousAlarmNum(0L);
            for (AnyAlarmTrendResDTO anyAlarmTrendResDTO : anyAlarmTrendResDTOList) {
                if (anyAlarmTrendResDTO.getStatisticsDate() == null) {
                    continue;
                }
                if (anyAlarmTrendResDTO.getStatisticsDate().equals(name)) {
                    if (anyAlarmTrendResDTO.getAlarmLevel() == null) {
                        continue;
                    }
                    if (1 == anyAlarmTrendResDTO.getAlarmLevel()) {
                        data.setEmergencyAlarmNum(anyAlarmTrendResDTO.getAlarmNum());
                    } else if (2 == anyAlarmTrendResDTO.getAlarmLevel()) {
                        data.setSeriousAlarmNum(anyAlarmTrendResDTO.getAlarmNum());
                    } else if (3 == anyAlarmTrendResDTO.getAlarmLevel()) {
                        data.setGeneralAlarmNum(anyAlarmTrendResDTO.getAlarmNum());
                    }
                }
            }
            list.add(data);
        }
        return list;
    }

    /**
     * 告警解决效率
     *
     * @param anyAlarmTrendReqDTO
     * @return
     */
    @Override
    public List<AlarmResolutionEfficiencyResDTO> alarmResolutionEfficiency(AnyAlarmTrendReqDTO anyAlarmTrendReqDTO) {
        List<AlarmResolutionEfficiencyResDTO> alarmResolutionEfficiencyResDTOList = alarmStatisticsMapper.alarmResolutionEfficiency(anyAlarmTrendReqDTO);
        if (null == alarmResolutionEfficiencyResDTOList || alarmResolutionEfficiencyResDTOList.isEmpty()) {
            log.warn("告警记录为空");
            return null;
        }
        List<AlarmResolutionEfficiencyResDTO> list = new ArrayList<>();
        List<String> names = new ArrayList<>(new LinkedHashSet<>(alarmResolutionEfficiencyResDTOList.stream().map(AlarmResolutionEfficiencyResDTO::getStatisticsDate).collect(Collectors.toList())));
        for (String name : names) {
            AlarmResolutionEfficiencyResDTO data = new AlarmResolutionEfficiencyResDTO();
            data.setStatisticsDate(name);
            data.setEmergencyDisposalTime(0L);
            data.setGeneralDisposalTime(0L);
            data.setSeriousDisposalTime(0L);
            for (AlarmResolutionEfficiencyResDTO alarmResolutionEfficiencyResDTO : alarmResolutionEfficiencyResDTOList) {
                if (alarmResolutionEfficiencyResDTO.getStatisticsDate().equals(name)) {
                    if (1 == alarmResolutionEfficiencyResDTO.getAlarmLevel()) {
                        data.setEmergencyDisposalTime(alarmResolutionEfficiencyResDTO.getDisposalTime());
                    } else if (2 == alarmResolutionEfficiencyResDTO.getAlarmLevel()) {
                        data.setSeriousDisposalTime(alarmResolutionEfficiencyResDTO.getDisposalTime());
                    } else if (3 == alarmResolutionEfficiencyResDTO.getAlarmLevel()) {
                        data.setGeneralDisposalTime(alarmResolutionEfficiencyResDTO.getDisposalTime());
                    }
                }
            }
            list.add(data);
        }
        return list;
    }

}
