package com.zhang.definitionview_demo.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhang . DATA: 2017/9/1 . Description : 四个角放置view的容器
 */
public class CornerLayout extends ViewGroup {
    public CornerLayout(Context context) {
        super(context);
    }

    public CornerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CornerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 定义子组件
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            switch (i) {
                case 0:
                    // 定位到左上角
                    child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
                    break;
                case 1:
                    // 定位到右上角
                    child.layout(getMeasuredWidth() - child.getMeasuredWidth(), 0, getMeasuredWidth(),
                            child.getMeasuredHeight());
                    break;
                case 2:
                    // 定位到左下角
                    child.layout(0, getMeasuredHeight() - child.getMeasuredHeight(), child.getMeasuredWidth(),
                            getMeasuredHeight());
                    break;
                case 3:
                    // 定位到右下角
                    child.layout(getMeasuredWidth() - child.getMeasuredWidth(),
                            getMeasuredHeight() - child.getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight());
                    break;
            }
        }
    }

    /**
     * 测量尺寸
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 先测量所有子组件的大小
        this.measureChildren(widthMeasureSpec, heightMeasureSpec);

        // 在测量自己的大小
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;
        int height = 0;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            // warp_content
            int aWidth = 0;
            int bWidth = 0;
            int cWidth = 0;
            int dWidth = 0;

            for (int i = 0; i < this.getChildCount(); i++) {
                switch (i) {
                    case 0:
                        aWidth = getChildAt(i).getMeasuredWidth();
                        break;
                    case 1:
                        bWidth = getChildAt(i).getMeasuredWidth();
                        break;
                    case 2:
                        cWidth = getChildAt(i).getMeasuredWidth();
                        break;
                    case 3:
                        dWidth = getChildAt(i).getMeasuredWidth();
                        break;
                }
            }
            width = Math.max(aWidth, cWidth) + Math.max(bWidth, dWidth);
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            // warp_content
            int aHeight = 0;
            int bHeight = 0;
            int cHeight = 0;
            int dHeight = 0;

            for (int i = 0; i < getChildCount(); i++) {
                switch (i) {
                    case 0:
                        aHeight = getChildAt(i).getMeasuredHeight();
                        break;
                    case 1:
                        bHeight = getChildAt(i).getMeasuredHeight();
                        break;
                    case 2:
                        cHeight = getChildAt(i).getMeasuredHeight();
                        break;
                    case 3:
                        dHeight = getChildAt(i).getMeasuredHeight();
                        break;
                }
            }

            height = Math.max(aHeight, bHeight) + Math.max(cHeight, dHeight);
        }

        setMeasuredDimension(width, height);

    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return super.generateLayoutParams(attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return super.generateLayoutParams(p);
    }
}
