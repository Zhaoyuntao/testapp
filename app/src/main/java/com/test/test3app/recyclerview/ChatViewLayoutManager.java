package com.test.test3app.recyclerview;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.zhaoyuntao.androidutils.tools.S;
import com.zhaoyuntao.androidutils.tools.thread.SafeRunnable;
import com.zhaoyuntao.androidutils.tools.thread.TP;

public class ChatViewLayoutManager extends LinearLayoutManager {

    //    private int extraLayoutSpace = -1;
    private volatile int lastOffset;
    private volatile int lastPosition;
    private final Context context;

    public ChatViewLayoutManager(Context context) {
        super(context);
        this.context = context;
        setOrientation(VERTICAL);
    }

    public void fastSmoothScrollToPosition(int position, boolean toBottom, AnimationListener animationListener) {

        fastSmoothScrollToPositionWithOffset(position, 0, toBottom, animationListener);
    }

    public void fastSmoothScrollToPositionWithOffset(int position, int offset, boolean toBottom, AnimationListener animationListener) {
        lastPosition = position;
        lastOffset = 0;
        if (toBottom) {
            scrollToPosition(position);
        } else if (position > 0) {
            scrollToPositionWithOffset(position, getHeight());
        }
        TP.removeFromUi(new SafeRunnable(context) {
            @Override
            protected void runSafely() {
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
        });
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
        TP.runOnUiDelayed(new SafeRunnable(context) {
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
        }, 100);
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
    void scrollToLastPosition() {
        scrollToPositionWithOffset(lastPosition, lastOffset);
    }

    /**
     * init position and offset of the first child.
     */
    void initPositionAndOffset() {
        if (getChildCount() == 0) {
            lastOffset = 0;
            lastPosition = 0;
        }
        //get the first child view
        View topView = getChildAt(0);
        if (topView != null) {
            //get offset of child
            lastOffset = getHeight() - topView.getBottom();
            //get current position of child
            lastPosition = getPosition(topView);
//            S.s("[" + topView.getTag() + "]initPositionAndOffset: lastPosition:" + lastPosition + "   lastOffset:" + lastOffset);
        }
    }

    void resetPosition() {
        lastOffset = 0;
        lastPosition = 0;
    }

    public boolean isPositionOnBottom() {
        return lastPosition == 0 && lastOffset == 0;
    }

    public interface AnimationListener {
        default void onStart() {
        }

        default void onStop(int positionTo) {
        }
    }

}
