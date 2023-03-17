package com.zte.msg.alarmcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.req.RoleReqDTO;
import com.zte.msg.alarmcenter.entity.OperationLog;
import com.zte.msg.alarmcenter.entity.Role;
import com.zte.msg.alarmcenter.entity.SystemParameter;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface OperationLogMapper extends BaseMapper<OperationLog> {

    /**
     * 分页查询操作日志列表
     *
     * @param page
     * @param userName
     * @param operationType
     * @param startTime
     * @param endTime
     * @return
     */
    Page<OperationLog> listOperationLog(Page<OperationLog> page,
                                        String userName,
                                        String operationType,
                                        Timestamp startTime,
                                        Timestamp endTime);

    List<OperationLog> exportOperationLog(String userName,
                                          String operationType,
                                          Timestamp startTime,
                                          Timestamp endTime);

    /**
     * 添加操作记录
     *
     * @param operationLog
     * @return
     */
    int addOperationLog(OperationLog operationLog);

    void deleteOperationLog();

    /**
     * 获取所有操作类型
     *
     * @return
     */
    List<String> getOperationType();
}
