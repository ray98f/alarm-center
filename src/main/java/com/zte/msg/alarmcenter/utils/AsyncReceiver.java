package com.zte.msg.alarmcenter.utils;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zte.msg.alarmcenter.config.RabbitMqConfig;
import com.zte.msg.alarmcenter.dto.AsyncVO;
import com.zte.msg.alarmcenter.dto.req.AlarmHistoryReqDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmRuleDataResDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmRuleDetailsResDTO;
import com.zte.msg.alarmcenter.dto.res.DataIdAndNameResDTO;
import com.zte.msg.alarmcenter.entity.*;
import com.zte.msg.alarmcenter.enums.ErrorCode;
import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.mapper.AlarmManageMapper;
import com.zte.msg.alarmcenter.mapper.SynchronizeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author frp
 */
@Slf4j
@Component
public class AsyncReceiver {

    @Autowired
    private SynchronizeMapper synchronizeMapper;

    @Autowired
    private AlarmManageMapper alarmManageMapper;

    @RabbitListener(queues = RabbitMqConfig.STRING_QUEUE)
    @RabbitHandler
    @Async
    public void process(String message) {
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("HelloReceiver接收到的字符串消息是 => " + message);
    }


    @RabbitListener(queues = RabbitMqConfig.ASYNC_QUEUE)
    @RabbitHandler
    @Async
    public void process(AsyncVO asyncVO) {
        log.info("------ 开始接收同步对象 ------");
        if (null != asyncVO.getAlarmCodeSyncReqDTOList()) {
            int result = synchronizeMapper.alarmCodesSync(asyncVO.getAlarmCodeSyncReqDTOList());
            log.info(JSONUtils.beanToJsonString(asyncVO.getAlarmCodeSyncReqDTOList()) + " : result = {}", result);
            if (result < 0) {
                throw new CommonException(ErrorCode.SYNC_ERROR);
            }
        } else if (null != asyncVO.getDeviceSyncReqDTOList()) {
            int result = synchronizeMapper.devicesSync(asyncVO.getDeviceSyncReqDTOList());
            log.info(JSONUtils.beanToJsonString(asyncVO.getDeviceSyncReqDTOList()) + " : result = {}", result);
            if (result < 0) {
                throw new CommonException(ErrorCode.SYNC_ERROR);
            }
        } else {
            int result = synchronizeMapper.slotsSync(asyncVO.getSlotSyncReqDTOList());
            log.info(JSONUtils.beanToJsonString(asyncVO.getSlotSyncReqDTOList()) + " : result = {}", result);
            if (result < 0) {
                throw new CommonException(ErrorCode.SYNC_ERROR);
            }
        }
        log.info("------ 接收同步对象结束 ------");
    }

    @RabbitListener(queues = RabbitMqConfig.ALARM_QUEUE)
    @RabbitHandler
    @Async
    public void alarmProcess(String json) {
        JSONArray jsonArray = JSONArray.parseArray(json);
        List<AlarmHistoryReqDTO> alarmReqDTO = jsonArray.toJavaList(AlarmHistoryReqDTO.class);
        if (null == alarmReqDTO || alarmReqDTO.isEmpty()) {
            log.warn("消息队列中信息为空");
            return;
        }
        log.info("------ 开始接收告警记录 ------");
        List<AlarmHistory> alarmHistories = conversionAndFilter(alarmReqDTO);
        editData(alarmHistories);
        log.info("------ 接收告警记录结束 ------");
    }

    @RabbitListener(queues = RabbitMqConfig.SYNC_ALARM_QUEUE)
    @RabbitHandler
    @Async
    public void syncAlarmProcess(String json) throws JsonProcessingException {
        JSONArray jsonArray = JSONArray.parseArray(json);
        List<AlarmHistoryReqDTO> alarmReqDTOList = jsonArray.toJavaList(AlarmHistoryReqDTO.class);
        if (null == alarmReqDTOList || alarmReqDTOList.isEmpty()) {
            log.warn("消息队列中信息为空");
            return;
        }
        log.info("------ 开始接收告警记录 ------");
        List<AlarmHistory> alarmHistories = conversionAndFilter(alarmReqDTOList);
        editData(alarmHistories);
        log.info("------ 接收告警记录结束 ------");
    }

    private void editData(List<AlarmHistory> alarmHistories) {
        if (null == alarmHistories || alarmHistories.isEmpty()) {
            log.error("告警记录接收为空");
            return;
        }
        int result = alarmManageMapper.editAlarmHistory(alarmHistories);
        if (result >= 0) {
            for (AlarmHistory alarmHistory : alarmHistories) {
                Long alarmId = alarmManageMapper.getAlarmHistoryId(alarmHistory);
                result = alarmManageMapper.editAlarmMessage(alarmId, alarmHistory.getAlarmMessageList());
                if (result < 0) {
                    log.error("ID为{}的告警记录添加附加信息失败", alarmId);
                }
            }
        }
    }

    private static List<AlarmHistory> conversionAndFilter(List<AlarmHistoryReqDTO> alarmReqDTOList) {
        List<AlarmHistory> alarmHistories = new ArrayList<>();
        for (AlarmHistoryReqDTO alarmReqDTO : alarmReqDTOList) {
            AlarmHistory alarmHistory = new AlarmHistory();
            if (null != DataCache.subsystemData.get(alarmReqDTO.getSystem())) {
                alarmHistory.setSubsystemId(DataCache.subsystemData.get(alarmReqDTO.getSystem()).getId());
            }
            for (Map.Entry<Long, Position> m : DataCache.positionData.entrySet()) {
                if (m.getValue().getType().equals(2) && alarmReqDTO.getLine().equals(m.getValue().getPositionCode())) {
                    alarmHistory.setLineId(m.getValue().getId());
                    break;
                }
            }
            for (Map.Entry<Long, Position> m : DataCache.positionData.entrySet()) {
                if (m.getValue().getType().equals(3) && alarmHistory.getLineId().equals(m.getValue().getPid())
                        && alarmReqDTO.getStation().equals(m.getValue().getPositionCode())) {
                    alarmHistory.setSiteId(m.getValue().getId());
                    break;
                }
            }
            for (Map.Entry<Long, Device> m : DataCache.deviceData.entrySet()) {
                if (alarmReqDTO.getDevice().equals(m.getValue().getDeviceCode())
                        && alarmHistory.getLineId().equals(m.getValue().getPositionId())
                        && alarmHistory.getSiteId().equals(m.getValue().getStationId())
                        && alarmHistory.getSubsystemId().equals(m.getValue().getSystemId())) {
                    alarmHistory.setDeviceId(m.getValue().getId());
                    break;
                }
            }
            for (Map.Entry<Long, DeviceSlot> m : DataCache.deviceSlotData.entrySet()) {
                if (alarmReqDTO.getSlot().equals(m.getValue().getSlotCode())
                        && alarmHistory.getLineId().equals(m.getValue().getPositionId())
                        && alarmHistory.getSiteId().equals(m.getValue().getStationId())
                        && alarmHistory.getSubsystemId().equals(m.getValue().getSystemId())
                        && alarmHistory.getDeviceId().equals(m.getValue().getDeviceId())) {
                    alarmHistory.setSlotId(m.getValue().getId());
                    break;
                }
            }
            for (Map.Entry<Long, AlarmCode> m : DataCache.alarmCodeData.entrySet()) {
                if (alarmReqDTO.getAlarmCode().equals(m.getValue().getCode())
                        && alarmHistory.getSubsystemId().equals(m.getValue().getSystemId())
                        && alarmHistory.getLineId().equals(m.getValue().getPositionId())) {
                    alarmHistory.setAlarmCode(m.getValue().getId());
                    break;
                }
            }
            for (Map.Entry<Long, AlarmRuleDataResDTO> m : DataCache.alarmRuleData.entrySet()) {
                if (m.getValue().getSystemIds().contains(alarmHistory.getSubsystemId())
                        && m.getValue().getDeviceIds().contains(alarmHistory.getDeviceId())
                        && m.getValue().getPositionIds().contains(alarmHistory.getSiteId())
                        && m.getValue().getAlarmIds().contains(alarmHistory.getAlarmCode())) {
                    // 根据告警规则获取告警状态
                    if (m.getValue().getType() == 1) {
                        alarmHistory.setAlarmState(1);
                        break;
                    } else if (m.getValue().getType() == 2) {
                        alarmHistory = null;
                        break;
                    } else if (m.getValue().getType() == 3) {
                        alarmHistory.setAlarmState(5);
                        break;
                    } else if (m.getValue().getType() == 4) {
                        alarmHistory.setAlarmState(3);
                        break;
                    } else if (m.getValue().getType() == 5) {
                        alarmHistory.setAlarmState(7);
                        break;
                    } else {
                        alarmHistory.setAlarmState(0);
                        break;
                    }
                }
            }
            if (!Objects.isNull(alarmHistory)) {
                alarmHistory.setAlarmLevel(DataCache.alarmCodeData.get(alarmHistory.getAlarmCode()).getLevelId());
                if (alarmReqDTO.getIsRecovery()) {
                    alarmHistory.setRecoveryTime(new Timestamp(System.currentTimeMillis()));
//                    alarmHistory.setAlarmState(7);
                }
                alarmHistory.setFirstTime(alarmReqDTO.getAlarmTime());
                alarmHistory.setFinalTime(alarmReqDTO.getAlarmTime());
                alarmHistories.add(alarmHistory);
                if (!Objects.isNull(alarmReqDTO.getAlarmMessageList())) {
                    alarmHistory.setAlarmMessageList(alarmReqDTO.getAlarmMessageList());
                }
            }
        }
        return alarmHistories;
    }
}