package com.jnsw.core.anotation;

/**
 * Created by foxundermoon on 2015/7/30.
 */

import android.renderscript.Element;

import com.j256.ormlite.field.DataType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RemoteDatabaseField {
    boolean isID() default false;
}
