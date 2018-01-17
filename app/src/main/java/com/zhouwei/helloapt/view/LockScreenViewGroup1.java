package com.zhouwei.helloapt.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.zhouwei.helloapt.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouwei on 2018/1/16.
 * <p>
 * <p>
 * 1.自定义一个LockScreenView来表示图案点，LockScreenView有四种状态
 * <p>
 * 2.自定义一个LockScreenViewGroup，在onMeasure中获取到宽度以后（根据宽度算图案点之间的间距），
 * 动态地将LockScreenView添加进来
 * <p>
 * 3.在LockScreenViewGroup的onTouchEvent中消耗触摸事件，根据触摸点的轨迹来更新LockScreenView、
 * 图案点连线和悬空线段
 */

public class LockScreenViewGroup1 extends RelativeLayout {

    private int smallRadius = 40;

    private int bigRadius = 60;

    private int normalColor = Color.GRAY;

    private int rightColor = Color.RED;

    private int wrongColor = Color.YELLOW;

    private int itemCount = 3;

    public LockScreenViewGroup1(Context context) {
        this(context, null);
    }

    public LockScreenViewGroup1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LockScreenViewGroup1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyledAttributes(context, attrs, defStyleAttr);
        init();
    }

    private void obtainStyledAttributes(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LockScreenViewGroup);
        smallRadius = (int) a.getDimension(R.styleable.LockScreenViewGroup_smallRadius, smallRadius);
        bigRadius = (int) a.getDimension(R.styleable.LockScreenViewGroup_bigRadius, bigRadius);
        normalColor = a.getColor(R.styleable.LockScreenViewGroup_normalColor, normalColor);
        rightColor = a.getColor(R.styleable.LockScreenViewGroup_rightColor, rightColor);
        wrongColor = a.getColor(R.styleable.LockScreenViewGroup_wrongColor, wrongColor);
        itemCount = a.getInt(R.styleable.LockScreenViewGroup_itemCount, itemCount);
        a.recycle();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(normalColor);
    }

    private LockScreenView1[] lockScreenViews = null;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (null == lockScreenViews) {
            lockScreenViews = new LockScreenView1[itemCount * itemCount];
            for (int i = 0; i < itemCount * itemCount; i++) {
                lockScreenViews[i] = new LockScreenView1(getContext(), smallRadius, bigRadius, normalColor, rightColor, wrongColor);
                lockScreenViews[i].setId(i + 1);
                LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

                int marginWidth = (getMeasuredWidth() - bigRadius * 2 * itemCount) / (itemCount + 1);

                // 出了第一列以外 其他的LockScreenView1都在某一个LockScreenView1的右边
                if (i % itemCount != 0) {
                    params.addRule(RIGHT_OF, lockScreenViews[i - 1].getId());
                }

                // 给LockScreenView1设置margin
                int left = marginWidth;
                int top = marginWidth;
                int bottom = 0;
                int right = 0;
                params.setMargins(left, top, right, bottom);
                lockScreenViews[i].setState(LockScreenView1.State.STATE_NORMAL);
                addView(lockScreenViews[i], params);
            }
        }


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    private Paint mPaint;
    private List<Integer> mCurrentViews = new ArrayList<>();
    private Path mCurrentPath = new Path();

    // 悬空线段起始的 x 和 y
    private int skyStartX;
    private int skyStartY;

    private int tempX;
    private int tempY;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) getX();
        int y = (int) getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                /**
                 * 判断是否在LockScreenView1区域，如果在某个LockScreenView1区域且这个LockScreenView1
                 * 之前没有被选中，则将这个LockScreenView1设置为选中状态。另外在onMove中还做了图案点
                 * 间线段路径和悬空线段起点和终点（mTempX、mTempY）的更新，悬空线段的起点就是上一个
                 * 被选中的LockScreenView1的中心点。
                 */
                mPaint.setColor(normalColor);
                LockScreenView1 view = findLockScreenView(x, y);
                if (null != view) {
                    int id = view.getId();
                    // 当前LockScreenView1不在选中列表中，将其添加到列表中，并且设置其状态为选中
                    if (!mCurrentViews.contains(id)) {
                        mCurrentViews.add(id);
                        view.setState(LockScreenView1.State.STATE_CHOOSED);
                        skyStartX = (view.getLeft() + view.getRight()) / 2;
                        skyStartY = (view.getTop() + view.getBottom()) / 2;
                    }

                    // path 中线段的添加
                    if (mCurrentViews.size() == 1) {
                        mCurrentPath.moveTo(skyStartX, skyStartY);
                    } else {
                        mCurrentPath.lineTo(skyStartX, skyStartY);
                    }
                }

                // 悬空线的末端更新
                tempX = x;
                tempY = y;
                break;
            case MotionEvent.ACTION_UP:
                /**
                 * 根据答案的正确与否，对LockScreenView1设置不同的状态，并且对悬空线段起始点进行重置
                 */
                // 根据图案正确与否 对LockScreenView1设置不同的状态
                if (checkAnswer()) {
                    setCurrentViewsState(LockScreenView1.State.STATE_RESULT_RIGHT);
                    mPaint.setColor(rightColor);
                } else {
                    setCurrentViewsState(LockScreenView1.State.STATE_RESULT_WRONG);
                    mPaint.setColor(wrongColor);
                }
                // 抬起手指后 对悬空线段的起始点进行重置
                skyStartX = -1;
                skyStartY = -1;
                break;

            default:
                break;
        }

        // 调用invalidate方法对视图进行重绘，这时会调用dispatchDraw方法进行子View的绘制。
        invalidate();

        return super.onTouchEvent(event);
    }

    @Override
    // 进行图像点间的线段路径以及悬空线段的绘制
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        // path线段的绘制
        if (!mCurrentPath.isEmpty()) {
            canvas.drawPath(mCurrentPath, mPaint);
        }

        // 悬空线段的绘制
        if (-1 != skyStartX) {
            canvas.drawLine(skyStartX, skyStartY, tempX, tempY, mPaint);
        }
    }

    private boolean checkAnswer() {
        return false;
    }

    private LockScreenView1 findLockScreenView(int x, int y) {
        return null;
    }

    private void resetView() {
        if (mCurrentViews.size() > 0) {
            mCurrentViews.clear();
        }

        if (!mCurrentPath.isEmpty()) {
            mCurrentPath.reset();
        }

        // 重置LockScreenView的状态
        for (int i = 0; i < itemCount * itemCount; i++) {
            lockScreenViews[i].setState(LockScreenView1.State.STATE_NORMAL);
        }

        skyStartX = -1;
        skyStartY = -1;
    }

    public void setCurrentViewsState(LockScreenView1.State currentViewsState) {
        for (int i = 0; i < mCurrentViews.size(); i++) {
            // mCurrentViews.get(i).sets
        }
    }
}
