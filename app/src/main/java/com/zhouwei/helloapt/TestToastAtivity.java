package com.zhouwei.helloapt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zhouwei.helloapt.util.CustomToast;

public class TestToastAtivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toast_ativity);
    }

    public void show(View v) {
        Log.i("AAAA","show");
        CustomToast.makeText(TestToastAtivity.this, "可控制的Toast", Toast.LENGTH_SHORT);
        CustomToast.show();
    }

    public void hide(View v) {
        Log.i("AAAA","hide");
        CustomToast.hide();
    }
}
