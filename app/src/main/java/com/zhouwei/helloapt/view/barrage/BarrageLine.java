package com.zhouwei.helloapt.view.barrage;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by zhouwei on 2018/1/15.
 * <p>
 * 一行弹幕
 * 使用handler.postdelay的方法实现一个定时器，使用setTranslationX的方法实现view向左移动的效果。
 * 当一行里有空间时，barrageline会添加一个view。当一个view移出barrageline时，清空view。
 */

public class BarrageLine extends FrameLayout {
    private Handler mHandler;

    // 保存每一行的所有的view
    private ConcurrentLinkedQueue<View> mQueue = new ConcurrentLinkedQueue<>();

    private int mWidth;
    private int PADDING = 20;
    private int HEIGHT = 100;

    private Random random = new Random();

    public static Executor mExecutor = Executors.newCachedThreadPool();

    public BarrageLine(@NonNull Context context) {
        this(context, null);
    }

    public BarrageLine(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarrageLine(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        flutter();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mWidth = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(mWidth, HEIGHT);
    }

    /**
     * 往队列里添加弹幕view
     *
     * @param view
     */
    public void addBarrage(View view) {
        mQueue.offer(view);
    }

    private class AddBarrageTask implements Runnable {
        View view;

        public AddBarrageTask(View view) {
            this.view = view;
        }

        @Override
        public void run() {
            mQueue.offer(view);
        }
    }

    /**
     * 开始执行动画
     */
    private void flutter() {
        mHandler.post(mFlutterTask);
    }

    private Runnable mFlutterTask = new Runnable() {
        @Override
        public void run() {
            addBarrageView();

            moveView();

            // 循环的动起来
            mHandler.postDelayed(this, 5);
        }
    };

    /**
     * 判断每一行是否要添加view
     */
    private void addBarrageView() {
        if (getChildCount() == 0) {
            addNextView();
            return;
        }

        View lastChild = this.getChildAt(getChildCount() - 1);
        int lastChildRight = (int) (lastChild.getTranslationX() + (int) lastChild.getTag());
        if (lastChildRight + PADDING >= mWidth) {
            return;
        }

        addNextView();
    }

    /**
     * 给每一行添加view
     */
    private void addNextView() {
        if (mQueue.isEmpty()) {
            return;
        }
        View view = mQueue.poll();
        view.measure(0, 0);

        // 控制一行当中两个相邻弹幕之间的距离
        int width = view.getMeasuredWidth();
        int interval = (random.nextInt(100) + 1) * (random.nextInt(200) + 1) % width + width;
        Log.i("AAAA", "interval: " + interval);
        view.setTag(interval);
        addView(view);
        view.setTranslationX(mWidth);
    }

    /**
     * 通过handler.post执行，形成动画
     */
    private void moveView() {
        if (this.getChildCount() == 0) {
            return;
        }

        for (int i = 0; i < getChildCount(); i++) {
            View view = this.getChildAt(i);
            view.setTranslationX(view.getTranslationX() - 3);
            if (view.getTranslationX() + (int) view.getTag() <= 0) {// 已经移除屏幕了
                removeBarrageView(view);
            }
        }
    }

    /**
     * 当view移出弹幕行，删除
     */
    private void removeBarrageView(View view) {
        view.setVisibility(GONE);
        this.removeView(view);
        view = null;
    }

    /**
     * 停止发消息，取消动画
     */
    private void stop() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mFlutterTask);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }
}
