package com.zte.msg.alarmcenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.req.PositionReqDTO;
import com.zte.msg.alarmcenter.dto.res.PositionResDTO;

public interface PositionService {

    void addPosition(PositionReqDTO reqDTO);

    void modifyPosition(Long id, PositionReqDTO reqDTO);

    void deletePosition(Long id);

    Page<PositionResDTO> getPositions(Long page,Long size);

//    Page<PositionResDTO> getPositionsById(Long pId, PageReqDTO pageReqDTO);
}
