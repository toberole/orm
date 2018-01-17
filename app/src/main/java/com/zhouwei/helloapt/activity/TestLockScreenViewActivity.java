package com.zhouwei.helloapt.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zhouwei.helloapt.R;
import com.zhouwei.helloapt.view.LockScreenViewGroup;

public class TestLockScreenViewActivity extends AppCompatActivity {

    private LockScreenViewGroup lockScreenViewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_lock_screen_view);

        lockScreenViewGroup = (LockScreenViewGroup) findViewById(R.id.lockScreenViewGroup);
        int[] answers = {1,2,3,5,7,8,9};
        lockScreenViewGroup.setAnswers(answers);
    }
}
