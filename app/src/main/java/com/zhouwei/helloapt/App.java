package com.zhouwei.helloapt;

import android.app.Application;
import android.content.Context;

import com.zhouwei.helloapt.fixbug.FixDexUtils1;

/**
 * Created by zhouwei on 2017/12/25.
 */

public class App extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        Hook.hookInstrumentation();
//        Hook.hookHandler();

        //  MultiDex.install(base);
        // Hook.hookActivityThread();
        FixDexUtils1.loadFixDex(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //DaoHelper.getInstance().init(this);
    }
}
