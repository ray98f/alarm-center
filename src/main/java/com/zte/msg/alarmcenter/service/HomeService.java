package com.zte.msg.alarmcenter.service;

import com.zte.msg.alarmcenter.dto.res.*;

import java.util.List;
import java.util.Map;

/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2021/3/9 9:01
 */
public interface HomeService {

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
     * 车站状况查询
     *
     * @return
     */
    List<HomeStationSituationResDTO> stationSituation();

    /**
     * 首页告警消息
     * @param size
     * @return
     */
    List<AlarmHistoryResDTO> selectAlarmHistory(Long size);

    /**
     * 首页告警消息导出
     */
    void exportAlarmHistory();

    /**
     * 静音
     *
     * @param ids
     * @return
     */
    void mute(List<Integer> ids);

    /**
     * 解除静音
     *
     * @param ids
     * @return
     */
    void unmute(List<Integer> ids);

    /**
     * 调节音量
     *
     * @param ids
     * @param alarmVolume
     * @return
     */
    void adjustVolume(List<Integer> ids, String alarmVolume);

    /**
     * 清除告警
     *
     * @param ids
     * @return
     */
    void clearAlarm(List<Integer> ids);

    /**
     * 确认告警
     *
     * @param ids
     * @return
     */
    void confirmAlarm(List<Integer> ids);

    /**
     * 过滤告警
     *
     * @param ids
     * @return
     */
    void filterAlarm(List<Integer> ids);

    /**
     * 恢复告警
     *
     * @param ids
     * @return
     */
    void recoveryAlarm(List<Integer> ids);

    /**
     * 修改响铃
     * @param isRing
     * @param ids
     * @return
     */
    void updateIsRing(Integer isRing, List<Long> ids);

    /**
     * 获取首页地图地址
     *
     * @return
     */
    List<HomeMapPathResDTO> getHomeMapPath();
}
