package com.zhouwei.helloapt.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zhouwei.helloapt.R;
import com.zhouwei.helloapt.view.CountDownTimerView;

public class TestCountDownTimerViewActivity extends AppCompatActivity {

    private com.zhouwei.helloapt.view.CountDownTimerView ctv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_count_down_timer_view);
        this.ctv = (CountDownTimerView) findViewById(R.id.ctv);

        ctv.addClickListener(new CountDownTimerView.ClickListener() {
            @Override
            public void onClick(View v) {
                ctv.start();
            }
        });
    }
}
