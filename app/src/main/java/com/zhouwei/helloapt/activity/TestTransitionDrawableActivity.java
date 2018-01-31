package com.zhouwei.helloapt.activity;

import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.zhouwei.helloapt.R;

public class TestTransitionDrawableActivity extends AppCompatActivity {

    private TextView tvtest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_transition_drawable);
        tvtest = (TextView) findViewById(R.id.tv_test);

        tvtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionDrawable drawable = (TransitionDrawable) tvtest.getBackground();
                drawable.startTransition(2000);
            }
        });
    }
}
