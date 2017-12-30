package com.zhouwei.helloapt.fixbug;

import android.content.Context;

import java.io.File;

import dalvik.system.PathClassLoader;

/**
 * Created by zhouwei on 2017/12/30.
 */

public class FixDexManager {
    private Context context;
    private File mDexDir;

    public void fixDex(String dexPath) {
        mDexDir = context.getDir("odex", Context.MODE_PRIVATE);
        PathClassLoader classLoader = (PathClassLoader) context.getClassLoader();
        Object elements = getElementsByClassloader(classLoader);
        File srcPath = new File(dexPath);
        File targetpath = new File(mDexDir, srcPath.getName());
    }

    private Object getElementsByClassloader(PathClassLoader classLoader) {
        return null;
    }
}
