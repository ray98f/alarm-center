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
//        log.info("------ 开始接收同步对象 ------");
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
//        log.info("------ 接收同步对象结束 ------");
    }

    @RabbitListener(queues = RabbitMqConfig.ALARM_QUEUE)
    @RabbitHandler
    @Async
    public void alarmProcess(String json) throws ParseException {
//        System.out.println(System.currentTimeMillis());
        JSONArray jsonArray = JSONArray.parseArray(json);
        List<AlarmHistoryReqDTO> alarmReqDTO = jsonArray.toJavaList(AlarmHistoryReqDTO.class);
        if (null == alarmReqDTO || alarmReqDTO.isEmpty()) {
            log.warn("消息队列中信息为空");
            return;
        }
        List<AlarmHistory> alarmHistories = conversionAndFilter(alarmReqDTO);
//        System.out.println(System.currentTimeMillis());
        editData(alarmHistories);
//        System.out.println(System.currentTimeMillis());
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
        List<AlarmHistory> alarmHistories = conversionAndFilter(alarmReqDTOList);
        syncData(alarmHistories);
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
//        log.info("------ 开始接收告警记录 ------");
        List<AlarmHistoryReqDTO> alarmReqDTOList = transferSnmpToAlarmHistory(snmpAlarmDTOS);
        List<AlarmHistory> alarmHistories = conversionAndFilter(alarmReqDTOList);
        editData(alarmHistories);
//        log.info("------ 接收告警记录结束 ------");
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
//        log.info("------ 开始接收告警记录 ------");
        List<AlarmHistoryReqDTO> alarmReqDTOList = transferSnmpToAlarmHistory(snmpAlarmDTOS);
        List<AlarmHistory> alarmHistories = conversionAndFilter(alarmReqDTOList);
        syncData(alarmHistories);
//        log.info("------ 接收告警记录结束 ------");
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
//        log.info("------ 开始接收系统心跳 ------");
        childSystemMapper.isOnline(heartbeatQueueReqDTOList);
//        log.info("------ 接收系统心跳结束 ------");
    }

    private void editData(List<AlarmHistory> alarmHistories) {
        if (null == alarmHistories || alarmHistories.isEmpty()) {
            log.error("告警记录接收为空");
            return;
        }
        int result = alarmManageMapper.editAlarmHistory(alarmHistories);
//        if (result >= 0) {
//            for (AlarmHistory alarmHistory : alarmHistories) {
//                if (null != alarmHistory.getAlarmMessageList() && !alarmHistory.getAlarmMessageList().isEmpty()) {
//                    result = alarmManageMapper.editAlarmMessage(alarmHistory);
//                    if (result < 0) {
//                        log.error("告警记录添加附加信息失败");
//
//                    }
//                }
//            }
//        }
    }

    private void syncData(List<AlarmHistory> alarmHistories) {
        if (null == alarmHistories || alarmHistories.isEmpty()) {
            log.error("告警记录接收为空");
            return;
        }
        List<AlarmHistoryResDTO> alarmHistoryResDTOList = homeMapper.selectAlarmHistory(null);
        for (AlarmHistory alarmHistory : alarmHistories) {
            alarmHistoryResDTOList.removeIf(alarmHistoryResDTO -> alarmHistory.getSubsystemCode().equals(alarmHistoryResDTO.getSubsystemCode()) &&
                    alarmHistory.getLineCode().equals(alarmHistoryResDTO.getLineCode()) &&
                    alarmHistory.getSiteCode().equals(alarmHistoryResDTO.getSiteCode()) &&
                    alarmHistory.getDeviceCode().equals(alarmHistoryResDTO.getDeviceCode()) &&
                    alarmHistory.getSlotCode().equals(alarmHistoryResDTO.getSlotPositionCode()) &&
                    alarmHistory.getAlarmCodeId().toString().equals(alarmHistoryResDTO.getAlarmCode()));
        }
        if (alarmHistoryResDTOList.size() > 0) {
            alarmManageMapper.updateSyncAlarmHistory(alarmHistoryResDTOList);
        }
        int result = alarmManageMapper.syncAlarmHistory(alarmHistories);
//        if (result >= 0) {
//            for (AlarmHistory alarmHistory : alarmHistories) {
//                Long alarmId = alarmManageMapper.getAlarmHistoryId(alarmHistory);
//                result = alarmManageMapper.syncAlarmMessage(alarmId, alarmHistory.getAlarmMessageList());
//                if (result < 0) {
//                    log.error("ID为{}的告警记录同步附加信息失败", alarmId);
//
//                }
//            }
//        }
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
            if (null != DataCacheTask.deviceSlotData.get("system:" + alarmHistory.getSubsystemId() + "-site:" + alarmHistory.getSiteId() +
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