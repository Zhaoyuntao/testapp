package com.test.test3app.fastrecordviewnew;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;

import com.test.test3app.threadpool.ThreadPool;
import com.zhaoyuntao.androidutils.tools.thread.SafeRunnable;

/**
 * created by zhaoyuntao
 * on 2019-12-15
 * description:
 */
public class ZImageButton extends AppCompatImageButton {
    private OnClickListener onClickListener;
    private OnLongClickListener onLongClickListener;
    private boolean touchEventLongPressedCustom;
    private TouchEvent touchEvent;

    public ZImageButton(Context context) {
        super(context);
    }

    private class LongClickEventRunnable extends SafeRunnable {

        private float x;
        private float y;

        public LongClickEventRunnable(Activity lifeCircle) {
            super(lifeCircle);
        }

        public LongClickEventRunnable(Fragment lifeCircle) {
            super(lifeCircle);
        }

        public LongClickEventRunnable(android.app.Fragment lifeCircle) {
            super(lifeCircle);
        }

        public LongClickEventRunnable(Context lifeCircle) {
            super(lifeCircle);
        }

        public LongClickEventRunnable(View lifeCircle) {
            super(lifeCircle);
        }

        public void setEvent(float x, float y) {
            this.x = x;
            this.y = y;
        }

        @Override
        protected void runSafely() {
            //if the event is consumed by other way
            boolean consume = onLongClick();
            //if not,our custom long click down will execute
            if (!consume) {
                touchEventLongPressedCustom = true;
                if (touchEvent != null) {
                    touchEvent.whenLongClickDown(x, y);
                }
            }
        }
    }

    private LongClickEventRunnable longClickEventRunnable = new LongClickEventRunnable(this);

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                longClickEventRunnable.setEvent(event.getX(), event.getY());
                if (touchEvent != null) {
                    touchEvent.whenActionDown();
                }
                ThreadPool.runUiDelayed(100, longClickEventRunnable);
                break;

            case MotionEvent.ACTION_UP:
                if (touchEvent != null) {
                    touchEvent.whenActionUp();
                }
                ThreadPool.removeUi(longClickEventRunnable);
                if (touchEventLongPressedCustom) {
                    if (touchEvent != null) {
                        touchEvent.whenLongClickUp(event);
                    }
                } else {
                    onClick();
                }
                touchEventLongPressedCustom = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (touchEventLongPressedCustom) {
                    if (touchEvent != null) {
                        touchEvent.whenPressAndMove(event);
                    }
                }
                break;
        }
        return true;
    }

    private void onClick() {
        if (onClickListener != null) {
            onClickListener.onClick(this);
        }
    }

    private boolean onLongClick() {
        return onLongClickListener != null && onLongClickListener.onLongClick(this);
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    /**
     * connect its touch event to param view
     *
     * @param touchEvent class implement TouchEvent
     */
    public void setTouchConnection(TouchEvent touchEvent) {
        this.touchEvent = touchEvent;
    }
}
