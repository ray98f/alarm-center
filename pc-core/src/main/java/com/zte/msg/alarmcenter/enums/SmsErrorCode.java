package com.zte.msg.alarmcenter.enums;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/1/8 11:05
 */
public enum SmsErrorCode {

    /**
     * 错误的手机号码
     */
    ERROR_PHONE_NUM(32100001, "错误的手机号码"),

    /**
     * 错误的模版id
     */
    ERROR_TEMPLATE_ID(32100002, "错误的模版id"),

    /**
     * 网络错误
     */
    NET_ERROR(32100003, "网络错误"),

    /**
     * 同一号码发送次数过于频繁
     */
    EXCEED_LIMIT(32100004, "同一号码发送次数过于频繁"),

    /**
     * 第三方服务鉴权失败
     */
    AUTH_FAIL(32100005, "第三方服务鉴权失败"),

    /**
     * 模版变量不符合规范
     */
    TEMPLATE_NOT_FORMAT(32100006, "模版变量不符合规范");

    private Integer code;

    private String message;

    SmsErrorCode(Integer code, String message) {
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
