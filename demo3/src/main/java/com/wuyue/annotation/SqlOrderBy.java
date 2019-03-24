package com.wuyue.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.FIELD})
@Documented
public @interface SqlOrderBy {

    String value() default "CREATE_DATE desc";


}


