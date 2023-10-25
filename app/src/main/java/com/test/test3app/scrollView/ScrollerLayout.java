package com.test.test3app.scrollView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import im.turbo.utils.log.S;

import im.turbo.baseui.utils.UiUtils;

/**
 * Created by guolin on 16/1/12.
 */
public class ScrollerLayout extends ViewGroup {

    private Scroller scroller;
    private int touchSlop;
    private float xDown, yDown;
    private int lastIndex;

    /**
     * 手机当时所处的屏幕坐标
     */
    private float xMove, yMove;

    /**
     * 上次触发ACTION_MOVE事件时的屏幕坐标
     */
    private float xLastMove, yLastMove;

    /**
     * 界面可滚动的左边界
     */
    private int leftBorder;
    private int topBorder;
    private int bottomBorder;

    /**
     * 界面可滚动的右边界
     */
    private int rightBorder;

    public ScrollerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 第一步，创建Scroller的实例
        scroller = new Scroller(context);
        // 获取TouchSlop值
        touchSlop = UiUtils.dipToPx(10); ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            // 为ScrollerLayout中的每一个子控件测量大小
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                // 为ScrollerLayout中的每一个子控件在水平方向上进行布局
                if (true) {
                    childView.layout(0, i * childView.getMeasuredHeight(), childView.getMeasuredWidth(), (i + 1) * childView.getMeasuredHeight());
                } else {
                    childView.layout(i * childView.getMeasuredWidth(), 0, (i + 1) * childView.getMeasuredWidth(), childView.getMeasuredHeight());
                }
            }
            // 初始化左右边界值
            leftBorder = getChildAt(0).getLeft();
            topBorder = getChildAt(0).getTop();
            rightBorder = getChildAt(getChildCount() - 1).getRight();
            bottomBorder = getChildAt(getChildCount() - 1).getBottom();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = ev.getRawX();
                yDown = ev.getRawY();
                xLastMove = xDown;
                yLastMove = yDown;
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = ev.getRawX();
                yMove = ev.getRawY();
                float diff = Math.abs(xMove - xDown);
                float diffY = Math.abs(yMove - yDown);
                xLastMove = xMove;
                yLastMove = yMove;
                // 当手指拖动值大于TouchSlop值时，认为应该进行滚动，拦截子控件的事件
                if ((true?diffY:diff) > touchSlop) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                xMove = event.getRawX();
                yMove = event.getRawY();
                int scrolledX = (int) (xLastMove - xMove);
                int scrolledY = (int) (yLastMove - yMove);
                if (true) {
                    if (getScrollY() + scrolledY < topBorder) {
                        scrollTo(0, topBorder);
                        return true;
                    } else if (getScrollY() + getHeight() + scrolledY > bottomBorder) {
                        scrollTo(0, bottomBorder - getHeight());
                        return true;
                    }
                    scrollBy(0, scrolledY);
                } else {
                    if (getScrollX() + scrolledX < leftBorder) {
                        scrollTo(leftBorder, 0);
                        return true;
                    } else if (getScrollX() + getWidth() + scrolledX > rightBorder) {
                        scrollTo(rightBorder - getWidth(), 0);
                        return true;
                    }
                    scrollBy(scrolledX, 0);
                }

                xLastMove = xMove;
                yLastMove = yMove;
                break;
            case MotionEvent.ACTION_UP:
                // 当手指抬起时，根据当前的滚动值来判定应该滚动到哪个子控件的界面
                int targetIndex=lastIndex;
                if (true) {
                    int distance= (int) (yMove-yDown);
                    if(Math.abs(distance)> UiUtils.dipToPx(100)){

                        if (distance<0) {
                            targetIndex = lastIndex >= (getChildCount() - 1) ? (getChildCount() - 1) : lastIndex + 1;
                        } else {
                            targetIndex = lastIndex == 0 ? 0 : lastIndex - 1;
                        }
//                    targetIndex = (getScrollY() + getHeight() / 2) / getHeight();
                        S.s("getScrollY() :" + getScrollY() + " " + getHeight() + " " + targetIndex);
                    }
                    int dy = targetIndex * getHeight() - getScrollY();
                    // 第二步，调用startScroll()方法来初始化滚动数据并刷新界面
                    scroller.startScroll(0, getScrollY(), 0, dy);
                } else {
                    targetIndex = (getScrollX() + getWidth() / 2) / getWidth();
                    int dx = targetIndex * getWidth() - getScrollX();
                    // 第二步，调用startScroll()方法来初始化滚动数据并刷新界面
                    scroller.startScroll(getScrollX(), 0, dx, 0);
                }
                lastIndex = targetIndex;
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        // 第三步，重写computeScroll()方法，并在其内部完成平滑滚动的逻辑
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }
}
