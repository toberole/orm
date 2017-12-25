package com.zhouwei.helloapt.bean;

import com.zhouwei.DBEntity;
import com.zhouwei.Property;
import com.zhouwei.helloapt.db.Saveable;

/**
 * Created by zhouwei on 2017/12/23.
 */

public class Person extends Saveable{
    private String tag;

    private String mag;

    private String time;

    @Property(type = "String")
    private String thread;

}
