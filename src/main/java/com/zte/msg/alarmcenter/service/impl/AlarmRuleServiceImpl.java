package com.zte.msg.alarmcenter.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.req.AlarmRuleDeviceReqDTO;
import com.zte.msg.alarmcenter.dto.req.AlarmRuleReqDTO;
import com.zte.msg.alarmcenter.dto.res.*;
import com.zte.msg.alarmcenter.entity.MsgConfig;
import com.zte.msg.alarmcenter.enums.ErrorCode;
import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.mapper.AlarmRuleMapper;
import com.zte.msg.alarmcenter.service.AlarmRuleService;
import com.zte.msg.alarmcenter.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class AlarmRuleServiceImpl implements AlarmRuleService {

    @Autowired
    private AlarmRuleMapper alarmRuleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAlarmRule(AlarmRuleReqDTO alarmRuleReqDTO, String userId) {
        int result = alarmRuleMapper.selectRuleExist(alarmRuleReqDTO,null);
        if (result > 0) {
            throw new CommonException(ErrorCode.RULE_EXIST);
        }
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
            throw new CommonException(ErrorCode.INSERT_ERROR);
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
                    throw new CommonException(ErrorCode.INSERT_ERROR);
                }
            }
            if (positionId > 0) {
                int positionCount = alarmRuleMapper.addAlarmRuleOnPosition(alarmRuleReqDTO.getId(), alarmRuleReqDTO.getPositionIds().get(i));
                if (positionCount == 0) {
                    throw new CommonException(ErrorCode.INSERT_ERROR);
                }
            }
            if (deviceId > 0) {
                int deviceCount = alarmRuleMapper.addAlarmRuleOnDevice(alarmRuleReqDTO.getId(), alarmRuleReqDTO.getDeviceIds().get(i));
                if (deviceCount == 0) {
                    throw new CommonException(ErrorCode.INSERT_ERROR);
                }
            }
            if (alarmId > 0) {
                int alarmCount = alarmRuleMapper.addAlarmRuleOnAlarm(alarmRuleReqDTO.getId(), alarmRuleReqDTO.getAlarmIds().get(i));
                if (alarmCount == 0) {
                    throw new CommonException(ErrorCode.INSERT_ERROR);
                }
            }
        }
    }

    @Override
    public Page<AlarmRuleResDTO> getAlarmRule(String name, Integer isEnable, Integer type, Long page, Long size) {
        List<AlarmRuleResDTO> childSystemList = null;
        if (name.contains(Constants.PERCENT_SIGN)) {
            name = "Prohibit input";
        }
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
    @Transactional(rollbackFor = Exception.class)
    public void modifyAlarmRule(AlarmRuleReqDTO alarmRuleReqDTO, Long id, String userId) {
        int result = alarmRuleMapper.selectRuleExist(alarmRuleReqDTO,id);
        if (result > 0) {
            throw new CommonException(ErrorCode.RULE_EXIST);
        }
        alarmRuleReqDTO.setSystemId(alarmRuleReqDTO.getSystemIds().size() == 0 ? 0 : 1);
        alarmRuleReqDTO.setPositionId(alarmRuleReqDTO.getPositionIds().size() == 0 ? 0 : 1);
        alarmRuleReqDTO.setDeviceId(alarmRuleReqDTO.getDeviceIds().size() == 0 ? 0 : 1);
        alarmRuleReqDTO.setAlarmId(alarmRuleReqDTO.getAlarmIds().size() == 0 ? 0 : 1);
        int modifyCount = alarmRuleMapper.modifyAlarmRule(alarmRuleReqDTO, id, userId);
        if (modifyCount == 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
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
                    throw new CommonException(ErrorCode.INSERT_ERROR);
                }
            }
            if (positionId > 0) {
                int positionCount = alarmRuleMapper.addAlarmRuleOnPosition(id, alarmRuleReqDTO.getPositionIds().get(i));
                if (positionCount == 0) {
                    throw new CommonException(ErrorCode.INSERT_ERROR);
                }
            }
            if (deviceId > 0) {
                int deviceCount = alarmRuleMapper.addAlarmRuleOnDevice(id, alarmRuleReqDTO.getDeviceIds().get(i));
                if (deviceCount == 0) {
                    throw new CommonException(ErrorCode.INSERT_ERROR);
                }
            }
            if (alarmId > 0) {
                int alarmCount = alarmRuleMapper.addAlarmRuleOnAlarm(id, alarmRuleReqDTO.getAlarmIds().get(i));
                if (alarmCount == 0) {
                    throw new CommonException(ErrorCode.INSERT_ERROR);
                }
            }
        }
    }

    @Override
    public AlarmRuleDetailsResDTO lookOverAlarmRuleDetails(String id) {
        AlarmRuleDetailsResDTO alarmRule = alarmRuleMapper.lookOverAlarmRuleDetails(id);
        alarmRule.setSystemIds(alarmRuleMapper.getSubsystemNameList(id));
        alarmRule.setPositionIds(alarmRuleMapper.getPositionNameList(id));
        alarmRule.setDeviceIds(alarmRuleMapper.getDeviceNameList(id));
        alarmRule.setAlarmIds(alarmRuleMapper.getAlarmCodeNameList(id));
        return alarmRule;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAlarmRule(Long id) {
        int alarmDelCount = alarmRuleMapper.deleteAlarmRule(id);
        if (alarmDelCount == 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    /**
     * 获取设备下拉列表
     *
     * @param alarmRuleDeviceReqDTO
     * @return
     */
    @Override
    public List<DeviceResDTO> getDevices(AlarmRuleDeviceReqDTO alarmRuleDeviceReqDTO) {
        List<DeviceResDTO> deviceResDTOList = alarmRuleMapper.getDevices(alarmRuleDeviceReqDTO);
        if (null == deviceResDTOList || deviceResDTOList.isEmpty()) {
            return null;
        }
        return deviceResDTOList;
    }

    /**
     * 获取告警码下拉列表
     *
     * @param systemIds
     * @return
     */
    @Override
    public List<AlarmCodeResDTO> getAlarmCodes(List<Long> systemIds) {
        List<AlarmCodeResDTO> alarmCodeResDTOList = alarmRuleMapper.getAlarmCodes(systemIds);
        if (null == alarmCodeResDTOList || alarmCodeResDTOList.isEmpty()) {
            return null;
        }
        return alarmCodeResDTOList;
    }

    /**
     * 获取前转消息推送配置列表
     *
     * @return
     */
    @Override
    public List<MsgConfig> getMsgConfigs() {
        List<MsgConfig> msgConfigs = alarmRuleMapper.getMsgConfigs();
        if (null == msgConfigs || msgConfigs.isEmpty()) {
            return null;
        }
        return msgConfigs;
    }
}
