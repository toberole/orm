package com.zhouwei.helloapt;

import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhouwei.helloapt.plugin.PluginUtil;

import java.io.File;

public class PluginActivity1 extends AppCompatActivity implements View.OnClickListener {

    private android.widget.TextView text;
    private android.widget.RelativeLayout reLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin);

        this.reLayout = (RelativeLayout) findViewById(R.id.re_Layout);
        this.text = (TextView) findViewById(R.id.text);

        reLayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String apkPath = Environment.getExternalStorageDirectory().toString() + File.separator + "testClassLoader.apk";
        ApplicationInfo apkinfo = PluginUtil.getUninstallApkInfo(this, apkPath);
        String packageName = null;
        if (null != apkinfo) {
            packageName = apkinfo.packageName;
        }

        try {
            Resources resources = PluginUtil.getPluginResources(apkPath, PluginActivity1.this);
            // , packageName, "R$drawable", "about_log"
            int resID = PluginUtil.getResIDValue(PluginActivity1.this, apkPath, packageName, "R$drawable", "about_log");
            Drawable drawable = resources.getDrawable(resID);
            reLayout.setBackgroundDrawable(drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
