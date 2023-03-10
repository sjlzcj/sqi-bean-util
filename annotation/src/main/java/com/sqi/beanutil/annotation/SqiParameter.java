package com.sqi.beanutil.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.CLASS)
public @interface SqiParameter {
    String name();

    Class<?> type() default Void.class;
}
