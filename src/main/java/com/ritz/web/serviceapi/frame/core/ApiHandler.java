package com.ritz.web.serviceapi.frame.core;

import com.ritz.web.serviceapi.frame.ex.ApiException;
import com.ritz.web.serviceapi.frame.http.RequestModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface ApiHandler {

    /**
     * @param requestModel 参数
     * @param result       结果集
     * @param request      request
     * @param response     response
     * @param userId       当前登录id
     * @throws ApiException ex
     */
    void handle(RequestModel requestModel, final Map<String, Object> result, HttpServletRequest request,
                HttpServletResponse response, Integer userId) throws ApiException;


}
