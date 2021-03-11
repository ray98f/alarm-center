package com.zte.msg.alarmcenter.mapper;

import com.zte.msg.alarmcenter.dto.req.AlarmLevelReqDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmLevelResDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmRuleResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AlarmLevelMapper {

    Integer getAlarmLevelCount();

    List<AlarmLevelResDTO> getAlarmLevelList(@Param("page") Long page, @Param("size") Long size);

    int modifyAlarmLevel(@Param("id") Long id, @Param("alarmLevelReqDTO") AlarmLevelReqDTO alarmLevelReqDTO, @Param("userId") String userId);
}
