package com.zhouwei.helloapt.view.lock_screen_view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhouwei on 2018/1/16.
 * <p>
 * 锁屏 View上的每一个点View 每个view有四种状态：默认、选中、正确和错误
 */

public class LockScreenView1 extends View {
    private int smallRadius;
    private int bigRadius;
    private int normalColor;
    private int rightColor;
    private int wrongColor;

    private int defaultWidth = 100;
    private int defaultHeight = 100;

    private State state = State.STATE_NORMAL;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public LockScreenView1(Context context, int smallRadius,
                          int bigRadius, int normalColor,
                          int rightColor, int wrongColor) {
        this(context);

        this.smallRadius = smallRadius;
        this.bigRadius = bigRadius;
        this.normalColor = normalColor;
        this.rightColor = rightColor;
        this.wrongColor = wrongColor;
    }

    public LockScreenView1(Context context) {
        this(context, null);
    }

    public LockScreenView1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LockScreenView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyledAttributes(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int resWidth = width;
        int resHeight = height;

        // 处理wrap_content padding
        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            resWidth = width - getPaddingLeft() - getPaddingRight();
            resHeight = height - getPaddingTop() - getPaddingBottom();
            setMeasuredDimension(resWidth, resHeight);
        } else if (widthMode == MeasureSpec.EXACTLY) {
            resWidth = width - getPaddingLeft() - getPaddingRight();
            resHeight = defaultHeight - getPaddingTop() - getPaddingBottom();
            setMeasuredDimension(resWidth, resHeight);
        } else {
            resWidth = defaultWidth - getPaddingLeft() - getPaddingRight();
            resHeight = height - getPaddingTop() - getPaddingBottom();
            setMeasuredDimension(resWidth, resHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (state) {
            case STATE_NORMAL:

                break;
            case STATE_CHOOSED:

                break;
            case STATE_RESULT_RIGHT:

                break;
            case STATE_RESULT_WRONG:

                break;
            default:
                break;
        }
    }

    // 标记当前的状态 放大 缩小
    private boolean needZooIn = false;

    /**
     * 选中的时候的方法动画
     */
    private void ZoomOut() {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "scaleX", 1f, 1.2f);
        animatorX.setDuration(500);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "scaleY", 1f, 1.2f);
        animatorY.setDuration(500);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorX, animatorY);
        animatorSet.start();

        needZooIn = true;
    }

    /**
     * 恢复正常的动画
     */
    private void ZoomIn() {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "scaleX", 1.2f, 1f);
        animatorX.setDuration(500);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "scaleY", 1.2f, 1f);
        animatorX.setDuration(500);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorX, animatorY);
        animatorSet.start();

        needZooIn = false;
    }

    private void init() {

    }

    private void obtainStyledAttributes(Context context, AttributeSet attrs, int defStyleAttr) {

    }

    public enum State {
        // 正常状态 选中状态 结果正确状态 结果错误状态
        STATE_NORMAL, STATE_CHOOSED, STATE_RESULT_RIGHT, STATE_RESULT_WRONG
    }
}
