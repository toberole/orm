package com.zhouwei.helloapt.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.VideoView;

import com.zhouwei.helloapt.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;

/**
 * 弹幕
 * 开源解决方案：
 * https://github.com/Bilibili/DanmakuFlameMaster
 */
public class BarrageActivity extends AppCompatActivity {

    private VideoView vvplayer;

    private DanmakuView danmukuView;

    private boolean showDanmu;


    private DanmakuContext danmakuContext;

    private BaseDanmakuParser parser = new BaseDanmakuParser() {
        @Override
        protected IDanmakus parse() {
            return new Danmakus();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_barrage);
        this.danmukuView = (DanmakuView) findViewById(R.id.danmuku_view);


        this.vvplayer = (VideoView) findViewById(R.id.vv_player);
        String path = Environment.getExternalStorageDirectory() + "/test01.mp4";
        vvplayer.setVideoPath(path);
        vvplayer.start();

        danmukuView.enableDanmakuDrawingCache(true);
        danmukuView.setCallback(new DrawHandler.Callback() {
            @Override
            public void prepared() {
                showDanmu = true;
                danmukuView.start();
                generateSomeDanmuku();
            }

            @Override
            public void updateTimer(DanmakuTimer timer) {

            }

            @Override
            public void danmakuShown(BaseDanmaku danmaku) {

            }

            @Override
            public void drawingFinished() {

            }
        });

        danmakuContext = DanmakuContext.create();
        Map<Integer, Integer> map = new HashMap<>();
        map.put(BaseDanmaku.TYPE_SCROLL_RL, 3);
        danmakuContext.setMaximumLines(map);
        danmukuView.prepare(parser, danmakuContext);

    }

    /**
     * 向弹幕View中添加一条弹幕
     *
     * @param content    弹幕的具体内容
     * @param withBorder 弹幕是否有边框
     */
    private void addDanmaku(String content, boolean withBorder) {
        BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        danmaku.text = content;
        danmaku.padding = 5;
        danmaku.textSize = sp2px(20);
        danmaku.textColor = Color.WHITE;
        danmaku.setTime(danmukuView.getCurrentTime());
        if (withBorder) {
            danmaku.borderColor = Color.GREEN;
        }
        danmukuView.addDanmaku(danmaku);
    }

    /**
     * 随机生成一些弹幕内容以供测试
     */
    private void generateSomeDanmuku() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (showDanmu) {
                    int time = new Random().nextInt(300);
                    String content = "" + time + time;
                    addDanmaku(content, false);
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * sp转px的方法。
     */
    public int sp2px(float spValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (danmukuView != null && danmukuView.isPrepared()) {
            danmukuView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (danmukuView != null && danmukuView.isPrepared() && danmukuView.isPaused()) {
            danmukuView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showDanmu = false;
        if (danmukuView != null) {
            danmukuView.release();
            danmukuView = null;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        /**
         * 沉浸式
         */
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
