package com.zhouwei;

import com.squareup.javapoet.ClassName;


public class TypeUtil {
    public static final ClassName FINDER = ClassName.get("com.zhouwei.bindview.finder", "Finder");
    public static final ClassName INJECTOR = ClassName.get("com.zhouwei.bindview", "Injector");
    public static final ClassName ONCLICK_LISTENER = ClassName.get("android.view", "View", "OnClickListener");
    public static final ClassName ANDROID_VIEW = ClassName.get("android.view", "View");
}
