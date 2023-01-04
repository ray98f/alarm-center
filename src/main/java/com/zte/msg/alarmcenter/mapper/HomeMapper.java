package com.zte.msg.alarmcenter.mapper;

import com.zte.msg.alarmcenter.dto.res.*;
import com.zte.msg.alarmcenter.entity.AlarmHistory;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface HomeMapper {
    /**
     * 项目情况查询
     *
     * @return
     */
    HomeAlarmStatusSituationResDTO alarmStatusSituation();

    /**
     * 系统状况查询
     *
     * @return
     */
    List<HomeSubsystemSituationResDTO> subsystemSituation();

    /**
     * 获取所有线路
     *
     * @return
     */
    List<HomeStationSituationResDTO> selectAllLine();

    /**
     * 获取线路下站点信息
     *
     * @param lineId
     * @return
     */
    List<HomeStationSituationResDTO.Station> stationSituation(Long lineId);

    /**
     * 首页告警消息
     * @param size
     * @return
     */
    List<AlarmHistoryResDTO> selectAlarmHistory(Long size, Integer isHome);

    /**
     * 静音
     *
     * @param ids
     * @return
     */
    int mute(List<Integer> ids);

    /**
     * 解除静音
     *
     * @param ids
     * @return
     */
    int unmute(List<Integer> ids);

    /**
     * 调节音量
     *
     * @param ids
     * @param alarmVolume
     * @return
     */
    int adjustVolume(List<Integer> ids, String alarmVolume);

    /**
     * 清除告警
     *
     * @param ids
     * @return
     */
    int clearAlarm(List<Integer> ids);

    /**
     * 确认告警
     *
     * @param ids
     * @return
     */
    int confirmAlarm(List<Integer> ids);

    /**
     * 过滤告警
     *
     * @param ids
     * @return
     */
    int filterAlarm(List<Integer> ids);

    /**
     * 恢复告警
     *
     * @param ids
     * @return
     */
    int recoveryAlarm(List<Integer> ids);

    /**
     * 修改响铃
     *
     * @param isRing
     * @param ids
     * @return
     */
    int updateIsRing(Integer isRing, List<Long> ids);

    /**
     * 获取首页地图地址
     *
     * @return
     */
    List<HomeMapPathResDTO> getHomeMapPath();

    /**
     * 根据id查询告警历史
     *
     * @param id
     * @return
     */
    AlarmHistory selectAlarmHistoryById(Integer id);
}
