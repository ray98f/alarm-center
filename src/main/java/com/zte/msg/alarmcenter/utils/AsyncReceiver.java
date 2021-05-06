package com.zte.msg.alarmcenter.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zte.msg.alarmcenter.config.RabbitMqConfig;
import com.zte.msg.alarmcenter.dto.AsyncVO;
import com.zte.msg.alarmcenter.dto.req.AlarmHistoryReqDTO;
import com.zte.msg.alarmcenter.dto.req.HeartbeatQueueReqDTO;
import com.zte.msg.alarmcenter.dto.req.SnmpAlarmDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmHistoryResDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmRuleDataResDTO;
import com.zte.msg.alarmcenter.dto.res.RedisUpdateFrequencyResDTO;
import com.zte.msg.alarmcenter.entity.*;
import com.zte.msg.alarmcenter.enums.ErrorCode;
import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.mapper.*;
import com.zte.msg.alarmcenter.utils.task.DataCacheTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Autowired
    private AlarmAbnormalMapper alarmAbnormalMapper;

    @Autowired
    private ChildSystemMapper childSystemMapper;

    @Autowired
    private SnmpAlarmMapper snmpAlarmMapper;

    @Autowired
    private AlarmRuleMapper alarmRuleMapper;

    @Autowired
    private HomeMapper homeMapper;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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
    public void alarmProcess(String json) throws ParseException {
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
    public void syncAlarmProcess(String json) throws ParseException {
        JSONArray jsonArray = JSONArray.parseArray(json);
        List<AlarmHistoryReqDTO> alarmReqDTOList = jsonArray.toJavaList(AlarmHistoryReqDTO.class);
        if (null == alarmReqDTOList || alarmReqDTOList.isEmpty()) {
            log.warn("消息队列中信息为空");
            return;
        }
        log.info("------ 开始同步告警记录 ------");
        List<AlarmHistory> alarmHistories = conversionAndFilter(alarmReqDTOList);
        syncData(alarmHistories);
        log.info("------ 同步告警记录结束 ------");
    }

    /**
     * SNMP 告警数据处理
     *
     * @param json
     */
    @RabbitListener(queues = RabbitMqConfig.SNMP_ALARM_QUEUE)
    @RabbitHandler
    @Async
    public void snmpAlarmProcess(String json) throws ParseException {
        JSONArray jsonArray = JSONArray.parseArray(json);
        List<SnmpAlarmDTO> snmpAlarmDTOS = jsonArray.toJavaList(SnmpAlarmDTO.class);
        if (null == snmpAlarmDTOS || snmpAlarmDTOS.isEmpty()) {
            log.warn("消息队列中信息为空");
            return;
        }
        log.info("------ 开始接收告警记录 ------");
        List<AlarmHistoryReqDTO> alarmReqDTOList = transferSnmpToAlarmHistory(snmpAlarmDTOS);
        List<AlarmHistory> alarmHistories = conversionAndFilter(alarmReqDTOList);
        editData(alarmHistories);
        log.info("------ 接收告警记录结束 ------");
    }

    /**
     * SNMP 告警同步数据处理
     *
     * @param json
     */
    @RabbitListener(queues = RabbitMqConfig.SNMP_SYNC_ALARM_QUEUE)
    @RabbitHandler
    @Async
    public void snmpSyncAlarmProcess(String json) throws ParseException {
        JSONArray jsonArray = JSONArray.parseArray(json);
        List<SnmpAlarmDTO> snmpAlarmDTOS = jsonArray.toJavaList(SnmpAlarmDTO.class);
        if (null == snmpAlarmDTOS || snmpAlarmDTOS.isEmpty()) {
            log.warn("消息队列中信息为空");
            return;
        }
        log.info("------ 开始接收告警记录 ------");
        List<AlarmHistoryReqDTO> alarmReqDTOList = transferSnmpToAlarmHistory(snmpAlarmDTOS);
        List<AlarmHistory> alarmHistories = conversionAndFilter(alarmReqDTOList);
        syncData(alarmHistories);
        log.info("------ 接收告警记录结束 ------");
    }

    /**
     * SNMP告警转换为通用告警数据
     *
     * @param snmpAlarmDTOS
     * @return
     */
    private List<AlarmHistoryReqDTO> transferSnmpToAlarmHistory(List<SnmpAlarmDTO> snmpAlarmDTOS) {
        List<AlarmHistoryReqDTO> alarmHistoryReqDTOS = new ArrayList<>();
        for (SnmpAlarmDTO snmpAlarmDTO : snmpAlarmDTOS) {

            Integer systemId = childSystemMapper.getIdBySidAndPositionCode(snmpAlarmDTO.getSystemCode(), snmpAlarmDTO.getLineCode());
            if (systemId == null || systemId <= 0) {
                alarmAbnormalMapper.insertAlarmError(new AlarmHistoryReqDTO(), null, "系统数据异常，未找到对应的系统 | Data: " + JSON.toJSONString(snmpAlarmDTO));
                continue;
            }

            AlarmHistoryReqDTO alarmHistoryReqDTO = snmpAlarmMapper.getAlarmHistoryBySnmpName(snmpAlarmDTO.getAlarmManagedObjectInstanceName());
            if (alarmHistoryReqDTO == null) {
                alarmAbnormalMapper.insertAlarmError(new AlarmHistoryReqDTO(), null, "系统数据异常，未找到SNMP位置 | Data: " + JSON.toJSONString(snmpAlarmDTO));
                continue;
            }

            Integer alarmCode = snmpAlarmMapper.getAlarmCodeBySnmpInfo(systemId, snmpAlarmDTO.getEmsAlarmCode(), snmpAlarmDTO.getAlarmNetype(), snmpAlarmDTO.getAlarmSpecificProblem());
            if (alarmCode == null) {
                alarmAbnormalMapper.insertAlarmError(new AlarmHistoryReqDTO(), null, "系统数据异常，未找到SNMP告警码 | Data: " + JSON.toJSONString(snmpAlarmDTO));
                continue;
            }

            alarmHistoryReqDTO.setAlarmCode(alarmCode);
            alarmHistoryReqDTO.setRecovery(snmpAlarmDTO.isCleared());
            alarmHistoryReqDTO.setAlarmTime(snmpAlarmDTO.getAlarmTime());
            alarmHistoryReqDTO.setAlarmMessageList(snmpAlarmDTO.getMessages());
            alarmHistoryReqDTOS.add(alarmHistoryReqDTO);
        }
        return alarmHistoryReqDTOS;
    }

    /**
     * 系统心跳
     *
     * @param json
     */
    @RabbitListener(queues = RabbitMqConfig.HEARTBEAT_QUEUE)
    @RabbitHandler
    @Async
    public void syncHeartbeatQueue(String json) {
        JSONArray jsonArray = JSONArray.parseArray(json);
        List<HeartbeatQueueReqDTO> heartbeatQueueReqDTOList = jsonArray.toJavaList(HeartbeatQueueReqDTO.class);
        if (null == heartbeatQueueReqDTOList || heartbeatQueueReqDTOList.isEmpty()) {
            log.warn("消息队列中信息为空");
            return;
        }
        log.info("------ 开始接收系统心跳 ------");
        childSystemMapper.isOnline(heartbeatQueueReqDTOList);
        log.info("------ 接收系统心跳结束 ------");
    }

    private void editData(List<AlarmHistory> alarmHistories) {
        if (null == alarmHistories || alarmHistories.isEmpty()) {
            log.error("告警记录接收为空");
            return;
        }
        int result = alarmManageMapper.editAlarmHistory(alarmHistories);
        if (result >= 0) {
            for (AlarmHistory alarmHistory : alarmHistories) {
                if (null != alarmHistory.getAlarmMessageList() && !alarmHistory.getAlarmMessageList().isEmpty()) {
                    Long alarmId = alarmManageMapper.getAlarmHistoryId(alarmHistory);
                    result = alarmManageMapper.editAlarmMessage(alarmId, alarmHistory.getAlarmMessageList());
                    if (result < 0) {
                        log.error("ID为{}的告警记录添加附加信息失败", alarmId);

                    }
                }
            }
        }
    }

    private void syncData(List<AlarmHistory> alarmHistories) {
        if (null == alarmHistories || alarmHistories.isEmpty()) {
            log.error("告警记录接收为空");
            return;
        }
        List<AlarmHistoryResDTO> alarmHistoryResDTOList = homeMapper.selectAlarmHistory();
        for (AlarmHistory alarmHistory : alarmHistories) {
            alarmHistoryResDTOList.removeIf(alarmHistoryResDTO -> alarmHistory.getSubsystemCode().equals(alarmHistoryResDTO.getSubsystemCode()) &&
                    alarmHistory.getLineCode().equals(alarmHistoryResDTO.getLineCode()) &&
                    alarmHistory.getSiteCode().equals(alarmHistoryResDTO.getSiteCode()) &&
                    alarmHistory.getDeviceCode().equals(alarmHistoryResDTO.getDeviceCode()) &&
                    alarmHistory.getSlotCode().equals(alarmHistoryResDTO.getSlotPositionCode()) &&
                    alarmHistory.getAlarmCodeId().toString().equals(alarmHistoryResDTO.getAlarmCode()) &&
                    alarmHistory.getAlarmState().equals(alarmHistoryResDTO.getAlarmState()));
        }
        if (alarmHistoryResDTOList.size() > 0) {
            alarmManageMapper.updateSyncAlarmHistory(alarmHistoryResDTOList);
        }
        int result = alarmManageMapper.syncAlarmHistory(alarmHistories);
        if (result >= 0) {
            for (AlarmHistory alarmHistory : alarmHistories) {
                Long alarmId = alarmManageMapper.getAlarmHistoryId(alarmHistory);
                result = alarmManageMapper.syncAlarmMessage(alarmId, alarmHistory.getAlarmMessageList());
                if (result < 0) {
                    log.error("ID为{}的告警记录同步附加信息失败", alarmId);

                }
            }
        }
    }

    private List<AlarmHistory> conversionAndFilter(List<AlarmHistoryReqDTO> alarmReqDTOList) throws ParseException {
        List<AlarmHistory> alarmHistories = new ArrayList<>();
        for (AlarmHistoryReqDTO alarmReqDTO : alarmReqDTOList) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Pattern a = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s((([0-1][0-9])|(2?[0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
            if (!a.matcher(alarmReqDTO.getAlarmTime()).matches()) {
                alarmReqDTO.setAlarmTime(null);
                alarmAbnormalMapper.insertAlarmError(alarmReqDTO, null, "时间格式错误，请检查时间格式| Data: " + JSON.toJSONString(alarmReqDTO));
                continue;
            }
            AlarmHistory alarmHistory = new AlarmHistory();
            if (null != DataCacheTask.subsystemData.get(alarmReqDTO.getSystem())) {
                alarmHistory.setSubsystemId(DataCacheTask.subsystemData.get(alarmReqDTO.getSystem()).getId());
                alarmHistory.setSubsystemCode(alarmReqDTO.getSystem());
                alarmHistory.setSubsystemName(DataCacheTask.subsystemData.get(alarmReqDTO.getSystem()).getName());
            }
            if (alarmHistory.getSubsystemId() == null) {
                alarmAbnormalMapper.insertAlarmError(alarmReqDTO, null, "系统数据异常，未找到系统 | Data: " + JSON.toJSONString(alarmReqDTO));
                continue;
            }
            for (Map.Entry<Long, Position> m : DataCacheTask.positionData.entrySet()) {
                if (m.getValue().getType().equals(2) && alarmReqDTO.getLine().equals(m.getValue().getPositionCode())) {
                    alarmHistory.setLineId(m.getValue().getId());
                    alarmHistory.setLineCode(alarmReqDTO.getLine());
                    alarmHistory.setLineName(m.getValue().getName());
                    break;
                }
            }
            if (alarmHistory.getLineId() == null) {
                alarmAbnormalMapper.insertAlarmError(alarmReqDTO, null, "线路数据异常，未找到线路 | Data: " + JSON.toJSONString(alarmReqDTO));
                continue;
            }
            for (Map.Entry<Long, Position> m : DataCacheTask.positionData.entrySet()) {
                if (m.getValue().getType().equals(3) && alarmHistory.getLineId().equals(m.getValue().getPid())
                        && alarmReqDTO.getStation().equals(m.getValue().getPositionCode())) {
                    alarmHistory.setSiteId(m.getValue().getId());
                    alarmHistory.setSiteCode(alarmReqDTO.getStation());
                    alarmHistory.setSiteName(m.getValue().getName());
                    break;
                }
            }
            if (alarmHistory.getSiteId() == null) {
                alarmAbnormalMapper.insertAlarmError(alarmReqDTO, null, "站点数据异常，未找到站点 | Data: " + JSON.toJSONString(alarmReqDTO));
                continue;
            }
            for (Map.Entry<Long, Device> m : DataCacheTask.deviceData.entrySet()) {
                if (alarmReqDTO.getDevice().equals(m.getValue().getDeviceCode())
                        && alarmHistory.getLineId().equals(m.getValue().getPositionId())
                        && alarmHistory.getSiteId().equals(m.getValue().getStationId())
                        && alarmHistory.getSubsystemId().equals(m.getValue().getSystemId())) {
                    alarmHistory.setDeviceId(m.getValue().getId());
                    alarmHistory.setDeviceCode(alarmReqDTO.getDevice());
                    alarmHistory.setDeviceName(m.getValue().getName());
                    break;
                }
            }
            if (alarmHistory.getDeviceId() == null) {
                alarmAbnormalMapper.insertAlarmError(alarmReqDTO, null, "设备数据异常，未找到设备 | Data: " + JSON.toJSONString(alarmReqDTO));
                continue;
            }
            for (Map.Entry<Long, DeviceSlot> m : DataCacheTask.deviceSlotData.entrySet()) {
                if (alarmReqDTO.getSlot().equals(m.getValue().getSlotCode())
                        && alarmHistory.getLineId().equals(m.getValue().getPositionId())
                        && alarmHistory.getSiteId().equals(m.getValue().getStationId())
                        && alarmHistory.getSubsystemId().equals(m.getValue().getSystemId())
                        && alarmHistory.getDeviceId().equals(m.getValue().getDeviceId())) {
                    alarmHistory.setSlotId(m.getValue().getId());
                    alarmHistory.setSlotCode(alarmReqDTO.getSlot());
                    alarmHistory.setSlotName(m.getValue().getName());
                    break;
                }
            }
            if (alarmHistory.getSlotId() == null) {
                alarmAbnormalMapper.insertAlarmError(alarmReqDTO, null, "槽位数据异常，未找到槽位 | Data: " + JSON.toJSONString(alarmReqDTO));
                continue;
            }
            for (Map.Entry<Long, AlarmCode> m : DataCacheTask.alarmCodeData.entrySet()) {
                if (alarmReqDTO.getAlarmCode().equals(m.getValue().getCode())
                        && alarmHistory.getSubsystemId().equals(m.getValue().getSystemId())
                        && alarmHistory.getLineId().equals(m.getValue().getPositionId())) {
                    alarmHistory.setAlarmCode(m.getValue().getId());
                    alarmHistory.setAlarmCodeId(alarmReqDTO.getAlarmCode());
                    alarmHistory.setAlarmName(m.getValue().getName());
                    alarmHistory.setAlarmReason(m.getValue().getReason());
                    alarmHistory.setAlarmLevel(m.getValue().getLevelId());
                    break;
                }
            }
            if (alarmHistory.getAlarmCode() == null) {
                alarmAbnormalMapper.insertAlarmError(alarmReqDTO, null, "告警码数据异常，未找到告警码 | Data: " + JSON.toJSONString(alarmReqDTO));
                continue;
            }
            alarmHistory.setAlarmState(0);
            ConcurrentHashMap<Long, AlarmRuleDataResDTO> ruleData = new ConcurrentHashMap<>();
            for (Map.Entry<Long, AlarmRuleDataResDTO> m : DataCacheTask.alarmRuleData.entrySet()) {
                if (m.getValue().getIsEnable() == 0
                        && m.getValue().getSystemIds().contains(alarmHistory.getSubsystemId())
                        && m.getValue().getDeviceIds().contains(alarmHistory.getDeviceId())
                        && m.getValue().getPositionIds().contains(alarmHistory.getSiteId())
                        && m.getValue().getAlarmIds().contains(alarmHistory.getAlarmCode())) {
                    ruleData.put(m.getValue().getType(), m.getValue());
                }
            }
            // 根据告警规则获取告警状态
            // 1-告警延迟规则;2-告警入库过滤规则;3-告警过滤规则;4-告警确认规则;5-告警清除规则;6-告警升级规则;7-告警前转规则
            if (ruleData.get(2L) != null) {
                alarmHistory = null;
            } else if (ruleData.get(3L) != null) {
                alarmHistory.setIsRing(0);
                alarmHistory.setAlarmState(5);
                String flag = "sysId:" + alarmHistory.getSubsystemId() +
                        "-lineId:" + alarmHistory.getLineId() +
                        "-siteId:" + alarmHistory.getSiteId() +
                        "-deviceId:" + alarmHistory.getDeviceId() +
                        "-slotId:" + alarmHistory.getSlotId() +
                        "-codeId:" + alarmHistory.getAlarmCode();
                String updateKey = "update_frequency:" + flag;
                stringRedisTemplate.opsForZSet().removeRangeByScore(updateKey, 0, System.currentTimeMillis());
            } else if (ruleData.get(5L) != null) {
                alarmHistory.setIsRing(0);
                alarmHistory.setAlarmState(7);
                String flag = "sysId:" + alarmHistory.getSubsystemId() +
                        "-lineId:" + alarmHistory.getLineId() +
                        "-siteId:" + alarmHistory.getSiteId() +
                        "-deviceId:" + alarmHistory.getDeviceId() +
                        "-slotId:" + alarmHistory.getSlotId() +
                        "-codeId:" + alarmHistory.getAlarmCode();
                String updateKey = "update_frequency:" + flag;
                stringRedisTemplate.opsForZSet().removeRangeByScore(updateKey, 0, System.currentTimeMillis());
            } else if (ruleData.get(4L) != null) {
                alarmHistory.setIsRing(0);
                alarmHistory.setAlarmState(3);
            } else {
                if (ruleData.get(6L) != null) {
                    alarmHistory.setAlarmFrequency(ruleData.get(6L).getFrequency());
                    alarmHistory.setFrequencyTime(ruleData.get(6L).getFrequencyTime());
                    alarmHistory.setExperienceTime(ruleData.get(6L).getExperienceTime());
                    if (alarmHistory.getAlarmLevel() > 1) {
                        frequencyAlarmHistory(alarmHistory);
                    }
                }
                if (ruleData.get(7L) != null) {
                    String content = alarmHistory.getLineName() + "线路-" + alarmHistory.getSiteName() + "站点-" + alarmHistory.getSubsystemName() + "系统 产生";
                    Integer result = alarmManageMapper.getUpdateLevel(alarmHistory);
                    if (result != null) {
                        alarmHistory.setAlarmLevel(result);
                    }
                    if (1 == alarmHistory.getAlarmLevel()) {
                        content = content + "紧急告警";
                    } else if (2 == alarmHistory.getAlarmLevel()) {
                        content = content + "重要告警";
                    } else if (3 == alarmHistory.getAlarmLevel()) {
                        content = content + "一般告警";
                    }
                    content = content + " | Data: " + JSON.toJSONString(alarmReqDTO);
                    if (ruleData.get(7L).getMsgConfigId() == null) {
                        alarmAbnormalMapper.insertAlarmError(alarmReqDTO, null, "告警规则前转信息数据异常，未找到告警规则前转信息 | Data: " + JSON.toJSONString(alarmReqDTO));
                    } else {
                        MsgConfig msgConfig = alarmRuleMapper.selectMsgConfigById(ruleData.get(7L).getMsgConfigId());
                        if (Objects.isNull(msgConfig)) {
                            alarmAbnormalMapper.insertAlarmError(alarmReqDTO, null, "告警规则前转信息数据异常，未找到告警规则前转信息 | Data: " + JSON.toJSONString(alarmReqDTO));
                        } else {
                            alarmRuleMapper.insertMsgPush(msgConfig, content);
                        }
                    }
                }
                if (ruleData.get(1L) != null) {
                    alarmHistory.setIsRing(0);
                    alarmHistory.setAlarmState(1);
                    alarmHistory.setDelayTime(new Timestamp(simpleDateFormat.parse(alarmReqDTO.getAlarmTime()).getTime() + ruleData.get(1L).getDelayTime() * 1000));
                }
            }
            if (!Objects.isNull(alarmHistory)) {
                if (alarmHistory.getAlarmState() == null) {
                    alarmAbnormalMapper.insertAlarmError(alarmReqDTO, null, "告警状态数据异常，未找到告警状态 | Data: " + JSON.toJSONString(alarmReqDTO));
                    continue;
                }
                if (alarmReqDTO.getRecovery()) {
                    alarmHistory.setRecoveryTime(new Timestamp(System.currentTimeMillis()));
                    alarmHistory.setAlarmState(7);
                }
                alarmHistory.setFirstTime(new Timestamp(simpleDateFormat.parse(alarmReqDTO.getAlarmTime()).getTime()));
                alarmHistory.setFinalTime(new Timestamp(simpleDateFormat.parse(alarmReqDTO.getAlarmTime()).getTime()));
                alarmHistories.add(alarmHistory);
                if (!Objects.isNull(alarmReqDTO.getAlarmMessageList())) {
                    alarmHistory.setAlarmMessageList(alarmReqDTO.getAlarmMessageList());
                }
            }
        }
        return alarmHistories;
    }

    /**
     * 单位时间内告警次数升级
     */
    private void frequencyAlarmHistory(AlarmHistory alarmHistory) {
        String flag = "sysId:" + alarmHistory.getSubsystemId() +
                "-lineId:" + alarmHistory.getLineId() +
                "-siteId:" + alarmHistory.getSiteId() +
                "-deviceId:" + alarmHistory.getDeviceId() +
                "-slotId:" + alarmHistory.getSlotId() +
                "-codeId:" + alarmHistory.getAlarmCode();
        String updateKey = "update_frequency:" + flag;
        String lockKey = "update_frequency_lock:" + flag;
        RLock locker = redissonClient.getLock(lockKey);
        try {
            locker.lock();
            long now = System.currentTimeMillis();
            if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(updateKey))) {
                stringRedisTemplate.opsForZSet().add(updateKey, String.valueOf(now), now);
                return;
            }
            if (Objects.requireNonNull(stringRedisTemplate.opsForZSet().count(updateKey, now - alarmHistory.getFrequencyTime() * 1000, now)) < alarmHistory.getAlarmFrequency()) {
                stringRedisTemplate.opsForZSet().add(updateKey, String.valueOf(now), now);
                stringRedisTemplate.opsForZSet().removeRangeByScore(updateKey, 0, now - alarmHistory.getFrequencyTime() * 1000);
            } else {
                stringRedisTemplate.opsForZSet().removeRangeByScore(updateKey, 0, now);
                alarmManageMapper.updateFrequencyAlarmHistory(alarmHistory);
            }
        } catch (Exception e) {
            log.error("告警升级失败: {}", e.getMessage());
        } finally {
            locker.unlock();
        }
    }
}