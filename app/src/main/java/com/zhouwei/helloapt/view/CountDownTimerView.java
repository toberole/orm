package com.zhouwei.helloapt.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.zhouwei.helloapt.R;

/**
 * Created by zhouwei on 2018/1/17.
 * 倒计时View
 */
public class CountDownTimerView extends android.support.v7.widget.AppCompatTextView implements View.OnClickListener {
    private final static String TAG = CountDownTimerView.class.getSimpleName();
    private final static int CountDown_MSG = 1;

    private boolean isNeedStart = true;

    private int countDownInterval = 1000;
    private int millisInFuture = countDownInterval * 60;
    private int totalCount = millisInFuture / countDownInterval;

    private String originStr_0 = "获取验证码";
    private String originStr = "%s秒之后可以重发";

    private CountDownTimerHandler handler;

    public CountDownTimerView(Context context) {
        this(context, null);
    }

    public CountDownTimerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownTimerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyledAttributes(context, attrs, defStyleAttr);
        init();
    }

    private void obtainStyledAttributes(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CountDownTimerView);
        originStr_0 = a.getString(R.styleable.CountDownTimerView_originStr_0);
        originStr = a.getString(R.styleable.CountDownTimerView_originStr);
        countDownInterval = a.getInt(R.styleable.CountDownTimerView_countDownInterval, countDownInterval);
        millisInFuture = a.getInt(R.styleable.CountDownTimerView_millisInFuture, millisInFuture);
        totalCount = millisInFuture / countDownInterval;
    }

    private class CountDownTimerHandler extends Handler {
        public Message obtain(int what) {
            return Message.obtain(this, what);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (totalCount >= 0) {
                setText(String.format(originStr, totalCount));
                totalCount--;
                sendMessageDelayed(obtain(CountDown_MSG), countDownInterval);
            } else {
                isNeedStart = true;
                setClickable(true);
                setText(originStr_0);
                totalCount = millisInFuture / countDownInterval;
            }
        }
    }

    private void init() {
        setText(originStr_0);
        setClickable(true);
        setOnClickListener(this);
        handler = new CountDownTimerHandler();
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick");
        if (isNeedStart) {
            start();
        }
    }

    /**
     * 开始倒计时
     */
    public synchronized void start() {
        Log.i(TAG, "start");
        if (isNeedStart) {
            isNeedStart = false;
            handler.sendMessage(handler.obtain(CountDown_MSG));
            setClickable(false);
        }
    }

    /**
     * 重新开始倒计时
     */
    public synchronized void restart() {
        Log.i(TAG, "restart");
        if (isNeedStart) {
            isNeedStart = false;
            //timer.start();
            handler.sendEmptyMessage(0);
            setClickable(false);
        }
    }


    public synchronized void cancle() {
        Log.i(TAG, "cancle");
        if (!isNeedStart) {
            isNeedStart = true;
            setClickable(true);
            setText(originStr_0);
            totalCount = millisInFuture / countDownInterval;
            handler.removeMessages(CountDown_MSG);
        }
    }


    public long getCountDownInterval() {
        return countDownInterval;
    }

    public void setCountDownInterval(int countDownInterval) {
        this.countDownInterval = countDownInterval;
    }

    public long getMillisInFuture() {
        return millisInFuture;
    }

    public void setMillisInFuture(int millisInFuture) {
        this.millisInFuture = millisInFuture;
    }

    public String getOriginStr() {
        return originStr;
    }

    public void setOriginStr(String originStr) {
        this.originStr = originStr;
    }
}
