package im.turbo.baseui.chat;

import android.content.Context;

/**
 * created by zhaoyuntao
 * on 2020/6/27
 * description:
 */
public class SlowSmoothToTopScroller extends BaseScroller {
    public SlowSmoothToTopScroller(Context context) {
        super(context);
    }

    @Override
    protected boolean snapToBottom() {
        return false;
    }

    @Override
    protected int calculateTimeForScrolling(int dx) {
        return 500;
    }
    @Override
    protected int calculateTimeForDeceleration(int dx) {
        return 1000;
    }
}

