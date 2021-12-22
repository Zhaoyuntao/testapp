package com.test.test3app.recyclerview;

import android.content.Context;

import androidx.recyclerview.widget.LinearSmoothScroller;

/**
 * created by zhaoyuntao
 * on 2020/8/14
 * description:
 */
public abstract class BaseScroller extends LinearSmoothScroller {
    private int offset;
    private ChatViewLayoutManager.AnimationListener animationListener;

    public BaseScroller(Context context) {
        super(context);
    }

    public void setAnimationListener(ChatViewLayoutManager.AnimationListener animationListener) {
        this.animationListener = animationListener;
    }

    @Override
    protected int getVerticalSnapPreference() {
        return snapToBottom() ? SNAP_TO_END : SNAP_TO_START;
    }

    protected abstract boolean snapToBottom();

    @Override
    protected void onStart() {
        super.onStart();
        if (animationListener != null) {
            animationListener.onStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (animationListener != null) {
            animationListener.onStop(getTargetPosition());
        }
    }

    @Override
    public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
        switch (snapPreference) {
            case SNAP_TO_START:
                return boxStart - viewStart + offset;
            case SNAP_TO_END:
                return boxEnd - viewEnd - offset;
            case SNAP_TO_ANY:
                final int dtStart = boxStart - viewStart;
                if (dtStart > 0) {
                    return dtStart;
                }
                final int dtEnd = boxEnd - viewEnd;
                if (dtEnd < 0) {
                    return dtEnd;
                }
                break;
            default:
                throw new IllegalArgumentException("snap preference should be one of the"
                        + " constants defined in SmoothScroller, starting with SNAP_");
        }
        return 0;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}