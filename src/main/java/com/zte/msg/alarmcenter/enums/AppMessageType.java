package com.zte.msg.alarmcenter.enums;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/1/18 16:23
 */
public enum AppMessageType {

    /**
     * 通知
     */
    NOTIFICATION(1, "notification"),

    /**
     * 自定义消息
     */
    MESSAGE(2, "message"),


    UNKNOWN(-1, "unknown");

    private Integer type;

    private String messageType;

    AppMessageType(Integer type, String messageType) {

        this.type = type;
        this.messageType = messageType;
    }

    public static AppMessageType valueOf(Integer type) {
        for (AppMessageType messageType : AppMessageType.values()) {
            if (messageType.type.equals(type)) {
                return messageType;
            }
        }
        return UNKNOWN;
    }

    public Integer value() {
        return this.type;
    }
}
