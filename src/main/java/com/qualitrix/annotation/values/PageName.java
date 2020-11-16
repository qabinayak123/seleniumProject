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

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PageName {
    /**
     * This is to describe name of the page.
     *
     * @return default
     */
    String value() default "";
}