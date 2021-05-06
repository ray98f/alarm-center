package com.zte.msg.alarmcenter.mapper;

import com.zte.msg.alarmcenter.dto.req.AlarmCodeReqDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmCodeResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AlarmCodeMapper {

    Long selectAlarmLevelId(String name);

    Integer importAlarmCode(@Param("list") List<AlarmCodeReqDTO> list, @Param("userId") String userId);

    List<AlarmCodeResDTO> exportAlarmCode(@Param("alarmCode") Long alarmCode, @Param("alarmName") String alarmName, @Param("systemId") Long systemId, @Param("alarmLevelId") Long alarmLevelId, @Param("page") Long page, @Param("size") Long size);

    Integer modifyAlarmCode(@Param("alarmCodeReqDTO") AlarmCodeReqDTO alarmCodeReqDTO, @Param("id") Long id, @Param("userId") String userId);

    Integer selectIsAlarmCodeUse(@Param("id") Long id);

    Integer deleteAlarmCode(@Param("id") Long id);

    Integer getAlarmCodeCount(@Param("alarmCode") Long alarmCode, @Param("alarmName") String alarmName, @Param("systemId") Long systemId, @Param("alarmLevelId") Long alarmLevelId);

    Long selectAlarmCodeIsExist(AlarmCodeReqDTO alarmCodeReqDTO,Long id);

    Integer addAlarmCode(@Param("alarmCodeReqDTO") AlarmCodeReqDTO alarmCodeReqDTO, @Param("userId") String userId);
}
