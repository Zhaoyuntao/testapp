package com.test.test3app.recyclerview;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.test3app.threadpool.ThreadPool;
import com.zhaoyuntao.androidutils.tools.S;
import com.zhaoyuntao.androidutils.tools.thread.SafeRunnable;

public class ChatLayoutManager extends LinearLayoutManager {

    private volatile int initialOffset = -1;
    private volatile int initialPosition = -1;

    private final Context context;
    private volatile boolean isLastItemVisible;

    public ChatLayoutManager(Context context) {
        super(context);
        setOrientation(RecyclerView.VERTICAL);
        setReverseLayout(false);
        setStackFromEnd(false);
        this.context = context;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        S.s("onLayoutChildren");
        if (state.getItemCount() > 0) {
            if (initialPosition >= 0) {
                scrollToPositionWithOffset(initialPosition, initialOffset);
                initialPosition = -1;
                initialOffset = -1;
            } else {
                int extent = computeVerticalScrollExtent(state);
                int range = computeVerticalScrollRange(state);
                int lastItemPosition = Math.max(state.getItemCount() - 1, 0);
                if (range > extent && isLastItemVisible) {
                    S.s("1");
                    scrollToPositionWithOffset(lastItemPosition, 0);
                }else{
                    S.e("no need scroll:"+(range>extent)+" isLastItemVisible:"+isLastItemVisible+"");
                }
            }
        }
        super.onLayoutChildren(recycler, state);
    }

    @Override
    public void attachView(@NonNull View child) {
        S.s("attach view");
        super.attachView(child);
    }

    @Override
    public void detachView(@NonNull View child) {
        S.s("detach view");
        super.detachView(child);
    }

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        S.s("onLayoutCompleted");
        initAlignBottom(state);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        initAlignBottom(state);
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    private void initAlignBottom(RecyclerView.State state) {
        if (state.getItemCount() > 0) {
            int lastItemPosition = Math.max(state.getItemCount() - 1, 0);
            boolean a = findLastVisibleItemPosition() == lastItemPosition;
            if (a != isLastItemVisible) {
                isLastItemVisible = a;
                if (isLastItemVisible) {
                    S.s("----------------------");
                } else {
                    S.e("----------------------");
                }
            }
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        S.s("onScrollStateChanged:"+state);
        super.onScrollStateChanged(state);
    }

    public void fastSmoothScrollToPosition(int position, boolean toBottom, AnimationListener animationListener) {
        fastSmoothScrollToPositionWithOffset(position, 0, toBottom, animationListener);
    }

    public void fastSmoothScrollToPositionWithOffset(int position, int offset, boolean toBottom, AnimationListener animationListener) {
        startSmoothScroll(createScroller(toBottom, position, offset, new AnimationListener() {
            @Override
            public void onStart() {
                animationListener.onStart();
            }

            @Override
            public void onStop(int positionTo) {
                scroll(createScroller(toBottom, position, offset, animationListener), position, 3, animationListener);
            }
        }));
    }

    private BaseScroller createScroller(boolean toBottom, int position, int offset, AnimationListener animationListener) {
        BaseScroller baseScroller = toBottom ? new FastSmoothToBottomScroller(context) : new FastSmoothToTopScroller(context);
        baseScroller.setOffset(offset);
        baseScroller.setTargetPosition(position);
        baseScroller.setAnimationListener(animationListener);
        return baseScroller;
    }

    private void scroll(BaseScroller baseScroller, int position, int count, AnimationListener animationListener) {
        if (count == 0) {
            animationListener.onStop(position);
            return;
        }
        ThreadPool.runUiDelayed(100, new SafeRunnable(context) {
            @Override
            public void runSafely() {
                baseScroller.setTargetPosition(position);
                baseScroller.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onStop(int positionTo) {
                        scroll(baseScroller, position, count - 1, animationListener);
                    }
                });
                startSmoothScroll(baseScroller);
            }
        });
    }

    public void setScrollToPosition(int scrollToPosition, int scrollToPositionOffset) {
        this.initialPosition = scrollToPosition;
        this.initialOffset = scrollToPositionOffset;
    }

    public interface AnimationListener {
        default void onStart() {
        }

        default void onStop(int positionTo) {
        }
    }

}
