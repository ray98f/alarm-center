package com.zte.msg.alarmcenter.mapper;

import com.zte.msg.alarmcenter.dto.req.ChildSystemConfigReqDTO;
import com.zte.msg.alarmcenter.dto.req.HeartbeatQueueReqDTO;
import com.zte.msg.alarmcenter.dto.res.ChildSystemConfigResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface ChildSystemMapper {

    /**
     * 查询子系统数量
     *
     * @return
     */
    int getChildSystemConfigsCount();

    /**
     * 查询子系统list
     *
     * @param current
     * @param pageSize
     * @return
     */
    List<ChildSystemConfigResDTO> getChildSystemConfigs(@Param("current") Long current, @Param("pageSize") Long pageSize);

    /**
     * 新增子系统
     *
     * @param childSystemConfigReqDTO
     */
    Integer addChildSystemConfigs(ChildSystemConfigReqDTO childSystemConfigReqDTO);

    /**
     * 修改子系统
     *
     * @param id
     * @param childSystemConfigReqDTO
     * @return
     */
    Integer modifyChildSystemConfig(@Param("id") Long id, @Param("childSystemConfigReqDTO") ChildSystemConfigReqDTO childSystemConfigReqDTO);

    /**
     * 删除子系统
     *
     * @param id
     * @return
     */
    int removeChildSystem(@Param("id") Long id);

    /**
     * 修改系统心跳
     *
     * @param heartbeatQueueReqDTOList
     * @return
     */
    int isOnline(List<HeartbeatQueueReqDTO> heartbeatQueueReqDTOList);

    /**
     * 离线
     *
     * @return
     */
    void offline();
}
