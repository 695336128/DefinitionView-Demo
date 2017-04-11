package com.zhang.definitionview_demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zhang.definitionview_demo.R;

/**
 * Created by zhang . DATA: 2017/4/11 . Description : 播放音频的自定义view
 */

public class VoiceView extends View {

    private Paint mPaint;

    private RectF mRectF;

    private int bgColor = 0xFFAFE36E;

    private int strokeColor = 0xFF95CE59;

    private int rippleColor = 0xFF589533;

    private int padding = 5;

    private int radius = 30;

    private int strokeWidth = 2;

    private int rippleCount = 3;

    private Boolean isTimerDown = false;

    public VoiceView(Context context) {
        this(context, null);
    }

    public VoiceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.VoiceView, defStyleAttr, 0);
        array.recycle();
        mPaint = new Paint();
        mRectF = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 背景
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(bgColor);
        mRectF.set(padding, padding, getWidth() - padding, getHeight() - padding);
        canvas.drawRoundRect(mRectF, radius, radius, mPaint);

        // 描边
        mPaint.setColor(strokeColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(strokeWidth);
        canvas.drawRoundRect(mRectF, radius, radius, mPaint);

        // 确定波纹的圆心
        int centerY = getHeight() / 2;
        int centerX = getHeight() / 2;

        // 第三波
        mPaint.setColor(rippleColor);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(7);
        mPaint.setStyle(Paint.Style.STROKE);

        if (!isTimerDown) {
            for (int i = 1; i <= rippleCount; i++) {
                mRectF.left = i * centerX / 5;
                mRectF.top = i * centerY / 5;
                mRectF.right = (getHeight() / 2 - mRectF.top) * 2 + mRectF.left;
                mRectF.bottom = getHeight() - mRectF.top;
                canvas.drawArc(mRectF, -35, 70, false, mPaint);
            }
        } else {
            for (int i = 1; i <= rippleCount;i++){
                mRectF.left = (4 - i) * centerX / 5;
                mRectF.top = (4 - i) * centerY / 5;
                mRectF.right = (getHeight() / 2 - mRectF.top) * 2 + mRectF.left;
                mRectF.bottom = getHeight() - mRectF.top;
                canvas.drawArc(mRectF, -35, 70, false, mPaint);
            }

        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                rippleCount = 0;
                CountDownTimer countDownTimer = new CountDownTimer(3000, 300) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        rippleCount = rippleCount % 3 + 1;
                        isTimerDown = true;
                        postInvalidate();
                    }

                    @Override
                    public void onFinish() {
                        rippleCount = 3;
                        isTimerDown = false;
                        postInvalidate();
                    }
                }.start();
                break;
        }
        return true;
    }

}
