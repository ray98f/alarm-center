package com.zte.msg.alarmcenter.mapper;

import com.zte.msg.alarmcenter.dto.res.AlarmAbnormalResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AlarmAbnormalMapper {


    Integer getAlarmAbnormalCount(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("systemCode") Long systemCode);

    List<AlarmAbnormalResDTO> getAlarmAbnormal(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("systemCode") Long systemCode, @Param("page") Long page, @Param("size") Long size);
}
