package com.zte.msg.alarmcenter.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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

    @Value("${spring.redis.key-prefix}")
    private String keyPrefix;

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

    @Autowired
    AsyncSender asyncSender;

    @RabbitListener(queues = RabbitMqConfig.STRING_QUEUE)
    @RabbitHandler
    @Async
    public void process(String message) {
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @RabbitListener(queues = RabbitMqConfig.ASYNC_QUEUE)
    @RabbitHandler
    @Async
    public void process(AsyncVO asyncVO) {
        if (null != asyncVO.getAlarmCodeSyncReqDTOList()) {
            int result = synchronizeMapper.alarmCodesSync(asyncVO.getAlarmCodeSyncReqDTOList());
            if (result < 0) {
                throw new CommonException(ErrorCode.SYNC_ERROR);
            }
        } else if (null != asyncVO.getDeviceSyncReqDTOList()) {
            int result = synchronizeMapper.devicesSync(asyncVO.getDeviceSyncReqDTOList());
            if (result < 0) {
                throw new CommonException(ErrorCode.SYNC_ERROR);
            }
        } else {
            int result = synchronizeMapper.slotsSync(asyncVO.getSlotSyncReqDTOList());
            if (result < 0) {
                throw new CommonException(ErrorCode.SYNC_ERROR);
            }
        }
    }

    @RabbitListener(queues = RabbitMqConfig.ALARM_QUEUE)
    @RabbitHandler
    public void alarmProcess(String json) {
        try {
            JSONArray jsonArray = JSONArray.parseArray(json);
            List<AlarmHistoryReqDTO> alarmReqList = jsonArray.toJavaList(AlarmHistoryReqDTO.class);
            if (alarmReqList != null && !alarmReqList.isEmpty()) {
                List<AlarmHistory> alarmHistories = conversionAndFilter(alarmReqList);
                editData(alarmHistories);
            }
        } catch (Exception e) {
            log.error("TCP单条告警失败：" + e.getMessage());
        }
    }

    @RabbitListener(queues = RabbitMqConfig.SYNC_ALARM_QUEUE)
    @RabbitHandler
    @Async
    public void syncAlarmProcess(String json) {
        try {
            JSONArray jsonArray = JSONArray.parseArray(json);
            List<AlarmHistoryReqDTO> alarmReqList = jsonArray.toJavaList(AlarmHistoryReqDTO.class);
            if (alarmReqList != null && !alarmReqList.isEmpty()) {
                List<AlarmHistory> alarmHistories = conversionAndFilter(alarmReqList);
                syncData(alarmHistories);
            }
        } catch (Exception e) {
            log.error("TCP同步告警失败：" + e.getMessage());
        }
    }

    /**
     * SNMP 告警数据处理
     *
     * @param json
     */
    @RabbitListener(queues = RabbitMqConfig.SNMP_ALARM_QUEUE)
    @RabbitHandler
    public void snmpAlarmProcess(String json) {
        try {
            JSONArray jsonArray = JSONArray.parseArray(json);
            List<SnmpAlarmDTO> snmpAlarms = jsonArray.toJavaList(SnmpAlarmDTO.class);
            if (snmpAlarms != null && !snmpAlarms.isEmpty()) {
                List<AlarmHistoryReqDTO> alarmReqDTOList = transferSnmpToAlarmHistory(snmpAlarms);
                List<AlarmHistory> alarmHistories = conversionAndFilter(alarmReqDTOList);
                editData(alarmHistories);
            }
        } catch (Exception e) {
            log.error("SNMP告警失败：" + e.getMessage());
        }
    }

    /**
     * SNMP 告警同步数据处理
     *
     * @param json
     */
    @RabbitListener(queues = RabbitMqConfig.SNMP_SYNC_ALARM_QUEUE)
    @RabbitHandler
    @Async
    public void snmpSyncAlarmProcess(String json) {
        try {
            JSONArray jsonArray = JSONArray.parseArray(json);
            List<SnmpAlarmDTO> snmpAlarms = jsonArray.toJavaList(SnmpAlarmDTO.class);
            if (snmpAlarms != null && !snmpAlarms.isEmpty()) {
                List<AlarmHistoryReqDTO> alarmReqDTOList = transferSnmpToAlarmHistory(snmpAlarms);
                List<AlarmHistory> alarmHistories = conversionAndFilter(alarmReqDTOList);
                syncData(alarmHistories);
            }
        } catch (Exception e) {
            log.error("SNMP同步告警失败：" + e.getMessage());
        }
    }

    /**
     * SNMP告警转换为通用告警数据
     *
     * @param snmpAlarms
     * @return
     */
    private List<AlarmHistoryReqDTO> transferSnmpToAlarmHistory(List<SnmpAlarmDTO> snmpAlarms) {
        List<AlarmHistoryReqDTO> alarmHistories = new ArrayList<>();
        for (SnmpAlarmDTO snmpAlarm : snmpAlarms) {
            AlarmHistoryReqDTO alarmHistoryReqDTO = new AlarmHistoryReqDTO();
            SnmpDeviceSlot snmpDeviceSlot = DataCacheTask.snmpDeviceSlotData.get("snmpName:" + snmpAlarm.getAlarmManagedObjectInstanceName() + "-site:" + snmpAlarm.getStationCode());
            if (snmpDeviceSlot == null) {
                AlarmHistoryReqDTO alarmHistory = new AlarmHistoryReqDTO();
                alarmHistory.setAlarmTime(snmpAlarm.getAlarmTime());
                alarmHistory.setSystem(snmpAlarm.getSystemCode());
                alarmHistory.setLine(snmpAlarm.getLineCode());
                alarmHistory.setStation(snmpAlarm.getStationCode());
                alarmAbnormalMapper.insertAlarmError(alarmHistory, null, "系统数据异常，未找到SNMP位置 | Data: " + JSON.toJSONString(snmpAlarm));
                continue;
            }
            SnmpAlarmCode snmpAlarmCode = DataCacheTask.snmpAlarmCodeData.get("system:" + snmpAlarm.getSystemCode() + "-snmpCode:" + snmpAlarm.getEmsAlarmCode());
            if (snmpAlarmCode == null) {
                AlarmHistoryReqDTO alarmHistory = new AlarmHistoryReqDTO();
                alarmHistory.setAlarmTime(snmpAlarm.getAlarmTime());
                alarmHistory.setSystem(snmpAlarm.getSystemCode());
                alarmHistory.setLine(snmpAlarm.getLineCode());
                alarmHistory.setStation(snmpAlarm.getStationCode());
                alarmAbnormalMapper.insertAlarmError(alarmHistory, null, "系统数据异常，未找到SNMP告警码 | Data: " + JSON.toJSONString(snmpAlarm));
                continue;
            }
            alarmHistoryReqDTO.setSystem(snmpAlarm.getSystemCode());
            alarmHistoryReqDTO.setLine(snmpDeviceSlot.getLine());
            alarmHistoryReqDTO.setStation(snmpDeviceSlot.getStation());
            alarmHistoryReqDTO.setDevice(snmpDeviceSlot.getDevice());
            alarmHistoryReqDTO.setSlot(snmpDeviceSlot.getSlot());
            alarmHistoryReqDTO.setAlarmCode(snmpAlarmCode.getCode());
            alarmHistoryReqDTO.setRecovery(snmpAlarm.isCleared());
            alarmHistoryReqDTO.setAlarmTime(snmpAlarm.getAlarmTime());
            alarmHistoryReqDTO.setAlarmMessageList(snmpAlarm.getMessages());
            alarmHistories.add(alarmHistoryReqDTO);
        }
        return alarmHistories;
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
        List<HeartbeatQueueReqDTO> heartbeatQueueReqList = jsonArray.toJavaList(HeartbeatQueueReqDTO.class);
        if (heartbeatQueueReqList != null && !heartbeatQueueReqList.isEmpty()) {
            childSystemMapper.isOnline(heartbeatQueueReqList);
        }
    }

    private void editData(List<AlarmHistory> alarmHistories) {
        if (alarmHistories != null && !alarmHistories.isEmpty()) {
            try {
                alarmManageMapper.editAlarmHistory(alarmHistories);
                AlarmHistory alarm = alarmHistories.get(0);
                if (alarm.getIsRing() == 1 && !alarm.getIsRecovery()) {
                    asyncSender.sendTts(alarm.getSubsystemName() + "产生"
                            + (alarm.getAlarmLevel() == 1 ? "紧急告警" : alarm.getAlarmLevel() == 2 ? "重要告警" : "一般告警")
                            + "，告警内容为：" + alarm.getAlarmName());
                }
            } catch (Exception e) {
                log.error("数据异常:" + JSONArray.toJSONString(alarmHistories), e.getMessage(), e);
            }
        }
    }

    private void syncData(List<AlarmHistory> alarmHistories) {
        if (alarmHistories != null && !alarmHistories.isEmpty()) {
            try {
                List<AlarmHistoryResDTO> alarmHistoryResDTOList = homeMapper.selectAlarmHistory(null, 0, alarmHistories.get(0).getSubsystemId(), null);
                for (AlarmHistory alarmHistory : alarmHistories) {
                    if (alarmHistory.getSlotCode() == null) {
                        alarmHistoryResDTOList.removeIf(alarmHistoryResDTO ->
                                alarmHistory.getSubsystemCode().equals(alarmHistoryResDTO.getSubsystemCode()) &&
                                        alarmHistory.getLineCode().equals(alarmHistoryResDTO.getLineCode()) &&
                                        alarmHistory.getSiteCode().equals(alarmHistoryResDTO.getSiteCode()) &&
                                        alarmHistory.getDeviceCode().equals(alarmHistoryResDTO.getDeviceCode()) &&
                                        alarmHistory.getAlarmCodeId().toString().equals(alarmHistoryResDTO.getAlarmCode()) &&
                                        (alarmHistory.getIsRecovery() ? 1 : 0) == (alarmHistoryResDTO.getIsRecovery()));
                    } else {
                        alarmHistoryResDTOList.removeIf(alarmHistoryResDTO ->
                                alarmHistory.getSubsystemCode().equals(alarmHistoryResDTO.getSubsystemCode()) &&
                                        alarmHistory.getLineCode().equals(alarmHistoryResDTO.getLineCode()) &&
                                        alarmHistory.getSiteCode().equals(alarmHistoryResDTO.getSiteCode()) &&
                                        alarmHistory.getDeviceCode().equals(alarmHistoryResDTO.getDeviceCode()) &&
                                        alarmHistory.getSlotCode().equals(alarmHistoryResDTO.getSlotPositionCode()) &&
                                        alarmHistory.getAlarmCodeId().toString().equals(alarmHistoryResDTO.getAlarmCode()) &&
                                        (alarmHistory.getIsRecovery() ? 1 : 0) == (alarmHistoryResDTO.getIsRecovery()));
                    }
                }
                if (alarmHistoryResDTOList.size() > 0) {
                    alarmManageMapper.updateSyncAlarmHistory(alarmHistoryResDTOList);
                }
                alarmManageMapper.syncAlarmHistory(alarmHistories);
            } catch (Exception e) {
                log.error("数据异常:" + JSONArray.toJSONString(alarmHistories), e.getMessage(), e);
            }
        }
    }

    private List<AlarmHistory> conversionAndFilter(List<AlarmHistoryReqDTO> alarmReqDTOList) throws ParseException {
        List<AlarmHistory> alarmHistories = new ArrayList<>();
        for (AlarmHistoryReqDTO alarmReqDTO : alarmReqDTOList) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Pattern time = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s((([0-1][0-9])|(2?[0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
            if (!time.matcher(alarmReqDTO.getAlarmTime()).matches()) {
                alarmReqDTO.setAlarmTime(null);
                alarmAbnormalMapper.insertAlarmError(alarmReqDTO, null, "时间格式错误，请检查时间格式| Data: " + JSON.toJSONString(alarmReqDTO));
                continue;
            }
            AlarmHistory alarmHistory = new AlarmHistory();
            alarmHistory.setIsRing(1);
            if (null != DataCacheTask.subsystemData.get(alarmReqDTO.getSystem())) {
                alarmHistory.setSubsystemId(DataCacheTask.subsystemData.get(alarmReqDTO.getSystem()).getId());
                alarmHistory.setSubsystemCode(alarmReqDTO.getSystem());
                alarmHistory.setSubsystemName(DataCacheTask.subsystemData.get(alarmReqDTO.getSystem()).getName());
            } else {
                alarmAbnormalMapper.insertAlarmError(alarmReqDTO, null, "系统数据异常，未找到系统 | Data: " + JSON.toJSONString(alarmReqDTO));
                continue;
            }
            if (null != DataCacheTask.lineData.get(alarmReqDTO.getLine())) {
                alarmHistory.setLineId(DataCacheTask.lineData.get(alarmReqDTO.getLine()).getId());
                alarmHistory.setLineCode(alarmReqDTO.getLine());
                alarmHistory.setLineName(DataCacheTask.lineData.get(alarmReqDTO.getLine()).getName());
            } else {
                alarmAbnormalMapper.insertAlarmError(alarmReqDTO, null, "线路数据异常，未找到线路 | Data: " + JSON.toJSONString(alarmReqDTO));
                continue;
            }
            if (null != DataCacheTask.siteData.get("line:" + alarmReqDTO.getLine() + "-site:" + alarmReqDTO.getStation())) {
                alarmHistory.setSiteId(DataCacheTask.siteData.get("line:" + alarmReqDTO.getLine() + "-site:" + alarmReqDTO.getStation()).getId());
                alarmHistory.setSiteCode(alarmReqDTO.getStation());
                alarmHistory.setSiteName(DataCacheTask.siteData.get("line:" + alarmReqDTO.getLine() + "-site:" + alarmReqDTO.getStation()).getName());
            } else {
                alarmAbnormalMapper.insertAlarmError(alarmReqDTO, null, "站点数据异常，未找到站点 | Data: " + JSON.toJSONString(alarmReqDTO));
                continue;
            }
            if (null != DataCacheTask.deviceData.get("system:" + alarmHistory.getSubsystemId() + "-site:" + alarmHistory.getSiteId() + "-device:" + alarmReqDTO.getDevice())) {
                alarmHistory.setDeviceId(DataCacheTask.deviceData.get("system:" + alarmHistory.getSubsystemId() + "-site:" + alarmHistory.getSiteId() + "-device:" + alarmReqDTO.getDevice()).getId());
                alarmHistory.setDeviceCode(alarmReqDTO.getDevice());
                alarmHistory.setDeviceName(DataCacheTask.deviceData.get("system:" + alarmHistory.getSubsystemId() + "-site:" + alarmHistory.getSiteId() + "-device:" + alarmReqDTO.getDevice()).getName());
            } else {
                alarmAbnormalMapper.insertAlarmError(alarmReqDTO, null, "设备数据异常，未找到设备 | Data: " + JSON.toJSONString(alarmReqDTO));
                continue;
            }
            if (alarmReqDTO.getSlot() == 0) {
                alarmHistory.setSlotId(null);
                alarmHistory.setSlotCode(null);
                alarmHistory.setSlotName(null);
            } else if (null != DataCacheTask.deviceSlotData.get("system:" + alarmHistory.getSubsystemId() + "-site:" + alarmHistory.getSiteId() +
                    "-device:" + alarmHistory.getDeviceId() + "-slot:" + alarmReqDTO.getSlot())) {
                alarmHistory.setSlotId(DataCacheTask.deviceSlotData.get("system:" + alarmHistory.getSubsystemId() + "-site:" + alarmHistory.getSiteId() +
                        "-device:" + alarmHistory.getDeviceId() + "-slot:" + alarmReqDTO.getSlot()).getId());
                alarmHistory.setSlotCode(alarmReqDTO.getSlot());
                alarmHistory.setSlotName(DataCacheTask.deviceSlotData.get("system:" + alarmHistory.getSubsystemId() + "-site:" + alarmHistory.getSiteId() +
                        "-device:" + alarmHistory.getDeviceId() + "-slot:" + alarmReqDTO.getSlot()).getName());
            } else {
                alarmAbnormalMapper.insertAlarmError(alarmReqDTO, null, "槽位数据异常，未找到槽位 | Data: " + JSON.toJSONString(alarmReqDTO));
                continue;
            }
            if (null != DataCacheTask.alarmCodeData.get("system:" + alarmHistory.getSubsystemId() + "-site:" + alarmHistory.getLineId() + "-rule:" + alarmReqDTO.getAlarmCode())) {
                alarmHistory.setAlarmCode(DataCacheTask.alarmCodeData.get("system:" + alarmHistory.getSubsystemId() + "-site:" + alarmHistory.getLineId() + "-rule:" + alarmReqDTO.getAlarmCode()).getId());
                alarmHistory.setAlarmCodeId(alarmReqDTO.getAlarmCode());
                alarmHistory.setAlarmName(DataCacheTask.alarmCodeData.get("system:" + alarmHistory.getSubsystemId() + "-site:" + alarmHistory.getLineId() + "-rule:" + alarmReqDTO.getAlarmCode()).getName());
                alarmHistory.setAlarmReason(DataCacheTask.alarmCodeData.get("system:" + alarmHistory.getSubsystemId() + "-site:" + alarmHistory.getLineId() + "-rule:" + alarmReqDTO.getAlarmCode()).getReason());
                alarmHistory.setAlarmLevel(DataCacheTask.alarmCodeData.get("system:" + alarmHistory.getSubsystemId() + "-site:" + alarmHistory.getLineId() + "-rule:" + alarmReqDTO.getAlarmCode()).getLevelId());
            } else {
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
            // 根据告警规则获取告警状态 1-告警延迟规则;2-告警入库过滤规则;3-告警过滤规则;4-告警确认规则;5-告警清除规则;6-告警升级规则;7-告警前转规则
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
                alarmHistory.setRecoveryTime(new Timestamp(System.currentTimeMillis()));
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
                if (ruleData.get(6L) != null && !alarmReqDTO.getRecovery()) {
                    alarmHistory.setAlarmFrequency(ruleData.get(6L).getFrequency());
                    alarmHistory.setFrequencyTime(ruleData.get(6L).getFrequencyTime());
                    alarmHistory.setExperienceTime(ruleData.get(6L).getExperienceTime());
                    if (alarmHistory.getAlarmLevel() > 1 && alarmHistory.getExperienceTime() == null) {
                        frequencyAlarmHistory(alarmHistory);
                    }
                }
                if (ruleData.get(7L) != null && !alarmReqDTO.getRecovery()) {
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
                    alarmHistory.setRecoveryTime(new Timestamp(simpleDateFormat.parse(alarmReqDTO.getAlarmTime()).getTime()));
                    alarmHistory.setIsRecovery(true);
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
        String updateKey = keyPrefix + "update-frequency:" + flag;
        String lockKey = keyPrefix + "update-frequency-lock:" + flag;
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
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(out));
            log.error("告警升级失败: {}", out.toString());
        } finally {
            locker.unlock();
        }
    }
}