package com.test.test3app.stickerreply;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


/**
 * created by zhaoyuntao
 * on 29/12/2020
 * description:
 */
public class AutoFitView extends ViewGroup {

    public AutoFitView(Context context) {
        this(context, null);
    }

    public AutoFitView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoFitView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAdapter(AutoFitAdapter<?> adapter) {
        adapter.setAutoFixCallback(new AutoFitAdapter.AutoFixCallback() {
            @Override
            public void onAddView(View view) {
                addView(view);
            }

            @Override
            public void onAddView(int position, View view) {
                addView(view, position);
            }

            @Override
            public void onRemoveView(int position) {
                removeViewAt(position);
            }

            @Override
            public View onGetView(int position) {
                return getChildAt(position);
            }

            @Override
            public int onGetChildCount() {
                return getChildCount();
            }

            @Override
            public ViewGroup onGetParent() {
                return AutoFitView.this;
            }

            @Override
            public void onRemoveAllViews() {
                removeAllViews();
            }

            @Override
            public void onChangeViewPosition(int fromPosition, int toPosition) {
                if (fromPosition == toPosition) {
                    return;
                }
                View child = getChildAt(fromPosition);
                removeView(child);
                if (fromPosition > toPosition) {
                    addView(child, toPosition);
                } else {
                    addView(child, toPosition + 1);
                }
            }
        });
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMeasure = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMeasure = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int maxLineWidth = 0;
        int totalHeight = 0;
        int curLineWidth = 0;
        int curLineHeight = 0;
        int count = getChildCount();
        View child;
        MarginLayoutParams params;
        int childWidth;
        int childHeight;
        for (int i = 0; i < count; i++) {
            child = getChildAt(i);
            if (View.GONE == child.getVisibility()) {
                continue;
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            params = (MarginLayoutParams) child.getLayoutParams();
            childWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            childHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;
            if (curLineWidth + childWidth > widthMeasure - getPaddingLeft() - getPaddingRight()) {
                maxLineWidth = Math.max(maxLineWidth, curLineWidth);
                totalHeight += curLineHeight;
                curLineWidth = childWidth;
                curLineHeight = childHeight;
            } else {
                curLineWidth += childWidth;
                curLineHeight = Math.max(curLineHeight, childHeight);
            }
            if (i == count - 1) {
                maxLineWidth = Math.max(maxLineWidth, curLineWidth);
                totalHeight += childHeight;
            }
        }
        setMeasuredDimension(
                widthMode != MeasureSpec.EXACTLY ? maxLineWidth + getPaddingLeft() + getPaddingRight() : widthMeasure,
                heightMode != MeasureSpec.EXACTLY ? totalHeight + getPaddingTop() + getPaddingBottom() : heightMeasure);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int width = getWidth();
        int rightLimit = width - getPaddingRight();
        int baseLeft = getPaddingLeft();
        int baseTop = getPaddingTop();
        int curLeft = baseLeft;
        int curTop = baseTop;
        View child;
        int viewL, viewT, viewR, viewB;
        MarginLayoutParams params;
        int childWidth;
        int childHeight;
        int childW, childH;
        int lastChildHeight = 0;
        for (int i = 0; i < count; i++) {
            child = getChildAt(i);
            if (View.GONE == child.getVisibility()) {
                continue;
            }
            childW = child.getMeasuredWidth();
            childH = child.getMeasuredHeight();
            params = (MarginLayoutParams) child.getLayoutParams();
            childWidth = childW + params.leftMargin + params.rightMargin;
            childHeight = childH + params.topMargin + params.bottomMargin;
            if (curLeft + childWidth > rightLimit) {
                curTop = curTop + lastChildHeight;
                viewL = baseLeft + params.leftMargin;
                viewT = curTop + params.topMargin;
                viewR = viewL + childW;
                viewB = viewT + childH;
                curLeft = baseLeft + childWidth;
            } else {
                viewL = curLeft + params.leftMargin;
                viewT = curTop + params.topMargin;
                viewR = viewL + childW;
                viewB = viewT + childH;
                curLeft = curLeft + childWidth;
            }
            lastChildHeight = childHeight;
            child.layout(viewL, viewT, viewR, viewB);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
