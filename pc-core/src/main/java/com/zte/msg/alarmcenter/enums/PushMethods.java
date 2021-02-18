package com.zte.msg.alarmcenter.enums;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2020/12/31 9:26
 */
public enum PushMethods {


    /**
     * 短信
     */
    SMS(1),

    /**
     * 邮件
     */
    MAIL(2),

    /**
     * App
     */
    APP(3),

    /**
     * 微信消息
     */
    WECHAT(4),

    UNKNOWN(-1);

    private Integer type;

    PushMethods(Integer type) {
        this.type = type;
    }

    public static PushMethods valueOf(Integer type) {
        for (PushMethods pushMethod : PushMethods.values()) {
            if (pushMethod.type.equals(type)) {
                return pushMethod;
            }
        }
        return UNKNOWN;
    }

    public Integer value() {
        return this.type;
    }

}
