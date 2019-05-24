package com.ritz.web.serviceapi.frame.http;

import lombok.Getter;

@Getter
public enum ApiStatus {

    OK(0, "请求成功"),
    NOT_LOGIN(-1, "请先登录"),
    TOKEN_EXPIRED(-2, "请重新登录"),
    USERNAME_NOT_EXISTED(-3, "用户不存在"),
    PASSWORD_INCORRECT(-4, "密码错误"),
    NEED_RE_AUTH(-5, "请重新授权"),
    FORBIDDEN(403, "无权访问"),
    API_NOT_FOUND(404, "API NOT FOUND"),
    ERROR(500, "服务异常"),
    FIELD_INVALID(1001, "参数校验失败"),
    RESOURCE_EXISTED(1002, "资源已存在"),
    RESOURCE_NOT_EXISTED(1003, "资源不存在"),
    REQUEST_TIME_OUT(1004, "请求超时");

    private int code;
    private String message;

    ApiStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
