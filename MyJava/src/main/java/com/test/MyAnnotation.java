package com.test;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * created by zhaoyuntao
 * on 05/04/2022
 * description:
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface MyAnnotation {
    String value() default "";
}
