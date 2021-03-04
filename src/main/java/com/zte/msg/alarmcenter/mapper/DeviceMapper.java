package com.zte.msg.alarmcenter.mapper;

import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.req.DeviceReqDTO;
import com.zte.msg.alarmcenter.dto.req.DeviceReqModifyDTO;
import com.zte.msg.alarmcenter.dto.res.DeviceResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DeviceMapper {

    List<DeviceResDTO> exportDevice(@Param("name") String name, @Param("deviceCode") String deviceCode,
                                    @Param("systemId") Long systemId, @Param("positionId") Long positionId, @Param("pageReq") PageReqDTO pageReq);

    Integer importDevice(@Param("list") List<DeviceReqDTO> list, @Param("userId") String userId);

    Integer modifyDevice(@Param("reqModifyDTO") DeviceReqModifyDTO reqModifyDTO, @Param("id") Long id, @Param("userId") String userId);

    int getDevicesCount(@Param("name") String name, @Param("deviceCode") String deviceCode, @Param("systemId") Long systemId, @Param("positionId") Long positionId);

    Integer deleteDevice(@Param("id") Long id);
}