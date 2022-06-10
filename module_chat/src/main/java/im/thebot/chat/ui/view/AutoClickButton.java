package im.thebot.chat.ui.view;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * created by zhaoyuntao
 * on 2020/6/1
 * description:
 */
public class AutoClickButton extends AppCompatImageView {
    private OnClickAndLongClickListener onClickAndLongClickListener;
    private long timeDown;

    public AutoClickButton(@NonNull Context context) {
        super(context);
    }

    public AutoClickButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoClickButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        long timeNow = SystemClock.elapsedRealtime();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            timeDown = timeNow;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if ((timeNow - timeDown) >= ViewConfiguration.getLongPressTimeout()) {
                if (onClickAndLongClickListener != null) {
                    onClickAndLongClickListener.onAutoPerformClick();
                }
                timeDown = timeNow;
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if ((timeNow - timeDown) < ViewConfiguration.getLongPressTimeout()) {
                if (onClickAndLongClickListener != null) {
                    onClickAndLongClickListener.onClick();
                }
            }
        }
        return super.onTouchEvent(event);
    }

    public void setOnClickAndLongClickListener(OnClickAndLongClickListener onClickAndLongClickListener) {
        this.onClickAndLongClickListener = onClickAndLongClickListener;
    }

    public interface OnClickAndLongClickListener {
        void onAutoPerformClick();

        void onClick();
    }
}
