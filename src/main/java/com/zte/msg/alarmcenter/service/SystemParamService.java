package com.zte.msg.alarmcenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.entity.SystemParameter;

import java.util.List;

/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2021/2/22 9:08
 */
public interface SystemParamService {

    /**
     * 查询系统参数列表
     * @param parameter
     * @param pageReqDTO
     * @return
     */
    Page<SystemParameter> listSystemParam(String parameter, PageReqDTO pageReqDTO);

    /**
     * 获取全部系统参数列表
     * @return
     */
    List<SystemParameter> listAllSystemParam();

    /**
     * 批量删除系统参数
     * @param ids
     */
    void deleteSystemParam(List<Long> ids);

    /**
     * 修改系统参数
     * @param systemParameter
     * @return
     */
    void updateSystemParam(SystemParameter systemParameter);

    /**
     * 新增系统参数
     * @param systemParameter
     * @return
     */
    void insertSystemParam(SystemParameter systemParameter);
}
