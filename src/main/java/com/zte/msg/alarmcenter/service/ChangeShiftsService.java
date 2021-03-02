package com.zte.msg.alarmcenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.entity.ChangeShifts;
import com.zte.msg.alarmcenter.entity.OperationLog;

import java.sql.Timestamp;

/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2021/2/24 9:01
 */
public interface ChangeShiftsService {
    /**
     * 添加交接班记录
     * @param changeShifts
     * @return
     */
    void addChangeShifts(ChangeShifts changeShifts);
}
