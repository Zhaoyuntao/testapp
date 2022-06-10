package im.turbo.baseui.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import im.turbo.basetools.observer.ListenerManager;
import im.turbo.baseui.utils.UiUtils;

/**
 * created by zhaoyuntao
 * on 19/12/2021
 * description: 0 -> 100, bottom -> top.
 */
public class ChatRecyclerView extends RecyclerView {

    private ChatLayoutManager layoutManager;

    private int TOUCH_MOVE_GAP;
    private long clickStartTime;
    private float startX;
    private float startY;

    private ListenerManager<ScrollListener> scrollListeners;
    private GestureDetector gestureDetector;
    private SwipeBackListener swipeBackListener;
    private boolean isFingerPress;

    public ChatRecyclerView(Context context) {
        super(context);
        init();
    }

    public ChatRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChatRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        layoutManager = new ChatLayoutManager(getContext());
        setLayoutManager(layoutManager);
        scrollListeners = new ListenerManager<>(false);
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
                onChildAttached(view);
            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                onChildDetached(view);
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

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int positionBottomItem = findBottomVisibleItemPosition();
                int positionTopItem = findTopVisibleItemPosition();
                switch (recyclerView.getScrollState()) {
                    case SCROLL_STATE_SETTLING:
                        onScrolling(positionBottomItem, positionTopItem, dy < 0, dy > 0, false);
                        break;
                    case SCROLL_STATE_DRAGGING:
                        onScrolling(positionBottomItem, positionTopItem, dy < 0, dy > 0, true);
                        break;
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (recyclerView.getScrollState() == SCROLL_STATE_IDLE) {
                    onScrollStop(findTopVisibleItemPosition(), findBottomVisibleItemPosition());
                }
            }
        });
    }

    private int getLastItemPosition() {
        return Math.max(0, getItemCount() - 1);
    }

    private void onFingerPress() {
        scrollListeners.notifyListeners(ScrollListener::onFingerPress);
    }

    private void onFingerClick() {
        scrollListeners.notifyListeners(ScrollListener::onFingerPress);
    }

    private void onFingerUp() {
        scrollListeners.notifyListeners(scrollListener -> {
            scrollListener.onFingerUp(getScrollState() != SCROLL_STATE_IDLE);
        });
    }

    private void onScrolling(int positionBottomItem, int positionTopItem, boolean down, boolean up, boolean isFingerPress) {
        scrollListeners.notifyListeners(scrollListener -> {
            scrollListener.onScrolling(positionBottomItem, positionTopItem, down, up, isFingerPress);
        });
    }

    private void onChildAttached(View child) {
        scrollListeners.notifyListeners(scrollListener -> {
            scrollListener.onChildAttached(child);
        });
    }

    private void onChildDetached(View child) {
        scrollListeners.notifyListeners(scrollListener -> {
            scrollListener.onChildDetached(child);
        });
    }

    private void onScrollStop(int positionBottomItem, int positionTopItem) {
        scrollListeners.notifyListeners(scrollListener -> {
            scrollListener.onScrollStop(positionBottomItem, positionTopItem, isFingerPress);
        });
    }

    public void scrollDynamicToBottom() {
        scrollDynamicToBottom(null);
    }

    public void scrollDynamicToBottom(ChatLayoutManager.AnimationListener animationListener) {
//        S.sd("scrollDynamicToBottom");
        int childCount = getItemCount();
        if (childCount <= 0) {
            if (animationListener != null) {
                animationListener.onStart();
                animationListener.onStop(0);
            }
            return;
        }
        if (animationListener != null) {
            animationListener.onStart();
        }
        stopScroll();
        int position = getLastItemPosition();
        layoutManager.scrollToPositionWithOffset(position, -10000);
        postDelayed(() -> {
            if (animationListener != null) {
                animationListener.onStop(position);
            }
        }, 300);
    }

    public void fastSmoothScrollToBottom(ChatLayoutManager.AnimationListener animationListener) {
        int position = getLastItemPosition();
        fastSmoothScrollToPosition(position, true, animationListener);
    }

    public void fastSmoothScrollToPosition(int position, boolean toBottom, ChatLayoutManager.AnimationListener animationListener) {
        fastSmoothScrollToPositionWithOffset(position, 0, toBottom, animationListener);
    }

    public void fastSmoothScrollToPositionWithOffset(int position, int offset, boolean toBottom, ChatLayoutManager.AnimationListener animationListener) {
//        S.sd("fastSmoothScrollToPositionWithOffset");
        stopScroll();
        layoutManager.fastSmoothScrollToPositionWithOffset(position, offset, toBottom, new ChatLayoutManager.AnimationListener() {
            @Override
            public void onStart() {
                if (animationListener != null) {
                    animationListener.onStart();
                }
            }

            @Override
            public void onStop(int positionTo) {
                if (animationListener != null) {
                    animationListener.onStop(positionTo);
                }
            }
        });
    }

    public int findTopVisibleItemPosition() {
        return layoutManager.findFirstVisibleItemPosition();
    }

    public int findBottomVisibleItemPosition() {
        return layoutManager.findLastVisibleItemPosition();
    }

    public int findTopCompletelyVisibleItemPosition() {
        return layoutManager.findFirstCompletelyVisibleItemPosition();
    }

    public int findBottomCompletelyVisibleItemPosition() {
        return layoutManager.findLastCompletelyVisibleItemPosition();
    }

    public int getItemCount() {
        return layoutManager.getItemCount();
    }

    public View findViewByPosition(int position) {
        return layoutManager.findViewByPosition(position);
    }

    public void addScrollListener(ScrollListener scrollListener) {
        this.scrollListeners.addListener(scrollListener);
    }

    public interface ScrollListener {
        default void onFingerPress() {
        }

        default void onFingerUp(boolean isScrolling) {
        }

        default void onScrolling(int positionBottomItem, int positionTopItem, boolean down, boolean up, boolean isFingerPress) {
        }

        default void onChildAttached(View child) {
        }

        default void onChildDetached(View child) {
        }

        default void onScrollStop(int positionBottomItem, int positionTopItem, boolean isFingerPress) {

        }
    }

    public void setSwipeBackListener(SwipeBackListener swipeBackListener) {
        this.swipeBackListener = swipeBackListener;
    }

    public void setScrollToPosition(int scrollToPosition, int scrollToPositionOffset) {
        if (scrollToPosition < 0) {
            scrollToPosition = getLastItemPosition();
        }
        this.layoutManager.setScrollToPosition(scrollToPosition, scrollToPositionOffset);
    }

    public interface SwipeBackListener {
        void onSwipeBack();
    }

    @Override
    protected void onDetachedFromWindow() {
        scrollListeners.clear();
        super.onDetachedFromWindow();
    }
}
