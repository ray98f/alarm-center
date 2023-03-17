package com.zte.msg.alarmcenter.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.res.*;
import com.zte.msg.alarmcenter.enums.ErrorCode;
import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.mapper.AlarmManageMapper;
import com.zte.msg.alarmcenter.service.AlarmManageService;
import com.zte.msg.alarmcenter.utils.ExcelPortUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

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
     * @param alarmCode
     * @param startTime
     * @param endTime
     * @param pageReqDTO
     * @return
     */
    @Override
    public Page<AlarmHistoryResDTO> pageAlarmHistory(Long subsystemId, Long siteId, Integer alarmLevel, Integer alarmCode, Timestamp startTime, Timestamp endTime, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPage().intValue(), pageReqDTO.getSize().intValue());
        return alarmManageMapper.pageAlarmHistory(pageReqDTO.of(), subsystemId, siteId, alarmLevel, alarmCode, startTime, endTime);
    }

    /**
     * 告警历史-导出
     *
     * @param subsystemId
     * @param siteId
     * @param alarmLevel
     * @param alarmCode
     * @param startTime
     * @param endTime
     * @param response
     */
    @Override
    public void exportAlarmHistory(Long subsystemId, Long siteId, Integer alarmLevel, Integer alarmCode, Timestamp startTime, Timestamp endTime, HttpServletResponse response) {
        List<String> listName = Arrays.asList("系统", "告警等级", "站点", "设备", "槽位", "告警码", "告警名称", "告警原因", "第一次告警时间", "最后告警时间", "告警状态", "告警恢复时间", "备注");
        List<AlarmHistoryResDTO> alarmHistory = alarmManageMapper.exportAlarmHistory(subsystemId, siteId, alarmLevel, alarmCode, startTime, endTime);
        List<Map<String, String>> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        if (null != alarmHistory && !alarmHistory.isEmpty()) {
            for (AlarmHistoryResDTO alarmHistoryResDTO : alarmHistory) {
                Map<String, String> map = new HashMap<>(16);
                String alarmLevelName, alarmStateName = "待处理";
                if (alarmHistoryResDTO.getAlarmLevel() == 1) {
                    alarmLevelName = "紧急告警";
                } else if (alarmHistoryResDTO.getAlarmLevel() == 2) {
                    alarmLevelName = "重要告警";
                } else {
                    alarmLevelName = "一般告警";
                }
                if (alarmHistoryResDTO.getAlarmState() == 1) {
                    alarmStateName = "延迟告警";
                } else if (alarmHistoryResDTO.getAlarmState() == 2) {
                    alarmStateName = "手动确认";
                } else if (alarmHistoryResDTO.getAlarmState() == 3) {
                    alarmStateName = "自动确认";
                } else if (alarmHistoryResDTO.getAlarmState() == 6) {
                    alarmStateName = "手动清除";
                } else if (alarmHistoryResDTO.getAlarmState() == 4) {
                    alarmStateName = "手动过滤";
                } else if (alarmHistoryResDTO.getAlarmState() == 5) {
                    alarmStateName = "自动过滤";
                } else if (alarmHistoryResDTO.getAlarmState() == 7) {
                    alarmStateName = "自动清除";
                }
                map.put("系统", alarmHistoryResDTO.getSubsystemName());
                map.put("告警等级", alarmLevelName);
                map.put("站点", alarmHistoryResDTO.getSiteName());
                map.put("设备", alarmHistoryResDTO.getDeviceName());
                map.put("槽位", alarmHistoryResDTO.getSlotPosition());
                map.put("告警码", alarmHistoryResDTO.getAlarmCode());
                map.put("告警名称", alarmHistoryResDTO.getAlarmName());
                map.put("告警原因", alarmHistoryResDTO.getAlarmReason());
                map.put("第一次告警时间", null == alarmHistoryResDTO.getFirstTime() ? null : sdf.format(alarmHistoryResDTO.getFirstTime()));
                map.put("最后告警时间", null == alarmHistoryResDTO.getFinalTime() ? null : sdf.format(alarmHistoryResDTO.getFinalTime()));
                map.put("告警状态", alarmStateName);
                map.put("告警恢复时间", null == alarmHistoryResDTO.getRecoveryTime() ? null : sdf.format(alarmHistoryResDTO.getRecoveryTime()));
                map.put("备注", alarmHistoryResDTO.getAlarmRemark());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("历史告警", listName, list, null, response);
    }

    /**
     * 添加备注
     *
     * @param alarmRemark
     * @param id
     */
    @Override
    public void editRemark(String alarmRemark, Long id) {
        int result = alarmManageMapper.editRemark(alarmRemark, id);
        if (result <= 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }
}
