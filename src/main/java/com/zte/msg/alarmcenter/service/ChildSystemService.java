package com.zte.msg.alarmcenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.req.ChildSystemConfigReqDTO;
import com.zte.msg.alarmcenter.dto.res.ChildSystemConfigResDTO;

public interface ChildSystemService {

    /**
     * 获取子系统列表
     * @return
     * @param current
     * @param pageSize
     */
    Page<ChildSystemConfigResDTO> getChildSystemConfigs(int current, int pageSize);

    /**
     * 新增子系统
     * @param childSystemConfigReqDTO
     */
    void addChildSystemConfigs(ChildSystemConfigReqDTO childSystemConfigReqDTO);

    /**
     * 修改子系统
     * @param id
     * @param childSystemConfigReqDTO
     */
    void modifyChildSystemConfig(Long id, ChildSystemConfigReqDTO childSystemConfigReqDTO);

    /**
     * 删除子系统
     * @param id
     */
    void removeChildSystem(Long id);
}
