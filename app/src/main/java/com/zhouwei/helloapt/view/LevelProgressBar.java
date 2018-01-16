package com.zhouwei.helloapt.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.zhouwei.helloapt.R;

/**
 * Created by zhouwei on 2018/1/16.
 * <p>
 * 实现效果：
 * 1.每个等级上有代表等级的文字（等级数和等级文字均可配置）
 * <p>
 * 2.进度条到达等级前要有一个动画的效果
 * <p>
 * 3.进度条到达指定等级后对应文字高亮
 * <p>
 * 4.进度条两端为圆角，从0到最高级，颜色有一个渐变的效果
 * <p>
 * <p>
 * 实现思路:
 * <p>
 * 1.由进度条想到ProgressBar，继承自ProgressBar，可以在onDraw（）中通过getProgress（）和getMax（）的比值来得到当前的进度
 * <p>
 * 2.动画效果其实就是间歇性地增加进度，这里采用Handler的sendEmptyMessageDelayed（）方法每隔一定的时间对进度进行加1，直到指定的等级
 * <p>
 * 3.根据当前进度和等级值来给文字设置不同的颜色，以实现高亮的效果
 * <p>
 * 4.颜色渐变的效果通过给Paint设置LinearGradient来实现
 */

public class LevelProgressBar extends ProgressBar {
    private final int EMPTY_MESSAGE = 1;

    /*xml中的自定义属性*/
    private int levelTextChooseColor;
    private int levelTextUnChooseColor;
    private int levelTextSize;
    private int progressStartColor;
    private int progressEndColor;
    private int progressBgColor;
    private int progressHeight;

    /*代码中需要设置的属性*/
    private int levels;
    private String[] levelTexts;
    private int currentLevel;
    private int animInterval;
    private int targetProgress;

    private Paint mPaint;
    private int mTotalWidth;
    int textHeight;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int progress = getProgress();
            // 小于目标值时增加进度，大于目标值时减小进度
            if (progress < targetProgress) {
                setProgress(++progress);
                handler.sendEmptyMessageDelayed(EMPTY_MESSAGE, animInterval);
            } else if (progress > targetProgress) {
                setProgress(--progress);
                handler.sendEmptyMessageDelayed(EMPTY_MESSAGE, animInterval);
            } else {
                handler.removeMessages(EMPTY_MESSAGE);
            }
        }
    };

    public LevelProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LevelProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 获取xml中设置的属性值
        obtainStyledAttributes(attrs);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(levelTextSize);
        mPaint.setColor(levelTextUnChooseColor);
    }

    private void obtainStyledAttributes(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LevelProgressBar);
        levelTextUnChooseColor = a.getColor(R.styleable.LevelProgressBar_levelTextUnChooseColor, 0x000000);
        levelTextChooseColor = a.getColor(R.styleable.LevelProgressBar_levelTextChooseColor, 0x333333);
        levelTextSize = (int) a.getDimension(R.styleable.LevelProgressBar_levelTextSize, dpTopx(15));
        progressStartColor = a.getColor(R.styleable.LevelProgressBar_progressStartColor, 0xCCFFCC);
        progressEndColor = a.getColor(R.styleable.LevelProgressBar_progressEndColor, 0x00FF00);
        progressBgColor = a.getColor(R.styleable.LevelProgressBar_progressBgColor, 0x000000);
        progressHeight = (int) a.getDimension(R.styleable.LevelProgressBar_progressHeight, dpTopx(20));
        a.recycle();
    }

    private int dpTopx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    // 设置等级数
    public void setLevels(int levels) {
        this.levels = levels;
    }

    // 设置不同等级对应的文字
    public void setLevelTexts(String[] texts) {
        levelTexts = texts;
    }

    // 设置当前等级
    public void setCurrentLevel(int level) {
        this.currentLevel = level;
        this.targetProgress = (int) (level * 1f / levels * getMax());
    }

    // 设置动画间隔，每隔animInterval秒进度+1或-1
    public void setAnimInterval(final int animInterval) {
        this.animInterval = animInterval;
        handler.sendEmptyMessage(EMPTY_MESSAGE);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        // layout_height为wrap_content时计算View的高度
        if (heightMode != MeasureSpec.EXACTLY) {
            textHeight = (int) (mPaint.descent() - mPaint.ascent());
            // 10dp为等级文字和进度条之间的间隔
            height = getPaddingTop() + getPaddingBottom() + textHeight + progressHeight + dpTopx(10);
        }

        setMeasuredDimension(width, height);

        mTotalWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        // 留出padding的位置
        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());

        // 绘制等级文字
        for (int i = 0; i < levels; i++) {
            int textWidth = (int) mPaint.measureText(levelTexts[i]);
            mPaint.setColor(levelTextUnChooseColor);
            mPaint.setTextSize(levelTextSize);
            // 到达指定等级时，设置相应的等级文字颜色为深色
            if (getProgress() == targetProgress && currentLevel >= 1 && currentLevel <= levels && i == currentLevel - 1) {
                mPaint.setColor(levelTextChooseColor);
            }
            canvas.drawText(levelTexts[i], mTotalWidth / levels * (i + 1) - textWidth, textHeight, mPaint);
        }

        int lineY = textHeight + progressHeight / 2 + dpTopx(10);

        // 绘制进度条底部
        mPaint.setColor(progressBgColor);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(progressHeight);
        canvas.drawLine(0 + progressHeight / 2, lineY, mTotalWidth - progressHeight / 2, lineY, mPaint);

        // 绘制进度条
        int reachedPartEnd = (int) (getProgress() * 1.0f / getMax() * mTotalWidth);
        if (reachedPartEnd > 0) {
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            // 设置进度条的渐变色
            Shader shader = new LinearGradient(0, lineY,
                    getWidth(), lineY,
                    progressStartColor, progressEndColor, Shader.TileMode.REPEAT);
            mPaint.setShader(shader);
            int accurateEnd = reachedPartEnd - progressHeight / 2;
            int accurateStart = 0 + progressHeight / 2;
            if (accurateEnd > accurateStart) {
                canvas.drawLine(accurateStart, lineY, accurateEnd, lineY, mPaint);
            } else {
                canvas.drawLine(accurateStart, lineY, accurateStart, lineY, mPaint);
            }
            mPaint.setShader(null);
        }

        canvas.restore();
    }
}
