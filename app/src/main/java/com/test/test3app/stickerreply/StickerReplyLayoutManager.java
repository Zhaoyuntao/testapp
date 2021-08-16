package com.test.test3app.stickerreply;

import android.graphics.Rect;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

/**
 * created by zhaoyuntao
 * on 28/12/2020
 * description:
 */
public class StickerReplyLayoutManager extends RecyclerView.LayoutManager {
    private int verticalOffset;
    private int firstVisibleViewPosition;
    private int lastVisibleViewPosition;

    private final SparseArray<Rect> itemRectList;

    public StickerReplyLayoutManager() {
        itemRectList = new SparseArray<>();
    }

    @Override
    public boolean isAutoMeasureEnabled() {
        return true;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0) {
            detachAndScrapAttachedViews(recycler);
            return;
        }
        if (getChildCount() == 0 && state.isPreLayout()) {
            return;
        }
        detachAndScrapAttachedViews(recycler);

        verticalOffset = 0;
        firstVisibleViewPosition = 0;
        lastVisibleViewPosition = getItemCount();

        fill(recycler);
    }


    private void fill(RecyclerView.Recycler recycler) {

        int topOffset = getPaddingTop();

        int leftOffset = getPaddingLeft();
        int lineMaxHeight = 0;

        int minPos = firstVisibleViewPosition;
        lastVisibleViewPosition = getItemCount() - 1;
        if (getChildCount() > 0) {
            View lastView = getChildAt(getChildCount() - 1);
            if (lastView != null) {
                minPos = getPosition(lastView) + 1;
                topOffset = getDecoratedTop(lastView);
                leftOffset = getDecoratedRight(lastView);
                lineMaxHeight = Math.max(lineMaxHeight, getDecoratedMeasurementVertical(lastView));
            }
        }

        for (int i = minPos; i <= lastVisibleViewPosition; i++) {
            View child = recycler.getViewForPosition(i);
            addView(child);
            measureChildWithMargins(child, 0, 0);
            if (leftOffset + getDecoratedMeasurementHorizontal(child) <= getHorizontalSpace()) {
                layoutDecoratedWithMargins(child, leftOffset, topOffset, leftOffset + getDecoratedMeasurementHorizontal(child), topOffset + getDecoratedMeasurementVertical(child));
                Rect rect = new Rect(leftOffset, topOffset + verticalOffset, leftOffset + getDecoratedMeasurementHorizontal(child), topOffset + getDecoratedMeasurementVertical(child) + verticalOffset);
                itemRectList.put(i, rect);
                leftOffset += getDecoratedMeasurementHorizontal(child);
                lineMaxHeight = Math.max(lineMaxHeight, getDecoratedMeasurementVertical(child));
            } else {
                leftOffset = getPaddingLeft();
                topOffset += lineMaxHeight;
                lineMaxHeight = 0;
                if (topOffset > getHeight() - getPaddingBottom()) {
                    removeAndRecycleView(child, recycler);
                    lastVisibleViewPosition = i - 1;
                } else {
                    layoutDecoratedWithMargins(child, leftOffset, topOffset, leftOffset + getDecoratedMeasurementHorizontal(child), topOffset + getDecoratedMeasurementVertical(child));
                    Rect rect = new Rect(leftOffset, topOffset + verticalOffset, leftOffset + getDecoratedMeasurementHorizontal(child), topOffset + getDecoratedMeasurementVertical(child) + verticalOffset);
                    itemRectList.put(i, rect);
                    leftOffset += getDecoratedMeasurementHorizontal(child);
                    lineMaxHeight = Math.max(lineMaxHeight, getDecoratedMeasurementVertical(child));
                }
            }
        }
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }

    public int getDecoratedMeasurementHorizontal(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        return getDecoratedMeasuredWidth(view) + params.leftMargin
                + params.rightMargin;
    }

    public int getDecoratedMeasurementVertical(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        return getDecoratedMeasuredHeight(view) + params.topMargin
                + params.bottomMargin;
    }

    public int getVerticalSpace() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    public int getHorizontalSpace() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }
}
