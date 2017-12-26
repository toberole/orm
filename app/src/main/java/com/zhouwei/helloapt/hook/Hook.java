package com.zhouwei.helloapt.hook;

import android.app.Instrumentation;
import android.content.Context;
import android.os.Handler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by zhouwei on 2017/12/26.
 */

public class Hook {
    /**
     * hook ActivityThread里面的handler
     */
    public static void hookHandler() {
        try {
            // ActivityThread就是传说中的UI线程 一个APP里面只有一个UI线程
            Class activityThreadClass = Class.forName("android.app.ActivityThread");

            Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
            currentActivityThreadMethod.setAccessible(true);

            // 获取主线程UI线程
            Object activityThread = currentActivityThreadMethod.invoke(null);

            // 获取ActivityThread里面的mH
            Field mH = activityThreadClass.getDeclaredField("mH");
            mH.setAccessible(true);

//            Field launchActivity = mH.getClass().getDeclaredField("LAUNCH_ACTIVITY");
//            int launchActivityAction = (int) launchActivity.get(mH);

            // 获取handler
            Handler handler = (Handler) mH.get(activityThread);

            // 获取handler的mCallBack字段
            Field mCallback = Handler.class.getDeclaredField("mCallback");
            mCallback.setAccessible(true);

            // 设置自己实现的接口
            mCallback.set(handler, new UserHandle(handler, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hookInstrumentation() {
        try {
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Method currentActivityThread = activityThread.getDeclaredMethod("currentActivityThread");
            currentActivityThread.setAccessible(true);
            //获取主线程对象
            Object activityThreadObject = currentActivityThread.invoke(null);
            //获取Instrumentation字段
            Field mInstrumentation = activityThread.getDeclaredField("mInstrumentation");
            mInstrumentation.setAccessible(true);
            Instrumentation instrumentation = (Instrumentation) mInstrumentation.get(activityThreadObject);
            UserInstrumentation userInstrumentation = new UserInstrumentation(instrumentation);
            //替换掉原来的,就是把系统的instrumentation替换为自己的Instrumentation对象
            mInstrumentation.set(activityThreadObject, userInstrumentation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
