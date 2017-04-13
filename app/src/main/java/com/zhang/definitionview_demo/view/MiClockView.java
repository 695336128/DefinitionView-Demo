package com.zhang.definitionview_demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.zhang.definitionview_demo.R;

import java.util.Calendar;

/**
 * Created by zhang . DATA: 2017/4/11 . Description : 仿小米时钟
 */

public class MiClockView extends View {

    /**
     * 背景色
     */
    private int mBackgroundColor;

    /**
     * 字号
     */
    private int mTextSize;

    /**
     * 表盘半径
     */
    private int mRadius;

    /**
     * 加一个默认的padding值，为了防止用camera旋转时钟时造成四周超出view大小
     */
    private float mDefaultPadding;

    /**
     * padding
     */
    private float mPaddingLeft;

    /**
     * padding
     */
    private float mPaddingTop;

    /**
     * padding
     */
    private float mPaddingRight;

    /**
     * padding
     */
    private float mPaddingBottom;

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
     * 亮色，用于分针、秒针、渐变终止色
     */
    private int mLightColor;

    /**
     * 暗色，圆弧、刻度线、时针、渐变起始色
     */
    private int mDarkColor;

    /**
     * 画布
     */
    private Canvas mCanvas;

    /**
     * 时间文字的画笔
     */
    private Paint mTextPaint;

    /**
     * 小时圆圈画笔
     */
    private Paint mCirclePaint;

    /**
     * 小刻度画笔
     */
    private Paint mScaleLinePaint;

    /**
     * 时间文字的外接矩形
     */
    private Rect mTextRect;

    /**
     * 弧线的外接矩形
     */
    private RectF mCircleRectF;

    /**
     * 刻度圆弧画笔
     */
    private Paint mScaleArcPaint;

    /**
     * 刻度圆弧的外接矩形
     */
    private RectF mScaleArcRectF;

    /**
     * 小时圆圈线条宽度
     */
    private float mCircleStrokeWidth = 2;

    /**
     * 指针的在x轴的位移
     */
    private float mCanvasTranslateX;

    /**
     * 指针的在y轴的位移
     */
    private float mCanvasTranslateY;

    /**
     * 刻度线长度
     */
    private float mScaleLength;

    /**
     * 渐变矩阵，作用在SweepGradient
     */
    private Matrix mGradientMatrix;

    /**
     * 梯度扫描渐变
     */
    private SweepGradient mSweepGradient;

    /**
     * 时针画笔
     */
    private Paint mHourHandPaint;
    /**
     * 分针画笔
     */
    private Paint mMinuteHandPaint;
    /**
     * 秒针画笔
     */
    private Paint mSecondHandPaint;
    /**
     * 时针路径
     */
    private Path mHourHandPath;
    /**
     * 分针路径
     */
    private Path mMinuteHandPath;
    /**
     * 秒针路径
     */
    private Path mSecondHandPath;

    private RectF mRectF;

    public MiClockView(Context context) {
        this(context, null);
    }

    public MiClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MiClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MiClockView, defStyleAttr, 0);
        mBackgroundColor = array.getColor(R.styleable.MiClockView_backgroundColor, Color.parseColor("#237EAD"));
        mLightColor = array.getColor(R.styleable.MiClockView_lightColor, Color.parseColor("#ffffff"));
        mDarkColor = array.getColor(R.styleable.MiClockView_darkColor, Color.parseColor("#80ffffff"));
        mTextSize = (int) array.getDimension(R.styleable.MiClockView_textSize,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
        array.recycle();
        setBackgroundColor(mBackgroundColor);
        initPaint();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mTextPaint = new Paint();
        mTextRect = new Rect();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(mDarkColor);
        mTextPaint.setTextSize(mTextSize);

        mCirclePaint = new Paint();
        mCircleRectF = new RectF();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setColor(mDarkColor);

        mScaleLinePaint = new Paint();
        mScaleLinePaint.setAntiAlias(true);
        mScaleLinePaint.setStyle(Paint.Style.STROKE);
        mScaleLinePaint.setStrokeWidth(1);
        mScaleLinePaint.setColor(mBackgroundColor);

        mScaleArcPaint = new Paint();
        mScaleArcRectF = new RectF();
        mScaleArcPaint.setAntiAlias(true);
        mScaleArcPaint.setStyle(Paint.Style.STROKE);

        mSecondHandPaint = new Paint();
        mSecondHandPaint.setAntiAlias(true);
        mSecondHandPaint.setColor(mLightColor);
        mSecondHandPaint.setStyle(Paint.Style.FILL);

        mMinuteHandPaint = new Paint();
        mMinuteHandPaint.setAntiAlias(true);
        mMinuteHandPaint.setColor(mLightColor);
        mMinuteHandPaint.setStyle(Paint.Style.FILL);

        mHourHandPaint = new Paint();
        mHourHandPaint.setAntiAlias(true);
        mHourHandPaint.setColor(mDarkColor);
        mHourHandPaint.setStyle(Paint.Style.FILL);

        mGradientMatrix = new Matrix();
        mSecondHandPath = new Path();
        mMinuteHandPath = new Path();
        mHourHandPath = new Path();
        mRectF = new RectF();


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthMeasureSpec;
        } else {
            width = 800;
            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, widthSize);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightMeasureSpec;
        } else {
            height = 800;
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 宽和高分别去掉padding值，取min的一半即表盘的半径
        mRadius = Math.min(w - getPaddingLeft() - getPaddingRight(), h - getPaddingTop() - getPaddingBottom()) / 2;
        mDefaultPadding = 0.12f * mRadius;// 根据比例确定默认padding大小
        mPaddingLeft = mDefaultPadding + w / 2 - mRadius + mPaddingLeft;
        mPaddingTop = mDefaultPadding + h / 2 - mRadius + getPaddingTop();
        mPaddingRight = mPaddingLeft;
        mPaddingBottom = mPaddingTop;
        mScaleLength = 0.12f * mRadius;// 根据比例确定刻度线长度
        mScaleArcPaint.setStrokeWidth(mScaleLength);
        mScaleLinePaint.setStrokeWidth(0.012f * mRadius);
        // mMaxCanvasTranslate = 0.02f * mRadius;
        // 梯度扫描渐变，以(w/2,h/2)为中心点，两种起止颜色梯度渐变
        // float数组表示，[0,0.75)为起始颜色所占比例，[0.75,1}为起止颜色渐变所占比例
        mSweepGradient = new SweepGradient(w / 2, h / 2, new int[]{mDarkColor, mLightColor},
                new float[]{0.75f, 1});
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mCanvas = canvas;
        getTimeDegree();
        drawTimeText();
        drawScaleLine();
        drawSecondHand();
        drawMinuteHand();
        drawHourHand();
        invalidate();
    }

    /**
     * 获取当前时分秒所对应的角度
     */
    private void getTimeDegree() {
        Calendar calendar = Calendar.getInstance();
        float milliSecond = calendar.get(Calendar.MILLISECOND);
        float second = calendar.get(Calendar.SECOND) + milliSecond / 1000;
        float minute = calendar.get(Calendar.MINUTE) + second / 60;
        float hour = calendar.get(Calendar.HOUR) + minute / 60;

        mSecondDegree = second / 60 * 360;
        mMinuteDegree = minute / 60 * 360;
        mHourDegree = hour / 12 * 360;
    }

    /**
     * 画最外圈的四个圆弧和时间
     */
    private void drawTimeText() {
        String timeText = "12";
        mTextPaint.getTextBounds(timeText, 0, timeText.length(), mTextRect);
        int textLargeWidth = mTextRect.width();// 两位数字的宽
        mCanvas.drawText(timeText, getWidth() / 2 - textLargeWidth / 2, mPaddingTop + mTextRect.height(), mTextPaint);

        timeText = "3";
        mTextPaint.getTextBounds(timeText, 0, timeText.length(), mTextRect);
        int textSmallWidth = mTextRect.width();
        mCanvas.drawText("3", getWidth() - textSmallWidth - mPaddingRight, getHeight() / 2 + mTextRect.height() / 2,
                mTextPaint);
        mCanvas.drawText("6", getWidth() / 2 - textSmallWidth / 2, getHeight() - mPaddingBottom, mTextPaint);
        mCanvas.drawText("9", mPaddingLeft, getHeight() / 2 + mTextRect.height() / 2, mTextPaint);

        // 画弧线
        mCircleRectF.set(mPaddingLeft + mTextRect.width() / 2, mPaddingTop + mTextRect.height() / 2,
                getWidth() - mPaddingRight - mTextRect.width() / 2,
                getHeight() - mPaddingBottom - mTextRect.height() / 2);
        for (int i = 0; i < 4; i++) {
            mCanvas.drawArc(mCircleRectF, i * 90 + 5, 80, false, mCirclePaint);
        }
    }

    /**
     * 画一圈刻度;这里的做法是，画一个颜色渐变的圆弧，在圆弧上画背景色的分割线，切割这个圆弧。
     */
    private void drawScaleLine() {
        mCanvas.save();

        // 1.5 是因为stroke占了0.5*strokeLength的半径
        mScaleArcRectF.set((float) (mPaddingLeft + mTextRect.height() / 2 + 1.5 * mScaleLength),
                (float) (mPaddingTop + mTextRect.height() / 2 + 1.5 * mScaleLength),
                (float) (getWidth() - mPaddingRight - mTextRect.height() / 2 - 1.5 * mScaleLength),
                (float) (getHeight() - mPaddingBottom - mTextRect.height() / 2 - 1.5 * mScaleLength));
        // 之所以减去90，是因为在3点钟方向为0°，而此处需要以12点钟方向
        mGradientMatrix.setRotate(mSecondDegree - 90, getWidth() / 2, getHeight() / 2);
        mSweepGradient.setLocalMatrix(mGradientMatrix);
        mScaleArcPaint.setShader(mSweepGradient);
        mCanvas.drawArc(mScaleArcRectF, 0, 360, false, mScaleArcPaint);

        // 画200条刻度,从下往上画的
        for (int i = 0; i < 200; i++) {
            mCanvas.drawLine(getWidth() / 2, mPaddingTop + 2 * mScaleLength + mTextRect.height() / 2, getWidth() / 2,
                    mPaddingTop + mTextRect.height() / 2 + mScaleLength, mScaleLinePaint);
            mCanvas.rotate(1.8f, getWidth() / 2, getHeight() / 2);
        }
        mCanvas.restore();
    }

    /**
     * 画秒针
     */
    private void drawSecondHand() {
        mCanvas.save();
        System.out.println(mSecondDegree);
        mCanvas.rotate(mSecondDegree, getWidth() / 2, getHeight() / 2);
        mSecondHandPath.reset();
        float offset = mPaddingTop + mTextRect.height() / 2;
        mSecondHandPath.moveTo(getWidth() / 2, offset + 0.26f * mRadius);
        mSecondHandPath.lineTo(getWidth() / 2 - 0.05f * mRadius, offset + 0.34f * mRadius);
        mSecondHandPath.lineTo(getWidth() / 2 + 0.05f * mRadius, offset + 0.34f * mRadius);
        mSecondHandPath.close();
        mCanvas.drawPath(mSecondHandPath, mSecondHandPaint);
        mCanvas.restore();
    }

    /**
     * 画分针
     */
    private void drawMinuteHand() {
        mCanvas.save();
        mCanvas.rotate(mMinuteDegree, getWidth() / 2, getHeight() / 2);
        mMinuteHandPath.reset();
        float offset = mPaddingTop + mTextRect.height() / 2;
        mMinuteHandPath.moveTo(getWidth() / 2 - 0.01f * mRadius, getHeight() / 2 - 0.03f * mRadius);
        mMinuteHandPath.lineTo(getWidth() / 2 - 0.008f * mRadius,offset + 0.365f * mRadius);
        mMinuteHandPath.quadTo(getWidth() / 2, offset + 0.345f * mRadius,
                getWidth() / 2 + 0.008f * mRadius, offset + 0.365f * mRadius);
        mMinuteHandPath.lineTo(getWidth() / 2 + 0.01f * mRadius, getHeight() / 2 - 0.03f * mRadius);
        mMinuteHandPath.close();
        mMinuteHandPaint.setStyle(Paint.Style.FILL);
        mCanvas.drawPath(mMinuteHandPath, mMinuteHandPaint);

        mRectF.set((float) (getWidth() / 2 - 0.03 * mRadius), getHeight() / 2 - 0.03f * mRadius,
                getWidth() / 2 + 0.03f * mRadius, getHeight() / 2 + 0.03f * mRadius);
        mMinuteHandPaint.setStyle(Paint.Style.STROKE);
        mMinuteHandPaint.setStrokeWidth(0.02f * mRadius);
        mCanvas.drawArc(mRectF,0,360,false,mMinuteHandPaint);
        mCanvas.restore();
    }

    /**
     * 画时针
     */
    private void drawHourHand(){
        mCanvas.save();
//        mCanvas.translate(mCanvasTranslateX * 1.2f, mCanvasTranslateY * 1.2f);
        mCanvas.rotate(mHourDegree, getWidth() / 2, getHeight() / 2);
        mHourHandPath.reset();
        float offset = mPaddingTop + mTextRect.height() / 2;
        mHourHandPath.moveTo(getWidth() / 2 - 0.018f * mRadius, getHeight() / 2 - 0.03f * mRadius);
        mHourHandPath.lineTo(getWidth() / 2 - 0.009f * mRadius, offset + 0.48f * mRadius);
        mHourHandPath.quadTo(getWidth() / 2, offset + 0.46f * mRadius,
                getWidth() / 2 + 0.009f * mRadius, offset + 0.48f * mRadius);
        mHourHandPath.lineTo(getWidth() / 2 + 0.018f * mRadius, getHeight() / 2 - 0.03f * mRadius);
        mHourHandPath.close();
        mHourHandPaint.setStyle(Paint.Style.FILL);
        mCanvas.drawPath(mHourHandPath, mHourHandPaint);

        mCircleRectF.set(getWidth() / 2 - 0.03f * mRadius, getHeight() / 2 - 0.03f * mRadius,
                getWidth() / 2 + 0.03f * mRadius, getHeight() / 2 + 0.03f * mRadius);
        mHourHandPaint.setStyle(Paint.Style.STROKE);
        mHourHandPaint.setStrokeWidth(0.01f * mRadius);
        mCanvas.drawArc(mCircleRectF, 0, 360, false, mHourHandPaint);
        mCanvas.restore();
    }

}
