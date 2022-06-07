package com.test.test3app.scrollView;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.zhaoyuntao.androidutils.tools.VibratorTool;

/**
 * created by zhaoyuntao
 * on 2020-03-25
 * description:
 */
public class SlideLayout extends ViewGroup {

    private final int touchSlop;
    private final long longClickTimeOut;
    private final Scroller scrollerChild;
    private final Scroller scroller;

    private View childView, hideTailView;
    private SlideListener listener;

    private float x, xDown, xLast;
    private int rightBorder;
    private long timeDown;
    private boolean isLongClicking;
    private boolean isDistanceOver;
    private boolean isTimeOut;

    public SlideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
        scrollerChild = new Scroller(context);
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        longClickTimeOut = ViewConfiguration.getLongPressTimeout();
    }

    @Override
    public SlideLayoutParams generateLayoutParams(AttributeSet attrs) {
        return new SlideLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        hideTailView = getChildAt(0);
        childView = getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        if (childCount > 2) {
            throw new RuntimeException("You can only put two children in this layout");
        }
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int calculateHeight = Math.max(childView.getMeasuredHeight(), hideTailView.getMeasuredHeight());
        setMeasuredDimension(sizeWidth, calculateHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        SlideLayoutParams layoutParams = (SlideLayoutParams) childView.getLayoutParams();
        int leftMain = layoutParams.gravity == Gravity.START ? l + layoutParams.getMarginStart() : r - layoutParams.getMarginEnd() - childView.getMeasuredWidth();
        int topMain = layoutParams.topMargin;
        int rightMain = layoutParams.gravity == Gravity.START ? leftMain + childView.getMeasuredWidth() : r - layoutParams.getMarginEnd();
        int bottomMain = topMain + childView.getMeasuredHeight();
        childView.layout(leftMain, topMain, rightMain, bottomMain);

        int leftFollow = l - hideTailView.getMeasuredWidth();
        int rightFollow = l;
        int topFollow = (int) (topMain + (getMeasuredHeight() - hideTailView.getMeasuredHeight()) / 2f);
        int bottomFollow = topFollow + hideTailView.getMeasuredHeight();
        hideTailView.layout(leftFollow, topFollow, rightFollow, bottomFollow);
        rightBorder = hideTailView.getMeasuredWidth();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = ev.getRawX();
                xLast = xDown;
                break;
            case MotionEvent.ACTION_MOVE:
                x = ev.getRawX();
                xLast = x;
                if (canSlide() && Math.abs(x - xDown) > touchSlop) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        long now = SystemClock.elapsedRealtime();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDistanceOver = false;
                isTimeOut = false;
                isLongClicking = false;
                timeDown = now;
                return true;
            case MotionEvent.ACTION_MOVE:
                isDistanceOver = Math.abs(x - xDown) > touchSlop;
                isTimeOut = (now - timeDown) > longClickTimeOut;
                x = event.getRawX();
                if (canSlide() && !isLongClicking && isDistanceOver) {
                    int scrolledXDistance = (int) (xLast - x);
                    if (getScrollX() + scrolledXDistance > 0) {
                        scrollTo(0, 0);
                        childView.scrollTo(0, 0);
                        if (listener != null) {
                            listener.onSlideDistanceChange(0);
                        }
                    } else if (getScrollX() + scrolledXDistance < -rightBorder || childView.getScrollX() < 0) {
                        if (getScrollX() != -rightBorder) {
                            scrollTo(-rightBorder, 0);
                            VibratorTool.vibrate(getContext());
                            if (listener != null) {
                                listener.onSlideDistanceChange(1);
                                listener.onSlideToRight();
                            }
                        }
                        childView.scrollBy(scrolledXDistance / 2, 0);
                    } else {
                        scrollBy(scrolledXDistance, 0);
                        childView.scrollTo(0, 0);
                        if (listener != null) {
                            listener.onSlideDistanceChange(Math.abs(getScrollX()) / (float) Math.abs(rightBorder));
                        }
                    }
                } else if (!isDistanceOver && !isLongClicking && isTimeOut) {
                    isLongClicking = true;
                    performLongClick();
                }
                xLast = x;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (!isDistanceOver && !isTimeOut) {
                    performClick();
                }
                isDistanceOver = false;
                isTimeOut = false;
                isLongClicking = false;
                int scrollX = getScrollX();
                int scrollXMain = childView.getScrollX();
                scroller.startScroll(scrollX, 0, -scrollX, 0);
                scrollerChild.startScroll(scrollXMain, 0, -scrollXMain, 0);
                invalidate();
                childView.invalidate();
                if (listener != null) {
                    listener.onSlideDistanceChange(0);
                    listener.onFingerUp(getScrollX() <= -rightBorder || childView.getScrollX() < 0);
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean canSlide() {
        if (listener != null) {
            return listener.canSlide();
        } else {
            return false;
        }
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
        if (scrollerChild.computeScrollOffset()) {
            childView.scrollTo(scrollerChild.getCurrX(), scrollerChild.getCurrY());
            childView.invalidate();
        }
    }

    public void setSlideListener(SlideListener listener) {
        this.listener = listener;
    }

    public interface SlideListener {
        default void onSlideToRight() {
        }

        void onFingerUp(boolean slideToRight);

        void onSlideDistanceChange(float percent);

        boolean canSlide();
    }

    public static class SlideLayoutParams extends LinearLayout.LayoutParams {
        public SlideLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public SlideLayoutParams(int width, int height) {
            super(width, height);
        }

        public SlideLayoutParams(int width, int height, float weight) {
            super(width, height, weight);
        }

        public SlideLayoutParams(LayoutParams p) {
            super(p);
        }

        public SlideLayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public SlideLayoutParams(LinearLayout.LayoutParams source) {
            super(source);
        }
    }
}
