package com.zte.msg.alarmcenter.enums;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2020/12/10 15:56
 */
public enum ErrorCode {

    /**
     * 鉴权
     */
    AUTHORIZATION_CHECK_FAIL(401, "authorization.check.fail"),
    AUTHORIZATION_IS_OVERDUE(401, "authorization.is.overdue"),
    AUTHORIZATION_INVALID(401, "authorization.invalid"),
    AUTHORIZATION_EMPTY(401, "authorization.empty"),
    /**
     * 该用户无相关资源操作权限
     */
    RESOURCE_AUTH_FAIL(32000001, "resource.authority.error"),

    /**
     * 参数错误
     */
    PARAM_ERROR(32000002, "param.error"),

    /**
     * 参数超过范围
     */
    PARAM_OUT_OF_RANGE(32000003, "param.range.error"),

    /**
     * 错误的枚举值
     */
    ENUM_VALUE_ERROR(32000004, "enum.value.error"),

    /**
     * 字段不符合要求，仅限中英文字母、数字、中划线和下划线，且长度在4-32之间
     */
    PARAM_PATTERN_INCOMPATIBLE(32000005, "param.pattern.incompatible"),

    /**
     * 参数不能为空
     */
    PARAM_NULL_ERROR(32000006, "param.null.error"),

    /**
     * 资源配置初始化失败
     */
    RESOURCE_INIT_ERROR(32000007, "resource.init.error"),

    /**
     * 参数小于最小值
     */
    PARAM_MIN_ERROR(32000008, "param.min"),

    DATA_EXIST(32000009, "data.exist"),

    INSERT_ERROR(31000001, "insert.error"),

    SELECT_ERROR(31000002, "select.error"),

    SELECT_EMPTY(31000002, "select.empty"),

    UPDATE_ERROR(31000003, "update.error"),

    DELETE_ERROR(31000004, "delete.error"),

    /**
     * 参数不在枚举范围中
     */
    NOT_IN_ENUM(32000010, "not.in.enum"),

    /**
     * 资源不存在
     */
    RESOURCE_NOT_EXIST(32000012, "resource.not.exist"),

    /**
     * 鉴权 32000060 - 32000079
     */
    USER_EXIST(32000060, "user.exist"),
    USER_NOT_EXIST(32000060, "user.not.exist"),
    USER_DISABLE(32000060, "user.disable"),
    LOGIN_PASSWORD_ERROR(32000061, "login.password.error"),
    USER_PWD_CHANGE_FAIL(32000062, "user.pwd.change.fail"),
    APP_ROLE_EDIT_ERROR(32000063, "app.role.edit.error"),
    USER_NO_ROLE(32000064, "user.no.role"),
    USER_LOGIN_CANT_DELETE(32000065, "user.login.cant.delete"),
    ROLE_USE_CANT_DELETE(32000066, "role.use.cant.delete"),

    /**
     * OpenApi签名校验
     */
    OPENAPI_VERIFY_FAIL(32000100, "openapi.verify.fail"),

    /**
     * 字典
     */
    DIC_TYPE_ALREADY_EXIST(32000121, "dic.type.already.exist"),
    DIC_TYPE_NOT_EXIST(32000122, "dic.type.not.exist"),
    /**
     * 其他
     */
    USER_NOT_LOGIN_IN(32100002, "user.not.login.in"),
    PERMISSION_FAILED(32100003, "permission.failed"),
    SECRET_NOT_EXIST(32100004, "secret.not.exist"),
    SYNC_ERROR(32100005, "sync.error"),
    ROLE_EXIST(32100006, "role.exist"),
    ALARM_CODE_EXIST(32100006, "alarm.code.exist"),
    SYSTEM_PARAM_EXIST(32100006, "system.param.exist"),
    DEVICE_EXIST(32100006, "device.exist"),
    MSG_CONFIG_EXIST(32100006, "msg.config.exist"),
    SNMP_SLOT_EXIST(32100006, "snmp.slot.exist"),
    DEVICE_SLOT_EXIST(32100006, "device.slot.exist"),
    SNMP_ALARM_CODE_EXIST(32100006, "snmp.alarm.code.exist"),
    ALARM_LEVEL_NOT_EXIST(32100006, "alarm.level.not.exist"),
    RULE_EXIST(3210006, "rule.exist"),
    IMPORT_DATA_EXIST(3210007, "import.data.exist"),
    RESOURCE_USE(3210008, "resource.use"),
    TIME_WRONG(3210009, "time.wrong"),
    OPENAPI_ERROR(3210010, "openapi.error");

    private Integer code;

    private String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String messageOf(Integer code) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.code.equals(code)) {
                return errorCode.message;
            }
        }
        return "";
    }

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

}
