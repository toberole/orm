package com.zhouwei.helloapt.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zhouwei.helloapt.R;
import com.zhouwei.helloapt.fixbug.FileUtils;
import com.zhouwei.helloapt.fixbug.FixDexUtils;
import com.zhouwei.helloapt.fixbug.HotFixDexUtils;

import java.io.File;
import java.io.IOException;

public class TestHotFixUtilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_hot_fixutil);
    }


    public void inject(View view) {
        Log.i("AAAA", "inject");

        // 无bug的classes2.dex文件存放地址
        String sourceFile = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                + "classes2.dex";

        // 系统的私有目录
        String targetFile = this.getDir("odex", Context.MODE_PRIVATE).getAbsolutePath() + File.separator
                + "classes2.dex";

        try {
            // android 4.1系统classloader加载.dex文件，必须放到私有目录odex下 否则无法加载 处于安全原因
            // 复制文件到私有目录
            FileUtils.copyFile(sourceFile, targetFile);

            // 加载.dex文件
            HotFixDexUtils.loadFixDex(this.getApplication());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void inject1(View view) {
        Log.i("AAAA", "inject1");
        // 加载.dex文件
        FixDexUtils.loadFixedDex(this.getApplication(), Environment.getExternalStorageDirectory());
    }


    public void test(View view) {
        Test test = new Test();
        Toast.makeText(TestHotFixUtilActivity.this, test.show(), Toast.LENGTH_SHORT).show();
    }
}
