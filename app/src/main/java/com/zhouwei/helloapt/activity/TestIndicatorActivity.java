package com.zhouwei.helloapt.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.zhouwei.helloapt.R;

import java.util.Calendar;

public class TestIndicatorActivity extends AppCompatActivity {

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_indicator);
        calendar = Calendar.getInstance();
        testTimeFormat();
        LinearLayout container = findViewById(R.id.ll_container);

        for (int i = 0; i < 4; i++) {
            View view = new View(TestIndicatorActivity.this);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30, 30);
            params.rightMargin = 10;
            view.setLayoutParams(params);
            view.setBackgroundResource(R.drawable.indicator);
            container.addView(view);

            if (i == 2) {
                view.setEnabled(true);
            }

            if (i == 3) {
                view.setEnabled(false);
            }
        }


    }

    private static final long ONE_DAY_TIMEMILLIS = 24 * 60 * 60 * 1000;

    private void testTimeFormat() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        int dayOfWeekInMonth = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);

        // 今天当前时间
        long currentTime = System.currentTimeMillis();

        // 昨天的24点 两种写法
        long yesterday = currentTime - currentTime % ONE_DAY_TIMEMILLIS;
        long yesterday1 = (currentTime / ONE_DAY_TIMEMILLIS) * ONE_DAY_TIMEMILLIS;

        // 明天的 0点
        long tomorrow = (currentTime / ONE_DAY_TIMEMILLIS) * ONE_DAY_TIMEMILLIS + ONE_DAY_TIMEMILLIS;
    }
}
