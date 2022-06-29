package im.turbo.baseui.chat;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import im.turbo.thread.SafeRunnable;
import im.turbo.thread.ThreadPool;

/**
 * created by zhaoyuntao
 * on 19/12/2021
 * description:
 */
public class ChatLayoutManager extends LinearLayoutManager {

    private volatile int initialOffset = -1;
    private volatile int initialPosition = -1;

    private final Context context;
    private volatile boolean isLastItemVisible;
    private boolean isCurrentScrolling;
    private int extentLast;

    public ChatLayoutManager(Context context) {
        super(context);
        setOrientation(RecyclerView.VERTICAL);
        setReverseLayout(false);
        setStackFromEnd(false);
        this.context = context;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.getItemCount() > 0) {
            if (initialPosition >= 0) {
                scrollToPositionWithOffset(initialPosition, initialOffset);
                initialPosition = -1;
                initialOffset = -1;
            } else {
                int extent = computeVerticalScrollExtent(state);
                int range = computeVerticalScrollRange(state);
                boolean extentChanged = extent > 0 && extentLast > 0 && extent != extentLast;
                extentLast = extent;
                int lastItemPosition = Math.max(state.getItemCount() - 1, 0);
                if (range > extent && isLastItemVisible && !isCurrentScrolling && extentChanged) {
                    scrollToPositionWithOffset(lastItemPosition, -123456789);
                }
            }
        }
        super.onLayoutChildren(recycler, state);
    }

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        initAlignBottom(state);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        isCurrentScrolling = true;
        initAlignBottom(state);
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    @Override
    public void onScrollStateChanged(int state) {
        isCurrentScrolling = state != RecyclerView.SCROLL_STATE_IDLE;
        super.onScrollStateChanged(state);
    }

    private void initAlignBottom(RecyclerView.State state) {
        if (state.getItemCount() > 0) {
            int lastItemPosition = Math.max(state.getItemCount() - 1, 0);
            isLastItemVisible = findLastVisibleItemPosition() == lastItemPosition;
        }
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
                scroll(createScroller(toBottom, position, offset, animationListener), position, 0, animationListener);
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
