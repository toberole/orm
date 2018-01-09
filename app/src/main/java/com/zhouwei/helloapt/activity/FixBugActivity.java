package com.zhouwei.helloapt.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zhouwei.helloapt.R;
import com.zhouwei.helloapt.bean.FixBug;

public class FixBugActivity extends AppCompatActivity implements View.OnClickListener {

    private android.widget.Button btnfixbug;
    private android.widget.Button btnhf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_bug);
        this.btnhf = (Button) findViewById(R.id.btn_hf);
        this.btnfixbug = (Button) findViewById(R.id.btn_fixbug);

        btnfixbug.setOnClickListener(this);
        btnhf.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fixbug:
                btn_fixbug();
                break;
            case R.id.btn_hf:
                btn_hf();
                break;
            default:
                btn_fixbug();
                break;
        }
    }

    public void btn_fixbug() {
        FixBug fixBug = new FixBug();
        Toast.makeText(FixBugActivity.this, "" + fixBug.getRes(), Toast.LENGTH_SHORT).show();
    }

    public void btn_hf() {

    }
}
