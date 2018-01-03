package com.zhouwei.helloapt.activity;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhouwei.helloapt.R;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.DexClassLoader;

public class PluginActivity extends AppCompatActivity {

    public static final String TAG = "DexClassLoaderApk";
    public static final String PKG_NAME = "pkgName";
    public static final String APK_PATH = "testClassLoader.apk";
    public static final String ADDSSETPATH = "addAssetPath";
    public static final String DEX = "dex";

    public static final String IMAGE_ID = "about_log";// 插件里面图片的名字
    public static final String DRAWABLE = "R$drawable";
    public TextView mTextView;
    //背景的布局
    public RelativeLayout mLayout;
    public Map<String, String> apkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin);
        final String apkPath = Environment.getExternalStorageDirectory().toString() + File.separator + APK_PATH;
        mTextView = (TextView) findViewById(R.id.text);
        mLayout = (RelativeLayout) findViewById(R.id.re_Layout);


        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //一定要记得加上android.permission.READ_EXTERNAL_STORAGE权限，不然死活都拿不到数据
                //我就换了一个这个错误，如果发现代码没问题，网上找也没问题，这个时候应该思考是不是没有加权限
                apkInfo = getUninstallApkInfo(PluginActivity.this, apkPath);
                String packageName = apkInfo.get(PKG_NAME);
                if (null != packageName) {
                    try {
                        dynamicLoadApk(apkPath, packageName);
                        Log.i(TAG, "packageName: " + packageName);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i(TAG, "change image fail");
                    }
                } else {
                    Log.i(TAG, "package is null");
                }
            }
        });
    }

    /**
     * 获取未安装apk的信息
     *
     * @param context
     * @param apkPath apk文件的path
     * @return
     */
    private Map<String, String> getUninstallApkInfo(Context context, String apkPath) {
        Map hashMap = new HashMap<String, String>();
        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        if (null != pkgInfo) {
            ApplicationInfo appInfo = pkgInfo.applicationInfo;
            String pkgName = appInfo.packageName;//包名
            hashMap.put(PKG_NAME, pkgName);
        } else {
            Log.d(TAG, "program don't get apk package information");
        }
        return hashMap;
    }

    /**
     * @param apkPath
     * @return 得到对应插件的Resource对象
     */
    private Resources getPluginResources(String apkPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            //反射调用方法addAssetPath(String path)
            Method addAssetPath = assetManager.getClass().getMethod(ADDSSETPATH, String.class);
            //将未安装的Apk文件的添加进AssetManager中,第二个参数是apk的路径
            addAssetPath.invoke(assetManager, apkPath);
            Resources superRes = this.getResources();
            Resources mResources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
            return mResources;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加载apk获得内部资源,并且替换背景
     *
     * @param apkPath        apk目录
     * @param apkPackageName apk名字,带.apk
     * @throws Exception
     */
    private void dynamicLoadApk(String apkPath, String apkPackageName) throws Exception {
        //在应用安装目录下创建一个名为app_dex文件夹目录,如果已经存在则不创建,这个目录主要是最优化目录，用于缓存dex文件
        File optimizedDirectoryFile = getDir(DEX, Context.MODE_PRIVATE);

        //打印路径 理论上是/data/data/package/app_dex
        Log.v(TAG, optimizedDirectoryFile.getPath().toString());

        //构建DexClassLoader
        DexClassLoader dexClassLoader = new DexClassLoader(apkPath, optimizedDirectoryFile.getPath(), null, ClassLoader.getSystemClassLoader());

        //通过使用apk自己的类加载器，反射出R类中相应的内部类进而获取我们需要的资源id
        Class<?> clazz = dexClassLoader.loadClass(apkPackageName + "." + DRAWABLE);

        //得到名为about_log的这张图片字段,这个图片是为安装apk里面的图片
        Field field = clazz.getDeclaredField(IMAGE_ID);

        //得到图片id
        int resId = field.getInt(R.id.class);
        //得到插件apk中的Resource
        Resources mResources = getPluginResources(apkPath);
        if (mResources != null) {
            //通过插件apk中的Resource得到resId对应的资源
            Drawable btnDrawable = mResources.getDrawable(resId);
            mLayout.setBackgroundDrawable(btnDrawable);
        } else {
            Log.d(TAG, "mResources is null");
        }
    }
}
