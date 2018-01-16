package com.zhouwei.helloapt.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhouwei.helloapt.R;
import com.zhouwei.helloapt.view.barrage.BarrageView;

public class Barrage2Activity extends AppCompatActivity {

    private BarrageView bv;
    private Button btn_send;

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barrage2);
        this.bv = (BarrageView) findViewById(R.id.bv);

        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBarrage();
            }
        });
    }

    private View getTextView() {
        TextView textview = (TextView) View.inflate(this, R.layout.danmu_item, null);
        textview.setText("弹幕" + count++);
        return textview;
    }

    private View getImageView() {
//        ImageView imageview = (ImageView) View.inflate(this, R.layout.imageview, null);
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(100, 100);
//        imageview.setLayoutParams(params);
//        return imageview;

        return null;
    }

    public void addBarrage() {
        bv.addBarrage(getTextView());
        //bv.addBarrage(getImageView());
    }
}
