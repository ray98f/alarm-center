package com.zte.msg.alarmcenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.res.TotalAlarmDataResDTO;

import java.sql.Timestamp;
import java.util.List;

/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2021/2/24 9:01
 */
public interface AlarmStatisticsService {
    /**
     * 告警数据总计
     * @param systemId
     * @param siteId
     * @param alarmReason
     * @param startTime
     * @param endTime
     * @param pageReqDTO
     * @return
     */
    Page<TotalAlarmDataResDTO> totalAlarmData(Long systemId, Long siteId, String alarmReason, Timestamp startTime, Timestamp endTime, PageReqDTO pageReqDTO);

}
