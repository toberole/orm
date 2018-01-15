package com.zhouwei.helloapt.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhouwei.helloapt.R;
import com.zhouwei.helloapt.view.MyViewPager;

public class TestViewGroupActivity extends AppCompatActivity {

    private int width;
    private int height;
    private MyViewPager mvp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_view_group);
        this.mvp = (MyViewPager) findViewById(R.id.mvp);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;

        initViewPager();

        getClassLoader();
    }

    private void initViewPager() {
        for (int i = 0; i < 4; i++) {
            View v = View.inflate(TestViewGroupActivity.this, R.layout.sub_view, null);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, height);
            v.setLayoutParams(params);
            TextView textView = v.findViewById(R.id.tv_text);
            textView.setText("hello vp " + i);
            mvp.addView(v);
        }
    }
}
