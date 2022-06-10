package im.turbo.thread;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.module_chat.BuildConfig;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import im.turbo.basetools.preconditions.Preconditions;

public class ThreadPool {
    private static final int UI_TASK_CHECK_INTERVAL = 10_000; // 10 seconds
    private static Handler sUiHandler = null;
    private static ITaskExecutor uiTaskExecutor;

    private static HandlerThread sHandlerThread = null;
    private static Handler sWorkerHandler = null;
    private static final Object mWorkObj = new Object();

    private static HandlerThread schedulerThread;
    private static TaskScheduler taskScheduler;


    public static void triggerTaskSchedulerCheck() {
        taskScheduler.trigger();
    }

    public static TaskScheduler getTaskScheduler() {
        return taskScheduler;
    }

    public static Looper getTaskSchedulerLooper() {
        return schedulerThread.getLooper();
    }


    public static void runIO(Runnable task) {
        ThreadManager.getInstance().getIo().execute(task);
    }

    /**
     * It's for other I/O operations (excluding DB operations and network operations).
     */
    public static void runIoDelayed(long delayMs, @NonNull Runnable task) {
        Preconditions.checkNotNull(task);
        taskScheduler.schedule(ThreadManager.getInstance().getIo(), delayMs, true, task);
    }

    public static void removeIo(@NonNull Runnable task) {
        Preconditions.checkNotNull(task);
        taskScheduler.cancel(task);
        ThreadManager.getInstance().getIo().remove(task);
    }

    /**
     * It's for other I/O operations (excluding DB operations and network operations).
     */
    public static ThreadPoolExecutor getIoPool() {
        return ThreadManager.getInstance().getIo();
    }


    public static void runDatabase(Runnable runnable) {
        ThreadManager.getInstance().getDb().execute(runnable);
    }

    /**
     * It's for data processing (esp. for DB operations).
     */
    public static void runDbDelayed(long delayMs, @NonNull Runnable task) {
        Preconditions.checkNotNull(task);
        taskScheduler.schedule(ThreadManager.getInstance().getDb(), delayMs, true, task);
    }


    /***
     * 定时执行db任务，这里需要注意所有定时任务如果传入的task是同一个对象则会进行覆盖（只改delay和period），不会开启新的定时任务
     * @param task
     * @param delayMs
     * @param periodMs
     */
    public static void scheduleDbTask(Runnable task, long delayMs, long periodMs) {
        Preconditions.checkNotNull(task);
        taskScheduler.schedulePeriod(ThreadManager.getInstance().getDb(), delayMs, periodMs, TaskScheduler.SchedulePolicy.REPLACE, false, task);
    }


    /***
     * 定时执行IO任务，这里需要注意所有定时任务如果传入的task是同一个对象则会进行覆盖（只改delay和period），不会开启新的定时任务
     * @param task
     * @param delayMs
     * @param periodMs
     */
    public static void scheduleIoTask(Runnable task, long delayMs, long periodMs) {
        Preconditions.checkNotNull(task);
        taskScheduler.schedulePeriod(ThreadManager.getInstance().getIo(), delayMs, periodMs, TaskScheduler.SchedulePolicy.REPLACE, false, task);
    }

    /***
     * 定时执行计算任务，这里需要注意所有定时任务如果传入的task是同一个对象则会进行覆盖（只改delay和period），不会开启新的定时任务
     * @param task
     * @param delayMs
     * @param periodMs
     */
    public static void scheduleCalculateTask(Runnable task, long delayMs, long periodMs) {
        Preconditions.checkNotNull(task);
        taskScheduler.schedulePeriod(ThreadManager.getInstance().getIo(), delayMs, periodMs, TaskScheduler.SchedulePolicy.REPLACE, false, task);
    }


    /**
     * It's for data processing (esp. for DB operations).
     * It'll be be ignored if the task was already scheduled.
     */
    public static void runDbDelayedIfAbsent(long delayMs, @NonNull Runnable task) {
        Preconditions.checkNotNull(task);
        taskScheduler.schedule(ThreadManager.getInstance().getDb(), delayMs,
                TaskScheduler.SchedulePolicy.IGNORE, true, task);
    }

    public static void removeDb(@NonNull Runnable task) {
        Preconditions.checkNotNull(task);
        taskScheduler.cancel(task);
        ThreadManager.getInstance().getDb().remove(task);
    }

    /**
     * It's for data processing (esp. for DB operations).
     */
    public static ThreadPoolExecutor getDbPool() {
        return ThreadManager.getInstance().getDb();
    }

    public static void runCalculate(Runnable r) {
        ThreadManager.getInstance().getCalculator().execute(r);
    }

    /**
     * It's for CPU related tasks.
     */
    public static void runCalculatorDelayed(long delayMs, @NonNull Runnable task) {
        Preconditions.checkNotNull(task);
        taskScheduler.schedule(ThreadManager.getInstance().getCalculator(), delayMs, true, task);
    }

    /**
     * It's for CPU related tasks.
     * It'll be be ignored if the task was already scheduled.
     */
    public static void runCalculatorDelayedIfAbsent(long delayMs, @NonNull Runnable task) {
        Preconditions.checkNotNull(task);
        taskScheduler.schedule(ThreadManager.getInstance().getCalculator(), delayMs,
                TaskScheduler.SchedulePolicy.IGNORE, true, task);
    }

    public static void removeCalculator(@NonNull Runnable task) {
        Preconditions.checkNotNull(task);
        taskScheduler.cancel(task);
        ThreadManager.getInstance().getCalculator().remove(task);
    }

    /**
     * It's for CPU related tasks.
     */
    public static ThreadPoolExecutor getCalculatorPool() {
        return ThreadManager.getInstance().getCalculator();
    }


    public static void runMain(Runnable r) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            r.run();
        } else
            sUiHandler.post(r);
    }

    public static void runMainDelay(Runnable r, int delay) {
        sUiHandler.postDelayed(r, delay);
    }


    //-----------------------------------------------------------
    // UI tasks

    public static void runUi(@NonNull SafeRunnable task) {
        Preconditions.checkNotNull(task);
        sUiHandler.post(task);
    }

    /**
     * Execute the task only when the context is still there. For example,
     * the Activity or the fragment
     */
    @Nullable
    public static SafeRunnable runUiSafely(@Nullable Context context, @NonNull Runnable task) {
        Preconditions.checkNotNull(task);
        if (context == null) {
            return null; // ignore
        }
        SafeRunnable safeTask = new SafeRunnable(context) {
            @Override
            protected void runSafely() {
                task.run();
            }
        };
        sUiHandler.post(safeTask);
        return safeTask;
    }

    public static void runUiDelayed(long delayMs, @NonNull SafeRunnable task) {
        Preconditions.checkNotNull(task);
        taskScheduler.schedule(uiTaskExecutor, delayMs, false, task);
    }

    @Nullable
    public static SafeRunnable runUiSafelyDelayed(@Nullable Context context, long delayMs,
                                                  @NonNull Runnable task) {
        Preconditions.checkNotNull(task);
        if (context == null) {
            return null; // ignore
        }
        SafeRunnable safeTask = new SafeRunnable(context) {
            @Override
            protected void runSafely() {
                task.run();
            }
        };
        runUiDelayed(delayMs, safeTask);
        return safeTask;
    }


    public static void runUiDelayed(@Nullable Context context, long delayMs,
                                    @NonNull Runnable task) {
        Preconditions.checkNotNull(task);
        if (context == null) {
            return; // ignore
        }
        SafeRunnable safeTask = new SafeRunnable(context) {
            @Override
            protected void runSafely() {
                task.run();
            }
        };
        sUiHandler.postDelayed(safeTask, delayMs);
    }

    public static void runUiImmediately(@NonNull Context context, @NonNull Runnable task) {
        SafeRunnable safeTask = new SafeRunnable(context) {
            @Override
            protected void runSafely() {
                task.run();
            }
        };
        sUiHandler.postAtFrontOfQueue(safeTask);
    }

    public static void runUiImmediately(@NonNull Runnable task) {
        sUiHandler.postAtFrontOfQueue(task);
    }


    public static void removeUi(@NonNull SafeRunnable task) {
        Preconditions.checkNotNull(task);
        taskScheduler.cancel(task);
        sUiHandler.removeCallbacks(task);
    }


    @Deprecated
    public static void runOnUiDelayedSafely(SafeRunnable r, long delay) {
        sUiHandler.postDelayed(r, delay);
    }

    @Deprecated
    public static void postOnUiSafely(SafeRunnable r, int delay) {
        sUiHandler.postDelayed(r, delay);
    }

    @Deprecated
    public static void runOnWorker(Runnable r) {
        initWorkHandler();
        sWorkerHandler.post(r);

    }


    @Deprecated
    public static void postOnWorkerDelayed(Runnable r, long delay) {
        initWorkHandler();
        sWorkerHandler.postDelayed(r, delay);

    }


    @Deprecated
    public static Handler getWorkerHandler() {
        initWorkHandler();
        return sWorkerHandler;
    }

    private static void initWorkHandler() {
        if (sWorkerHandler == null) {
            // handler based thread runner
            synchronized (mWorkObj) {
                if (sWorkerHandler == null) {
                    sHandlerThread = new HandlerThread("normal-worker");
                    sHandlerThread.setPriority(Thread.NORM_PRIORITY - 1);
                    sHandlerThread.start();
                    sWorkerHandler = new Handler(sHandlerThread.getLooper());
                }
            }
        }
    }

    public static void startup() {
        if (BuildConfig.DEBUG) {
            Preconditions.checkMainThread();
        }

        // Use a HandlerThread to schedule tasks
        schedulerThread = new HandlerThread("TaskScheduler",
                Process.THREAD_PRIORITY_MORE_FAVORABLE);
        schedulerThread.start();
        taskScheduler = new TaskScheduler(schedulerThread.getLooper(), "ThreadPool");
        taskScheduler.setCheckInterval(UI_TASK_CHECK_INTERVAL);

        // ui thread runner
        sUiHandler = new Handler(Looper.getMainLooper());
        uiTaskExecutor = new HandlerExecutor(sUiHandler);
    }

    public static void shutdown() {
        if (sHandlerThread != null) {
            sHandlerThread.quit();
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


    @Deprecated
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

        new Thread(runnable, threadName).start();
    }


}
