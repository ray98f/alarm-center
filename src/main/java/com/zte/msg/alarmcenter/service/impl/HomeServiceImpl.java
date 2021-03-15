package com.zte.msg.alarmcenter.service.impl;

import com.baomidou.mybatisplus.extension.api.R;
import com.zte.msg.alarmcenter.dto.res.*;
import com.zte.msg.alarmcenter.enums.ErrorCode;
import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.mapper.HomeMapper;
import com.zte.msg.alarmcenter.service.HomeService;
import com.zte.msg.alarmcenter.utils.ExcelPortUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2021/3/9 10:00
 */
@Service
@Slf4j
public class HomeServiceImpl implements HomeService {

    @Autowired
    private HomeMapper homeMapper;

    /**
     * 项目情况查询
     *
     * @return
     */
    @Override
    public HomeProjSituationResDTO projectSituation() {
        HomeProjSituationResDTO homeProjSituationResDTO = homeMapper.projectSituation();
        if (Objects.isNull(homeProjSituationResDTO)) {
            throw new CommonException(ErrorCode.SELECT_ERROR);
        }
        return homeProjSituationResDTO;
    }

    /**
     * 线路情况查询
     *
     * @return
     */
    @Override
    public HomeLineSituationResDTO lineSituation() {
        HomeLineSituationResDTO homeLineSituationResDTO = homeMapper.lineSituation();
        if (Objects.isNull(homeLineSituationResDTO)) {
            throw new CommonException(ErrorCode.SELECT_ERROR);
        }
        return homeLineSituationResDTO;
    }

    /**
     * 设备情况查询
     *
     * @return
     */
    @Override
    public HomeDeviceSituationResDTO deviceSituation() {
        HomeDeviceSituationResDTO homeDeviceSituationResDTO = homeMapper.deviceSituation();
        if (Objects.isNull(homeDeviceSituationResDTO)) {
            throw new CommonException(ErrorCode.SELECT_ERROR);
        }
        homeDeviceSituationResDTO.setNormalRate(homeDeviceSituationResDTO.getTotalNum(), homeDeviceSituationResDTO.getNormalNum());
        return homeDeviceSituationResDTO;
    }

    /**
     * 告警处置情况
     *
     * @return
     */
    @Override
    public Map<String, Object> alarmHandleSituation() {
        Map<String, Object> data = new HashMap<>(16);
        Integer handleNum = homeMapper.alarmHandleNum();
        Integer notHandleNum = homeMapper.alarmNotHandleNum();
        data.put("handleNum", handleNum);
        data.put("notHandleNum", notHandleNum);
        data.put("handleRate", (handleNum / (handleNum + notHandleNum)) * 100);
        data.put("notHandleRate", (notHandleNum / (handleNum + notHandleNum)) * 100);
        return data;
    }

    /**
     * 系统状况查询
     *
     * @return
     */
    @Override
    public HomeSubsystemSituationResDTO subsystemSituation() {
        HomeSubsystemSituationResDTO homeSubsystemSituationResDTO = homeMapper.subsystemSituation();
        if (Objects.isNull(homeSubsystemSituationResDTO)) {
            throw new CommonException(ErrorCode.SELECT_ERROR);
        }
        return homeSubsystemSituationResDTO;
    }

    /**
     * 首页告警消息
     *
     * @return
     */
    @Override
    public List<AlarmHistoryResDTO> selectAlarmHistory() {
        List<AlarmHistoryResDTO> alarmHistoryResDTOList = homeMapper.selectAlarmHistory();
        if (null == alarmHistoryResDTOList || alarmHistoryResDTOList.isEmpty()) {
            log.warn("当前无告警");
            return null;
        }
        return alarmHistoryResDTOList;
    }

    /**
     * 首页告警消息导出
     *
     * @param response
     */
    @Override
    public void exportAlarmHistory(HttpServletResponse response) {
        List<String> listName = Arrays.asList("子系统", "告警等级", "站点", "设备", "槽位", "告警码", "告警名称", "告警原因", "第一次告警时间", "最后告警时间", "告警次数", "告警音量", "是否静音", "告警状态", "告警升级", "告警备注");
        List<AlarmHistoryResDTO> alarmHistory = homeMapper.selectAlarmHistory();
        List<Map<String, String>> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        if (null != alarmHistory) {
            for (AlarmHistoryResDTO alarmHistoryResDTO : alarmHistory) {
                Map<String, String> map = new HashMap<>(16);
                String alarmLevelName, alarmStateName;
                if (alarmHistoryResDTO.getAlarmLevel() == 1) {
                    alarmLevelName = "紧急告警";
                } else if (alarmHistoryResDTO.getAlarmLevel() == 2) {
                    alarmLevelName = "重要告警";
                } else {
                    alarmLevelName = "一般告警";
                }
                if (alarmHistoryResDTO.getAlarmLevel() == 1) {
                    alarmStateName = "待处理";
                } else if (alarmHistoryResDTO.getAlarmLevel() == 2) {
                    alarmStateName = "手动确认";
                } else {
                    alarmStateName = "自动确认";
                }
                map.put("子系统", alarmHistoryResDTO.getSubsystemName());
                map.put("告警等级", alarmLevelName);
                map.put("站点", alarmHistoryResDTO.getSiteName());
                map.put("设备", alarmHistoryResDTO.getDeviceName());
                map.put("槽位", alarmHistoryResDTO.getSlotPosition());
                map.put("告警码", alarmHistoryResDTO.getAlarmCode());
                map.put("告警名称", alarmHistoryResDTO.getAlarmName());
                map.put("告警原因", alarmHistoryResDTO.getAlarmReason());
                map.put("第一次告警时间", sdf.format(alarmHistoryResDTO.getFirstTime()));
                map.put("最后告警时间", sdf.format(alarmHistoryResDTO.getFinalTime()));
                map.put("告警次数", alarmHistoryResDTO.getFrequency().toString());
                map.put("告警音量", alarmHistoryResDTO.getAlarmVolume());
                map.put("是否静音", (alarmHistoryResDTO.getIsMute() == 0 ? "否" : "是"));
                map.put("告警状态", alarmStateName);
                map.put("告警升级", (alarmHistoryResDTO.getIsUpgrade() == 0 ? "否" : "是"));
                map.put("告警备注", alarmHistoryResDTO.getAlarmRemark());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("历史告警", listName, list, null, response);
    }

    /**
     * 静音
     *
     * @param ids
     * @return
     */
    @Override
    public void mute(List<Integer> ids) {
        if (ids.isEmpty()) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        int result = homeMapper.mute(ids);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    /**
     * 解除静音
     *
     * @param ids
     * @return
     */
    @Override
    public void unmute(List<Integer> ids) {
        if (ids.isEmpty()) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        int result = homeMapper.unmute(ids);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    /**
     * 调节音量
     *
     * @param ids
     * @param alarmVolume
     * @return
     */
    @Override
    public void adjustVolume(List<Integer> ids, String alarmVolume){
        if (ids.isEmpty() || alarmVolume.isEmpty()) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        int result = homeMapper.adjustVolume(ids, alarmVolume);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    /**
     * 清除告警
     *
     * @param ids
     * @return
     */
    @Override
    public void clearAlarm(List<Integer> ids){
        if (ids.isEmpty()) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        int result = homeMapper.clearAlarm(ids);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    /**
     * 确认告警
     *
     * @param ids
     * @return
     */
    @Override
    public void confirmAlarm(List<Integer> ids){
        if (ids.isEmpty()) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        int result = homeMapper.confirmAlarm(ids);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    /**
     * 过滤告警
     *
     * @param ids
     * @return
     */
    @Override
    public void filterAlarm(List<Integer> ids){
        if (ids.isEmpty()) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        int result = homeMapper.filterAlarm(ids);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    /**
     * 恢复告警
     *
     * @param ids
     * @return
     */
    @Override
    public void recoveryAlarm(List<Integer> ids){
        if (ids.isEmpty()) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        int result = homeMapper.recoveryAlarm(ids);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }
}