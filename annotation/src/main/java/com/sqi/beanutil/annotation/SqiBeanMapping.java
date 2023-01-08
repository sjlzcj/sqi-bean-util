package com.sqi.beanutil.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Function;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface SqiBeanMapping {
    String source() default "";

    Class<?> sourceType() default Void.class;

    String target() default "";

    Class<?> targetType() default Void.class;

    Class<? extends Function> convertProvider() default Function.class;

    Class<? extends Function> format() default Function.class;

}
