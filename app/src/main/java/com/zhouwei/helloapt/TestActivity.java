package com.zhouwei.helloapt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.i("[app]", "==onStart==");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("[app]", "==onResume==");
    }
}
