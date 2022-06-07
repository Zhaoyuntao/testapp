package com.test.test3app.event;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

/**
 * created by zhaoyuntao
 * on 06/06/2022
 * description:
 */
public class AViewGroup extends ViewGroup implements ViewInterface {
    private long time;
    private String content;
    private boolean isSliding;
    private boolean isLongClick;

    public AViewGroup(Context context) {
        super(context);
    }

    public AViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int wV = r - l;
        int hV = b - t;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            int w = child.getMeasuredWidth();
            int h = child.getMeasuredHeight();
            int left = (wV - w) / 2;
            int top = (hV - h) / 2;
            int right = left + w;
            int bottom = top + h;
            child.layout(left, top, right, bottom);
        }
    }

    private float xDown;
    private long timeDown;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        long now = SystemClock.elapsedRealtime();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                e("ViewGroup onInterceptTouchEvent down");
                isLongClick = false;
                isSliding = false;
                timeDown = now;
                xDown = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
//                e("ViewGroup onInterceptTouchEvent move");
                if (Math.abs(ev.getX() - xDown) > ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                    e("is sliding>>>>>>>>>>>>>>>>>>>>");
                    isSliding = true;
                    return true;
                } else if ((now - timeDown) > ViewConfiguration.getLongPressTimeout()) {
                    e("is long click>>>>>>>>>>>>>>>>>>>>");
                    isLongClick = true;
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                e("ViewGroup onInterceptTouchEvent up");
                if (isSliding || isLongClick) {
                    isSliding = false;
                    isLongClick = false;
                    timeDown = 0;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (isSliding) {
                    S.s("isSliding");
                    isSliding = false;
                    return true;
                } else if (isLongClick) {
                    performLongClick();
                    isLongClick = false;
                    return true;
                }
                break;
//            case MotionEvent.ACTION_UP:
//                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String a) {
        this.content = a;
    }

    @Override
    public void setTime(long time) {
        this.time = time;
    }
}
