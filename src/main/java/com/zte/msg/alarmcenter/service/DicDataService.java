package com.zte.msg.alarmcenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zte.msg.alarmcenter.dto.req.DicDataReqDTO;
import com.zte.msg.alarmcenter.dto.req.DicDataUpdateReqDTO;
import com.zte.msg.alarmcenter.dto.res.DicDataResDTO;
import com.zte.msg.alarmcenter.entity.DicData;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/1/19 15:04
 */
public interface DicDataService extends IService<DicData> {

    /**
     * 分页查询字典数据
     *
     * @param page
     * @param type
     * @param value
     * @param isEnable
     * @return
     */
    Page<DicDataResDTO> getDicDataByPage(Page<DicDataResDTO> page,
                                         String type,
                                         String value,
                                         Integer isEnable);

    /**
     * 添加字典数据
     *
     * @param reqDTO
     */
    void addDicData(DicDataReqDTO reqDTO);

    /**
     * 更新字典数据
     *
     * @param reqDTO
     */
    void updateDicData(DicDataUpdateReqDTO reqDTO);

    /**
     * 删除字典数据
     *
     * @param ids
     */
    void deleteDicData(Long[] ids);
}
