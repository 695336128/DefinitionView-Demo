package com.zhang.definitionview_demo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zhang.definitionview_demo.R;

import java.util.Random;

/**
 * Created by zhang . DATA: 2017/8/29 . Description : 刮刮乐
 */

public class GuaGuaLeView extends View {

    private Random mRandom;

    private Paint mPaint;

    private Paint clearPaint;

    private static final String[] PRIZE = { "一等奖", "二等奖", "三等奖", "未中奖" };

    /** 涂抹的粗细 */
    private static final int FINGER = 50;

    /** 缓冲区 */
    private Bitmap bmpBuffer;

    /** 缓冲区画布 */
    private Canvas cvsBuffer;

    private int curX, curY;

    public GuaGuaLeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mRandom = new Random();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(50);
        mPaint.setColor(Color.WHITE);

        clearPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        clearPaint.setStrokeJoin(Paint.Join.ROUND);
        clearPaint.setStrokeCap(Paint.Cap.ROUND);
        clearPaint.setStrokeWidth(FINGER);

        // 画背景
        drawBackground();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 初始化缓冲区
        bmpBuffer = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        cvsBuffer = new Canvas(bmpBuffer);
        // 会缓冲区蒙上一层灰色
        cvsBuffer.drawColor(Color.parseColor("#ff808080"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bmpBuffer, 0, 0, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            curX = x;
            curY = y;
            break;
        case MotionEvent.ACTION_MOVE:
            cvsBuffer.drawLine(curX, curY, x, y, clearPaint);
            invalidate();
            curX = x;
            curY = y;
            break;
        case MotionEvent.ACTION_UP:
            invalidate();
            break;
        }
        return true;
    }

    /**
     * 绘制背景
     */
    private void drawBackground() {
        Bitmap bmpBackground = BitmapFactory.decodeResource(getResources(), R.drawable.camera);
        // 从资源中读取的 bmpBackground 不可以修改，复制出一张可以修改的图片
        Bitmap bmpBackgroundMutab = bmpBackground.copy(Bitmap.Config.ARGB_8888, true);
        // 在图片上画上中奖信息
        Canvas cvsBackground = new Canvas(bmpBackgroundMutab);
        // 计算出文字所占的区域，将文字放在正中间
        String text = PRIZE[getPrizeIndex()];
        Rect rect = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), rect);
        int x = (bmpBackgroundMutab.getWidth() - rect.width()) / 2;
        int y = (bmpBackgroundMutab.getHeight() - rect.height()) / 2;
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, mPaint);
        mPaint.setShadowLayer(10, 0, 0, Color.GREEN);
        mPaint.setTextAlign(Paint.Align.CENTER);
        cvsBackground.drawText(text, bmpBackgroundMutab.getWidth() / 2, bmpBackgroundMutab.getHeight() / 2, mPaint);
        mPaint.setShadowLayer(0, 0, 0, Color.YELLOW);
        // 画背景
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.setBackground(new BitmapDrawable(getResources(), bmpBackgroundMutab));
        } else {
            this.setBackgroundDrawable(new BitmapDrawable(bmpBackgroundMutab));
        }
    }

    /**
     * 随机生成中奖信息
     * 
     * @return
     */
    private int getPrizeIndex() {
        return mRandom.nextInt(PRIZE.length);
    }

}
