package com.jnsw.core.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by foxundermoon on 2015/7/30.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface  RemoteTable {
    public String tableName() default "";
    public boolean sameAsLocalTableName() default false;
}
