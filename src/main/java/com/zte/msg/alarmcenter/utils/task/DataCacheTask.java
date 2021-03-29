package com.zte.msg.alarmcenter.utils.task;

import com.zte.msg.alarmcenter.dto.res.AlarmRuleDataResDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmRuleDetailsResDTO;
import com.zte.msg.alarmcenter.entity.*;
import com.zte.msg.alarmcenter.mapper.DataCacheMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public static ConcurrentHashMap<Long, Position> positionData = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Long, Device> deviceData = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Long, DeviceSlot> deviceSlotData = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Long, AlarmCode> alarmCodeData = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Long, AlarmRuleDataResDTO> alarmRuleData = new ConcurrentHashMap<>();

    @Scheduled(cron = "0 */1 * * * ? ")
    public void cacheOne() {
        Timestamp startTime = new Timestamp(System.currentTimeMillis() - 60000);
        Timestamp endTime = new Timestamp(System.currentTimeMillis());
        List<Subsystem> subsystems;
        List<Position> positions;
        List<Device> devices;
        List<DeviceSlot> deviceSlots;
        List<AlarmCode> alarmCodes;
        List<AlarmRuleDataResDTO> alarmRules;
        if (subsystemData.isEmpty()) {
            subsystems = dataCacheMapper.selectSubsystem();
            log.info("---------- 系统全量缓存开始 ----------");
        } else {
            subsystems = dataCacheMapper.selectSubsystemByTime(startTime, endTime);
            log.info("---------- 系统增量缓存开始 ----------");
        }
        if (positionData.isEmpty()) {
            positions = dataCacheMapper.selectPosition();
            log.info("---------- 系统全量缓存开始 ----------");
        } else {
            positions = dataCacheMapper.selectPositionByTime(startTime, endTime);
            log.info("---------- 系统增量缓存开始 ----------");
        }
        if (deviceData.isEmpty()) {
            devices = dataCacheMapper.selectDevice();
            log.info("---------- 设备全量缓存开始 ----------");
        } else {
            devices = dataCacheMapper.selectDeviceByTime(startTime, endTime);
            log.info("---------- 设备增量缓存开始 ----------");
        }
        if (deviceSlotData.isEmpty()) {
            deviceSlots = dataCacheMapper.selectDeviceSlot();
            log.info("---------- 设备槽位全量缓存开始 ----------");
        } else {
            deviceSlots = dataCacheMapper.selectDeviceSlotByTime(startTime, endTime);
            log.info("---------- 设备槽位增量缓存开始 ----------");
        }
        if (alarmCodeData.isEmpty()) {
            alarmCodes = dataCacheMapper.selectAlarmCode();
            log.info("---------- 告警码全量缓存开始 ----------");
        } else {
            alarmCodes = dataCacheMapper.selectAlarmCodeByTime(startTime, endTime);
            log.info("---------- 告警码增量缓存开始 ----------");
        }
        if (alarmRuleData.isEmpty()) {
            alarmRules = dataCacheMapper.selectAlarmRule();
            log.info("---------- 告警规则全量缓存开始 ----------");
        } else {
            alarmRules = dataCacheMapper.selectAlarmRuleByTime(startTime, endTime);
            log.info("---------- 告警规则增量缓存开始 ----------");
        }
        addDate(subsystems, positions, devices, deviceSlots, alarmCodes, alarmRules);
    }

    private void addDate(List<Subsystem> subsystems, List<Position> positions, List<Device> devices, List<DeviceSlot> deviceSlots, List<AlarmCode> alarmCodes, List<AlarmRuleDataResDTO> alarmRules) {
        if (subsystems != null && subsystems.size() > 0) {
            for (Subsystem subsystem : subsystems) {
                subsystemData.put(subsystem.getSid(), subsystem);
            }
        }
        if (positions != null && positions.size() > 0) {
            for (Position position : positions) {
                positionData.put(position.getId(), position);
            }
        }
        if (devices != null && devices.size() > 0) {
            for (Device device : devices) {
                deviceData.put(device.getId(), device);
            }
        }
        if (deviceSlots != null && deviceSlots.size() > 0) {
            for (DeviceSlot deviceSlot : deviceSlots) {
                deviceSlotData.put(deviceSlot.getId(), deviceSlot);
            }
        }
        if (alarmCodes != null && alarmCodes.size() > 0) {
            for (AlarmCode alarmCode : alarmCodes) {
                alarmCodeData.put(alarmCode.getId(), alarmCode);
            }
        }
        if (alarmRules != null && alarmRules.size() > 0) {
            for (AlarmRuleDataResDTO alarmCode : alarmRules) {
                alarmRuleData.put(alarmCode.getId(), alarmCode);
            }
        }
        log.info("------------ 缓存结束 ------------");
    }

    @Scheduled(cron = "0 0 6 * * ?")
    public void cacheAll() {
        log.info("---------- 全量缓存开始 ----------");
        List<Subsystem> subsystemList = dataCacheMapper.selectSubsystem();
        List<Position> positionList = dataCacheMapper.selectPosition();
        List<Device> deviceList = dataCacheMapper.selectDevice();
        List<DeviceSlot> deviceSlotList = dataCacheMapper.selectDeviceSlot();
        List<AlarmCode> alarmCodeList = dataCacheMapper.selectAlarmCode();
        List<AlarmRuleDataResDTO> alarmRuleDetailsResDTOList = dataCacheMapper.selectAlarmRule();
        subsystemData = new ConcurrentHashMap<>();
        positionData = new ConcurrentHashMap<>();
        deviceData = new ConcurrentHashMap<>();
        deviceSlotData = new ConcurrentHashMap<>();
        alarmCodeData = new ConcurrentHashMap<>();
        alarmRuleData = new ConcurrentHashMap<>();
        for (Subsystem subsystem : subsystemList) {
            subsystemData.put(subsystem.getSid(), subsystem);
        }
        for (Position position : positionList) {
            positionData.put(position.getId(), position);
        }
        for (Device device : deviceList) {
            deviceData.put(device.getId(), device);
        }
        for (DeviceSlot deviceSlot : deviceSlotList) {
            deviceSlotData.put(deviceSlot.getId(), deviceSlot);
        }
        for (AlarmCode alarmCode : alarmCodeList) {
            alarmCodeData.put(alarmCode.getId(), alarmCode);
        }
        for (AlarmRuleDataResDTO alarmRuleDetailsResDTO : alarmRuleDetailsResDTOList) {
            alarmRuleData.put(alarmRuleDetailsResDTO.getId(), alarmRuleDetailsResDTO);
        }
    }
}
