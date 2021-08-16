package com.test.test3app.fastrecordviewnew;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
    private static ScheduledThreadPoolExecutor scheduledPool = new ScheduledThreadPoolExecutor(1, createThreadFactory(
            "schedule", true));

    private static Handler sUiHandler = null;
    private static HandlerThread sHandlerThread = null;
    private static Handler sWorkerHandler = null;

    private static final Object mWorkObj = new Object();


    private static HandlerThread sFtsHandlerThread = null;
    private static Handler sFtsWorkerHandler = null;

    private static final Object mFtsWorkObj = new Object();



    public static void runOnUiWithPriority(Runnable r) {
        sUiHandler.postAtFrontOfQueue(r);
    }

    public static void runOnUi(Runnable r) {
        sUiHandler.post(r);
    }


    public static Handler getUiHandler() {
        return sUiHandler;
    }

    public static void runOnUiSafely(ZRunnable r) {
        sUiHandler.post(r);
    }

    public static void removeFromUi(Runnable r) {
        sUiHandler.removeCallbacks(r);
    }

    public static void runOnUiDelayedSafely(ZRunnable r, int delay) {
        if(sUiHandler==null){
            // ui thread runner
            sUiHandler = new Handler(Looper.getMainLooper());
        }
        sUiHandler.postDelayed(r, delay);
    }


    private static void initWorkHandler() {
        if (sWorkerHandler == null) {
            // handler based thread runner
            synchronized (mWorkObj) {
                if (sWorkerHandler == null) {
                    sHandlerThread = new HandlerThread("internal");
                    sHandlerThread.setPriority(Thread.NORM_PRIORITY - 1);
                    sHandlerThread.start();
                    sWorkerHandler = new Handler(sHandlerThread.getLooper());
                }
            }
        }
    }

    public static Handler getFtsWorkerHandler() {
        initFtsWorkHandler();
        return sFtsWorkerHandler;
    }

    private static void initFtsWorkHandler() {
        if (sFtsWorkerHandler == null) {
            // handler based thread runner
            synchronized (mFtsWorkObj) {
                if (sFtsWorkerHandler == null) {
                    sFtsHandlerThread = new HandlerThread("internal");
                    sFtsHandlerThread.setPriority(Thread.NORM_PRIORITY + 1);
                    sFtsHandlerThread.start();
                    sFtsWorkerHandler = new Handler(sFtsHandlerThread.getLooper());
                }
            }
        }
    }

    /**
     * Schedule a runnable running at fixed rate
     */
    public static ScheduledFuture<?> schedule(Runnable r, long initialDelay, long period, TimeUnit unit) {
        return scheduledPool.scheduleAtFixedRate(r, initialDelay, period, unit);
    }

    public static void runOnScheduleQueue(Runnable r) {
        scheduledPool.execute(r);
    }


    public static void shutdown() {
        if (sHandlerThread != null) {
            sHandlerThread.quit();
        }
    }

    public static void shutdownfts() {
        if (sFtsHandlerThread != null) {
            sFtsHandlerThread.quit();
        }
    }

    private static ThreadFactory createThreadFactory(final String purpose, final boolean highPriority) {
        return new ThreadFactory() {
            int count = 0;

            @Override
            public Thread newThread(Runnable r) {
                count++;
                Thread thr = new Thread(r, purpose + "-" + count);
                thr.setDaemon(false);

                if (highPriority) {
                    thr.setPriority(Thread.NORM_PRIORITY + 1);
                } else {
                    thr.setPriority((Thread.NORM_PRIORITY + Thread.MIN_PRIORITY) / 2);
                }

                return thr;
            }
        };
    }


}
