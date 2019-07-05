package com.ritz.web.serviceapi.frame.core;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ritz.web.serviceapi.frame.ex.ApiException;
import com.ritz.web.serviceapi.frame.http.RequestAttr;
import com.ritz.web.serviceapi.frame.valid.BeanValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractApiHandler implements ApiHandler {


    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final ThreadLocal<HttpServletRequest> REQUEST_HOLDER = new ThreadLocal<>();
    private static final ThreadLocal<HttpServletResponse> RESPONSE_HOLDER = new ThreadLocal<>();
    private static final ThreadLocal<Map<String, Object>> RESULT_HOLDER = new ThreadLocal<>();

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    protected <T> T trans(RequestAttr requestModel, Class<T> cls) {
        if (requestModel != null && requestModel.getParams() != null) {
            return MAPPER.convertValue(requestModel.getParams(), cls);
        }
        return null;
    }


    /**
     * transfer to target model and valid model without complex field
     * <p>
     * Person {
     *
     * @param requestModel requestModel
     * @param cls          target class
     * @param <T>          target type
     * @return T
     * @throws ApiException ex
     *                      e.g.
     *                      class Complex {
     * @NotBlank String id; √
     * School {
     * @NotBlank String id; ×
     * }
     * }
     */
    protected <T> T transAndSimpleValid(RequestAttr requestModel, Class<T> cls) throws ApiException {
        T trans = trans(requestModel, cls);
        if (trans != null) {
            BeanValidator.valid(trans);
        }
        return trans;
    }

    /**
     * transfer to target model and valid model with complex field
     * <p>
     * Person {
     *
     * @param requestModel requestModel
     * @param cls          target class
     * @param <T>          target type
     * @return T
     * @throws ApiException ex
     *                      e.g.
     *                      class Complex {
     * @NotBlank String id; √
     * School {
     * @NotBlank String id; √
     * }
     * }
     */
    protected <T> T transAndComplexValid(RequestAttr requestModel, Class<T> cls) throws ApiException {
        T trans = trans(requestModel, cls);
        if (trans != null) {
            BeanValidator.valid(trans, false);
        }
        return trans;
    }

    @Override
    public final void initial(HttpServletRequest request, HttpServletResponse response) {
        RESULT_HOLDER.set(new HashMap<>());
        REQUEST_HOLDER.set(request);
        RESPONSE_HOLDER.set(response);
        System.out.println(Thread.currentThread().getName());
    }

    @Override
    public final HttpServletRequest getRequest() {
        return REQUEST_HOLDER.get();
    }

    @Override
    public final HttpServletResponse getResponse() {
        return RESPONSE_HOLDER.get();
    }

    @Override
    public final Map<String, Object> getResultMap() {
        return RESULT_HOLDER.get();
    }

    @Override
    public final void addResult(String key, Object value) {
        Map<String, Object> stringObjectMap = RESULT_HOLDER.get();
        System.out.println(stringObjectMap);
        System.out.println(Thread.currentThread().getName());
    }
}
