package com.zhouwei.helloapt.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.zhouwei.helloapt.R;

public class DTProgressActivity extends AppCompatActivity {
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dtprogress);

        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mProgressBar.setMax(100);
        mProgressBar.setProgress(5);
    }
}
