package com.zte.msg.alarmcenter.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.req.AnyAlarmTrendReqDTO;
import com.zte.msg.alarmcenter.dto.res.*;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface AlarmManageMapper {

    /**
     * 告警历史-查询
     * @param page
     * @param subsystemId
     * @param siteId
     * @param alarmLevel
     * @param alarmState
     * @param startTime
     * @param endTime
     * @return
     */
    Page<AlarmHistoryResDTO> pageAlarmHistory(Page<AlarmHistoryResDTO> page, Long subsystemId, Long siteId, Integer alarmLevel, Integer alarmState, Timestamp startTime, Timestamp endTime);
}
