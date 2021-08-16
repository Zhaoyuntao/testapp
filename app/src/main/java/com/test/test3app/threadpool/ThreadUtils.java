package com.test.test3app.threadpool;

import android.os.Looper;

import com.test.test3app.BuildConfig;
import com.zhaoyuntao.androidutils.tools.S;

public class ThreadUtils {

    public static void ensureUiThread() {
        if (!ThreadUtils.isUiThread()) {
            IllegalStateException tr = new IllegalStateException("ensureUiThread: thread check failed");
            if (BuildConfig.DEBUG) {
                throw tr;
            } else {
                S.e("ensureUiThread: thread check failed", tr);
            }
        }
    }

    public static void ensureNonUiThread() {
        if (ThreadUtils.isUiThread()) {
            IllegalStateException tr = new IllegalStateException("ensureNonUiThread: thread check failed");
            if (BuildConfig.DEBUG) {
                throw tr;
            } else {
                S.e("ensureNonUiThread: thread check failed", tr);
            }
        }
    }

    public static boolean isUiThread() {
        final Looper myLooper = Looper.myLooper();
        final Looper mainLooper = Looper.getMainLooper(); // never null

        return mainLooper.equals(myLooper);
    }

}
