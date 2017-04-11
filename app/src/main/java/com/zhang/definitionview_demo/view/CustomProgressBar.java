package com.zhang.definitionview_demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.zhang.definitionview_demo.R;

/**
 * Created by zhang . DATA: 2017/4/7 . Description : 圆环
 */

public class CustomProgressBar extends View {

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
    private int mProgress;

    /**
     * 进度
     */
    private int mSpeed;


    public CustomProgressBar(Context context) {
        this(context, null);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, defStyleAttr,
                0);
        mFirstColor = array.getColor(R.styleable.CustomProgressBar_firstColor, Color.GREEN);
        mSecondColor = array.getColor(R.styleable.CustomProgressBar_secondColor, Color.RED);
        mCircleWidth = array.getDimensionPixelSize(R.styleable.CustomProgressBar_circleWidth,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()));
        mSpeed = array.getInt(R.styleable.CustomProgressBar_speed, 20);// 默认20
        array.recycle();
        mPaint = new Paint();
        // 绘图线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    mProgress++;
                    if (mProgress == 360) {
                        mProgress = 0;
                        int tmp = mFirstColor;
                        mFirstColor = mSecondColor;
                        mSecondColor = tmp;
                    }
                    postInvalidate();
                    try {
                        Thread.sleep(mSpeed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int centre = getWidth() / 2; // 获取圆心的x坐标
        int radius = centre - mCircleWidth / 2;// 半径
        mPaint.setStrokeWidth(mCircleWidth); // 设置圆环的宽度
        mPaint.setAntiAlias(true); // 消除锯齿
        mPaint.setStyle(Paint.Style.FILL); // 设置空心
        RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius); // 用于定义的圆弧的形状和大小的界限

//        mPaint.setColor(mFirstColor); // 圆环的颜色
//        canvas.drawCircle(centre, centre, radius, mPaint);
        mPaint.setColor(mSecondColor);
        canvas.drawArc(oval,-90, mProgress, true, mPaint);
    }
}
