package com.zte.msg.alarmcenter.mapper;

import com.zte.msg.alarmcenter.dto.req.AlarmRuleReqDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmRuleDetailsResDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmRuleResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AlarmRuleMapper {

    Integer addAlarmRule(AlarmRuleReqDTO alarmRuleReqDTO);

    Integer addAlarmRuleOnSystem(@Param("ruleId") Long ruleId, @Param("systemId") Integer systemId);

    Integer addAlarmRuleOnPosition(@Param("ruleId") Long ruleId, @Param("positionId") Integer positionId);

    Integer addAlarmRuleOnDevice(@Param("ruleId") Long ruleId, @Param("deviceId") Integer deviceId);

    Integer addAlarmRuleOnAlarm(@Param("ruleId") Long ruleId, @Param("alarmId") Integer alarmId);

    Integer getAlarmRuleCount(@Param("name") String name, @Param("isEnable") Integer isEnable, @Param("type") Integer type);

    List<AlarmRuleResDTO> getAlarmRule(@Param("name") String name, @Param("isEnable") Integer isEnable, @Param("type") Integer type, @Param("page") Long page, @Param("size") Long size);

    AlarmRuleDetailsResDTO lookOverAlarmRuleDetails(String id);

    List<String> getSubsystemNameList(@Param("id") String id);

    List<String> getPositionNameList(@Param("id") String id);

    List<String> getDeviceNameList(@Param("id") String id);

    List<String> getAlarmCodeNameList(@Param("id") String id);

    Integer modifyAlarmRule(@Param("alarmRuleReqDTO") AlarmRuleReqDTO alarmRuleReqDTO, @Param("id") Long id, @Param("userId") String userId);

    Integer deleteFilter(@Param("id") Long id);
}
