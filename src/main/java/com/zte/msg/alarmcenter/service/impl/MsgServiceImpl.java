package com.zte.msg.alarmcenter.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.entity.MsgConfig;
import com.zte.msg.alarmcenter.entity.MsgPushHistory;
import com.zte.msg.alarmcenter.enums.ErrorCode;
import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.mapper.MsgMapper;
import com.zte.msg.alarmcenter.service.MsgService;
import com.zte.msg.alarmcenter.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class MsgServiceImpl implements MsgService {

    @Autowired
    private MsgMapper msgMapper;

    /**
     * 获取告警推送配置列表
     *
     * @param name
     * @param type
     * @param pageReqDTO
     * @return
     */
    @Override
    public Page<MsgConfig> pageMsgConfig(String name, Integer type, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPage().intValue(), pageReqDTO.getSize().intValue());
        if (name.contains(Constants.PERCENT_SIGN)) {
            name = "Prohibit input";
        }
        return msgMapper.pageMsgConfig(pageReqDTO.of(), name, type);
    }

    /**
     * 新增前转配置
     *
     * @param msgConfig
     */
    @Override
    public void insertMsgConfig(MsgConfig msgConfig) {
        if (Objects.isNull(msgConfig)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Long id = msgMapper.selectMsgConfigIsExist(msgConfig);
        if (!Objects.isNull(id)) {
            throw new CommonException(ErrorCode.MSG_CONFIG_EXIST);
        }
        int result = msgMapper.insertMsgConfig(msgConfig);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    /**
     * 修改前转配置
     *
     * @param msgConfig
     */
    @Override
    public void editMsgConfig(MsgConfig msgConfig) {
        if (Objects.isNull(msgConfig)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Long id = msgMapper.selectMsgConfigIsExist(msgConfig);
        if (!Objects.isNull(id)) {
            throw new CommonException(ErrorCode.MSG_CONFIG_EXIST);
        }
        int result = msgMapper.editMsgConfig(msgConfig);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    /**
     * 删除前转配置
     *
     * @param ids
     */
    @Override
    public void deleteMsgConfig(List<Long> ids) {
        if (null == ids || ids.isEmpty()) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        int result = msgMapper.deleteMsgConfig(ids);
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    /**
     * 获取告警记录推送历史
     *
     * @param pageReqDTO
     * @return
     */
    @Override
    public Page<MsgPushHistory> pageMsgHistory(PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPage().intValue(), pageReqDTO.getSize().intValue());
        return msgMapper.pageMsgHistory(pageReqDTO.of());
    }
}
