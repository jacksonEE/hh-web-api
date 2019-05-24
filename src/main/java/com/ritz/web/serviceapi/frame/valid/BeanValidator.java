package com.ritz.web.serviceapi.frame.valid;

import com.ritz.web.serviceapi.frame.ex.ApiException;
import com.ritz.web.serviceapi.frame.http.ApiStatus;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public final class BeanValidator {

    private static final Validator VALIDATOR = Validation
            .byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();

    public static void valid(Object obj) throws ApiException {
        Set<ConstraintViolation<Object>> constraintViolations = VALIDATOR.validate(obj);
        if (!constraintViolations.isEmpty()) {
            Iterator<ConstraintViolation<Object>> iterator = constraintViolations.iterator();
            ConstraintViolation<Object> constraintViolation = iterator.next();
            throw new ApiException(ApiStatus.FIELD_INVALID.getCode(), constraintViolation.getMessage());
        }
    }

    public static void valid(Object obj, boolean isCollection) throws ApiException {
        if (isPrimitive(obj)) return;
        if (isCollection) {
            Collection c = (Collection) obj;
            if (!c.isEmpty()) {
                Iterator iterator = c.iterator();
                while (iterator.hasNext()) {
                    Object next = iterator.next();
                    if (!isPrimitive(next)) {
                        valid(next);
                    }
                }
            }
        } else {
            valid(obj);
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            if (declaredFields.length > 0) {
                for (Field field : declaredFields) {
                    field.setAccessible(true);
                    Object value;
                    try {
                        value = field.get(obj);
                    } catch (IllegalAccessException e) {
                        log.error("参数校验失败:{}", e);
                        throw new ApiException(ApiStatus.FIELD_INVALID);
                    }
                    if (value instanceof Collection) {
                        valid(value, true);
                    } else {
                        valid(value);
                    }
                }
            }
        }
    }

    private static boolean isPrimitive(Object obj) {
        Class<?> aClass = obj.getClass();
        return (aClass.isPrimitive() || aClass == String.class
                || Number.class.isAssignableFrom(aClass));
    }

}
