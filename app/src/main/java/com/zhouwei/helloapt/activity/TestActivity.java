package com.zhouwei.helloapt.activity;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.zhouwei.helloapt.R;

public class TestActivity extends Activity {
    public Button btn_bindView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();

        // getContentResolver().query()
        XmlResourceParser parser = null;
        ViewGroup viewGroup = null;
        viewGroup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        View view = null;
        Activity activity = null;


        view.post(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public void bindViewOnClick(View v) {
        Log.i("AAAA", "bindViewOnClick");
    }
}
