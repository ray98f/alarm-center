package com.zte.msg.alarmcenter.mapper;

import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.req.DeviceSlotReqDTO;
import com.zte.msg.alarmcenter.dto.res.DeviceSlotResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DeviceSlotMapper {

    List<DeviceSlotResDTO> exportDevice(@Param("slotName") String slotName, @Param("deviceName") String deviceName,
                                        @Param("deviceCode") String deviceCode, @Param("systemId") Long systemId,
                                        @Param("positionId") Long positionId, @Param("page") Long page, @Param("size") Long size);

    Integer deleteDevice(@Param("id") Long id);

    Integer importDevice(@Param("list") List<DeviceSlotReqDTO> list, @Param("userId") String userId);

    Integer importOneDevice(@Param("device") DeviceSlotReqDTO device, @Param("userId") String userId);

    Integer addDeviceSlot(@Param("deviceSlotReqDTO") DeviceSlotReqDTO deviceSlotReqDTO, @Param("userId") String userId);

    Integer modifyDevice(@Param("id") Long id, @Param("deviceSlotReqDTO") DeviceSlotReqDTO deviceSlotReqDTO, @Param("userId") String userId);

    Integer getDevicesSlotCount(@Param("slotName") String slotName, @Param("deviceName") String deviceName,
                                @Param("deviceCode") String deviceCode, @Param("systemId") Long systemId,
                                @Param("positionId") Long positionId);

}
