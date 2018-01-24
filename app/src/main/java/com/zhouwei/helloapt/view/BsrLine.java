package com.zhouwei.helloapt.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Charles on 2018/1/24.
 */

public class BsrLine extends View {

    public BsrLine(Context context) {
        this(context, null);
    }

    public BsrLine(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BsrLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private Paint mPaint;
    private Path mPath;

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#FF0000"));
        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        mPath.reset();

        // 起始点
        mPath.moveTo(0, height / 2);
        // 控制点，结束点
        mPath.quadTo(width / 2, 0, width, height / 2);

        canvas.drawPath(mPath, mPaint);
    }
}
