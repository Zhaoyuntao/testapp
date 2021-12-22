package com.test.test3app.recyclerview;

import android.content.Context;

/**
 * created by zhaoyuntao
 * on 2020/6/27
 * description:
 */
public class FastSmoothToTopScroller extends BaseScroller {

    public FastSmoothToTopScroller(Context context) {
        super(context);
    }

    @Override
    protected boolean snapToBottom() {
        return false;
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
