package com.zhang.definitionview_demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.zhang.definitionview_demo.R;

/**
 * Created by zhang . DATA: 2017/4/7 . Description :
 */

public class CustomVolumControlBar extends View {

    /**
     * 第一圈的颜色
     */
    private int mFirstColor;

    /**
     * 第二圈的颜色
     */
    private int mSecondColor;

    /**
     * 圈的宽度
     */
    private int mCircleWidth;

    /**
     * 画笔
     */
    private Paint mPaint;

    /**
     * 当前进度
     */
    private int mCurrentCount = 0;

    /**
     * 中间的图片
     */
    private Bitmap mImage;

    /**
     * 每个块块间的间隙
     */
    private int mSplitSize;

    /**
     * 个数
     */
    private int mCount;

    private Rect mRect;

    private RectF mRectF;

    public CustomVolumControlBar(Context context) {
        this(context, null);
    }

    public CustomVolumControlBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomVolumControlBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomVolumControlBar,
                defStyleAttr, 0);
        mFirstColor = array.getColor(R.styleable.CustomVolumControlBar_firstColor, Color.GREEN);
        mSecondColor = array.getColor(R.styleable.CustomVolumControlBar_secondColor, Color.GRAY);
        mCircleWidth = array.getDimensionPixelSize(R.styleable.CustomVolumControlBar_circleWidth,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()));
        mImage = BitmapFactory.decodeResource(getResources(),
                array.getResourceId(R.styleable.CustomVolumControlBar_bg, 0));
        mCount = array.getInteger(R.styleable.CustomVolumControlBar_dotCount, 10);
        mSplitSize = array.getInt(R.styleable.CustomVolumControlBar_splitSize, 20);
        array.recycle();
        mPaint = new Paint();
        mRect = new Rect();
        mRectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0x994C4C4C);
        mRectF.set(0,0,getWidth(),getHeight());
        canvas.drawRoundRect(mRectF,50,50,mPaint);

        mPaint.setAntiAlias(true); // 消除锯齿
        mPaint.setStrokeWidth(mCircleWidth); // 设置宽度
        mPaint.setStrokeCap(Paint.Cap.ROUND); // 设置圆头
        mPaint.setStyle(Paint.Style.STROKE); // 设置空心
        int centerX = getWidth() / 2; // 圆心坐标
        int centerY = getHeight() / 2 + 20;
        int radius = Math.min(getWidth(),getHeight()) / 2 - mCircleWidth / 2; // 半径

        // 画小块
        drawOval(canvas, centerX,centerY, radius);

        // 计算内切正方形的位置
        int relRadius = radius - mCircleWidth / 2;

        mRect.left = (int) (centerX - Math.sqrt(2) / 2 * relRadius);
        mRect.top = (int) (centerY - Math.sqrt(2) / 2 * relRadius);
        mRect.right = (int) (centerX + Math.sqrt(2) / 2 * relRadius);
        mRect.bottom = (int) (centerY + Math.sqrt(2) / 2 * relRadius);

        /**
         * 图片尺寸小于内切正方形边长，根据图片尺寸绘制到正中间
         */
        if (mImage.getWidth() < Math.sqrt(2) * relRadius) {
            mRect.left = centerX - mImage.getWidth() / 2;
            mRect.top = centerY - mImage.getHeight() / 2;
            mRect.right = centerX + mImage.getWidth() / 2;
            mRect.bottom = centerY + mImage.getHeight() / 2;
        }

        canvas.drawBitmap(mImage, null, mRect, mPaint);

    }

    /**
     * 画小块
     */
    private void drawOval(Canvas canvas, int centerX,int centerY, int radius) {
        // 每个小块的所占的角度
        float itemSize = (240 * 1.0f - (mCount - 1) * mSplitSize) / mCount;
        RectF rectF = new RectF(centerX - radius, centerY - radius + 10, centerX + radius, centerY + radius + 10);
        mPaint.setColor(mFirstColor);
        for (int i = 0; i < mCount; i++) {
            canvas.drawArc(rectF, -210 + i * (itemSize + mSplitSize), itemSize, false, mPaint);
        }

        mPaint.setColor(mSecondColor);
        for (int i = 0; i < mCurrentCount; i++) {
            canvas.drawArc(rectF, -210 + i * (itemSize + mSplitSize), itemSize, false, mPaint);
        }

    }

    private int xDown = 0;

    private int yDown = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            xDown = (int) event.getX();
            yDown = (int) event.getY();
            break;
        case MotionEvent.ACTION_UP:

            break;
        case MotionEvent.ACTION_MOVE:
            int xM = (int) event.getX();
            if (xM % 5 == 0) { // 减缓触发灵敏度
                if (xDown < xM) {
                    mCurrentCount++;
                    if (mCurrentCount > mCount) {
                        mCurrentCount = mCount;
                    }
                    postInvalidate();
                } else {
                    mCurrentCount--;
                    if (mCurrentCount < 0) {
                        mCurrentCount = 0;
                    }
                    postInvalidate();
                }
            }
            break;
        }
        return true;
    }
}
