package com.zhouwei.helloapt.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.zhouwei.helloapt.R;

/**
 * Created by zhouwei on 2018/1/17.
 */
public class CountDownTimerView extends android.support.v7.widget.AppCompatTextView implements View.OnClickListener {
    private final static String TAG = CountDownTimerView.class.getSimpleName();
    private final static int CountDown_MSG = 1;

    private boolean isCounting = false;

    public int countDownInterval = 1000;
    public int intervalCount = 30;
    private int totalCount = intervalCount;

    public String hintText = "获取验证码";
    public String text = "%s秒之后重发";

    private CountDownTimerHandler handler;

    private ClickListener clickListener;

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

        countDownInterval = a.getInt(R.styleable.CountDownTimerView_countDownInterval, countDownInterval);
        intervalCount = a.getInt(R.styleable.CountDownTimerView_intervalCount, intervalCount);
        totalCount = intervalCount;

        String txt = String.valueOf(getHint()) + "";
        if (!TextUtils.isEmpty(txt)) {
            hintText = txt;
        }

        txt = String.valueOf(getText()) + "";
        if (!TextUtils.isEmpty(txt)) {
            text = txt;
        }

        Log.i(TAG, "hint: " + hintText + " text: " + text);
    }

    private void init() {
        setHint(hintText);
        setText("");
        setEnabled(true);
        setClickable(true);
        setOnClickListener(this);
        handler = new CountDownTimerHandler();
    }

    public void addClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick");

        if (null != clickListener) {
            clickListener.onClick(v);
        }
    }

    public synchronized void start() {
        Log.i(TAG, "start");
        if (!isCounting) {
            post(new Runnable() {
                @Override
                public void run() {
                    isCounting = true;
                    handler.sendMessage(handler.obtain(CountDown_MSG));
                    setEnabled(false);
                    setClickable(false);
                }
            });
        }
    }

    public synchronized void restart() {
        Log.i(TAG, "restart");
        cancle();
        if (!isCounting) {
            post(new Runnable() {
                @Override
                public void run() {
                    isCounting = true;
                    handler.sendMessage(handler.obtain(CountDown_MSG));
                    setEnabled(false);
                    setClickable(false);
                }

            });
        }
    }

    public void stop() {
        Log.i(TAG, "stop");
        cancle();
    }

    public synchronized void cancle() {
        Log.i(TAG, "cancle");
        if (isCounting) {
            post(new Runnable() {
                @Override
                public void run() {
                    handler.removeMessages(CountDown_MSG);
                    isCounting = false;
                    setEnabled(true);
                    setClickable(true);
                    setHint(hintText);
                    setText("");
                    totalCount = intervalCount;
                }

            });
        }
    }

    private class CountDownTimerHandler extends Handler {
        public Message obtain(int what) {
            return Message.obtain(this, what);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (totalCount >= 0) {
                setText(String.format(text, totalCount));
                totalCount--;
                sendMessageDelayed(obtain(CountDown_MSG), countDownInterval);
            } else {
                isCounting = false;
                setEnabled(true);
                setClickable(true);
                setText("");
                setHint(hintText);
                totalCount = intervalCount;
            }
        }
    }

    public interface ClickListener {
        void onClick(View v);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeMessages(CountDown_MSG);
        handler = null;
    }
}
