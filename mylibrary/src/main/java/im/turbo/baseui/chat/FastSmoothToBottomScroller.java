package im.turbo.baseui.chat;

import android.content.Context;

/**
 * created by zhaoyuntao
 * on 2020/6/27
 * description:
 */
public class FastSmoothToBottomScroller extends BaseScroller {
    public FastSmoothToBottomScroller(Context context) {
        super(context);
    }

    @Override
    protected boolean snapToBottom() {
        return true;
    }

    @Override
    protected int calculateTimeForScrolling(int dx) {
        return 250;
    }
    @Override
    protected int calculateTimeForDeceleration(int dx) {
        return 100;
    }
}
