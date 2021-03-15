package com.zte.msg.alarmcenter.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.req.AlarmRuleReqDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmRuleDetailsResDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmRuleResDTO;
import com.zte.msg.alarmcenter.dto.res.ChildSystemConfigResDTO;
import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.mapper.AlarmRuleMapper;
import com.zte.msg.alarmcenter.service.AlarmRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


@Service
public class AlarmRuleServiceImpl implements AlarmRuleService {

    @Autowired
    private AlarmRuleMapper alarmRuleMapper;

    @Override
    public void addAlarmRule(AlarmRuleReqDTO alarmRuleReqDTO, String userId) {

        int systemIdSize = alarmRuleReqDTO.getSystemIds().size();
        int positionIdSize = alarmRuleReqDTO.getPositionIds().size();
        int deviceIdSize = alarmRuleReqDTO.getDeviceIds().size();
        int alarmIdSize = alarmRuleReqDTO.getAlarmIds().size();
        alarmRuleReqDTO.setSystemId(systemIdSize == 0 ? 0 : 1);
        alarmRuleReqDTO.setPositionId(positionIdSize == 0 ? 0 : 1);
        alarmRuleReqDTO.setDeviceId(deviceIdSize == 0 ? 0 : 1);
        alarmRuleReqDTO.setAlarmId(alarmIdSize == 0 ? 0 : 1);
        alarmRuleReqDTO.setUserId(userId);
        int integer = alarmRuleMapper.addAlarmRule(alarmRuleReqDTO);
        if (integer == 0) {
            throw new CommonException(4000, "告警码规则新增失败");
        }
        int[] ints = {systemIdSize, positionIdSize, deviceIdSize, alarmIdSize};
        Arrays.sort(ints);
        int size = ints[ints.length - 1];
        for (int i = 0; i < size; i++) {
            Integer systemId = 0;
            Integer positionId = 0;
            Integer deviceId = 0;
            Integer alarmId = 0;
            if (systemIdSize > i) {
                systemId = alarmRuleReqDTO.getSystemIds().get(i);
            }
            if (positionIdSize > i) {
                positionId = alarmRuleReqDTO.getPositionIds().get(i);
            }
            if (deviceIdSize > i) {
                deviceId = alarmRuleReqDTO.getDeviceIds().get(i);
            }
            if (alarmIdSize > i) {
                alarmId = alarmRuleReqDTO.getAlarmIds().get(i);
            }
            if (systemId > 0) {
                int sysCount = alarmRuleMapper.addAlarmRuleOnSystem(alarmRuleReqDTO.getId(), alarmRuleReqDTO.getSystemIds().get(i));
                if (sysCount == 0) {
                    throw new CommonException(4000, "系统过滤新增失败");
                }
            }
            if (positionId > 0) {
                int positionCount = alarmRuleMapper.addAlarmRuleOnPosition(alarmRuleReqDTO.getId(), alarmRuleReqDTO.getPositionIds().get(i));
                if (positionCount == 0) {
                    throw new CommonException(4000, "位置过滤新增失败");
                }
            }
            if (deviceId > 0) {
                int deviceCount = alarmRuleMapper.addAlarmRuleOnDevice(alarmRuleReqDTO.getId(), alarmRuleReqDTO.getDeviceIds().get(i));
                if (deviceCount == 0) {
                    throw new CommonException(4000, "设备过滤新增失败");
                }
            }
            if (alarmId > 0) {
                int alarmCount = alarmRuleMapper.addAlarmRuleOnAlarm(alarmRuleReqDTO.getId(), alarmRuleReqDTO.getAlarmIds().get(i));
                if (alarmCount == 0) {
                    throw new CommonException(4000, "告警过滤新增失败");
                }
            }
        }
    }

    @Override
    public Page<AlarmRuleResDTO> getAlarmRule(String name, Integer isEnable, Integer type, Long page, Long size) {
        List<AlarmRuleResDTO> childSystemList = null;
        int count = alarmRuleMapper.getAlarmRuleCount(name, isEnable, type);
        Page<AlarmRuleResDTO> pageBean = new Page<>();
        pageBean.setCurrent(page).setPages(size).setTotal(count);
        if (count > 0) {
            page = (page - 1) * size;
            childSystemList = alarmRuleMapper.getAlarmRule(name, isEnable, type, page, size);
            pageBean.setRecords(childSystemList);
        }
        return pageBean;
    }

    @Override
    public void modifyAlarmRule(AlarmRuleReqDTO alarmRuleReqDTO, Long id, String userId) {
        alarmRuleReqDTO.setSystemId(alarmRuleReqDTO.getSystemIds().size() == 0 ? 0 : 1);
        alarmRuleReqDTO.setPositionId(alarmRuleReqDTO.getPositionIds().size() == 0 ? 0 : 1);
        alarmRuleReqDTO.setDeviceId(alarmRuleReqDTO.getDeviceIds().size() == 0 ? 0 : 1);
        alarmRuleReqDTO.setAlarmId(alarmRuleReqDTO.getAlarmIds().size() == 0 ? 0 : 1);
        int modifyCount = alarmRuleMapper.modifyAlarmRule(alarmRuleReqDTO, id, userId);
        if (modifyCount == 0) {
            throw new CommonException(4000, "编辑失败！");
        }
        //todo 根据id多表删除 根据id多表新增
        int delFilterCount = alarmRuleMapper.deleteFilter(id);
        int systemIdSize = alarmRuleReqDTO.getSystemIds().size();
        int positionIdSize = alarmRuleReqDTO.getPositionIds().size();
        int deviceIdSize = alarmRuleReqDTO.getDeviceIds().size();
        int alarmIdSize = alarmRuleReqDTO.getAlarmIds().size();
        int[] ints = {systemIdSize, positionIdSize, deviceIdSize, alarmIdSize};
        Arrays.sort(ints);
        int size = ints[ints.length - 1];
        for (int i = 0; i < size; i++) {
            Integer systemId = 0;
            Integer positionId = 0;
            Integer deviceId = 0;
            Integer alarmId = 0;
            if (systemIdSize > i) {
                systemId = alarmRuleReqDTO.getSystemIds().get(i);
            }
            if (positionIdSize > i) {
                positionId = alarmRuleReqDTO.getPositionIds().get(i);
            }
            if (deviceIdSize > i) {
                deviceId = alarmRuleReqDTO.getDeviceIds().get(i);
            }
            if (alarmIdSize > i) {
                alarmId = alarmRuleReqDTO.getAlarmIds().get(i);
            }
            if (systemId > 0) {
                int sysCount = alarmRuleMapper.addAlarmRuleOnSystem(id, alarmRuleReqDTO.getSystemIds().get(i));
                if (sysCount == 0) {
                    throw new CommonException(4000, "系统过滤新增失败");
                }
            }
            if (positionId > 0) {
                int positionCount = alarmRuleMapper.addAlarmRuleOnPosition(id, alarmRuleReqDTO.getPositionIds().get(i));
                if (positionCount == 0) {
                    throw new CommonException(4000, "位置过滤新增失败");
                }
            }
            if (deviceId > 0) {
                int deviceCount = alarmRuleMapper.addAlarmRuleOnDevice(id, alarmRuleReqDTO.getDeviceIds().get(i));
                if (deviceCount == 0) {
                    throw new CommonException(4000, "设备过滤新增失败");
                }
            }
            if (alarmId > 0) {
                int alarmCount = alarmRuleMapper.addAlarmRuleOnAlarm(id, alarmRuleReqDTO.getAlarmIds().get(i));
                if (alarmCount == 0) {
                    throw new CommonException(4000, "告警过滤新增失败");
                }
            }
        }
    }

    @Override
    public AlarmRuleDetailsResDTO lookOverAlarmRuleDetails(String id) {
        AlarmRuleDetailsResDTO alarmRule = alarmRuleMapper.lookOverAlarmRuleDetails(id);
        alarmRule.setSystemIds(alarmRuleMapper.getSubsystemById(id));
        alarmRule.setPositionIds(alarmRuleMapper.getPositionById(id));
        alarmRule.setDeviceIds(alarmRuleMapper.getDeviceById(id));
        alarmRule.setAlarmIds(alarmRuleMapper.getAlarmCodeById(id));
        return alarmRule;
    }
}
