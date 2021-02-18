package com.zte.msg.alarmcenter.enums;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/1/18 14:44
 */
public enum AppErrorCode {

    /**
     * 缺少了必须的参数
     */
    ERROR_PHONE_NUM(32200001, "缺少了必须的参数"),

    /**
     * 参数值不合法
     */
    ERROR_TEMPLATE_ID(32200002, "参数值不合法"),

    /**
     * 验证失败
     */
    NET_ERROR(32200003, "验证失败"),

    /**
     * 消息体太大
     */
    EXCEED_LIMIT(32200004, "消息体太大"),

    /**
     * app_key 参数非法
     */
    AUTH_FAIL(32200005, "app_key 参数非法"),

    /**
     * 推送对象中有不支持的 key
     */
    TEMPLATE_NOT_FORMAT(32200006, "推送对象中有不支持的 key");

    private Integer code;

    private String message;

    AppErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    protected void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    protected void setMessage(String message) {
        this.message = message;
    }
}
