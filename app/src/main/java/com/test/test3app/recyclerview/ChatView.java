package com.test.test3app.recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.test.test3app.fastrecordviewnew.UiUtils;
import com.zhaoyuntao.androidutils.tools.S;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * refactor by zhaoyuntao
 */
public class ChatView extends RecyclerView {

    private ChatViewLayoutManager layoutManager;

    private int TOUCH_MOVE_GAP;
    private long clickStartTime;
    private float startX;
    private float startY;

    private List<ScrollListener> scrollListeners;
    private GestureDetector gestureDetector;
    private SwipeBackListener swipeBackListener;
    private int distanceScroll;
    private boolean isFingerPress;
    private int scrollRangeLast;
    private int scrollExtentLast;
    private boolean stackFromEnd;

    public ChatView(Context context) {
        super(context);
        init();
    }

    public ChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChatView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        layoutManager = new ChatViewLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        setLayoutManager(layoutManager);
        scrollListeners = new ArrayList<>(2);
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1 == null || e2 == null) {
                    return false;
                }
                float distanceX = e2.getX() - e1.getX();
                float distanceY = Math.abs(e2.getY() - e1.getY());
                distanceX = UiUtils.pxToDip(distanceX);
                distanceY = UiUtils.pxToDip(distanceY);

                if (distanceX > 90 && Math.abs(distanceY) < 30) {
                    if (swipeBackListener != null) {
                        swipeBackListener.onSwipeBack();
                    }
                }
                return false;
            }
        });
        setHasFixedSize(true); // improves performance for true
        setItemAnimator(null);
        setItemViewCacheSize(10);
        addOnLayoutChangeListener(new OnLayoutChangeListener() {
            private int height;

            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int heightNow = bottom - top;
                boolean changed = height != heightNow;
//                S.s("heightNow:" + heightNow + " lastHeight:" + height);
                height = heightNow;
                flushConversationList(changed);
            }
        });
        TOUCH_MOVE_GAP = UiUtils.dipToPx(8);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new AdapterDataObserver() {
            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                flushConversationList(true);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        final int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            clickStartTime = SystemClock.elapsedRealtime();
            startX = event.getX();
            startY = event.getY();
            isFingerPress = true;
            onFingerPress();
        } else if (action == MotionEvent.ACTION_MOVE) {
        } else if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            long timeDiff = SystemClock.elapsedRealtime() - clickStartTime;
            if (timeDiff < 500) {
                float xDiff = Math.abs(event.getX() - startX);
                float yDiff = Math.abs(event.getY() - startY);
                if (xDiff < TOUCH_MOVE_GAP && yDiff < TOUCH_MOVE_GAP) {
                    onFingerClick();
                }
            }
            isFingerPress = false;
            onFingerUp();
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void setLayoutManager(final LayoutManager layoutManager) {
        super.setLayoutManager(layoutManager);
        addOnScrollListener(new OnScrollListener() {
            private boolean isScrollByUser = true;

            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                if (isScrollByUser) {
                    ChatView.this.layoutManager.initPositionAndOffset();
                } else {
                    S.e("is not ScrollByUser");
                }
                distanceScroll -= dy;
                int positionBottomItem = findFirstVisibleItemPosition();
                if (positionBottomItem == 0 || distanceScroll < 0) {
                    distanceScroll = 0;
                }
                int positionTopItem = findLastVisibleItemPosition();
                switch (recyclerView.getScrollState()) {
                    case SCROLL_STATE_SETTLING:
                        onScrollingWithoutHand();
                        onScrolling(positionBottomItem, positionTopItem);
                        break;
                    case SCROLL_STATE_DRAGGING:
                        onScrollingWithHand();
                        onScrolling(positionBottomItem, positionTopItem);
                        break;
                }
                findFirstVisibleItemPosition();
            }

            @Override
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
                S.s("onScrollStateChanged:" + newState);
                isScrollByUser = (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING);
                ChatView.this.layoutManager.initPositionAndOffset();
                int positionBottomItem = findFirstVisibleItemPosition();
                if (positionBottomItem == 0 || distanceScroll < 0) {
                    distanceScroll = 0;
                }
                int positionTopItem = findLastVisibleItemPosition();
                if (recyclerView.getScrollState() == SCROLL_STATE_IDLE) {
                    onScrollStop(positionBottomItem, positionTopItem);
                    int firstPosition = findFirstVisibleItemPosition();
                    if (firstPosition > 15) {
                        distanceScroll = getHeight() * firstPosition;
                    }
                }
                if (positionBottomItem == 0 || distanceScroll < 0) {
                    distanceScroll = 0;
                }
            }
        });
    }

    public boolean isOverOneScreenHeight() {
        if (findFirstVisibleItemPosition() == 0) {
            distanceScroll = 0;
        }
        return distanceScroll > getHeight();
    }

    public void setOverOneScreenHeight() {
        distanceScroll = getHeight() * findFirstVisibleItemPosition();
    }

    private void onFingerPress() {
        for (int i = 0; i < scrollListeners.size(); i++) {
            ScrollListener scrollListener = scrollListeners.get(i);
            if (scrollListener != null) {
                scrollListener.onFingerPress();
            }
        }
    }

    private void onFingerClick() {
        for (int i = 0; i < scrollListeners.size(); i++) {
            ScrollListener scrollListener = scrollListeners.get(i);
            if (scrollListener != null) {
                scrollListener.onFingerPress();
            }
        }
    }

    private void onFingerUp() {
        for (int i = 0; i < scrollListeners.size(); i++) {
            ScrollListener scrollListener = scrollListeners.get(i);
            if (scrollListener != null) {
                scrollListener.onFingerUp(getScrollState() != SCROLL_STATE_IDLE);
            }
        }
    }

    private void onScrollingWithoutHand() {
        for (int i = 0; i < scrollListeners.size(); i++) {
            ScrollListener scrollListener = scrollListeners.get(i);
            if (scrollListener != null) {
                scrollListener.onScrollingWithoutHand();
            }
        }
    }

    private void onScrollingWithHand() {
        for (int i = 0; i < scrollListeners.size(); i++) {
            ScrollListener scrollListener = scrollListeners.get(i);
            if (scrollListener != null) {
                scrollListener.onScrollingWithHand();
            }
        }
    }

    private void onScrolling(int positionBottomItem, int positionTopItem) {
        for (int i = 0; i < scrollListeners.size(); i++) {
            ScrollListener scrollListener = scrollListeners.get(i);
            if (scrollListener != null) {
                scrollListener.onScrolling(positionBottomItem, positionTopItem);
            }
        }
    }

    private void onScrollStop(int positionBottomItem, int positionTopItem) {
        for (int i = 0; i < scrollListeners.size(); i++) {
            ScrollListener scrollListener = scrollListeners.get(i);
            if (scrollListener != null) {
                scrollListener.onScrollStop(positionBottomItem, positionTopItem, isFingerPress);
            }
        }
    }

    public void scrollDynamicToBottom() {
        layoutManager.scrollDynamicToBottom(new ChatViewLayoutManager.AnimationListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onStop(int positionTo) {
                distanceScroll = 0;
                scrollToPosition(0);
            }
        });
    }

    public void fastSmoothScrollToPosition(int position, boolean toBottom) {
        fastSmoothScrollToPosition(position, toBottom, null);
    }

    public void fastSmoothScrollToBottom(ChatViewLayoutManager.AnimationListener animationListener) {
        layoutManager.fastSmoothScrollToPosition(0, true, new ChatViewLayoutManager.AnimationListener() {
            @Override
            public void onStart() {
                animationListener.onStart();
            }

            @Override
            public void onStop(int positionTo) {
                distanceScroll = 0;
                scrollToPosition(0);
                animationListener.onStop(positionTo);
            }
        });
    }

    public void fastSmoothScrollToPosition(int position, boolean toBottom, ChatViewLayoutManager.AnimationListener animationListener) {
        if (position == 0) {
            distanceScroll = 0;
        }
        layoutManager.fastSmoothScrollToPosition(position, toBottom, animationListener);
    }

    public void fastSmoothScrollToPositionWithOffset(int position, int offset, boolean toBottom, ChatViewLayoutManager.AnimationListener animationListener) {
        if (position == 0) {
            distanceScroll = 0;
        }
        layoutManager.fastSmoothScrollToPositionWithOffset(position, offset, toBottom, animationListener);
    }

//    public int findFirstVisibleItemPosition() {
//        return mTheListLayoutManager.findFirstVisibleItemPosition();
//    }

//    public int findLastVisibleItemPosition() {
//        return mTheListLayoutManager.findLastVisibleItemPosition();
//    }

    /**
     * No better way to solve this.
     *
     * @return first visible child position.
     */
    public int findFirstVisibleItemPosition() {
//        if (getAdapter() == null) {
//            return -1;
//        }
//        int pos = 0;
//        int size = getAdapter().getItemCount();
//        while (pos < size) {
//            if (layoutManager.findViewByPosition(pos) != null) {
//                break;
//            }
//            pos++;
//        }
        return layoutManager.findFirstCompletelyVisibleItemPosition();
    }

    public int findLastVisibleItemPosition() {
//        if (getAdapter() == null) {
//            return -1;
//        }
//        int size = getAdapter().getItemCount();
//        int pos = size - 1;
//        while (pos >= 0) {
//            if (layoutManager.findViewByPosition(pos) != null) {
//                break;
//            }
//            pos--;
//        }
        return layoutManager.findLastCompletelyVisibleItemPosition();
    }

    public int findFirstCompletelyVisibleItemPosition() {
        return layoutManager.findFirstCompletelyVisibleItemPosition();
    }

    public int findLastCompletelyVisibleItemPosition() {
        return layoutManager.findLastCompletelyVisibleItemPosition();
    }
//
//    public boolean isPositionOnBottom() {
//        return mTheListLayoutManager.isPositionOnBottom();
//    }

    public int getItemCount() {
        return layoutManager.getItemCount();
    }

    public View findViewByPosition(int position) {
        return layoutManager.findViewByPosition(position);
    }

    public void addScrollListener(ScrollListener scrollListener) {
        this.scrollListeners.add(scrollListener);
    }

    /**
     * when layout changed,we should reset the stack way and the layout direction by message count.
     * if not out of range,just reverse the layout and stack from end.(for ex:here is just one message,and it should be displayed on the top of the view)
     * else if out of range,we should just reverse the layout, but not stack from end.
     */
    public void flushConversationList(boolean force) {
//        S.sd("flushConversationList:" + force);
//        int scrollExtentLast = computeVerticalScrollExtent();
//        int scrollRangeLast = computeVerticalScrollRange();
//        if (this.scrollRangeLast == scrollRangeLast && this.scrollExtentLast == scrollExtentLast && !force) {
//            return;
//        }
//        this.scrollRangeLast = scrollRangeLast;
//        this.scrollExtentLast = scrollExtentLast;
//        boolean changed = false;
//        if (scrollExtentLast >= scrollRangeLast) {
//            stackFromEnd = true;
//            if (!layoutManager.getStackFromEnd()) {
//                changed = true;
//                layoutManager.setStackFromEnd(true);
//            }
//        } else {
//            if (stackFromEnd) {
//                stackFromEnd = false;
//                layoutManager.resetPosition();
//            }
//            if (layoutManager.getStackFromEnd()) {
//                changed = true;
//                layoutManager.setStackFromEnd(false);
//            }
//        }
//        if (force) {
//            post(new Runnable() {
//                @Override
//                public void run() {
//                    layoutManager.scrollToLastPosition();
//                }
//            });
//        }
    }

    public interface ScrollListener {
        default void onFingerPress() {
        }

        default void onFingerUp(boolean isScrolling) {
        }

        default void onScrollingWithoutHand() {
        }

        default void onScrollingWithHand() {
        }

        default void onScrolling(int positionBottomItem, int positionTopItem) {
        }

        default void onScrollStop(int positionBottomItem, int positionTopItem, boolean isFingerPress) {

        }
    }

    public void setSwipeBackListener(SwipeBackListener swipeBackListener) {
        this.swipeBackListener = swipeBackListener;
    }

    public interface SwipeBackListener {
        void onSwipeBack();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        scrollListeners.clear();
    }
}
