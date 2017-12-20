package com.zhang.definitionview_demo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zhang.definitionview_demo.R;

/**
 * Created by zhang . DATA: 2017/8/21 . Description :
 */

public class BallMoveView extends View {

    private int i = 0;

    private Bitmap bmpBoom;

    private Paint paint;

    /** 移动的方向 */
    private boolean direction;

    public BallMoveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // 初始化画笔
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);

        bmpBoom = BitmapFactory.decodeResource(getResources(), R.drawable.boom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        // 获取位置的宽度和高度
        int width = bmpBoom.getWidth();
        int height = bmpBoom.getHeight();

        int frameWidth = width / 7;
        Rect rect = new Rect(0, 0, frameWidth, height);
        canvas.save();
        canvas.clipRect(rect);
        canvas.drawBitmap(bmpBoom, -i * frameWidth, 0, null);
        canvas.restore();
        i++;
        if (i == 7) {
            i = 0;
        }

    }
}
