package com.zhouwei.helloapt.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhouwei on 2018/1/15.
 */

public class MyViewPager extends ViewGroup {
    public MyViewPager(Context context) {
        this(context, null);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int childCount = getChildCount();

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int measuredWidth = 0;
        int measuredHeight = 0;

        if (childCount == 0) {
            setMeasuredDimension(0, 0);
        } else if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            View v = getChildAt(0);
            measuredWidth = v.getMeasuredWidth() * childCount;
            measuredHeight = v.getMeasuredHeight();
            setMeasuredDimension(measuredWidth, measuredHeight);
        } else if (MeasureSpec.AT_MOST == heightSpecMode) {
            View v = getChildAt(0);
            measuredWidth = v.getMeasuredWidth();
            measuredHeight = v.getMeasuredHeight();
            setMeasuredDimension(measuredWidth, measuredHeight);
        } else if (MeasureSpec.AT_MOST == widthSpecMode) {
            View v = getChildAt(0);
            measuredWidth = v.getMeasuredWidth() * childCount;
            measuredHeight = v.getMeasuredHeight();
            setMeasuredDimension(measuredWidth, measuredHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            if (v.getVisibility() == View.VISIBLE) {
                int width = v.getMeasuredWidth();
                v.layout(i * width, 0, (i + 1) * width, v.getMeasuredHeight());
            }
        }
    }

    private int lastX = 0;
    private int lastY = 0;

    private int startX = 0;

    /**
     * scrollBy scrollTo 相当于滑动的时候视窗 ，视窗可以理解为屏幕，移动的时候就是相当于
     * 移动的屏幕，而屏幕里面的内容没有动，由于运动是相对的，所以呈现在视觉上就是屏幕里面的内容移动了
     *
     * @param event
     * @return
     */

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getX();
                lastY = (int) event.getY();
                startX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                scrollBy(x - lastX, 0);
                break;
            case MotionEvent.ACTION_UP:
                View v = getChildAt(0);
                if (Math.abs(event.getX() - startX) > 200) {
                    scrollBy(v.getWidth() - lastX, 0);
                }
                break;
            default:
                break;
        }
        
        lastX = x;
        lastY = y;
        return true;
    }
}
