package com.zhouwei.helloapt.hook;

import android.app.Instrumentation;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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

    /**
     * 修改 SharedPreferences 路径
     *
     * @param context
     */
    public static void hookContextImpl(Context context) {
        try {
            Class<?> clazz = Class.forName("android.app.ContextImpl");
            Method method = clazz.getDeclaredMethod("getImpl", Context.class);
            method.setAccessible(true);
            Object mContextImpl = method.invoke(null, context);
            //获取ContextImpl的实例
            Field mPreferencesDir = clazz.getDeclaredField("mPreferencesDir");
            mPreferencesDir.setAccessible(true);
            //自定义的目录假设在SD卡
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File file = new File(Environment.getExternalStorageDirectory(), "new_shared_pres");
                if (!file.exists()) {
                    file.mkdirs();
                }
                mPreferencesDir.set(mContextImpl, file);
                Log.i("[app]", "修改sp路径成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * hook NotificationManager 自定义通知的相关逻辑
     *
     * @param context
     */
    public static void hookNotificationManager(Context context) {
        try {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Method method = notificationManager.getClass().getDeclaredMethod("getService");
            method.setAccessible(true);
            //获取代理对象
            final Object sService = method.invoke(notificationManager);
            Log.i("[app]", "sService=" + sService);
            Class<?> INotificationManagerClazz = Class.forName("android.app.INotificationManager");
            Object proxy = Proxy.newProxyInstance(INotificationManagerClazz.getClassLoader(),
                    new Class[]{INotificationManagerClazz}, new NotifictionProxy(sService));
            //获取原来的对象
            Field mServiceField = notificationManager.getClass().getDeclaredField("sService");
            mServiceField.setAccessible(true);
            //替换
            mServiceField.set(notificationManager, proxy);
            Log.i("[app]", "Hook NoticeManager Success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通知代理类
     */
    public static class NotifictionProxy implements java.lang.reflect.InvocationHandler {
        private Object mObject;

        public NotifictionProxy(Object mObject) {
            this.mObject = mObject;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Log.d("[app]", "方法为:" + method.getName());
            /**
             * 做一些业务上的判断
             * 这里以发送通知为准,发送通知最终的调用了enqueueNotificationWithTag
             */
            if (method.getName().equals("enqueueNotificationWithTag")) {
                //具体的逻辑
                for (int i = 0; i < args.length; i++) {
                    if (args[i] != null) {
                        Log.d("[app]", "参数为:" + args[i].toString());
                    }
                }
                //做些其他事情，然后替换参数之类
                return method.invoke(mObject, args);
            }
            return null;
        }
    }

    /**
     * hook ActivityManagerService的代理
     * 通过代理模式可以拦截里面的所有方法
     * 比如： 可以拦截activity的启动
     */
    public static void hookActivityManagerService(Context context) throws Exception {
        // 先获取ActivityManagerNative中的gDefault
        Class<?> amnClazz = Class.forName("android.app.ActivityManagerNative");
        Field defaultField = amnClazz.getDeclaredField("gDefault");
        defaultField.setAccessible(true);
        Object gDefaultObj = defaultField.get(null);

        // 获取Singleton里面的mInstance
        Class<?> singletonClazz = Class.forName("android.util.Singleton");
        Field amsField = singletonClazz.getDeclaredField("mInstance");
        amsField.setAccessible(true);
        Object amsObj = amsField.get(gDefaultObj);

        // 动态代理Hook下钩子 勾住AMS
        amsObj = Proxy.newProxyInstance(context.getClass().getClassLoader(),
                amsObj.getClass().getInterfaces(),
                new InvocationHandler(amsObj, context));
        // 注入
        amsField.set(gDefaultObj, amsObj);
    }

    /**
     * Invocation Handler
     */
    private static class InvocationHandler implements java.lang.reflect.InvocationHandler {
        private Object mAmsObj;
        private Context context;

        public InvocationHandler(Object amsObj, Context context) {
            this.mAmsObj = amsObj;
            this.context = context;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // 拦截到所有ActivityManagerService的方法
            Log.e("TAG", "methodName" + method.getName());
            // TODO 干点自己相干的事情
//            if ("startActivity".equals(method.getName())) {
//                Log.e("Main","startActivity方法拦截了");
//
//                // 找到参数里面的第一个Intent 对象
//                Intent raw;
//                int index = 0;
//
//                for (int i = 0; i < args.length; i++) {
//                    if (args[i] instanceof Intent) {
//                        index = i;
//                        break;
//                    }
//                }
//                raw = (Intent) args[index];
//                //创建一个要被掉包的Intent
//                Intent newIntent = new Intent();
//                // 替身Activity的包名, 也就是我们自己的"包名"
//                String stubPackage = MyApplication.getContext().getPackageName();
//
//                // 这里我们把启动的Activity临时替换为 ZhanKengActivitiy
//                ComponentName componentName = new ComponentName(stubPackage, ZhanKengActivitiy.class.getName());
//                newIntent.setComponent(componentName);
//
//                // 把我们原始要启动的TargetActivity先存起来
//                newIntent.putExtra(AMSHookHelper.EXTRA_TARGET_INTENT, raw);
//
//                // 替换掉Intent, 达到欺骗AMS的目的
//                args[index] = newIntent;
//                Log.e("Main","startActivity方法 hook 成功");
//                Log.e("Main","args[index] hook = " + args[index]);
//
//            }

            return method.invoke(mAmsObj, args);
        }
    }

    /**
     * 统计哪些Activity被使用过、还可以拦截
     * 还可以通过其他的方法
     */
    public static void hookActivityThread() {
        try {
            Class clazz = Class.forName("android.app.ActivityThread");
            Method currentActivityThread = clazz.getDeclaredMethod("currentActivityThread");
            Object activityThread = currentActivityThread.invoke(null);

            // 换掉mActivities 使用我们特供的
            Field mActivities = clazz.getDeclaredField("mActivities");
            mActivities.setAccessible(true);
            Object mActivitiesValue = mActivities.get(activityThread);

            Log.i("hookActivityThread", "" + mActivitiesValue.getClass().getName());


            Object proxy = Proxy.newProxyInstance(activityThread.getClass().getClassLoader(),
                    new Class[]{java.util.Map.class}, new ArrayMapProxy(mActivitiesValue));

            mActivities.set(activityThread, proxy);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static class ArrayMapProxy implements java.lang.reflect.InvocationHandler {
        private Object mObject;

        public ArrayMapProxy(Object mObject) {
            this.mObject = mObject;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Log.i("[hookapp]", "方法为:" + method.getName());

            if (method.getName().equals("put")) {
                //具体的逻辑

            }
            return method.invoke(mObject, args);
        }


    }
}