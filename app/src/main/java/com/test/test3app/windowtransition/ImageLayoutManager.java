package com.test.test3app.windowtransition;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ImageLayoutManager extends LinearLayoutManager {

    private volatile int initialOffset = -1;
    private volatile int initialPosition = -1;

    public ImageLayoutManager(Context context) {
        super(context);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.getItemCount() > 0) {
            if (initialPosition >= 0) {
                scrollToPositionWithOffset(initialPosition, initialOffset);
                initialPosition = -1;
                initialOffset = -1;
            }
        }
        super.onLayoutChildren(recycler, state);
    }

    public void setScrollToPosition(int scrollToPosition, int scrollToPositionOffset) {
        this.initialPosition = scrollToPosition;
        this.initialOffset = scrollToPositionOffset;
    }
}
