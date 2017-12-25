package com.zhouwei.helloapt.db;

import android.content.ContentValues;
import android.util.Log;

import java.lang.reflect.Field;


/**
 * Created by zhouwei on 2017/12/22.
 */

public abstract class Saveable {
    // save 回调
    public void save(ContentValues values) {
        Field[] fields = getClass().getDeclaredFields();
        if (null != fields) {
            for (int i = 0; i < fields.length; i++) {
                try {
                    Field field = fields[i];
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    String value = String.valueOf(field.get(this));
                    values.put(fieldName, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 创建对象的是时候回调
    public abstract void newInstance(ContentValues values);
}
