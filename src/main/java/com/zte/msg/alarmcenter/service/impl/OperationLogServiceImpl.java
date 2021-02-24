package com.zte.msg.alarmcenter.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.entity.OperationLog;
import com.zte.msg.alarmcenter.enums.ErrorCode;
import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.mapper.OperationLogMapper;
import com.zte.msg.alarmcenter.mapper.RoleMapper;
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
public class OperationLogServiceImpl implements OperationLogService {

    @Autowired
    private OperationLogMapper operationLogMapper;

    /**
     * 分页查询操作日志列表
     * @param userName
     * @param operationType
     * @param startTime
     * @param endTime
     * @param pageReqDTO
     * @return
     */
    @Override
    public Page<OperationLog> listOperationLog(String userName,
                                               Integer operationType,
                                               Timestamp startTime,
                                               Timestamp endTime,
                                               PageReqDTO pageReqDTO){
        if (Objects.isNull(userName)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        if (Objects.isNull(operationType)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        if (Objects.isNull(startTime)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        if (Objects.isNull(endTime)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        PageHelper.startPage(pageReqDTO.getPage().intValue(), pageReqDTO.getSize().intValue());
        return operationLogMapper.listOperationLog(pageReqDTO.of(), userName, operationType, startTime, endTime);
    }


}
