package com.hh.web.serviceapi.frame.core;

import com.hh.web.serviceapi.frame.ex.ApiException;
import com.hh.web.serviceapi.frame.http.RequestAttr;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface ApiHandler {

    /**
     * @param ra     参数
     * @param userId 当前登录id (user or manager?)
     * @throws ApiException ex
     */
    void handle(RequestAttr ra, Integer userId) throws ApiException;

    /**
     * get request
     *
     * @return get current Thread request
     */
    HttpServletRequest getRequest();

    /**
     * get response
     *
     * @return current Thread response
     */
    HttpServletResponse getResponse();

    /**
     * init result map
     */
    void initial(HttpServletRequest request, HttpServletResponse response);

    /**
     * get res
     *
     * @return result map
     */
    Map<String, Object> getResultMap();

    /**
     * add k-v to result map
     *
     * @param key   键
     * @param value 值
     */
    void addResult(String key, Object value);


}
