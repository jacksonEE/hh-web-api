package com.hh.web.serviceapi.frame.ex;

import com.hh.web.serviceapi.frame.http.ApiStatus;
import lombok.Getter;

@Getter
public class ApiException extends Exception {

    // error code
    private int code;

    // error message
    private String message;

    private String extendMsg;

    /**
     * constructor
     *
     * @param code    code
     * @param message message
     */
    public ApiException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    /**
     * constructor
     *
     * @param code      code
     * @param message   message
     * @param extendMsg 补充说明
     */
    public ApiException(int code, String message, String extendMsg) {
        super(message);
        this.code = code;
        this.message = message;
        this.extendMsg = extendMsg;
    }

    public ApiException(ApiStatus status) {
        this(status.getCode(), status.getMessage());
    }
}
