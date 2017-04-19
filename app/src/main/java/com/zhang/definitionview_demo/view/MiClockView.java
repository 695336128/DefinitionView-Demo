package com.zhang.definitionview_demo.view;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;

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

    /**
     * camera，用于旋转时钟实现3D效果
     */
    private Camera mCamera;

    /**
     * camera 获取的变换矩阵
     */
    private Matrix mCameraMatrix;

    /**
     * camera绕X轴旋转的角度
     */
    private float mCameraRotateX;

    /**
     * camera绕Y轴旋转的角度
     */
    private float mCameraRotateY;

    /**
     * camera旋转的最大角度
     */
    private float mMaxCameraRotate = 10;

    /**
     * 手指松开时时钟晃动的动画
     */
    private ValueAnimator mShakeAnim;

    /** 指针的最大位移 */
    private float mMaxCanvasTranslate;

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

        mCamera = new Camera();
        mCameraMatrix = new Matrix();

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
        mMaxCanvasTranslate = 0.02f * mRadius;
        // 梯度扫描渐变，以(w/2,h/2)为中心点，两种起止颜色梯度渐变
        // float数组表示，[0,0.75)为起始颜色所占比例，[0.75,1}为起止颜色渐变所占比例
        mSweepGradient = new SweepGradient(w / 2, h / 2, new int[] { mDarkColor, mLightColor },
                new float[] { 0.75f, 1 });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mCanvas = canvas;
        setCameraRotate();
        getTimeDegree();
        drawTimeText();
        drawScaleLine();
        drawSecondHand();
        drawMinuteHand();
        drawHourHand();
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            if (mShakeAnim != null && mShakeAnim.isRunning()) {
                mShakeAnim.cancel();
            }
            getCameraRotate(event);
            getCanvasTranslate(event);
            break;
        case MotionEvent.ACTION_MOVE:
            getCameraRotate(event);
            getCanvasTranslate(event);
            break;
        case MotionEvent.ACTION_UP:
            startShakeAnim();
            break;
        }
        return true;
    }

    /**
     * 获取camera旋转的大小
     */
    private void getCameraRotate(MotionEvent event) {
        float rotateX = -(event.getY() - getHeight() / 2);
        float rotateY = event.getX() - getWidth() / 2;
        // 求出此时旋转的大小与半径之比
        float[] percentArr = getPercent(rotateX, rotateY);
        // 最终位移的大小按比例匀称改变
        mCameraRotateX = percentArr[0] * mMaxCanvasTranslate;
        mCameraRotateY = percentArr[1] * mMaxCanvasTranslate;
    }

    /***
     * 当拨动时钟时，会发现时针、分针、秒针和刻度盘会有一个较小的偏移量，形成近大远小的立体偏移效果 一开始我打算使用 matrix 和 camera 的
     * mCamera.translate(x, y, z) 方法改变 z 的值 但是并没有效果，所以就动态计算距离，然后在 onDraw()中分零件地
     * mCanvas.translate(x, y)
     *
     * @param event
     *            motionEvent
     */
    private void getCanvasTranslate(MotionEvent event) {
        float translateX = (event.getX() - getWidth() / 2);
        float translateY = (event.getY() - getHeight() / 2);
        // 求出此时位移的大小与半径之比
        float[] percentArr = getPercent(translateX, translateY);
        // 最终位移的大小按比例匀称改变
        mCanvasTranslateX = percentArr[0] * mMaxCanvasTranslate;
        mCanvasTranslateY = percentArr[1] * mMaxCanvasTranslate;
    }

    /**
     * 获取一个操作旋转或位移大小的比例
     *
     * @param x
     * @param y
     * @return
     */
    private float[] getPercent(float x, float y) {
        float[] percentArr = new float[2];
        float percentX = x / mRadius;
        float percentY = y / mRadius;
        if (percentX > 1) {
            percentX = 1;
        } else if (percentX < -1) {
            percentX = -1;
        }
        if (percentY > 1) {
            percentY = 1;
        } else if (percentY < -1) {
            percentY = -1;
        }
        percentArr[0] = percentX;
        percentArr[1] = percentY;
        return percentArr;
    }

    /**
     * 设置3D时钟效果，触摸矩阵的相关设置、照相机的旋转大小 应用在绘制图形之前，否则无效 用Camera实现canvas变换
     */
    private void setCameraRotate() {
        mCameraMatrix.reset();
        mCamera.save();
        mCamera.rotateX(mCameraRotateX);
        mCamera.rotateY(mCameraRotateY);
        mCamera.getMatrix(mCameraMatrix);
        mCamera.restore();
        // camera在view左上角那个点，故旋转默认是以左上角为中心旋转
        // 故在动作之前pre将matrix向左移动getWidth()/2长度，向上移动getHeight()/2长度
        mCameraMatrix.preTranslate(-getWidth() / 2, -getHeight() / 2);
        mCameraMatrix.postTranslate(getWidth() / 2, getHeight() / 2);
        mCanvas.concat(mCameraMatrix);
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
        mCanvas.translate(mCanvasTranslateX, mCanvasTranslateY);
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
        mCanvas.translate(mCanvasTranslateX, mCanvasTranslateY);
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
        mCanvas.translate(mCanvasTranslateX * 1.2f, mCanvasTranslateY * 1.2f);
        mCanvas.rotate(mMinuteDegree, getWidth() / 2, getHeight() / 2);
        mMinuteHandPath.reset();
        float offset = mPaddingTop + mTextRect.height() / 2;
        mMinuteHandPath.moveTo(getWidth() / 2 - 0.01f * mRadius, getHeight() / 2 - 0.03f * mRadius);
        mMinuteHandPath.lineTo(getWidth() / 2 - 0.008f * mRadius, offset + 0.365f * mRadius);
        mMinuteHandPath.quadTo(getWidth() / 2, offset + 0.345f * mRadius, getWidth() / 2 + 0.008f * mRadius,
                offset + 0.365f * mRadius);
        mMinuteHandPath.lineTo(getWidth() / 2 + 0.01f * mRadius, getHeight() / 2 - 0.03f * mRadius);
        mMinuteHandPath.close();
        mMinuteHandPaint.setStyle(Paint.Style.FILL);
        mCanvas.drawPath(mMinuteHandPath, mMinuteHandPaint);

        mRectF.set((float) (getWidth() / 2 - 0.03 * mRadius), getHeight() / 2 - 0.03f * mRadius,
                getWidth() / 2 + 0.03f * mRadius, getHeight() / 2 + 0.03f * mRadius);
        mMinuteHandPaint.setStyle(Paint.Style.STROKE);
        mMinuteHandPaint.setStrokeWidth(0.02f * mRadius);
        mCanvas.drawArc(mRectF, 0, 360, false, mMinuteHandPaint);
        mCanvas.restore();
    }

    /**
     * 画时针
     */
    private void drawHourHand() {
        mCanvas.save();
        mCanvas.translate(mCanvasTranslateX * 1.2f, mCanvasTranslateY * 1.2f);
        mCanvas.rotate(mHourDegree, getWidth() / 2, getHeight() / 2);
        mHourHandPath.reset();
        float offset = mPaddingTop + mTextRect.height() / 2;
        mHourHandPath.moveTo(getWidth() / 2 - 0.018f * mRadius, getHeight() / 2 - 0.03f * mRadius);
        mHourHandPath.lineTo(getWidth() / 2 - 0.009f * mRadius, offset + 0.48f * mRadius);
        mHourHandPath.quadTo(getWidth() / 2, offset + 0.46f * mRadius, getWidth() / 2 + 0.009f * mRadius,
                offset + 0.48f * mRadius);
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

    /**
     * 时钟晃动动画
     */
    private void startShakeAnim() {
        final String cameraRotateXName = "cameraRotateX";
        final String cameraRotateYName = "cameraRotateY";
        final String canvasTranslateXName = "canvasTranslateX";
        final String canvasTranslateYName = "canvasTranslateY";

        // PropertyValuesHolder这个类可以先将动画属性和值暂时的存储起来，后一起执行
        PropertyValuesHolder cameraRotateXHolder = PropertyValuesHolder.ofFloat(cameraRotateXName, mCameraRotateX, 0);
        PropertyValuesHolder cameraRotateYHolder = PropertyValuesHolder.ofFloat(cameraRotateYName, mCameraRotateY, 0);
        PropertyValuesHolder canvasTranslateXHolder = PropertyValuesHolder.ofFloat(canvasTranslateXName,
                mCanvasTranslateX, 0);
        PropertyValuesHolder canvasTranslateYHolder = PropertyValuesHolder.ofFloat(canvasTranslateYName,
                mCanvasTranslateY, 0);

        mShakeAnim = ValueAnimator.ofPropertyValuesHolder(cameraRotateXHolder, cameraRotateYHolder,
                canvasTranslateXHolder, canvasTranslateYHolder);
        mShakeAnim.setInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float input) {
                // http://inloop.github.io/interpolator/
                float f = 0.571429f;
                return (float) (Math.pow(2, -2 * input) * Math.sin((input - f / 4) * (2 * Math.PI) / f) + 1);
            }
        });
        mShakeAnim.setDuration(1000);
        mShakeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCameraRotateX = (float) animation.getAnimatedValue(cameraRotateXName);
                mCameraRotateY = (float) animation.getAnimatedValue(cameraRotateYName);
                mCanvasTranslateX = (float) animation.getAnimatedValue(canvasTranslateXName);
                mCanvasTranslateY = (float) animation.getAnimatedValue(canvasTranslateYName);
            }
        });
        mShakeAnim.start();
    }

}
