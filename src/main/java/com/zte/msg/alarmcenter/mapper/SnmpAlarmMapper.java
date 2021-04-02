package com.zte.msg.alarmcenter.mapper;

import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.req.AlarmHistoryReqDTO;
import com.zte.msg.alarmcenter.dto.req.SnmpAlarmCodeReqDTO;
import com.zte.msg.alarmcenter.dto.req.SnmpSlotModifyReqDTO;
import com.zte.msg.alarmcenter.dto.req.SnmpSlotReqDTO;
import com.zte.msg.alarmcenter.dto.res.SnmpAlarmCodeResDTO;
import com.zte.msg.alarmcenter.dto.res.SnmpSlotResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SnmpAlarmMapper {

    AlarmHistoryReqDTO getAlarmHistoryBySnmpName(String snmpName);

    Integer getAlarmCodeBySnmpInfo(int systemId, String emsAlarmCode, String alarmNetype, String reason);
}
