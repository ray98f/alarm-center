package com.zte.msg.alarmcenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.entity.MsgConfig;
import com.zte.msg.alarmcenter.entity.MsgPushHistory;

import java.util.List;

/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2021/4/7 8:56
 */
public interface MsgService {

    /**
     * 获取告警推送配置列表
     *
     * @param name
     * @param type
     * @param pageReqDTO
     * @return
     */
    Page<MsgConfig> pageMsgConfig(String name, Integer type, PageReqDTO pageReqDTO);

    /**
     * 新增前转配置
     *
     * @param msgConfig
     */
    void insertMsgConfig(MsgConfig msgConfig);

    /**
     * 修改前转配置
     *
     * @param msgConfig
     */
    void editMsgConfig(MsgConfig msgConfig);

    /**
     * 删除前转配置
     *
     * @param ids
     */
    void deleteMsgConfig(List<Long> ids);

    /**
     * 获取告警记录推送历史
     *
     * @param pageReqDTO
     * @return
     */
    Page<MsgPushHistory> pageMsgHistory(PageReqDTO pageReqDTO);
}
