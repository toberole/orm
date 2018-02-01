package com.zhouwei.helloapt.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.zhouwei.helloapt.R;

public class Test_NEW_TASK_Activity extends AppCompatActivity implements View.OnClickListener {

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test__new__task_);

        btn = findViewById(R.id.btn_open);
        btn.setOnClickListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.i("AAAA","onNewIntent");
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(),Test_NEW_TASK_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }
}
