package com.zte.msg.alarmcenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.entity.OperationLog;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.List;

/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2021/2/24 9:01
 */
public interface OperationLogService {
    /**
     * 分页查询操作日志列表
     * @param userName
     * @param operationType
     * @param startTime
     * @param endTime
     * @param pageReqDTO
     * @return
     */
    Page<OperationLog> listOperationLog(String userName, String operationType, Timestamp startTime, Timestamp endTime, PageReqDTO pageReqDTO);

    void exportOperationLog(String userName, String operationType, Timestamp startTime, Timestamp endTime, HttpServletResponse response);

    /**
     * 获取所有操作类型
     *
     * @return
     */
    List<String> getOperationType();
}
