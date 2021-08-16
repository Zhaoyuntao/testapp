package com.test.test3app.fastrecordview;

import android.content.Context;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatImageButton;

import com.test.test3app.fastrecordviewnew.ThreadPool;
import com.test.test3app.fastrecordviewnew.TouchEvent;
import com.test.test3app.fastrecordviewnew.ZRunnable;


/**
 * created by zhaoyuntao
 * on 2019-12-15
 * description:
 */
public class RecordImageButton extends AppCompatImageButton {
    private OnClickListener onClickListener;
    private OnLongClickListener onLongClickListener;
    private boolean touchEventLongPressedCustom;
    private TouchEvent touchEvent;

    public RecordImageButton(Context context) {
        super(context);
    }

    private class LongClickEventRunnable extends ZRunnable {

        private float x;
        private float y;

        public LongClickEventRunnable(Object t) {
            super(t);
        }

        public void setEvent(float x, float y) {
            this.x = x;
            this.y = y;
        }

        @Override
        protected void todo() {
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
                ThreadPool.runOnUiDelayedSafely(longClickEventRunnable, 100);
                break;

            case MotionEvent.ACTION_UP:
                if (touchEvent != null) {
                    touchEvent.whenActionUp();
                }
                ThreadPool.removeFromUi(longClickEventRunnable);
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
    public void setOnClickListener(OnClickListener onClickListener) {
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
