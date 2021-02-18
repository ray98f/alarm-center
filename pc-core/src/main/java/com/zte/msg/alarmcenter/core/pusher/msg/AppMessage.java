package com.zte.msg.alarmcenter.core.pusher.msg;

import com.zte.msg.alarmcenter.core.pusher.base.Message;
import com.zte.msg.alarmcenter.enums.PushMethods;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/1/15 15:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AppMessage extends Message {

    private Integer targetPlatform;

    private String[] registrationId;

    private Integer messageType;

    private String title;

    private String content;

    public AppMessage build(AppMessageReqDTO reqDTO) {

        this.setPushMethod(PushMethods.APP);
        BeanUtils.copyProperties(reqDTO, this);
        return this;
    }
}
