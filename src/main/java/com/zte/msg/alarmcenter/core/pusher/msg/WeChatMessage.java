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
 * @date 2020/12/15 10:27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WeChatMessage extends Message {

    private String openId;

    private Long templateId;

    private String data;

    private String appletData;

    private String skipUrl;

    public WeChatMessage build(WeChatMessageReqDTO reqDTO) {
        this.setPushMethod(PushMethods.WECHAT);
        BeanUtils.copyProperties(reqDTO, this);
        return this;
    }
}
