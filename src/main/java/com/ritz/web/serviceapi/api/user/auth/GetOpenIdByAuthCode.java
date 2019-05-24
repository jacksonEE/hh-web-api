package com.ritz.web.serviceapi.api.user.auth;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.ritz.web.serviceapi.frame.RedisKeyConstant;
import com.ritz.web.serviceapi.frame.annotation.Api;
import com.ritz.web.serviceapi.frame.core.AbstractApiHandler;
import com.ritz.web.serviceapi.frame.ex.ApiException;
import com.ritz.web.serviceapi.frame.http.ApiStatus;
import com.ritz.web.serviceapi.frame.http.RequestModel;
import com.ritz.web.serviceapi.utils.RedisUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.Map;

@Slf4j
@Api(value = "user.getOpenIdByAuthCode", needLogin = false, desc = "通过授权码获取openid")
public class GetOpenIdByAuthCode extends AbstractApiHandler {

    @Autowired
    private WxMaService wxMaService;

    @Data
    private static class AuthCode {
        @NotBlank(message = "授权码不能为空")
        private String authCode;
    }

    @Override
    public void handle(
            RequestModel requestModel,
            Map<String, Object> result,
            HttpServletRequest request,
            HttpServletResponse response, Integer userId) throws ApiException {
        AuthCode authCode = transAndSimpleValid(requestModel, AuthCode.class);
        WxMaJscode2SessionResult sessionInfo;
        try {
            sessionInfo = wxMaService.getUserService().getSessionInfo(authCode.getAuthCode());
        } catch (WxErrorException e) {
            log.info("获取sessionInfo 失败 code-{}: {}", authCode.getAuthCode(), e);
            throw new ApiException(ApiStatus.ERROR);
        }
        String openid = sessionInfo.getOpenid();
        RedisUtil.set(String.format(RedisKeyConstant.USER_SESSION_INFO, openid),
                sessionInfo.getSessionKey(), 300L);
        result.put("openid", openid);
    }
}
