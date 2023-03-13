package com.zte.msg.alarmcenter.mapper;

import com.zte.msg.alarmcenter.entity.SnmpAlarmCode;
import com.zte.msg.alarmcenter.entity.SnmpDeviceSlot;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Mapper
@Repository
public interface SnmpAlarmMapper {

    List<SnmpDeviceSlot> listAlarmHistoryBySnmpName();

    List<SnmpDeviceSlot> listAlarmHistoryBySnmpNameByTime(Timestamp startTime, Timestamp endTime);

    List<SnmpAlarmCode> getAlarmCodeBySnmpInfo();

    List<SnmpAlarmCode> getAlarmCodeBySnmpInfoByTime(Timestamp startTime, Timestamp endTime);
}
