package com.zhouwei.helloapt.hook;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.util.Log;

/**
 * Created by zhouwei on 2017/12/26.
 */

public class UserInstrumentation extends Instrumentation {
    private Instrumentation base;

    public UserInstrumentation(Instrumentation base) {
        this.base = base;
    }

    //重写创建Activity的方法
    @Override
    public Activity newActivity(ClassLoader cl, String className, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Log.i("[app]", "do anything that what you want");
        Log.i("[app]", "className=" + className + " intent=" + intent);

        return super.newActivity(cl, className, intent);
    }
}
