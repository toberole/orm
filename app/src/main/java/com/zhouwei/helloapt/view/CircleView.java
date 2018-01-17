package com.zhouwei.helloapt.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.zhouwei.helloapt.R;

/**
 * Created by zhouwei on 2018/1/15.
 */

public class CircleView extends View {
    private Paint mPaint;

    private int radius = 100;

    private int defaultParam = 200;

    private int color = Color.parseColor("#FF8899");

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initProperty(context, attrs, defStyleAttr);
        init();
    }

    private void initProperty(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        color = typedArray.getColor(R.styleable.CircleView_color, Color.GREEN);
        typedArray.recycle();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * 自定义继承自View的时候，需要自己处理padding，否则padding属性无效
         * 注意padding是计算在view的总的宽度和高度里面的
         * 比如：View的
         * width =100
         * padding = 20
         * 那么留给view的内容的宽度就剩余 80了
         */
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        // 此时计算的宽和高度就是View内容可用的空间
        int width = getWidth() - paddingLeft - paddingRight;
        int heigt = getHeight() - paddingBottom - paddingTop;


//        int temp = (width < heigt ? heigt : width) / 2;
//        radius = temp < radius ? temp : radius;

        Log.i("AAAA", "width: " + width + " height: " + heigt + " radius: " + radius);

        canvas.drawCircle(width / 2, width / 2, width / 2, mPaint);

    }

    @Override
    /**
     * 自定View的时候 需要自己处理 wrap_content 否则wrap_content就是match_parent的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getDimension(widthMeasureSpec);
        int height = getDimension(heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    private int getDimension(int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        int result = size;
        switch (mode) {
            case MeasureSpec.AT_MOST:
                if (size < defaultParam) {
                    result = size;
                } else {
                    result = defaultParam;
                }
                break;
            case MeasureSpec.EXACTLY:
                result = size;
                break;
            case MeasureSpec.UNSPECIFIED:
                result = defaultParam;
                break;
            default:
                break;
        }
        return result;
    }
}
