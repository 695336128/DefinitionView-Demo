package com.zhang.definitionview_demo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zhang.definitionview_demo.R;

/**
 * Created by zhang . DATA: 2017/8/29 . Description : 圆形头像
 */

public class CirclePhotoView extends View {

    private Bitmap catBitmap;
    private Bitmap icBitmap;

    private Bitmap circleBitmap;

    private Canvas cvsCanvas;

    private Paint mPaint;

    public CirclePhotoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        catBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cat);
        icBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        int minWidth = Math.min(catBitmap.getWidth(),catBitmap.getHeight());
        circleBitmap = Bitmap.createBitmap(minWidth,minWidth, Bitmap.Config.ARGB_8888);
        cvsCanvas = new Canvas(circleBitmap);
        int r = minWidth/2;
        cvsCanvas.drawCircle(r,r,r,mPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = circleBitmap.getWidth();
        int layer = canvas.saveLayer(100,100,100 + icBitmap.getWidth(),100 + icBitmap.getWidth(),null,Canvas.ALL_SAVE_FLAG);
        canvas.drawBitmap(catBitmap,0,0,null);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(icBitmap,100,100,mPaint);
        canvas.restoreToCount(layer);
    }
}
