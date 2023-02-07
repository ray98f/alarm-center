package com.zte.msg.alarmcenter.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.req.AlarmHistoryReqDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmAbnormalResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Mapper
@Repository
public interface AlarmAbnormalMapper {

    Integer insertAlarmError(AlarmHistoryReqDTO alarmHistoryReqDTO, String ip, String error);

    Integer getAlarmAbnormalCount(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("systemCode") Long systemCode);

    Page<AlarmAbnormalResDTO> getAlarmAbnormal(Page<AlarmAbnormalResDTO> page, Timestamp startTime, Timestamp endTime, Long systemCode);
}
