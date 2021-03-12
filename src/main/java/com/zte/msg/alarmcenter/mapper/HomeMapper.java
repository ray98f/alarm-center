package com.zte.msg.alarmcenter.mapper;

import com.zte.msg.alarmcenter.dto.res.*;
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
     * 告警处理数获取
     *
     * @return
     */
    Integer alarmHandleNum();

    /**
     * 告警未处理数获取
     *
     * @return
     */
    Integer alarmNotHandleNum();

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
}
