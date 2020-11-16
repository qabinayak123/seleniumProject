package com.qualitrix.annotation.values;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Qualitrix on DD/MM/YYY.
 *
 * @author
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Author {
    /**
     * This is to describe author of testcases.
     *
     * @return default
     */
    String name() default "";
}