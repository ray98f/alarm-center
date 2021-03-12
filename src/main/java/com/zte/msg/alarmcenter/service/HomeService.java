package com.zte.msg.alarmcenter.service;

import com.zte.msg.alarmcenter.dto.res.*;

import javax.servlet.http.HttpServletResponse;
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
    HomeProjSituationResDTO projectSituation();

    /**
     * 线路情况查询
     *
     * @return
     */
    HomeLineSituationResDTO lineSituation();

    /**
     * 设备情况查询
     *
     * @return
     */
    HomeDeviceSituationResDTO deviceSituation();

    /**
     * 告警处置情况
     *
     * @return
     */
    Map<String, Object> alarmHandleSituation();

    /**
     * 系统状况查询
     *
     * @return
     */
    HomeSubsystemSituationResDTO subsystemSituation();

    /**
     * 首页告警消息
     *
     * @return
     */
    List<AlarmHistoryResDTO> selectAlarmHistory();

    /**
     * 首页告警消息导出
     *
     * @param response
     */
    void exportAlarmHistory(HttpServletResponse response);

    /**
     * 静音
     * @param ids
     * @return
     */
    void mute(List<Integer> ids);

    /**
     * 解除静音
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
}
