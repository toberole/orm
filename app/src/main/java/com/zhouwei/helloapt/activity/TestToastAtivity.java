package com.zhouwei.helloapt.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zhouwei.helloapt.R;
import com.zhouwei.helloapt.util.ToastCustom;

public class TestToastAtivity extends AppCompatActivity {

    private ToastCustom toastCustom;
    private android.widget.Button btnshow;
    private android.widget.Button btnhide;
    private int y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toast_ativity);
        this.btnhide = (Button) findViewById(R.id.btn_hide);
        this.btnshow = (Button) findViewById(R.id.btn_show);
        toastCustom = ToastCustom.makeText(TestToastAtivity.this, "测试自定义Toast", 2);

        MyBroadcastReceiver receiver = new MyBroadcastReceiver();
        IntentFilter intent = new IntentFilter();
        intent.addAction("com.zhouwei.dy");
        registerReceiver(receiver, intent);

        y = btnhide.getTop();

        //
//        this.sendStickyBroadcast();
//        this.removeStickyBroadcast();

        View view = null;
        ViewGroup viewGroup = null;

    }

    public void show(View v) {
        Log.i("AAAA", "show");
        //toastCustom.show();
//        Intent intent = new Intent(TestToastAtivity.this, TestService.class);
//        TestToastAtivity.this.startService(intent);

        Intent intent = new Intent("com.zhouwei.dy");
        sendBroadcast(intent);

//        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                float value = (float) animation.getAnimatedValue();
//                btnhide.scrollTo(0, (int) (y + value * 10));
//            }
//        });
//
//        valueAnimator.start();

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(btnhide, "translationY", 0, 20);
        objectAnimator.setDuration(500);
        objectAnimator.start();
    }

    public void hide(View v) {
        Log.i("AAAA", "hide");
        //toastCustom.cancel();
    }
}
