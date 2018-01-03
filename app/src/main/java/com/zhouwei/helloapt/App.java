package com.zhouwei.helloapt;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

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
        // HotFixDexUtils.loadFixDex(base);

        // 应用启动的时候就需要注入 因为有可能需要替换的dex 里面的class已经加载进虚拟机了缓存起来了
        // 在这之后在动态的加载dex可能就无效 注意类的加载机制
        // FixDexUtils.loadFixedDex(this, Environment.getExternalStorageDirectory());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //DaoHelper.getInstance().init(this);
        Log.i("AAAA", "APP: " + getCurProcessName(this));
    }

    String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
}
