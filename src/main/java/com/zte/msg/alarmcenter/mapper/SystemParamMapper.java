package com.zte.msg.alarmcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.entity.SystemParameter;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface SystemParamMapper {

    /**
     * 查询系统参数列表
     *
     * @param page
     * @param parameter
     * @return
     */
    Page<SystemParameter> listSystemParam(Page<SystemParameter> page, String parameter);

    /**
     * 获取全部系统参数列表
     *
     * @return
     */
    List<SystemParameter> listAllSystemParam();

    /**
     * 批量删除系统参数
     *
     * @param ids
     * @return
     */
    int deleteSystemParam(List<Long> ids);

    /**
     * 修改系统参数
     *
     * @param systemParameter
     * @return
     */
    int updateSystemParam(SystemParameter systemParameter);

    /**
     * 查询是否存在系统参数
     *
     * @param systemParameter
     * @return
     */
    Long selectSystemParamIsExist(SystemParameter systemParameter);

    /**
     * 新增系统参数
     *
     * @param systemParameter
     * @return
     */
    int insertSystemParam(SystemParameter systemParameter);

}
