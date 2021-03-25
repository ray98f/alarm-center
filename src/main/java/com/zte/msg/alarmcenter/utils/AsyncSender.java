package com.zte.msg.alarmcenter.utils;

import com.zte.msg.alarmcenter.config.RabbitMqConfig;
import com.zte.msg.alarmcenter.dto.AsyncVO;
import com.zte.msg.alarmcenter.dto.req.AlarmHistoryReqDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author frp
 */
@Slf4j
@Component
public class AsyncSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public boolean send(String message) throws Exception {
        if (StringUtils.isEmpty(message)) {
            return false;
        }
        rabbitTemplate.convertAndSend(RabbitMqConfig.STRING_QUEUE, message);
        log.info(String.format("Sender发送字符串消息结果：%s", true));
        return true;
    }

    public boolean send(AsyncVO asyncVO) throws Exception {
        if (Objects.isNull(asyncVO)) {
            return false;
        }
        rabbitTemplate.convertAndSend(RabbitMqConfig.ASYNC_QUEUE, asyncVO);
        log.info(String.format("Sender发送同步消息消息结果：%s", true));
        return true;
    }

    public boolean test() throws Exception {
        List<AlarmHistoryReqDTO> alarmHistoryReqDTOList = new ArrayList<>();
        AlarmHistoryReqDTO alarmHistoryReqDTO = new AlarmHistoryReqDTO();
        alarmHistoryReqDTO.setSystem(22);
        alarmHistoryReqDTO.setLine(23);
        alarmHistoryReqDTO.setStation(342);
        alarmHistoryReqDTO.setDevice(232323);
        alarmHistoryReqDTO.setSlot(213);
        alarmHistoryReqDTO.setAlarmCode(100);
        alarmHistoryReqDTO.setIsRecovery(true);
        alarmHistoryReqDTO.setAlarmTime(new Timestamp(System.currentTimeMillis()));
        List<AlarmHistoryReqDTO.AlarmMessage> alarmMessageList = new ArrayList<>();
        AlarmHistoryReqDTO.AlarmMessage alarmMessage1 = new AlarmHistoryReqDTO.AlarmMessage();
        AlarmHistoryReqDTO.AlarmMessage alarmMessage2 = new AlarmHistoryReqDTO.AlarmMessage();
        alarmMessage1.setTitle("1111");
        alarmMessage1.setContent("ASDASD");
        alarmMessage2.setTitle("2222");
        alarmMessage2.setContent("FRPFRP");
        alarmMessageList.add(alarmMessage1);
        alarmMessageList.add(alarmMessage2);
        alarmHistoryReqDTO.setAlarmMessageList(alarmMessageList);
        alarmHistoryReqDTOList.add(alarmHistoryReqDTO);
        rabbitTemplate.convertAndSend(RabbitMqConfig.SYNC_ALARM_QUEUE, alarmHistoryReqDTOList);
        log.info(String.format("Sender发送告警记录结果：%s", true));
        return true;
    }
}
