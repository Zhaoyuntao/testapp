package com.test.test3app.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.zhaoyuntao.androidutils.tools.S;

/**
 * created by zhaoyuntao
 * on 19/12/2021
 * description:
 */
public class ScaleFrameLayout extends ViewGroup {
    public ScaleFrameLayout(Context context) {
        super(context);
    }

    public ScaleFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScaleFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ScaleFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = r - l;
        int height = b - t;
        View child = getChildAt(0);
        if (child != null) {
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            if (childHeight >= height) {
//                S.s("onLayout : bigger       h:" + height + " t:" + t + " b:" + b + " child h:" + childHeight);
                child.layout(0, b - childHeight, childWidth, b);
            } else {
//                S.s("onLayout : smaller      h:" + height + " t:" + t + " b:" + b + " child h:" + childHeight);
                child.layout(0, t, childWidth, t + childHeight);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightMax = MeasureSpec.getSize(heightMeasureSpec);
        View child = getChildAt(0);
        if (child != null) {
//            S.lll();
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            if (child.getMeasuredHeight() > heightMax) {
//                S.s("onMeasure        bigger heightMeasureSpec:" + heightMeasureSpec + " heightMax:" + heightMax + " child height:" + child.getMeasuredHeight());
            } else {
//                S.s("onMeasure        smaller heightMeasureSpec:" + heightMeasureSpec + " heightMax:" + heightMax + " child height:" + child.getMeasuredHeight());
            }
        }
    }

}
