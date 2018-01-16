package com.zhouwei.helloapt.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.zhouwei.helloapt.R;
import com.zhouwei.helloapt.view.LevelProgressBar;

public class TestLevelProgressBarActivity extends AppCompatActivity implements View.OnClickListener {

    private LevelProgressBar myProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_level_progress_bar);

        myProgressBar = (LevelProgressBar) findViewById(R.id.progress_bar);
        myProgressBar.setLevels(4);
        String[] texts = {"倔强青铜", "持续白银", "荣耀黄金", "尊贵铂金"};
        myProgressBar.setLevelTexts(texts);

        Button button1 = (Button) findViewById(R.id.level1);
        button1.setOnClickListener(this);
        Button button2 = (Button) findViewById(R.id.level2);
        button2.setOnClickListener(this);
        Button button3 = (Button) findViewById(R.id.level3);
        button3.setOnClickListener(this);
        Button button4 = (Button) findViewById(R.id.level4);
        button4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.level1:
                myProgressBar.setCurrentLevel(1);
                myProgressBar.setAnimInterval(10);
                break;
            case R.id.level2:
                myProgressBar.setCurrentLevel(2);
                myProgressBar.setAnimInterval(10);
                break;
            case R.id.level3:
                myProgressBar.setCurrentLevel(3);
                myProgressBar.setAnimInterval(10);
                break;
            case R.id.level4:
                myProgressBar.setCurrentLevel(4);
                myProgressBar.setAnimInterval(10);
                break;
        }
    }
}