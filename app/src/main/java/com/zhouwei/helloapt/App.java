package com.zhouwei.helloapt;

import android.app.Application;
import android.content.Context;

import com.zhouwei.helloapt.db.DaoHelper;
import com.zhouwei.helloapt.hook.Hook;

/**
 * Created by zhouwei on 2017/12/25.
 */

public class App extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Hook.hookInstrumentation();
        Hook.hookHandler();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DaoHelper.getInstance().init(this);


    }
}
