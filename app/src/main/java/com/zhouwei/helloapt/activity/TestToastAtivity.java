package com.zhouwei.helloapt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.zhouwei.helloapt.R;
import com.zhouwei.helloapt.service.TestService;
import com.zhouwei.helloapt.util.ToastCustom;

public class TestToastAtivity extends AppCompatActivity {

    private ToastCustom toastCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toast_ativity);
        toastCustom = ToastCustom.makeText(TestToastAtivity.this, "测试自定义Toast", 2);
    }

    public void show(View v) {
        Log.i("AAAA", "show");
        //toastCustom.show();
        Intent intent = new Intent(TestToastAtivity.this, TestService.class);
        TestToastAtivity.this.startService(intent);
    }

    public void hide(View v) {
        Log.i("AAAA", "hide");
        //toastCustom.cancel();
    }
}
