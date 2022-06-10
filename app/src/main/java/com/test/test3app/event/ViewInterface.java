package com.test.test3app.event;

import android.os.SystemClock;

import im.turbo.utils.log.S;

/**
 * created by zhaoyuntao
 * on 06/06/2022
 * description:
 */
interface ViewInterface {

    default void s(String o) {
        _s(o, false);
    }

    default void e(String o) {
        _s(o, true);
    }

    default void _s(String o, boolean error) {
        long now = SystemClock.elapsedRealtime();
        long time = getTime();
        if (o.equals(getContent())) {
            if ((now - time) < 500) {
                return;
            }
        }
        setTime(now);
        setContent(o.toString());
        if (error) {
           S.ed(o,2,0);
        } else {
            S.sd(o,2,0);
        }
    }

    long getTime();

    String getContent();

    void setContent(String a);

    void setTime(long time);
}
