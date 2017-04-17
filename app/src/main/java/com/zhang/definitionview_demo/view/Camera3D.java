package com.zhang.definitionview_demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.zhang.definitionview_demo.R;

/**
 * Created by zhang . DATA: 2017/4/14 . Description : 使用Camera进行图片变换
 */

public class Camera3D extends View {

    private Paint mPaint;

    private Canvas mCanvas;

    private Camera camera;

    private Bitmap mImage;

    private Matrix mMatrix;

    public Camera3D(Context context) {
        this(context, null);
    }

    public Camera3D(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Camera3D(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Camera3D, defStyleAttr,
                0);
        mImage = BitmapFactory.decodeResource(getResources(),array.getResourceId(R.styleable.Camera3D_image,0));
        array.recycle();
        mPaint = new Paint();
        camera = new Camera();
        mMatrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        mPaint.setAntiAlias(true);
//        canvas.drawBitmap(mImage,0,0,mPaint);
        camera.rotate(60,0,0);
        camera.setLocation(0,0,-8);
        camera.getMatrix(mMatrix);

        canvas.drawBitmap(mImage, mMatrix, mPaint);
    }
}
