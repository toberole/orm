package com.zhouwei.helloapt.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.zhouwei.helloapt.R;
import com.zhouwei.helloapt.view.TestViewGroup;

public class TestViewActivity extends AppCompatActivity {

    private com.zhouwei.helloapt.view.TestViewGroup tvg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_view);
        this.tvg = (TestViewGroup) findViewById(R.id.tvg);

        tvg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
               // Log.i("AAAA", "onTouch");
                return false;
            }
        });

        tvg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("AAAA", "onClick");
            }
        });

        tvg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("AAAA","onLongClick");
                return true;
            }
        });
    }
}
