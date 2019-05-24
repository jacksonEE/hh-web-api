package com.ritz.web.serviceapi.frame.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Component
@Inherited
public @interface Api {

    String value() default "";

    boolean needLogin() default true;

    boolean isManager() default false;

    String desc() default "";
}
