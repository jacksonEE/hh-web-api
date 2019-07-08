package com.hh.web.serviceapi.api.user.auth;

import com.hh.web.serviceapi.frame.annotation.Api;
import com.hh.web.serviceapi.frame.core.AbstractApiHandler;
import com.hh.web.serviceapi.frame.ex.ApiException;
import com.hh.web.serviceapi.frame.http.RequestAttr;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;

/**
 * created by Jackson at 2019/7/5 17:31
 **/
@Api(value = "testApi", needLogin = false)
public class TestApi extends AbstractApiHandler {

    @Data
    public static class Modell {
        @NotBlank(message = "不能为空")
        private String name;

        private String address;
    }

    @Override
    public void handle(RequestAttr requestModel, Integer userId) throws ApiException {
        HttpServletRequest request = getRequest();
        HttpServletResponse response = getResponse();
        Modell modell = transAndSimpleValid(requestModel, Modell.class);
        addResult("k", "v");
    }


}
