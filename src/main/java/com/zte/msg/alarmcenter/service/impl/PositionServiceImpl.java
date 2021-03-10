package com.zte.msg.alarmcenter.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.req.PositionReqDTO;
import com.zte.msg.alarmcenter.dto.res.PositionResDTO;
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
            throw new CommonException(4000, "新增失败！");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyPosition(Long id, PositionReqDTO reqDTO) {
        int integer = myPositionMapper.modifyPosition(id, reqDTO);
        if (integer == 0) {
            throw new CommonException(4000, "修改失败！");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePosition(Long id) {
        int integer = myPositionMapper.deletePosition(id);
        if (integer == 0) {
            throw new CommonException(4000, "删除失败！");
        }
    }

    @Override
    public Page<PositionResDTO> getPositions(Long page, Long size) {
        List<PositionResDTO> positionsList = null;
        int count = myPositionMapper.getPositionsCount();
        Page<PositionResDTO> pageBean = new Page<>();
        pageBean.setCurrent(page).setPages(size).setTotal(count);
        if (count > 0) {
//            pageReqDTO.setPage((pageReqDTO.getPage() - 1) * pageReqDTO.getSize());
            page = (page - 1) * size;
            positionsList = myPositionMapper.getPositions(page,size);
            pageBean.setRecords(positionsList);
        }
        return pageBean;
    }

//    @Override
//    public Page<PositionResDTO> getPositionsById(Long pId, PageReqDTO pageReqDTO) {
//        List<PositionResDTO> positionsByIdList = null;
//        int count = myPositionMapper.getPositionsByIdCount(pId);
//        Page<PositionResDTO> page = new Page<>();
//        page.setCurrent(pageReqDTO.getPage()).setPages(pageReqDTO.getSize()).setTotal(count);
//        if (count > 0) {
//            pageReqDTO.setPage((pageReqDTO.getPage() - 1) * pageReqDTO.getSize());
//            positionsByIdList = myPositionMapper.getPositionsById(pId,pageReqDTO);
//            page.setRecords(positionsByIdList);
//        }
//        return page;
//    }

}
