package com.zte.msg.alarmcenter.utils;

import com.zte.msg.alarmcenter.config.RabbitMqConfig;
import com.zte.msg.alarmcenter.dto.AsyncVO;
import com.zte.msg.alarmcenter.enums.ErrorCode;
import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.mapper.SynchronizeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author frp
 */
@Slf4j
@Component
public class AsyncReceiver {

    @Autowired
    private SynchronizeMapper synchronizeMapper;

    @RabbitListener(queues = RabbitMqConfig.STRING_QUEUE)
    @RabbitHandler
    public void process(String message) {
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("HelloReceiver接收到的字符串消息是 => " + message);
    }


    @RabbitListener(queues = RabbitMqConfig.ASYNC_QUEUE)
    @RabbitHandler
    public void process(AsyncVO asyncVO) {
        log.info("------ 接收实体对象 ------");
        if (null != asyncVO.getAlarmCodeSyncReqDTOList()) {
            int result = synchronizeMapper.alarmCodesSync(asyncVO.getAlarmCodeSyncReqDTOList());
            if (result < 0) {
                throw new CommonException(ErrorCode.SYNC_ERROR);
            }
        } else if (null != asyncVO.getDeviceSyncReqDTOList()) {
            int result = synchronizeMapper.devicesSync(asyncVO.getDeviceSyncReqDTOList());
            if (result < 0) {
                throw new CommonException(ErrorCode.SYNC_ERROR);
            }
        } else {
            int result = synchronizeMapper.slotsSync(asyncVO.getSlotSyncReqDTOList());
            if (result < 0) {
                throw new CommonException(ErrorCode.SYNC_ERROR);
            }
        }
    }
}
