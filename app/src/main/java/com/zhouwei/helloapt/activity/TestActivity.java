package com.zhouwei.helloapt.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zhouwei.helloapt.R;

import static com.zhouwei.helloapt.R.id.get;

public class TestActivity extends Activity {
    public Button btn_bindView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();

        getContentResolver().query()
    }

    public void bindViewOnClick(View v) {
        Log.i("AAAA", "bindViewOnClick");
    }
}
