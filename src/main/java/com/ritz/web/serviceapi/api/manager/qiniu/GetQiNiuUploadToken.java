package com.ritz.web.serviceapi.api.manager.qiniu;

import com.ritz.web.serviceapi.configs.QiniuProperties;
import com.ritz.web.serviceapi.frame.annotation.Api;
import com.ritz.web.serviceapi.frame.core.AbstractApiHandler;
import com.ritz.web.serviceapi.frame.ex.ApiException;
import com.ritz.web.serviceapi.frame.http.RequestAttr;
import org.springframework.beans.factory.annotation.Autowired;

@Api(value = "manager.qiniu.token", isManager = true)
public class GetQiNiuUploadToken extends AbstractApiHandler {
    @Autowired
    private QiniuProperties qiniuProperties;

    @Override
    public void handle(RequestAttr requestModel,
                       Integer userId) throws ApiException {
       /* Auth auth = Auth.create(qiniuProperties.getAccessKey(), qiniuProperties.getSecretKey());
        String upToken = auth.uploadToken(qiniuProperties.getBucket());
        addResult("token", upToken);*/
    }
}
