package com.zte.msg.alarmcenter.exception;

import com.zte.msg.alarmcenter.enums.ErrorCode;
import lombok.Data;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2020/4/22 15:52
 */
@Data
public class CommonException extends RuntimeException {

    private Integer code;

    private String message;

    private String[] params;

    public CommonException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public CommonException(ErrorCode error) {
        this.code = error.code();
        this.message = error.message();
    }

    public CommonException(ErrorCode error, String... params) {
        this.code = error.code();
        this.message = error.message();
        this.params = params;
    }

}
