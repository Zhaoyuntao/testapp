package com.test.test3app.scrollView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.zhaoyuntao.androidutils.tools.VibratorTool;

/**
 * created by zhaoyuntao
 * on 2020-03-25
 * description:
 */
public class SlideLayout extends ViewGroup {

    private final Scroller scrollerChild;
    private final Scroller scroller;
    private float xDown;
    private float x;
    private float xLast;
    private int rightBorder;
    private View childView, hideTailView;
    private SlideListener listener;
    private int touchSlop;

    public SlideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
        scrollerChild = new Scroller(context);
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        if (childCount > 2) {
            throw new RuntimeException("You can only put two child in this layout");
        }
        childView = getChildAt(1);
        hideTailView = getChildAt(0);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int calculateHeight = Math.max(childView.getMeasuredHeight(), hideTailView.getMeasuredHeight());
        setMeasuredDimension(sizeWidth, calculateHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        MarginLayoutParams cParams = (MarginLayoutParams) childView.getLayoutParams();
        int leftMain = cParams.leftMargin;
        int topMain = cParams.topMargin;
        int rightMain = leftMain + childView.getMeasuredWidth();
        int bottomMain = topMain + childView.getMeasuredHeight();
        childView.layout(leftMain, topMain, rightMain, bottomMain);

        MarginLayoutParams cParams2 = (MarginLayoutParams) childView.getLayoutParams();
        int leftFollow = cParams2.leftMargin;
        int topFollow = cParams2.topMargin;
        int bottomFollow = topFollow + hideTailView.getMeasuredHeight();
        hideTailView.layout(leftFollow, topFollow, leftMain, bottomFollow);
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
                // 当手指拖动值大于TouchSlop值时，认为应该进行滚动，拦截子控件的事件
                if (Math.abs(x - xDown) > touchSlop) {
//                    S.s("Math.abs(x - xDown) > touchSlop:" + touchSlop);
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                x = event.getRawX();
                int scrolledXDistance = (int) (xLast - x);
                if (getScrollX() + scrolledXDistance > 0) {
                    scrollTo(0, 0);
                    childView.scrollTo(0, 0);
                } else if (getScrollX() + scrolledXDistance < -rightBorder || childView.getScrollX() < 0) {
                    if (getScrollX() != -rightBorder) {
                        scrollTo(-rightBorder, 0);
                        VibratorTool.vibrate(getContext());
                        if (listener != null) {
                            listener.onSlideToRight();
                        }
                    }
                    childView.scrollBy(scrolledXDistance / 2, 0);
                } else {
                    scrollBy(scrolledXDistance, 0);
                    childView.scrollTo(0, 0);
                }
                xLast = x;
                break;
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                int scrollXMain = childView.getScrollX();
                scroller.startScroll(scrollX, 0, -scrollX, 0);
                scrollerChild.startScroll(scrollXMain, 0, -scrollXMain, 0);
                invalidate();
                childView.invalidate();
                if (listener != null) {
                    listener.onFingerUp(getScrollX() <= -rightBorder || childView.getScrollX() < 0);
                }
                break;
        }
        return super.onTouchEvent(event);
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

    public void setListener(SlideListener listener) {
        this.listener = listener;
    }

    public interface SlideListener {
        default void onSlideToRight() {
        }

        void onFingerUp(boolean slideToRight);
    }
}
