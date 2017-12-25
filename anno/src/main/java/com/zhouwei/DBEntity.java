package com.zhouwei;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhouwei on 2017/12/23.
 */

/**
 * 使用注解的时候 不传入dbName 那么生成的SQLiteOpenHelper 类名称即为SQLiteDb
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface DBEntity {
    String dbName() default "SQLiteDB";
    int dbVersion() default 1;
}
