package com.ritz.web.serviceapi.frame.core;

import com.ritz.web.serviceapi.frame.annotation.Api;
import com.ritz.web.serviceapi.frame.ex.ApiException;
import com.ritz.web.serviceapi.frame.http.ApiStatus;
import com.ritz.web.serviceapi.frame.http.RequestAttr;
import com.ritz.web.serviceapi.frame.http.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class ApiEngine {

    // ApiHandler mapping
    private Map<String, ApiHandler> mapping;
    @Autowired
    private AccessTokenService accessTokenService;
    @Autowired
    private List<ApiHandler> handlers;
    @Value("${token.time-out:2}")
    private Integer timeOut;
    @Value("${api.time-out:10}")
    private Integer apiTimeOut;

    private static final String AUTHORIZATION = "Authorization";

    public ApiEngine() {
    }

    @PostConstruct
    private void init() throws RuntimeException {
        this.mapping = new HashMap<>();
        if (null != this.handlers && !handlers.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ApiHandler handler : this.handlers) {
                Api api = handler.getClass().getAnnotation(Api.class);
                if (api == null) {
                    continue;
                }
                if (StringUtils.isBlank(api.value())) {
                    throw new RuntimeException("api value is blank");
                }
                sb.append("\r\n  ").append(api.value());
                mapping.put(api.value(), handler);
            }
            if (StringUtils.isNotBlank(sb.toString())) {
                log.info("\r\nApiHandler Loaded:" + sb.toString());
            } else {
                log.info("No ApiHandler Loaded");
            }
            this.handlers = null;
        }
    }

    /**
     * handle request
     *
     * @param request api request
     * @return response
     */
    public Response handle(RequestAttr ra, HttpServletRequest request, HttpServletResponse response) {
        log.info("请求参数:{}", ra);
        String apiName = ra.getApi();
        if (StringUtils.isBlank(apiName)) {
            log.warn("api is blank");
            return Response.failure(ApiStatus.API_NOT_FOUND);
        }

        ApiHandler handler = mapping.get(apiName);
        if (handler == null) {
            log.info(String.format("ApiHandler `%s` not found", apiName));
            return Response.failure(ApiStatus.API_NOT_FOUND);
        }

        try {
            String tokenId = request.getHeader(AUTHORIZATION);
            Api api = handler.getClass().getAnnotation(Api.class);
            Integer userId = 0;
            if (api.needLogin()) {
                log.info("请求token:" + tokenId);
                if (StringUtils.isBlank(tokenId)) {
                    throw new ApiException(ApiStatus.NO_LOGIN);
                }
                Token t = accessTokenService.find(tokenId);
                if (t == null) {
                    throw new ApiException(ApiStatus.NO_LOGIN);
                }
                long time = System.currentTimeMillis() - t.getLoginTime().getTime();
                if (time > timeOut * 3600 * 1000 * 24) {
                    accessTokenService.remove(tokenId);
                    throw new ApiException(ApiStatus.TOKEN_EXPIRED);
                }
                UserType userType = t.getUserType();
                if ((api.isManager() && userType == UserType.USER) ||
                        (!api.isManager() && userType == UserType.MANAGER)) {
                    throw new ApiException(ApiStatus.FORBIDDEN);
                }
                userId = t.getUserId();
                log.info("当前请求用户:{},类型:{}", userId, t.getUserType().name());
            }
            handler.initial(request, response);
            handler.handle(ra, userId);
            return Response.success(handler.getResultMap());
        } catch (ApiException ex) {
            return Response.failure(ex.getCode(), ex.getMessage(), ex.getExtendMsg());
        } catch (Exception ex) {
            if (ex.getCause() instanceof TimeoutException) {
                log.error("{} 请求超时", apiName);
                return Response.failure(ApiStatus.REQUEST_TIME_OUT);
            }
            log.error("请求{} ,服务异常:", apiName, ex);
            return Response.failure(ApiStatus.ERROR);
        }
    }

    /**
     * handle batch api request
     *
     * @param ras      ras
     * @param request  request
     * @param response response
     * @return responses
     */
    public Response handleBatch(
            List<RequestAttr> ras, HttpServletRequest request, HttpServletResponse response) {
        log.info("=====执行batch请求=====");
        long start = System.currentTimeMillis();
        final Map<String, Object> result = new HashMap<>();
        if (ras != null && !ras.isEmpty()) {
            List<CompletableFuture> completableFutures = new ArrayList<>();
            ras.forEach(ra -> completableFutures.add(
                    CompletableFuture.supplyAsync(() -> handle(ra, request, response))
                            .thenAcceptAsync(r -> result.put(ra.getApi(), r))
            ));
            try {
                CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]))
                        .get(apiTimeOut, TimeUnit.SECONDS);
            } catch (Exception e) {
                // ignore
            }
        }
        log.info("=====完成batch请求,耗时:{}ms=====", System.currentTimeMillis() - start);
        return Response.success(result);
    }
}
