package com.zhang.definitionview_demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.zhang.definitionview_demo.R;

import java.util.Calendar;

/**
 * Created by zhang . DATA: 2017/4/19 . Description : 防华为天气控件
 */

public class HuaweiWeatherView extends View {

    private Paint mPaint = null;

    private RectF mRectF = null;

    private int mRadius = 0;

    private int mPadding = 10;

    /**
     * 时针角度
     */
    private float mHourDegree;

    /**
     * 分针角度
     */
    private float mMinuteDegree;

    /**
     * 秒针角度
     */
    private float mSecondDegree;

    /**
     * 时针画笔
     */
    private Paint mHourPaint;

    /**
     * 分针画笔
     */
    private Paint mMinutePaint;

    /**
     * 秒针画笔
     */
    private Paint mSecondPaint;

    public HuaweiWeatherView(Context context) {
        this(context, null);
    }

    public HuaweiWeatherView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HuaweiWeatherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.HuaweiWeatherView, defStyleAttr,
                0);
        array.recycle();
        mPaint = new Paint();
        mRectF = new RectF();
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1);
        mPaint.setAntiAlias(true);

        mHourPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHourPaint.setColor(Color.WHITE);

        mMinutePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMinutePaint.setColor(Color.WHITE);

        mSecondPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSecondPaint.setColor(Color.RED);

        setBackgroundColor(Color.parseColor("#6495ED"));
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = 800;
            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(widthSize, width);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = 800;
            if (heightMode == MeasureSpec.EXACTLY) {
                height = Math.min(widthMeasureSpec, heightMeasureSpec);
            }
        }

        setMeasuredDimension(width, height);
    }

    // public void run() {
    // new Timer().schedule(new TimerTask() {
    // @Override
    // public void run() {
    // postInvalidate();
    // }
    // }, 0, 1000);
    // }

    @Override
    protected void onDraw(Canvas canvas) {

        int width = this.getMeasuredWidth();
        int height = this.getMeasuredHeight();
        mRadius = Math.min(width, height) / 2 - mPadding;
        getTimeAndDegree();
        drawPlate(canvas);
        drawHourPoints(canvas);
        drawMinutePoints(canvas);
        drawSecondPoints(canvas);
        invalidate();
    }

    /**
     * 画表盘
     *
     * @param canvas
     */
    private void drawPlate(Canvas canvas) {
        canvas.save();
        canvas.translate(mPadding, mPadding);

        // 画圆
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
        // 画刻度
        for (int i = 0; i < 60; i++) {
            if (i % 5 == 0) {
                // 长刻度，占半径 1/10
                mPaint.setColor(Color.RED);
                mPaint.setStrokeWidth(4);
                canvas.drawLine(mRadius + 9 * mRadius / 10, mRadius, 2 * mRadius, mRadius, mPaint);
            } else {
                mPaint.setColor(Color.WHITE);
                mPaint.setStrokeWidth(1);
                canvas.drawLine(mRadius + 14 * mRadius / 15, mRadius, 2 * mRadius, mRadius, mPaint);
            }
            canvas.rotate(6, mRadius, mRadius);

        }
        canvas.restore();
    }

    private void getTimeAndDegree() {
        Calendar mCalender = Calendar.getInstance();
        float milliSecond = mCalender.get(Calendar.MILLISECOND);
        float seconds = mCalender.get(Calendar.SECOND) + milliSecond / 1000;
        float minutes = mCalender.get(Calendar.MINUTE) + seconds / 60;
        float hours = mCalender.get(Calendar.HOUR) + minutes / 60;

        mHourDegree = hours / 12 * 360;
        mMinuteDegree = minutes / 60 * 360;
        mSecondDegree = seconds / 60 * 360;
    }

    /**
     * 画时针
     *
     * @param canvas
     */
    private void drawHourPoints(Canvas canvas) {
        mHourPaint.setStrokeWidth(10);
        mHourPaint.setStyle(Paint.Style.STROKE);
        // 画时针
        canvas.save();
        canvas.translate(mPadding, mPadding);
        canvas.rotate(mHourDegree, mRadius, mRadius);
        canvas.drawCircle(mRadius, mRadius, 15, mHourPaint);

        mHourPaint.setStrokeWidth(1);
        mHourPaint.setStyle(Paint.Style.FILL);

        Path path = new Path();
        path.moveTo(mRadius - 10, mRadius);
        path.lineTo(mRadius - 5, mRadius * 1 / 2);
        path.quadTo(mRadius, mRadius * 3 / 8, mRadius + 5, mRadius * 1 / 2);
        path.lineTo(mRadius + 10, mRadius);
        path.close();
        Path path1 = new Path();
        path1.addCircle(mRadius, mRadius, 15, Path.Direction.CCW);
        path.op(path1, Path.Op.DIFFERENCE);
        canvas.drawPath(path, mHourPaint);
        canvas.restore();

    }

    /**
     * 画分针
     *
     * @param canvas
     */
    private void drawMinutePoints(Canvas canvas) {
        mMinutePaint.setStrokeWidth(10);
        mMinutePaint.setStyle(Paint.Style.STROKE);
        canvas.save();
        canvas.translate(mPadding, mPadding);
        canvas.rotate(mMinuteDegree, mRadius, mRadius);

        canvas.drawCircle(mRadius, mRadius, 15, mMinutePaint);
        mMinutePaint.setStrokeWidth(1);
        mMinutePaint.setStyle(Paint.Style.FILL);

        Path path = new Path();
        path.moveTo(mRadius - 8, mRadius);
        path.lineTo(mRadius - 3, mRadius * 1 / 3);
        path.quadTo(mRadius, mRadius * 2 / 8, mRadius + 3, mRadius * 1 / 3);
        path.lineTo(mRadius + 8, mRadius);
        path.close();
        Path path1 = new Path();
        path1.addCircle(mRadius, mRadius, 15, Path.Direction.CCW);
        path.op(path1, Path.Op.DIFFERENCE);
        canvas.drawPath(path, mMinutePaint);
        canvas.restore();
    }

    /**
     * 画秒针
     */
    private void drawSecondPoints(Canvas canvas) {
        mSecondPaint.setStrokeWidth(4);
        mSecondPaint.setStyle(Paint.Style.FILL);
        canvas.save();
        canvas.translate(mPadding, mPadding);
        canvas.rotate(mSecondDegree, mRadius, mRadius);

        canvas.drawCircle(mRadius, mRadius, 4, mSecondPaint);
        mSecondPaint.setStrokeWidth(2);
        mSecondPaint.setStyle(Paint.Style.FILL);
        mSecondPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawLine(mRadius, mRadius + 15, mRadius, mRadius * 1 / 4, mSecondPaint);
        canvas.restore();
    }

}
