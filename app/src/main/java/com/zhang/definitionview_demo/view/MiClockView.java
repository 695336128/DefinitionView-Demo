package com.zhang.definitionview_demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.zhang.definitionview_demo.R;

/**
 * Created by zhang . DATA: 2017/4/11 . Description : 仿小米时钟
 */

public class MiClockView extends View {

    /** 背景色 */
    private int backgroundColor;

    /** 亮色 */
    private int lightColor;

    /** 暗色 */
    private int darkColor;

    /** 字号 */
    private int textSize;

    private Paint mPaint;

    private RectF mRectF;

    public MiClockView(Context context) {
        this(context, null);
    }

    public MiClockView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public MiClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MiClockView, defStyleAttr, 0);
        backgroundColor = array.getColor(R.styleable.MiClockView_backgroundColor, Color.parseColor("#237EAD"));
        lightColor = array.getColor(R.styleable.MiClockView_lightColor, Color.parseColor("#ffffff"));
        darkColor = array.getColor(R.styleable.MiClockView_darkColor, Color.parseColor("#80ffffff"));
        textSize = (int) array.getDimension(R.styleable.MiClockView_textSize,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
        array.recycle();
        mPaint = new Paint();
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
    }
}
