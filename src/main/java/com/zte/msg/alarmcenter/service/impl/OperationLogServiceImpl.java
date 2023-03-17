package com.zte.msg.alarmcenter.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.res.DeviceResDTO;
import com.zte.msg.alarmcenter.entity.OperationLog;
import com.zte.msg.alarmcenter.enums.ErrorCode;
import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.mapper.OperationLogMapper;
import com.zte.msg.alarmcenter.mapper.RoleMapper;
import com.zte.msg.alarmcenter.service.OperationLogService;
import com.zte.msg.alarmcenter.utils.Constants;
import com.zte.msg.alarmcenter.utils.ExcelPortUtil;
import lombok.extern.slf4j.Slf4j;
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
public class OperationLogServiceImpl implements OperationLogService {

    @Autowired
    private OperationLogMapper operationLogMapper;

    /**
     * 分页查询操作日志列表
     *
     * @param userName
     * @param operationType
     * @param startTime
     * @param endTime
     * @param pageReqDTO
     * @return
     */
    @Override
    public Page<OperationLog> listOperationLog(String userName,
                                               String operationType,
                                               Timestamp startTime,
                                               Timestamp endTime,
                                               PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPage().intValue(), pageReqDTO.getSize().intValue());
        if (userName.contains(Constants.PERCENT_SIGN)) {
            userName = "Prohibit input";
        }
        return operationLogMapper.listOperationLog(pageReqDTO.of(), userName, operationType, startTime, endTime);
    }

    @Override
    public void exportOperationLog(String userName, String operationType, Timestamp startTime, Timestamp endTime, HttpServletResponse response) {
        List<String> listName = Arrays.asList("操作员", "操作时间", "IP", "操作类型", "操作描述");
        List<OperationLog> logs = operationLogMapper.exportOperationLog(userName, operationType, startTime, endTime);
        List<Map<String, String>> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        if (logs != null && !logs.isEmpty()) {
            for (OperationLog log : logs) {
                Map<String, String> map = new HashMap<>();
                map.put("操作员", log.getUserName());
                map.put("操作时间", log.getOperationTime() == null ? null : sdf.format(log.getOperationTime()));
                map.put("IP", log.getHostIp());
                map.put("操作类型", log.getOperationType());
                map.put("操作描述", log.getParams());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("操作日志信息", listName, list, null, response);
    }

    /**
     * 获取所有操作类型
     *
     * @return
     */
    @Override
    public List<String> getOperationType() {
        List<String> list = operationLogMapper.getOperationType();
        if (null == list || list.isEmpty()) {
            return null;
        }
        return list;
    }
}
