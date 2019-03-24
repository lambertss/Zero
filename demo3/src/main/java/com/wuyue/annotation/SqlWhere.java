package com.wuyue.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface SqlWhere {

    SqlWhereValue value() default SqlWhereValue.LIKE;
    String proprtityName();

    enum SqlWhereValue{
        GT(" > "),
        LT(" < "),
        GTE(" >= "),
        LTE(" <= "),
        LIKE(" like "),
        IN(" in "),
        LTDATE(" <= "),
        GTDATE(" >= ");

        private  String value;

        public String getValue() {
            return value;
        }

        SqlWhereValue(String value){
            this.value = value;
        }
    }
}


