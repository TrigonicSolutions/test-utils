package com.trigonic.utils.test.junit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface SuiteBaseClass {
    /**
     * @return the base class of the suite
     */
    public Class<?> value();

    /**
     * @return the package name under which to search - if omitted, this will be the package of the base class
     */
    public String packageName() default "";
}