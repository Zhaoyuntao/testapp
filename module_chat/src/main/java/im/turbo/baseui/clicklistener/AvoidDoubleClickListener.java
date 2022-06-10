package im.turbo.baseui.clicklistener;

import android.os.SystemClock;
import android.view.View;

/**
 * created by zhaoyuntao
 * on 2020/6/12
 * description:Avoid clicking twice in one press.
 */
public abstract class AvoidDoubleClickListener implements View.OnClickListener {

    private long duration = 300;
    private long timeOfClick;

    public AvoidDoubleClickListener() {
    }

    public AvoidDoubleClickListener(long duration) {
        this.duration = duration;
    }

    @Override
    final public void onClick(View view) {
        long timeNow = SystemClock.elapsedRealtime();
        long timePassed = timeNow - timeOfClick;
        if (controlByHand()) {
            boolean timeShort = timePassed < duration;
            onClickView(view, timeShort);
            if (!timeShort) {
                timeOfClick = timeNow;
            }
        } else {
            if (timePassed >= duration) {
                timeOfClick = timeNow;
                onClickView(view);
            }
        }
    }

    protected boolean controlByHand() {
        return false;
    }

    public abstract void onClickView(View view);

    public void onClickView(View view, boolean timeShort) {
    }
}
