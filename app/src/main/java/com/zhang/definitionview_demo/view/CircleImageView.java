package com.zhang.definitionview_demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.zhang.definitionview_demo.R;

/**
 * Created by zhang . DATA: 2017/8/31 . Description : 圆形头像
 */

public class CircleImageView extends ImageView {

    private Paint paint;

    private Xfermode xfermode;

    private Path path = new Path();

    private int borderSize;

    private int borderColor;

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        path = new Path();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
        borderSize = a.getDimensionPixelSize(R.styleable.CircleImageView_circle_border, 0);
        borderColor = a.getColor(R.styleable.CircleImageView_circle_border_color, Color.GREEN);
        a.recycle();// 及时回收
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
        setMeasuredDimension(width, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Drawable mDrawable = getDrawable();
        if (mDrawable == null) {
            super.onDraw(canvas);
        }

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        RectF ovalRect = new RectF(0, 0, width, height);
        int layerId = canvas.saveLayer(getPaddingLeft(), getPaddingTop(), width, height, null, Canvas.ALL_SAVE_FLAG);
        Bitmap bitmap = ((BitmapDrawable) mDrawable).getBitmap();
        canvas.drawBitmap(bitmap, new Rect(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight()),
                ovalRect, null);
        paint.setXfermode(xfermode);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        path.reset();
        path.addOval(ovalRect, Path.Direction.CCW);
        canvas.drawPath(path, paint);
        paint.setXfermode(null);
        canvas.restoreToCount(layerId);

        if (borderSize != 0) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(borderColor);
            paint.setStrokeWidth(borderSize);
            ovalRect.inset(borderSize / 2, borderSize / 2);
            canvas.drawOval(ovalRect, paint);
        }
    }
}
