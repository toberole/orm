package com.zhouwei.helloapt.util;

import android.text.TextUtils;
import android.util.Log;

import com.zhouwei.helloapt.bean.LogInfo;


/**
 * Created by zhouwei on 2017/12/26.
 */

public class L {
    public static boolean isDebug = true;
    public static boolean isSave = true;
    private static String LName = L.class.getName();

    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }

        if (isSave) {
            save(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }

        if (isSave) {
            save(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }


        if (isSave) {
            save(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
        if (isSave) {
            save(tag, msg);
        }
    }

    private static void save(String tag, String msg) {
        if (!TextUtils.isEmpty(tag) && !TextUtils.isEmpty(msg)) {
            LogInfo appLog = new LogInfo();
            appLog.setTag(tag);
            appLog.setMsg(msg);
            appLog.setThread(Thread.currentThread().getName());

            String extraInfo = Log.getStackTraceString(new Throwable());
            appLog.setExtraInfo(extraInfo);

           // DaoHelper.getInstance().save(appLog);
        }
    }

    private static String getExtraInfo(StackTraceElement[] sElements) {
        StringBuilder sb = new StringBuilder();
        if (null != sElements) {
            for (StackTraceElement stackTraceElement : sElements) {
                String className = stackTraceElement.getClassName();
                String fileName = stackTraceElement.getFileName();
                String methodName = stackTraceElement.getMethodName();
                int lineNumber = stackTraceElement.getLineNumber();

                sb.append(className + "  ")
                        .append(fileName + "  ")
                        .append(methodName + "  ")
                        .append(lineNumber + "  ");

            }
        }

        return sb.toString();
    }

}
