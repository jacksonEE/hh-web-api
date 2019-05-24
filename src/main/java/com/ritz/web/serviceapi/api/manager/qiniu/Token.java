package com.ritz.web.serviceapi.api.manager.qiniu;

import com.qiniu.util.Auth;
import com.ritz.web.serviceapi.configs.QiniuProperties;
import com.ritz.web.serviceapi.frame.annotation.Api;
import com.ritz.web.serviceapi.frame.core.AbstractApiHandler;
import com.ritz.web.serviceapi.frame.ex.ApiException;
import com.ritz.web.serviceapi.frame.http.RequestModel;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Api(value = "manager.qiniu.token", isManager = true)
public class Token extends AbstractApiHandler {
    @Autowired
    private QiniuProperties qiniuProperties;

    @Override
    public void handle(RequestModel requestModel,
                       Map<String, Object> result,
                       HttpServletRequest request,
                       HttpServletResponse response,
                       Integer userId) throws ApiException {
        Auth auth = Auth.create(qiniuProperties.getAccessKey(), qiniuProperties.getSecretKey());
        String upToken = auth.uploadToken(qiniuProperties.getBucket());
        result.put("token", upToken);
    }
}
