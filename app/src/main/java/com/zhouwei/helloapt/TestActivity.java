package com.zhouwei.helloapt;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.zhouwei.BindView;
import com.zhouwei.OnClick;

public class TestActivity extends Activity {
    @BindView
    public Button btn_bindView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    @OnClick({R.id.btn_bindView})
    public void bindViewOnClick(View v) {
        Log.i("AAAA", "bindViewOnClick");
    }
}
