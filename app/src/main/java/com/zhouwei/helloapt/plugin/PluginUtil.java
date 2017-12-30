package com.zhouwei.helloapt.plugin;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

import static android.content.ContentValues.TAG;


/**
 * Created by zhouwei on 2017/12/30.
 */

public class PluginUtil {
    public static final String ADDSSETPATH = "addAssetPath";
    public static final String DEX = "dex";

    /**
     * 获取未安装apk的信息
     *
     * @param context
     * @param apkPath apk文件的path
     * @return
     */
    public static ApplicationInfo getUninstallApkInfo(Context context, String apkPath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        ApplicationInfo appInfo = null;
        if (null != pkgInfo) {
            appInfo = pkgInfo.applicationInfo;
        } else {
            Log.d(TAG, "program don't get apk package information");
        }
        return appInfo;
    }

    /**
     * 加载apk获得内部资源
     *
     * @param apkPath
     * @return 得到对应插件的Resource对象
     */
    public static Resources getPluginResources(String apkPath, Context context) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            //反射调用方法addAssetPath(String path)
            Method addAssetPath = assetManager.getClass().getMethod(ADDSSETPATH, String.class);
            //将未安装的Apk文件的添加进AssetManager中,第二个参数是apk的路径
            addAssetPath.invoke(assetManager, apkPath);
            Resources superRes = context.getResources();
            Resources mResources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
            return mResources;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @param apkPath                 被加载apk的path
     * @param apkPackageName          {@link PluginUtil#getUninstallApkInfo} 被加载apk PackageName
     * @param pluginResourceClassName 需要从apk里面获取的resource对应的类名称
     * @param pluginResourceIDName    需要从apk里面获取的resource对应的id名称
     * @return
     * @throws Exception
     */
    public static int getResIDValue(Context context,
                                    String apkPath,
                                    String apkPackageName,
                                    String pluginResourceClassName,
                                    String pluginResourceIDName) throws Exception {
        //在应用安装目录下创建一个名为app_dex文件夹目录
        // 如果已经存在则不创建,这个目录主要是最优化目录
        // 用于缓存dex文件
        // 注意在android4.1之后 系统处于安全性的考虑 动态加载的dex zip apk需要在应用的私有目录里面，这种情况可以先把资源apk
        // 拷贝到应用的私有目录下面
        File optimizedDirectoryFile = context.getDir(DEX, Context.MODE_PRIVATE);
        //打印路径 理论上是/data/data/package/app_dex
        Log.v(TAG, optimizedDirectoryFile.getPath().toString());
        //构建DexClassLoader
        DexClassLoader dexClassLoader = new DexClassLoader(apkPath, optimizedDirectoryFile.getPath(), null, ClassLoader.getSystemClassLoader());
        //通过使用apk自己的类加载器，反射出R类中相应的内部类进而获取我们需要的资源id
        Class<?> clazz = dexClassLoader.loadClass(apkPackageName + "." + pluginResourceClassName);
        //得到名为about_log的这张图片字段,这个图片是为安装apk里面的图片
        Field field = clazz.getDeclaredField(pluginResourceIDName);
        //得到图片id
        int resId = field.getInt(null);
        return resId;
    }
}
