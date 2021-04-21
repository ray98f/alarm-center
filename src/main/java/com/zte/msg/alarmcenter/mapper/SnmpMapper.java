package com.zte.msg.alarmcenter.mapper;

import com.zte.msg.alarmcenter.dto.PageReqDTO;
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
public interface SnmpMapper {

    Integer importDevice(@Param("list") List<SnmpSlotReqDTO> list, @Param("userId") String userId);

    List<SnmpSlotResDTO> exportDevice(@Param("snmpSlotName") String snmpSlotName, @Param("systemId") Long systemId,
                                      @Param("siteId") Long siteId, @Param("pageReq") PageReqDTO pageReq);

    Long selectSnmpSlotIsExist(SnmpSlotModifyReqDTO slotModifyReqDTO);

    Integer addSnmpSlot(@Param("slotModifyReqDTO") SnmpSlotModifyReqDTO slotModifyReqDTO, @Param("userId") String userId);

    Integer modifySnmpSlot(@Param("slotModifyReqDTO") SnmpSlotModifyReqDTO slotModifyReqDTO, @Param("id") Long id, @Param("userId") String userId);

    Integer deleteSnmpSlot(@Param("id") Long id);

    Integer getSnmpSlotCount(@Param("snmpSlotName") String snmpSlotName, @Param("systemId") Long systemId, @Param("siteId") Long siteId);

    List<SnmpSlotResDTO> getSnmpSlot(@Param("snmpSlotName") String snmpSlotName, @Param("systemId") Long systemId, @Param("siteId") Long siteId, @Param("page") Long page, @Param("size") Long size);

    Integer importSnmpAlarmCode(@Param("list") List<SnmpAlarmCodeReqDTO> list, @Param("userId") String userId);

    List<SnmpAlarmCodeResDTO> exportSnmpAlarmCode(@Param("alarmCode") String alarmCode, @Param("systemId") Long systemId);

    Long selectSnmpAlarmCodeIsExist(SnmpAlarmCodeReqDTO snmpAlarmCode);

    Integer addSSnmpAlarmCode(@Param("snmpAlarmCode") SnmpAlarmCodeReqDTO snmpAlarmCode, @Param("userId") String userId);

    Integer modifySnmpAlarmCode(@Param("snmpAlarmCode") SnmpAlarmCodeReqDTO snmpAlarmCode, @Param("id") Long id, @Param("userId") String userId);

    Integer deleteSnmpAlarmCode(@Param("id") Long id);

    int getSnmpAlarmCodeCount(@Param("code") String code, @Param("systemId") Long systemId);

    List<SnmpAlarmCodeResDTO> getSnmpAlarmCode(@Param("code") String code, @Param("systemId") Long systemId, @Param("page") Long page, @Param("size") Long size);
}
