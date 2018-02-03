package com.zhouwei.helloapt.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zhouwei.helloapt.R;

public class TestProgressActivity extends AppCompatActivity {

    private View ivcircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_progress);
        ivcircle = findViewById(R.id.iv_circle);


        ivcircle.post(new Runnable() {
            @Override
            public void run() {

                int pivotX = (int) (ivcircle.getX() + ivcircle.getWidth() / 2);
                int pivotY = (int) (ivcircle.getHeight() + 100);

                //ivcircle.setPivotX(pivotX);
                ivcircle.setPivotY(pivotY);

                AnimatorSet set = new AnimatorSet();

                set.setDuration(1000);

                ObjectAnimator rotation = ObjectAnimator.ofFloat(ivcircle, "rotation", 0, 360);
                rotation.setRepeatCount(5);

                ObjectAnimator scaleX = ObjectAnimator.ofFloat(ivcircle, "scaleX", 1.5f, 1.0f);
                scaleX.setRepeatCount(5);

                ObjectAnimator scaleY = ObjectAnimator.ofFloat(ivcircle, "scaleY", 1.5f, 1.0f);
                scaleY.setRepeatCount(5);

                set.playTogether(rotation, scaleX, scaleY);
                set.start();
            }
        });


    }
}
