package com.zte.msg.alarmcenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.req.AnyAlarmTrendReqDTO;
import com.zte.msg.alarmcenter.dto.req.StatisticsByAnyReqDTO;
import com.zte.msg.alarmcenter.dto.res.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2021/2/24 9:01
 */
public interface AlarmManageService {
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
    Page<AlarmHistoryResDTO> pageAlarmHistory(Long subsystemId, Long siteId, Integer alarmLevel, Integer alarmCode, Timestamp startTime, Timestamp endTime, PageReqDTO pageReqDTO);

    /**
     * 添加备注
     *
     * @param alarmRemark
     * @param id
     */
    void editRemark(String alarmRemark, Long id);
}
