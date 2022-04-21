package com.test.test3app.recyclerview;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.test3app.threadpool.ThreadPool;
import com.zhaoyuntao.androidutils.tools.S;
import com.zhaoyuntao.androidutils.tools.thread.SafeRunnable;

public class ChatViewLayoutManager extends LinearLayoutManager {

    private volatile int lastOffset;
    private volatile int lastPosition;
    private final Context context;
    private final ChatView chatView;
    private int extentLast;
    private int rangeLast;

    private int initialPosition = -1;
    private int initialOffset = -1;

    private boolean isFingerFling;

    public ChatViewLayoutManager(Context context, ChatView chatView, boolean reverseLayout) {
        super(context, RecyclerView.VERTICAL, reverseLayout);
        this.context = context;
        this.chatView = chatView;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        S.s("===============================================     onLayoutChildren: count:" + state.getItemCount() + " extent:" + computeVerticalScrollExtent(state) + "  extentLast:" + extentLast + " range:" + computeVerticalScrollRange(state) + " rangeLast:" + rangeLast);
        int extent = computeVerticalScrollExtent(state);
        int range = computeVerticalScrollRange(state);
        boolean contentFullThisTime = range > extent;
        boolean contentFullLastTime = rangeLast > extentLast;
        boolean fullToFull = contentFullLastTime && contentFullThisTime;
        if (state.getItemCount() > 0) {
            if (initialPosition >= 0) {
                S.e("initialPosition >= 0 scroll to :" + initialPosition + " " + initialOffset);
                scrollToPositionWithOffset(initialPosition, initialOffset);
                lastPosition = initialPosition;
                lastOffset = initialOffset;
                initialPosition = -1;
                initialOffset = -1;
                extentLast = 0;
            } else {
                S.s("initialPosition <0");

                if (extentLast < 0) {
                    S.e("extentLast<0, scroll to : 0 , 0");
                    resetPosition();
                    scrollToLastPosition();
                } else {
                    if (extentLast > 0 && extent > 0) {
                        S.s("extent > 0 :" + extent);

                        S.s("contentFullThisTime:" + contentFullThisTime + "  contentFullLastTime:" + contentFullLastTime);
                        if (!fullToFull) {
                            S.s("! full to full, reset position");
                            resetPosition();
                        }
                        if (extent != extentLast) {
                            S.e("extent changed, scroll to : " + lastPosition + " , " + lastOffset);
                            scrollToLastPosition();
                        }
                    }
                }
                extentLast = extent;
                rangeLast = range;
            }
        }
        super.onLayoutChildren(recycler, state);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (chatView.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING) {
            isFingerFling = true;
            initPositionAndOffset();
        } else if (isFingerFling && chatView.getScrollState() == RecyclerView.SCROLL_STATE_SETTLING) {
            initPositionAndOffset();
        } else {
            isFingerFling = false;
        }
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    public void fastSmoothScrollToPosition(int position, boolean toBottom, AnimationListener animationListener) {
        fastSmoothScrollToPositionWithOffset(position, 0, toBottom, animationListener);
    }

    public void fastSmoothScrollToPositionWithOffset(int position, int offset, boolean toBottom, AnimationListener animationListener) {
        lastPosition = position;
        lastOffset = 0;
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

    public void scrollDynamicToBottom(AnimationListener animationListener) {
        lastPosition = 0;
        lastOffset = 0;
        fastSmoothScrollToPosition(0, true, animationListener);
    }

    @Override
    public void scrollToPosition(int position) {
        lastPosition = position;
        lastOffset = 0;
        scrollToPositionWithOffset(lastPosition, lastOffset);
    }

    /**
     * keep position the list scroll to last time.
     */
    private void scrollToLastPosition() {
        scrollToPositionWithOffset(lastPosition, lastOffset);
    }

    @Override
    public void scrollToPositionWithOffset(int position, int offset) {
//        S.sd("scrollToPositionWithOffset:"+position+" "+offset);
        super.scrollToPositionWithOffset(position, offset);
    }

    /**
     * init position and offset of the first child.
     */
    private void initPositionAndOffset() {
        if (getChildCount() == 0) {
            resetPosition();
            return;
        }
        //get the first child view
        View topView = getChildAt(0);
        if (topView != null) {
            //get offset of child
            lastOffset = getHeight() - topView.getBottom();
            //get current position of child
            lastPosition = getPosition(topView);
        }
    }

    void resetPosition() {
        lastOffset = 0;
        lastPosition = 0;
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
