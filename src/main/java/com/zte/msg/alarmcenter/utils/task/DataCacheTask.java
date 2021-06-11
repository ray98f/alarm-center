package com.zte.msg.alarmcenter.utils.task;

import com.zte.msg.alarmcenter.dto.res.AlarmRuleDataResDTO;
import com.zte.msg.alarmcenter.entity.*;
import com.zte.msg.alarmcenter.mapper.DataCacheMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author frp
 */
@Component
@Slf4j
public class DataCacheTask {
    @Autowired
    private DataCacheMapper dataCacheMapper;

    public static ConcurrentHashMap<Integer, Subsystem> subsystemData = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Integer, Position> lineData = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Position> siteData = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Device> deviceData = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, DeviceSlot> deviceSlotData = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, AlarmCode> alarmCodeData = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Long, AlarmRuleDataResDTO> alarmRuleData = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Long, AlarmHistory> frequencyAlarmHistoryData = new ConcurrentHashMap<>();

    /**
     * 数据库增量同步
     */
    @Scheduled(cron = "*/2 * * * * ? ")
    @Async
    public void cacheOne() {
        Timestamp startTime = new Timestamp(System.currentTimeMillis() - 10000);
        Timestamp endTime = new Timestamp(System.currentTimeMillis());
        List<Subsystem> subsystems;
        List<Position> linePositions;
        List<Position> sitePositions;
        List<Device> devices;
        List<DeviceSlot> deviceSlots;
        List<AlarmCode> alarmCodes;
        List<AlarmRuleDataResDTO> alarmRules;
        List<AlarmHistory> alarmHistories;
        if (subsystemData.isEmpty()) {
            subsystems = dataCacheMapper.selectSubsystem();
        } else {
            subsystems = dataCacheMapper.selectSubsystemByTime(startTime, endTime);
        }
        if (lineData.isEmpty()) {
            linePositions = dataCacheMapper.selectLinePosition();
        } else {
            linePositions = dataCacheMapper.selectLinePositionByTime(startTime, endTime);
        }
        if (siteData.isEmpty()) {
            sitePositions = dataCacheMapper.selectSitePosition();
        } else {
            sitePositions = dataCacheMapper.selectSitePositionByTime(startTime, endTime);
        }
        if (deviceData.isEmpty()) {
            devices = dataCacheMapper.selectDevice();
        } else {
            devices = dataCacheMapper.selectDeviceByTime(startTime, endTime);
        }
        if (deviceSlotData.isEmpty()) {
            deviceSlots = dataCacheMapper.selectDeviceSlot();
        } else {
            deviceSlots = dataCacheMapper.selectDeviceSlotByTime(startTime, endTime);
        }
        if (alarmCodeData.isEmpty()) {
            alarmCodes = dataCacheMapper.selectAlarmCode();
        } else {
            alarmCodes = dataCacheMapper.selectAlarmCodeByTime(startTime, endTime);
        }
        if (alarmRuleData.isEmpty()) {
            alarmRules = dataCacheMapper.selectAlarmRule();
        } else {
            alarmRules = dataCacheMapper.selectAlarmRuleByTime(startTime, endTime);
        }
        if (frequencyAlarmHistoryData.isEmpty()) {
            alarmHistories = dataCacheMapper.selectFrequencyAlarmHistory();
        } else {
            alarmHistories = dataCacheMapper.selectFrequencyAlarmHistoryByTime(startTime, endTime);
        }
        addDate(subsystems, linePositions, sitePositions, devices, deviceSlots, alarmCodes, alarmRules, alarmHistories);
    }

    private void addDate(List<Subsystem> subsystems, List<Position> linePositions, List<Position> sitePositions, List<Device> devices, List<DeviceSlot> deviceSlots,
                         List<AlarmCode> alarmCodes, List<AlarmRuleDataResDTO> alarmRules, List<AlarmHistory> alarmHistories) {
        if (subsystems != null && subsystems.size() > 0) {
            for (Subsystem subsystem : subsystems) {
                subsystemData.put(subsystem.getSid(), subsystem);
            }
        }
        if (linePositions != null && linePositions.size() > 0) {
            for (Position position : linePositions) {
                lineData.put(position.getPositionCode(), position);
            }
        }
        if (sitePositions != null && sitePositions.size() > 0) {
            for (Position position : sitePositions) {
                String key = "line:" + position.getLineCode() + "-site:" + position.getPositionCode();
                siteData.put(key, position);
            }
        }
        if (devices != null && devices.size() > 0) {
            for (Device device : devices) {
                String key = "system:" + device.getSystemId() + "-site:" + device.getStationId() + "-device:" + device.getDeviceCode();
                deviceData.put(key, device);
            }
        }
        if (deviceSlots != null && deviceSlots.size() > 0) {
            for (DeviceSlot deviceSlot : deviceSlots) {
                String key = "system:" + deviceSlot.getSystemId() + "-site:" + deviceSlot.getStationId() + "-device:" + deviceSlot.getDeviceId() + "-slot:" + deviceSlot.getSlotCode();
                deviceSlotData.put(key, deviceSlot);
            }
        }
        if (alarmCodes != null && alarmCodes.size() > 0) {
            for (AlarmCode alarmCode : alarmCodes) {
                String key = "system:" + alarmCode.getSystemId() + "-site:" + alarmCode.getPositionId() + "-rule:" + alarmCode.getCode();
                alarmCodeData.put(key, alarmCode);
            }
        }
        if (alarmRules != null && alarmRules.size() > 0) {
            for (AlarmRuleDataResDTO alarmCode : alarmRules) {
                alarmRuleData.put(alarmCode.getId(), alarmCode);
            }
        }
        if (alarmHistories != null && alarmHistories.size() > 0) {
            for (AlarmHistory alarmHistory : alarmHistories) {
                frequencyAlarmHistoryData.put(alarmHistory.getId(), alarmHistory);
            }
        }
    }

    /**
     * 数据库全量同步
     */
    @Scheduled(cron = "0 0 6 * * ?")
    @Async
    public void cacheAll() {
        log.info("---------- 全量缓存开始 ----------");
        List<Subsystem> subsystemList = dataCacheMapper.selectSubsystem();
        List<Position> lineList = dataCacheMapper.selectLinePosition();
        List<Position> siteList = dataCacheMapper.selectSitePosition();
        List<Device> deviceList = dataCacheMapper.selectDevice();
        List<DeviceSlot> deviceSlotList = dataCacheMapper.selectDeviceSlot();
        List<AlarmCode> alarmCodeList = dataCacheMapper.selectAlarmCode();
        List<AlarmRuleDataResDTO> alarmRuleDetailsResDTOList = dataCacheMapper.selectAlarmRule();
        List<AlarmHistory> alarmHistoryList = dataCacheMapper.selectFrequencyAlarmHistory();
        subsystemData = new ConcurrentHashMap<>();
        lineData = new ConcurrentHashMap<>();
        siteData = new ConcurrentHashMap<>();
        deviceData = new ConcurrentHashMap<>();
        deviceSlotData = new ConcurrentHashMap<>();
        alarmCodeData = new ConcurrentHashMap<>();
        alarmRuleData = new ConcurrentHashMap<>();
        frequencyAlarmHistoryData = new ConcurrentHashMap<>();
        for (Subsystem subsystem : subsystemList) {
            subsystemData.put(subsystem.getSid(), subsystem);
        }
        for (Position position : lineList) {
            lineData.put(position.getPositionCode(), position);
        }
        for (Position position : siteList) {
            String key = "line:" + position.getLineCode() + "-site:" + position.getPositionCode();
            siteData.put(key, position);
        }
        for (Device device : deviceList) {
            String key = "system:" + device.getSystemId() + "-site:" + device.getStationId() + "-device:" + device.getDeviceCode();
            deviceData.put(key, device);
        }
        for (DeviceSlot deviceSlot : deviceSlotList) {
            String key = "system:" + deviceSlot.getSystemId() + "-site:" + deviceSlot.getStationId() + "-device:" + deviceSlot.getDeviceId() + "-slot:" + deviceSlot.getSlotCode();
            deviceSlotData.put(key, deviceSlot);
        }
        for (AlarmCode alarmCode : alarmCodeList) {
            String key = "system:" + alarmCode.getSystemId() + "-site:" + alarmCode.getPositionId() + "-rule:" + alarmCode.getCode();
            alarmCodeData.put(key, alarmCode);
        }
        for (AlarmRuleDataResDTO alarmRuleDetailsResDTO : alarmRuleDetailsResDTOList) {
            alarmRuleData.put(alarmRuleDetailsResDTO.getId(), alarmRuleDetailsResDTO);
        }
        for (AlarmHistory alarmHistory : alarmHistoryList) {
            frequencyAlarmHistoryData.put(alarmHistory.getId(), alarmHistory);
        }
    }
}
