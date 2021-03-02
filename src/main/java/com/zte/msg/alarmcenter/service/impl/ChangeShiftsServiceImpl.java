package com.zte.msg.alarmcenter.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.entity.ChangeShifts;
import com.zte.msg.alarmcenter.entity.OperationLog;
import com.zte.msg.alarmcenter.enums.ErrorCode;
import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.mapper.ChangeShiftsMapper;
import com.zte.msg.alarmcenter.mapper.OperationLogMapper;
import com.zte.msg.alarmcenter.service.ChangeShiftsService;
import com.zte.msg.alarmcenter.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2021/2/24 9:15
 */
@Service
@Slf4j
public class ChangeShiftsServiceImpl implements ChangeShiftsService {

    @Autowired
    private ChangeShiftsMapper changeShiftsMapper;

    /**
     * 添加交接班记录
     *
     * @param changeShifts
     * @return
     */
    @Override
    public void addChangeShifts(ChangeShifts changeShifts) {
        if (Objects.isNull(changeShifts)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        int result = changeShiftsMapper.addChangeShifts(changeShifts);
        if (result <= 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

}
