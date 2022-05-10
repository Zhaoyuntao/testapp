package com.test.test3app.recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
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

    private ChatLayoutManager mTheListLayoutManager;

    private int TOUCH_MOVE_GAP;
    private long mClickStartTime;
    private float mStartX;
    private float mStartY;

    private List<ScrollListener> scrollListeners;
    private GestureDetector gestureDetector;
    private SwipeBackListener swipeBackListener;
    private int distanceScroll;
    private boolean isFingerPress;

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
        mTheListLayoutManager = new ChatLayoutManager(getContext());
        setLayoutManager(mTheListLayoutManager);
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
        TOUCH_MOVE_GAP = UiUtils.dipToPx(8);

        addOnChildAttachStateChangeListener(new OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {
            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
            }
        });
    }

    public void setPreScrollPosition(int scrollPosition, int scrollPositionOffset) {
        mTheListLayoutManager.setScrollToPosition(scrollPosition, scrollPositionOffset);
    }

    //
    public void setAdapter(@NonNull Adapter<?> adapter, @NonNull AdapterDataObserver adapterDataObserver) {
        super.setAdapter(adapter);
        adapter.registerAdapterDataObserver(adapterDataObserver);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        boolean result = super.onTouchEvent(event);
        final int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            mClickStartTime = SystemClock.elapsedRealtime();
            mStartX = event.getX();
            mStartY = event.getY();
            isFingerPress = true;
            onFingerPress();
        } else if (action == MotionEvent.ACTION_MOVE) {
        } else if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            long timeDiff = SystemClock.elapsedRealtime() - mClickStartTime;
            if (timeDiff < 500) {
                float xDiff = Math.abs(event.getX() - mStartX);
                float yDiff = Math.abs(event.getY() - mStartY);
                if (xDiff < TOUCH_MOVE_GAP && yDiff < TOUCH_MOVE_GAP) {
                    onFingerClick();
                }
            }
            isFingerPress = false;
            onFingerUp();
        }

        return result;
    }

    @Override
    public void setLayoutManager(final LayoutManager layoutManager) {
        super.setLayoutManager(layoutManager);
        addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
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
                int positionBottomItem = findFirstVisibleItemPosition();
                if (positionBottomItem == 0 || distanceScroll < 0) {
                    distanceScroll = 0;
                }
                int positionTopItem = findLastVisibleItemPosition();
                if (recyclerView.getScrollState() == SCROLL_STATE_IDLE) {
                    onScrollStop(positionBottomItem, positionTopItem);
                    int firstPosition;
                    if (distanceScroll < getHeight()) {
                        if ((firstPosition = findFirstVisibleItemPosition()) > 15) {
                            distanceScroll = getHeight() * firstPosition;
                        } else {
                            distanceScroll = getHeight();
                        }
                    }
                }
                if (positionBottomItem == 0 || distanceScroll < 0) {
                    distanceScroll = 0;
                }
            }
        });
    }

    public boolean isOverOneScreenHeight() {
        if (findFirstVisibleItemPosition() <= 0) {
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

    public void fastSmoothScrollToPosition(int position, boolean toBottom) {
        fastSmoothScrollToPosition(position, toBottom, null);
    }

    public void fastSmoothScrollToBottom(ChatLayoutManager.AnimationListener animationListener) {
        mTheListLayoutManager.fastSmoothScrollToPosition(0, true, new ChatLayoutManager.AnimationListener() {
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

    public void fastSmoothScrollToPosition(int position, boolean toBottom, ChatLayoutManager.AnimationListener animationListener) {
        if (position == 0) {
            distanceScroll = 0;
        }
        mTheListLayoutManager.fastSmoothScrollToPosition(position, toBottom, animationListener);
    }


    public void fastSmoothScrollToPositionWithOffset(int position, int offset, boolean toBottom, ChatLayoutManager.AnimationListener animationListener) {
        if (position == 0) {
            distanceScroll = 0;
        }
        mTheListLayoutManager.fastSmoothScrollToPositionWithOffset(position, offset, toBottom, animationListener);
    }

    /**
     * No better way to solve this.
     *
     * @return first visible child position.
     */
    public int findFirstVisibleItemPosition() {
        if (getAdapter() == null) {
            return -1;
        }
        int pos = 0;
        int size = getAdapter().getItemCount();
        while (pos < size) {
            if (mTheListLayoutManager.findViewByPosition(pos) != null) {
                break;
            }
            pos++;
        }
        return pos;
    }

    public int findLastVisibleItemPosition() {
        if (getAdapter() == null) {
            return -1;
        }
        int size = getAdapter().getItemCount();
        int pos = size - 1;
        while (pos >= 0) {
            if (mTheListLayoutManager.findViewByPosition(pos) != null) {
                break;
            }
            pos--;
        }
        return pos;
    }

    public int findFirstCompletelyVisibleItemPosition() {
        return mTheListLayoutManager.findFirstCompletelyVisibleItemPosition();
    }

    public int findLastCompletelyVisibleItemPosition() {
        return mTheListLayoutManager.findLastCompletelyVisibleItemPosition();
    }

    public int getItemCount() {
        return mTheListLayoutManager.getItemCount();
    }

    public View findViewByPosition(int position) {
        return mTheListLayoutManager.findViewByPosition(position);
    }

    public void addScrollListener(ScrollListener scrollListener) {
        this.scrollListeners.add(scrollListener);
    }

    public void scrollToPositionWithOffset(int position, int offset) {
        stopScroll();
        mTheListLayoutManager.setScrollToPosition(position, offset);
        mTheListLayoutManager.scrollToPositionWithOffset(position, offset);
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
}
