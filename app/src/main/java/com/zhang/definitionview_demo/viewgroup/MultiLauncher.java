package com.zhang.definitionview_demo.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by zhang . DATA: 2017/9/4 . Description : 触摸滑屏
 */

public class MultiLauncher extends ViewGroup {

    private static final int TOUCH_STATE_STOP = 0x001; // 停止状态

    private static final int TOUCH_STATE_FLING = 0x002; // 滑动状态

    private int touchState = TOUCH_STATE_STOP;

    public static final int SNAP_VELOCITY = 100;

    /** 滑动器 */
    private Scroller scroller;

    /** 最小滑动距离 */
    private int touchSlop = 0;

    private float lastionMotionX = 0;// 上次触摸屏的 x 位置

    private float downMotionX = 0; // 按下时候的 x 位置

    private int curScreen; // 当前屏

    private VelocityTracker velocityTracker; // 速率跟踪器

    public MultiLauncher(Context context) {
        this(context, null);
    }

    public MultiLauncher(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    // 第一步 初始化
    public MultiLauncher(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scroller = new Scroller(context);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop(); // 获取当前手机上默认的最小滑动距离
    }

    // 第二步 测量容器本身大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.AT_MOST) {
            throw new IllegalArgumentException("Must not be MeasureSpec.AT_MOST");
        } else {
            width = widthSize * this.getChildCount();
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            throw new IllegalArgumentException("Must not be MeasureSpec.AT_MOST");
        } else {
            height = heightSize;
        }

        setMeasuredDimension(width, height);
    }

    // 第三步 定位子组件
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int n = this.getChildCount();
        int w = (r - l) / n; // 分屏的宽度
        int h = b - t;// 容器的高度

        for (int i = 0; i < this.getChildCount(); i++) {
            View child = getChildAt(i);
            int left = w * i;
            int right = w * (i + 1);
            int top = 0;
            int bottom = h;

            child.layout(left, top, right, bottom);
        }
    }

    // 第四步 判断滚动状态决定是否拦截事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        if (action == MotionEvent.ACTION_DOWN && touchState == TOUCH_STATE_STOP) {
            lastionMotionX = x;
            return false;
        }

        switch (action) {
        case MotionEvent.ACTION_DOWN:
            lastionMotionX = x;
            touchState = scroller.isFinished() ? TOUCH_STATE_STOP : TOUCH_STATE_FLING;
            break;
        case MotionEvent.ACTION_MOVE:
            // 滑动距离过小不算滑动
            int dx = (int) Math.abs(x - lastionMotionX);
            if (dx > touchSlop) {
                touchState = TOUCH_STATE_FLING;
            }
            break;
        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
            touchState = TOUCH_STATE_STOP;
            break;
        default:
            break;
        }
        return touchState != TOUCH_STATE_STOP;
    }

    // 第五步 惯性滚屏
    public void moveToScreen(int whichScreen) {
        curScreen = whichScreen;
        if (curScreen > getChildCount() - 1) {
            curScreen = getChildCount() - 1;
        }
        if (curScreen < 0) {
            curScreen = 0;
        }

        int scrollX = getScrollX(); // 获取x方向偏移值
        // 每一屏的宽度
        int splitWidth = getWidth() / getChildCount();
        // 要移动的距离
        int dx = curScreen * splitWidth - scrollX;
        // 开始移动
        scroller.startScroll(scrollX, 0, dx, 0, Math.abs(dx));
        invalidate();
    }

    /**
     * 根据距离判断滑动到哪里
     */
    public void moveToDestination() {
        // 每一屏的宽度
        int splitWidth = getWidth() / getChildCount();
        // 判断是回滚还是滑到下一屏
        int toScreen = (getScrollX() + splitWidth / 2) / splitWidth;
        moveToScreen(toScreen);
    }

    public void moveToNext() {
        moveToScreen(curScreen + 1);
    }

    public void moveToPrevious() {
        moveToScreen(curScreen - 1);
    }

    // 第六步 响应触屏事件

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
        super.onTouchEvent(event);

        final int x = (int) event.getX();
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            // 手指按下时，如果正在滚动，立即停止
            if (scroller != null && !scroller.isFinished()) {
                scroller.forceFinished(true);
            }
            downMotionX = x;
            lastionMotionX = x;
        case MotionEvent.ACTION_MOVE:
            // 随手指滚动
            int dx = (int) (lastionMotionX - x);
            scrollBy(dx, 0);
            lastionMotionX = x;
            break;
        case MotionEvent.ACTION_UP:
            dx = (int) (x - downMotionX);
            // 手指抬起
            velocityTracker.computeCurrentVelocity(1000);
            int velocityX = (int) velocityTracker.getXVelocity();
            // 通过velocityX的正负值可以判断滑动方向
            if (velocityX > SNAP_VELOCITY && Math.abs(dx) > 300 && curScreen > 0) {
                moveToPrevious();
            } else if (velocityX < -SNAP_VELOCITY && Math.abs(dx) > 300 && curScreen < getChildCount() - 1) {
                moveToNext();
            } else {
                moveToDestination();
            }

            if (velocityTracker != null) {
                velocityTracker.clear();
                velocityTracker.recycle();
                velocityTracker = null;
            }

            touchState = TOUCH_STATE_STOP;
            break;
        case MotionEvent.ACTION_CANCEL:
            touchState = TOUCH_STATE_STOP;
            break;
        }
        return true;
    }

    /**
     * 计算滚动偏移量，必调方法之一
     */
    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            // 滚动尚未完成
            this.scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }
}
