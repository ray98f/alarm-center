package com.zte.msg.alarmcenter.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.req.PositionReqDTO;
import com.zte.msg.alarmcenter.dto.res.PositionResDTO;
import com.zte.msg.alarmcenter.enums.ErrorCode;
import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.mapper.PositionMapper;
import com.zte.msg.alarmcenter.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PositionServiceImpl implements PositionService {

    @Autowired
    private PositionMapper myPositionMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPosition(PositionReqDTO reqDTO) {
        int integer = myPositionMapper.addPosition(reqDTO);
        if (integer == 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyPosition(Long id, PositionReqDTO reqDTO) {
        int integer = myPositionMapper.modifyPosition(id, reqDTO);
        if (integer == 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePosition(Long id) {
        int result = myPositionMapper.selectIsPositionUse(id);
        if (result != 0) {
            throw new CommonException(ErrorCode.RESOURCE_USE);
        }
        int integer = myPositionMapper.deletePosition(id);
        if (integer == 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    @Override
    public Page<PositionResDTO> getPositions(Long page, Long size) {
        List<PositionResDTO> positionsList = null;
        int count = myPositionMapper.getPositionsCount();
        Page<PositionResDTO> pageBean = new Page<>();
        pageBean.setCurrent(page).setPages(size).setTotal(count);
        if (count > 0) {
            page = (page - 1) * size;
            positionsList = myPositionMapper.getPositions(page,size);
            pageBean.setRecords(positionsList);
        }
        return pageBean;
    }

}
