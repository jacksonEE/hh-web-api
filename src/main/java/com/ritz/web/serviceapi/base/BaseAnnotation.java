package com.ritz.web.serviceapi.base;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.lang.annotation.Inherited;

@Inherited
@DynamicInsert
@DynamicUpdate
public @interface BaseAnnotation {
}
