package com.zhang.definitionview_demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ListView;

/**
 * Created by zhang . DATA: 2017/9/6 . Description : item 可左右滑动的listview
 */

public class FlingRemovedListView extends ListView {

    /**
     * 速度测量器
     */
    private VelocityTracker velocityTracker;

    /**
     * 手指滑动过程中上一个点的 X 坐标
     */
    private float preX;

    /**
     * 手指第一次按下的点的 X 坐标
     */
    private float firstX;

    /**
     * 手指第一次按下的点的 Y 坐标
     */
    private float firstY;

    /**
     * 要滑动的item项
     */
    private View willFlingView;

    /**
     * 要滑动的列表项View的索引位置
     */
    private int position = INVALID_POSITION;

    /**
     * 最小滑动距离
     */
    private int touchSlop;

    /**
     * 最小速率
     */
    private static int SNAP_VELOCITY = 100;

    /**
     * 是否正在滑动
     */
    private boolean isSlide;

    public FlingRemovedListView(Context context) {
        this(context, null);
    }

    public FlingRemovedListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlingRemovedListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    /**
     * 事件分发
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(ev);

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstX = preX = x;
                firstY = y;
                position = this.pointToPosition(x, y);
                if (position != INVALID_POSITION) {
                    int visibleIndex = position - getFirstVisiblePosition();
                    willFlingView = getChildAt(visibleIndex);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                velocityTracker.computeCurrentVelocity(1000);
                float xVelocity = velocityTracker.getXVelocity();
                System.out.println(xVelocity + "");
                if (Math.abs(xVelocity) > SNAP_VELOCITY
                        || Math.abs(x - firstX) > touchSlop
                        && Math.abs(y - firstX) < touchSlop) {
                    isSlide = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (velocityTracker != null) {
                    velocityTracker.clear();
                    velocityTracker.recycle();
                    velocityTracker = null;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isSlide && position != INVALID_POSITION) {
            float x = ev.getX();
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    float dx = preX - x;
                    willFlingView.scrollBy((int) dx, 0);
                    preX = x;
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    willFlingView = null;
                    position = INVALID_POSITION;
                    if (velocityTracker != null) {
                        velocityTracker.clear();
                        velocityTracker.recycle();
                        velocityTracker = null;
                    }
                    isSlide = false;
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }
}
