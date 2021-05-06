package com.zte.msg.alarmcenter.mapper;

import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.req.PositionReqDTO;
import com.zte.msg.alarmcenter.dto.res.PositionResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PositionMapper {

    Integer addPosition(PositionReqDTO reqDTO);

    Integer modifyPosition(@Param("id") Long id, @Param("reqDTO") PositionReqDTO reqDTO);

    Integer selectIsPositionUse(@Param("id") Long id);

    Integer deletePosition(@Param("id") Long id);

    Integer getPositionsCount();

    List<PositionResDTO> getPositions(@Param("page") Long page, @Param("size") Long size);
//
//    Integer getPositionsByIdCount(@Param("pId")Long pId);
//
//    List<PositionResDTO> getPositionsById(@Param("pId") Long pId,@Param("pageReqDTO") PageReqDTO pageReqDTO);
}
