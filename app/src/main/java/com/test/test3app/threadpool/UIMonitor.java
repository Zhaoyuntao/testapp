package com.test.test3app.threadpool;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

/**
 * Time: 2020-03-18
 * Author Hay
 */
public class UIMonitor {
    private static UIMonitor sInstance = new UIMonitor();
    private HandlerThread mUIThread = new HandlerThread("ui-monitor");
    private Handler mIoHandler;
    private static final long TIME_BLOCK = 1000;

    private UIMonitor() {
        mUIThread.start();
        mIoHandler = new Handler(mUIThread.getLooper());
    }

    private static Runnable mLogRunnable = new Runnable() {
        @Override
        public void run() {
            StringBuilder sb = new StringBuilder();
            StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();
            for (StackTraceElement s : stackTrace) {
                sb.append(s.toString() + "\n");
            }
            Log.e("UIMonitor:", sb.toString());
        }
    };

    public static UIMonitor getInstance() {
        return sInstance;
    }

    public void startMonitor() {
        mIoHandler.postDelayed(mLogRunnable, TIME_BLOCK);
    }

    public void removeMonitor() {
        mIoHandler.removeCallbacks(mLogRunnable);
    }

}
