package com.zhouwei.helloapt;

import android.app.Application;

import com.zhouwei.helloapt.util.DaoHelper;

/**
 * Created by zhouwei on 2017/12/25.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DaoHelper.getInstance().init(this);
    }
}
