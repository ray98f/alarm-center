package com.zte.msg.alarmcenter.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.entity.MsgConfig;
import com.zte.msg.alarmcenter.entity.MsgPushHistory;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface MsgMapper {
    /**
     * 获取告警推送配置列表
     *
     * @param page
     * @param name
     * @param type
     * @return
     */
    Page<MsgConfig> pageMsgConfig(Page<MsgConfig> page, String name, Integer type);

    /**
     * 新增前转配置
     *
     * @param msgConfig
     * @return
     */
    int insertMsgConfig(MsgConfig msgConfig);

    /**
     * 修改前转配置
     *
     * @param msgConfig
     * @return
     */
    int editMsgConfig(MsgConfig msgConfig);

    /**
     * 删除前转配置
     *
     * @param ids
     * @return
     */
    int deleteMsgConfig(List<Long> ids);

    /**
     * 获取告警记录推送历史
     *
     * @param page
     * @return
     */
    Page<MsgPushHistory> pageMsgHistory(Page<MsgPushHistory> page);

}
