package com.zhouwei.helloapt;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zhouwei.BindView;
import com.zhouwei.OnClick;

public class TestActivity extends Activity {
    @BindView
    public Button btn_bindView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        Looper.prepare();
        Handler handler = new Handler();
        Looper.loop();
    }

    @OnClick({R.id.btn_bindView})
    public void bindViewOnClick(View v) {
        Log.i("AAAA", "bindViewOnClick");
    }
}
