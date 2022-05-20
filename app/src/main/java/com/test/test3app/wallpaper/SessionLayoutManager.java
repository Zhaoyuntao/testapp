package com.test.test3app.wallpaper;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * created by zhaoyuntao
 * on 10/05/2022
 * description:
 */
public class SessionLayoutManager extends LinearLayoutManager {
    private int initialPosition = 0;
    private int initialOffset = 0;

    public SessionLayoutManager(Context context) {
        super(context);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.getItemCount() > 0) {
            if (initialPosition >= 0) {
                scrollToPositionWithOffset(initialPosition, initialOffset);
            }
        }
        super.onLayoutChildren(recycler, state);
    }

    @Override
    public void onScrollStateChanged(int state) {
        initPositionAndOffset();
        super.onScrollStateChanged(state);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        initPositionAndOffset();
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    private void initPositionAndOffset() {
        View topView = getChildAt(0);
        if (topView != null) {
            initialPosition = getPosition(topView);
            initialOffset = topView.getTop();
        }
    }
}
