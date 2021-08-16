package com.test.test3app.threadpool;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Printer;

import com.test.test3app.BuildConfig;
import com.zhaoyuntao.androidutils.tools.S;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
    private static ScheduledThreadPoolExecutor scheduledPool = new ScheduledThreadPoolExecutor(1, createThreadFactory(
            "schedule", true));

    private static final Map<Runnable, ScheduledFuture> SCHEDULED_FUTURE_MAP = new ConcurrentHashMap<>();

    private static Handler sUiHandler = null;
    private static HandlerThread sHandlerThread = null;
    private static Handler sWorkerHandler = null;
    private static final Object mWorkObj = new Object();

    private static HandlerThread sFtsHandlerThread = null;
    private static Handler sFtsWorkerHandler = null;
    private static final Object mFtsWorkObj = new Object();

    public static void runOnPool(Runnable r) {
        if (BuildConfig.DEBUG)
            ThreadManager.getIO().execute(new ShowExceptionRunnable(r));
        else
            ThreadManager.getIO().execute(r);
    }

    public static void runCache(Runnable r) {
        if (BuildConfig.DEBUG)
            ThreadManager.getCache().execute(new ShowExceptionRunnable(r));
        else
            ThreadManager.getCache().execute(r);
    }

    public static void runOnPoolDelayed(final Runnable runnable, final int delay) {
        if (BuildConfig.DEBUG) {
            S.s("Debug");
            SCHEDULED_FUTURE_MAP.put(runnable, ((ScheduledThreadPoolExecutor) ThreadManager.getScheduleIo().getExecutor()).schedule(new ShowExceptionRunnable(runnable) {
                @Override
                public void run() {
                    super.run();
                    SCHEDULED_FUTURE_MAP.remove(runnable);
                    S.s("map[d]:"+SCHEDULED_FUTURE_MAP.size());
                }

                @Override
                protected void onRuntimeException(Throwable e) {
                    SCHEDULED_FUTURE_MAP.remove(runnable);
                    S.s("map[dd]:"+SCHEDULED_FUTURE_MAP.size());
                }
            }, delay, TimeUnit.MILLISECONDS));
        } else {
            SCHEDULED_FUTURE_MAP.put(runnable, ((ScheduledThreadPoolExecutor) ThreadManager.getScheduleIo().getExecutor()).schedule(new Runnable() {
                @Override
                public void run() {
                    runnable.run();
                    SCHEDULED_FUTURE_MAP.remove(runnable);
                    S.s("map:"+SCHEDULED_FUTURE_MAP.size());
                }
            }, delay, TimeUnit.MILLISECONDS));
        }
    }

    public static void removeFromPool(Runnable runnable) {
        ((ThreadPoolExecutor) ThreadManager.getIO().getExecutor()).remove(runnable);
        ScheduledFuture scheduledFuture = SCHEDULED_FUTURE_MAP.remove(runnable);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
    }

    public static ThreadPoolExecutor getPoolExecutor() {
        return (ThreadPoolExecutor) ThreadManager.getIO().getExecutor();
    }

    public static void postOnPoolDelayed(final Runnable r, int delay) {
        postOnWorkerDelayed(new Runnable() {
            @Override
            public void run() {
                runOnPool(r);
            }
        }, delay);
    }

    public static void runOnUiWithPriority(Runnable r) {
        sUiHandler.postAtFrontOfQueue(r);
    }

    public static void runOnUi(Runnable r) {
        sUiHandler.post(r);
    }

    public static void runOnUiOpt(Runnable r) {
        AndroidDeliver.getInstance().execute(r);
    }

    public static void postOnUiDelayed(Runnable r, int delay) {
        if (BuildConfig.DEBUG)
            sUiHandler.postDelayed(r, delay);
        else
            sUiHandler.postDelayed(r, delay);
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

    public static void runOnUiDelayedSafely(ZRunnable r, long delay) {
        sUiHandler.postDelayed(r, delay);
    }

    public static void runCriticalTask(Runnable r) {
        if (BuildConfig.DEBUG) {
            ThreadManager.getCalculator().execute(new ShowExceptionRunnable(r));
        } else {
            ThreadManager.getCalculator().execute(r);
        }
    }

    public static void runOnWorker(Runnable r) {
        initWorkHandler();
        if (BuildConfig.DEBUG)
            sWorkerHandler.post(new ShowExceptionRunnable(r));
        else
            sWorkerHandler.post(r);
    }


    public static void postOnWorkerDelayed(Runnable r, long delay) {
        initWorkHandler();
        if (BuildConfig.DEBUG)
            sWorkerHandler.postDelayed(new ShowExceptionRunnable(r), delay);
        else
            sWorkerHandler.postDelayed(r, delay);
    }

    public static Looper getWorkerLooper() {
        initWorkHandler();
        return sHandlerThread.getLooper();
    }

    public static Handler getWorkerHandler() {
        initWorkHandler();
        return sWorkerHandler;
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
                    sFtsHandlerThread = new HandlerThread("fts");
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

    public static void startup(boolean isDebug) {
        if (isDebug) {
            ThreadUtils.ensureUiThread();
        }

        AsyncTask.setDefaultExecutor(ThreadManager.getIO());

        // ui thread runner
        sUiHandler = new Handler(Looper.getMainLooper());
        // debug ui thread
        if (BuildConfig.DEBUG) {
            Looper.getMainLooper().setMessageLogging(new Printer() {
                @Override
                public void println(String x) {
                    if (x.startsWith(">>>>> Dispatching")) {
                        UIMonitor.getInstance().startMonitor();
                    }
                    if (x.startsWith("<<<<< Finished")) {
                        UIMonitor.getInstance().removeMonitor();
                    }
                }
            });
        }
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

    public static void runImageLoader(Runnable r) {
        if (r == null) {
            return;
        }

        ThreadManager.getFile().execute(r);
    }

    public static void runThread(Runnable runnable) {
        runThread(runnable, "");
    }

    public static void runThread(Runnable runnable, String threadName) {

        if (TextUtils.isEmpty(threadName)) {
            threadName = "tN" + System.currentTimeMillis();
        }
        if (BuildConfig.DEBUG) {
            try {
                int stackTraceCount = 3;
                StackTraceElement[] elements = new Throwable().getStackTrace();
                String callerClassName = elements.length > stackTraceCount ? elements[stackTraceCount].getClassName() : "NA";
                String callerLineNumber = elements.length > stackTraceCount ? String.valueOf(elements[stackTraceCount].getLineNumber()) : "NA";

                int pos = callerClassName.lastIndexOf('.');
                if (pos >= 0) {
                    callerClassName = callerClassName.substring(pos + 1);
                }

                threadName += callerClassName + "_" + callerLineNumber;
            } catch (Exception e) {
                //do nothing
            }
        }

        new TotokThread(runnable, threadName).start();
    }
}
