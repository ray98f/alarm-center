package com.zte.msg.alarmcenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zte.msg.alarmcenter.dto.req.DicReqDTO;
import com.zte.msg.alarmcenter.dto.req.DicUpdateReqDTO;
import com.zte.msg.alarmcenter.dto.res.DicResDTO;
import com.zte.msg.alarmcenter.entity.Dic;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/1/19 9:54
 */
public interface DicService extends IService<Dic> {

    /**
     * 分页查询字典类型
     *
     * @param page
     * @param name
     * @param type
     * @param isEnable
     * @return
     */
    Page<DicResDTO> getDics(Page<DicResDTO> page,
                            String name,
                            String type,
                            Integer isEnable);

    /**
     * 添加字典类型
     *
     * @param reqDTO
     */
    void addDic(DicReqDTO reqDTO);

    /**
     * 更新字典类型
     *
     * @param reqDTO
     */
    void updateDic(DicUpdateReqDTO reqDTO);

    /**
     * 删除字典类型
     *
     * @param ids
     */
    void deleteDic(Long[] ids);


}
