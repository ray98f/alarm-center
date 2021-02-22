package com.zte.msg.alarmcenter.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.entity.SystemParameter;
import com.zte.msg.alarmcenter.enums.ErrorCode;
import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.mapper.SystemParamMapper;
import com.zte.msg.alarmcenter.service.SystemParamService;
import com.zte.msg.alarmcenter.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2021/2/22 9:08
 */
@Service
@Slf4j
public class SystemParamServiceImpl implements SystemParamService {

    @Autowired
    private SystemParamMapper systemParamMapper;

    /**
     * 查询系统参数列表
     *
     * @param parameter
     * @param pageReqDTO
     * @return
     */
    @Override
    public Page<SystemParameter> listSystemParam(String parameter, PageReqDTO pageReqDTO) {
        if (Objects.isNull(parameter)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        PageHelper.startPage(pageReqDTO.getPage().intValue(), pageReqDTO.getSize().intValue());
        return systemParamMapper.listSystemParam(pageReqDTO.of(), parameter);
    }

    /**
     * 获取全部系统参数列表
     *
     * @return
     */
    @Override
    public List<SystemParameter> listAllSystemParam() {
        List<SystemParameter> list = systemParamMapper.listAllSystemParam();
        if (null == list || list.isEmpty()) {
            log.warn("系统参数列表为空");
        }
        return list;
    }

    /**
     * 批量删除系统参数
     *
     * @param ids
     */
    @Override
    public void deleteSystemParam(List<Long> ids) {
        if (null == ids || ids.isEmpty()) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        int result = systemParamMapper.deleteSystemParam(ids);
        if (result >= 0) {
            log.info("系统参数删除成功");
        } else {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    /**
     * 修改系统参数
     *
     * @param systemParameter
     * @return
     */
    @Override
    public void updateSystemParam(SystemParameter systemParameter) {
        if (Objects.isNull(systemParameter)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        systemParameter.setCreatedBy(TokenUtil.getCurrentUserName());
        int updateRole = systemParamMapper.updateSystemParam(systemParameter);
        if (updateRole <= 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
        log.info("修改系统参数成功");
    }

    /**
     * 新增系统参数
     *
     * @param systemParameter
     * @return
     */
    @Override
    public void insertSystemParam(SystemParameter systemParameter) {
        if (Objects.isNull(systemParameter)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        systemParameter.setCreatedBy(TokenUtil.getCurrentUserName());
        int insertRole = systemParamMapper.insertSystemParam(systemParameter);
        if (insertRole <= 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
        log.info("新增系统参数成功");
    }

}
