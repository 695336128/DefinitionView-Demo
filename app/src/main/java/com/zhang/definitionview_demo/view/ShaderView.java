package com.zhang.definitionview_demo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhang . DATA: 2017/8/24 . Description :
 */

public class ShaderView extends View {

    private int width;

    private int height;

    private Matrix matrix = new Matrix();

    private float mRotate;

    public ShaderView(Context context) {
        super(context);
    }

    public ShaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        Rect rect = new Rect(0, 0, width, height);
        // Bitmap bmp = BitmapFactory.decodeResource(getResources(),
        // R.mipmap.ic_launcher);
        // BitmapShader bs = new BitmapShader(bmp, Shader.TileMode.REPEAT,
        // Shader.TileMode.MIRROR);
        // LinearGradient lg = new LinearGradient(0, 0, getMeasuredWidth(), 0,
        // Color.RED, Color.BLUE,
        // Shader.TileMode.CLAMP);
        // ComposeShader cs = new ComposeShader(bs, lg,
        // PorterDuff.Mode.SRC_ATOP);
        SweepGradient mShader = new SweepGradient(width / 2, height / 2,
                new int[] { Color.GREEN, Color.RED, Color.BLUE, Color.GREEN }, null);
        matrix.setRotate(mRotate,width/2,height/2);
        mShader.setLocalMatrix(matrix);
        mRotate += 3;

        if (mRotate >= 360) {
            mRotate = 0;
        }

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(mShader);
        canvas.drawRect(rect, paint);

        invalidate();

    }
}
