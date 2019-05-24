package com.ritz.web.serviceapi.frame.core;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ritz.web.serviceapi.frame.ex.ApiException;
import com.ritz.web.serviceapi.frame.http.RequestModel;
import com.ritz.web.serviceapi.frame.valid.BeanValidator;

public abstract class AbstractApiHandler implements ApiHandler {


    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    protected <T> T trans(RequestModel requestModel, Class<T> cls) {
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
     * e.g.
     * class Complex {
     *     @NotBlank
     *     String id; √
     *     School {
     *       @NotBlank
     *       String id; ×
     *     }
     * }
     *
     */
    protected <T> T transAndSimpleValid(RequestModel requestModel, Class<T> cls) throws ApiException {
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
     * e.g.
     * class Complex {
     *     @NotBlank
     *     String id; √
     *     School {
     *       @NotBlank
     *       String id; √
     *     }
     * }
     *
     */
    protected <T> T transAndComplexValid(RequestModel requestModel, Class<T> cls) throws ApiException {
        T trans = trans(requestModel, cls);
        if (trans != null) {
            BeanValidator.valid(trans, false);
        }
        return trans;
    }

}
