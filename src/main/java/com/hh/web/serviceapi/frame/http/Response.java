package com.hh.web.serviceapi.frame.http;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.io.Serializable;
import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response implements Serializable {

    private int code;
    private String message;
    private String extendMessage;
    private Map<String, Object> content;

    public static Response success(Map<String, Object> content) {
        Response response = new Response();
        response.code = 0;
        response.content = content;
        return response;
    }


    public static Response failure(int code, String message, String extendMessage) {
        Response response = new Response();
        response.code = code;
        response.message = message;
        response.extendMessage = extendMessage;
        return response;
    }

    public static Response failure(ApiStatus apiStatus) {
        Response response = new Response();
        response.code = apiStatus.getCode();
        response.message = apiStatus.getMessage();
        return response;
    }


    /*public static <T> Response failure(Set<ConstraintViolation<T>> violations) {
        StringBuilder builder = new StringBuilder("数据验证失败：");
        boolean firstTime = true;
        for (ConstraintViolation<T> violation : violations) {
            if (!firstTime) {
                builder.append(", ");
            } else {
                firstTime = false;
            }
            builder.append(violation.getMessage());
        }
        return failure(500, builder.toString(), "");
    }*/
}
