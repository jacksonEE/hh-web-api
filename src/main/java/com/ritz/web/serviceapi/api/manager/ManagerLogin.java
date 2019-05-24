package com.ritz.web.serviceapi.api.manager;

import com.ritz.web.serviceapi.frame.annotation.Api;
import com.ritz.web.serviceapi.frame.core.AbstractApiHandler;
import com.ritz.web.serviceapi.frame.core.AccessTokenService;
import com.ritz.web.serviceapi.frame.core.UserType;
import com.ritz.web.serviceapi.frame.ex.ApiException;
import com.ritz.web.serviceapi.frame.http.ApiStatus;
import com.ritz.web.serviceapi.frame.http.RequestModel;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.Map;

@Api(value = "manager.login", isManager = true, needLogin = false)
public class ManagerLogin extends AbstractApiHandler {

    @Data
    private static class Manager {
        @NotBlank(message = "用户名不能为空")
        private String username;
        @NotBlank(message = "密码不能为空")
        private String password;
    }

    @Autowired
    private AccessTokenService accessTokenService;

    @Override
    public void handle(
            RequestModel requestModel,
            Map<String, Object> result,
            HttpServletRequest request,
            HttpServletResponse response,
            Integer userId) throws ApiException {
        Manager manager = transAndSimpleValid(requestModel, Manager.class);
        if (manager.getUsername().equals("root") && manager.getPassword().equals("123456")) {
            String tokenId = accessTokenService.save(1, UserType.MANAGER);
            result.put("token", tokenId);
            return;
        }
        throw new ApiException(ApiStatus.USERNAME_NOT_EXISTED.getCode(), "用户名或密码错误");
    }
}
