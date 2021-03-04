package com.zte.msg.alarmcenter.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.req.ChildSystemConfigReqDTO;
import com.zte.msg.alarmcenter.dto.res.ChildSystemConfigResDTO;
import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.mapper.ChildSystemMapper;
import com.zte.msg.alarmcenter.service.ChildSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChildSystemServiceImpl implements ChildSystemService {

    @Autowired
    private ChildSystemMapper myChildSystemMapper;

    /**
     * 获取子系统列表
     *
     * @param current
     * @param pageSize
     * @return
     */
    @Override
    public Page<ChildSystemConfigResDTO> getChildSystemConfigs(int current, int pageSize) {
        List<ChildSystemConfigResDTO> childSystemList = null;
        int count = myChildSystemMapper.getChildSystemConfigsCount();
        Page<ChildSystemConfigResDTO> page = new Page<>();
        page.setCurrent(current).setPages(pageSize).setTotal(count);
        if (count > 0) {
            current = (current - 1) * pageSize;
            childSystemList = myChildSystemMapper.getChildSystemConfigs(current, pageSize);
            page.setRecords(childSystemList);
        }
        return page;
    }

    /**
     * 新增子系统
     *
     * @param childSystemConfigReqDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addChildSystemConfigs(ChildSystemConfigReqDTO childSystemConfigReqDTO) {
        Integer integer = myChildSystemMapper.addChildSystemConfigs(childSystemConfigReqDTO);
        if (integer == 0) {
            throw new CommonException(4000, "新增失败！");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyChildSystemConfig(Long id, ChildSystemConfigReqDTO childSystemConfigReqDTO) {
        int integer = myChildSystemMapper.modifyChildSystemConfig(id,childSystemConfigReqDTO);
        if (integer == 0) {
            throw new CommonException(4000, "修改失败！");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeChildSystem(Long id) {
        int integer = myChildSystemMapper.removeChildSystem(id);
        if (integer == 0) {
            throw new CommonException(4000, "删除失败！");
        }
    }
}
