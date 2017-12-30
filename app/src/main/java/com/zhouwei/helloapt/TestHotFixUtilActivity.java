package com.zhouwei.helloapt;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zhouwei.helloapt.fixbug.FileUtils;
import com.zhouwei.helloapt.fixbug.FixDexUtils1;

import java.io.File;
import java.io.IOException;

public class TestHotFixUtilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_hot_fixutil);
    }

    public void inject(View view) {

        // 无bug的classes2.dex文件存放地址
        String sourceFile = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                + "classes2.dex";

        // 系统的私有目录
        String targetFile = this.getDir("odex", Context.MODE_PRIVATE).getAbsolutePath() + File.separator
                + "classes2.dex";

        try {
            // android 系统的原因，如果加载.dex文件，必须放到私有目录odex下
            // 复制文件到私有目录
            FileUtils.copyFile(sourceFile, targetFile);

            // 加载.dex文件
            FixDexUtils1.loadFixDex(this.getApplication());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void test(View view) {
        Test.show(this);
    }
}
