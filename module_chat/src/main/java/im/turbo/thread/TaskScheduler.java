package im.turbo.thread;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

import androidx.annotation.IntDef;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import im.turbo.basetools.preconditions.Preconditions;

@SuppressWarnings({"unused", "WeakerAccess"})
public class TaskScheduler {
    private static final String TAG = "TaskScheduler";

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SchedulePolicy.NO_CHECK, SchedulePolicy.IGNORE, SchedulePolicy.REPLACE})
    public @interface SchedulePolicy {
        int NO_CHECK = 1;
        int IGNORE = 2;
        int REPLACE = 3;
    }

    private static final int MSG_ADD_TASK = 1;
    private static final int MSG_REMOVE_TASK = 2;
    private static final int MSG_CHECK_TASKS = 3;
    private static final int MSG_CLEAR_TASKS = 4;

    @VisibleForTesting
    static final long DEFAULT_CHECK_INTERVAL = 10_000; // 10 seconds

    private static final AtomicInteger sTaskSchedulerId = new AtomicInteger(1);

    private final Handler scheduleHandler;
    private final String mOwnerTag;
    private long mCheckInterval = DEFAULT_CHECK_INTERVAL;
    private boolean mLogEnabled = false;

    private final ArrayList<TaskInfo> mTasks = new ArrayList<>();

    // for test only
    @VisibleForTesting int mCheckCount;

    public TaskScheduler(@NonNull Looper scheduleLooper, @NonNull String ownerTag) {
        Preconditions.checkNotNull(scheduleLooper);
        Preconditions.checkNotNull(ownerTag);
        scheduleHandler = new ScheduleHandler(scheduleLooper);
        mOwnerTag = sTaskSchedulerId.getAndIncrement() + "-" + ownerTag;
    }

    public void setCheckInterval(long interval) {
        if (interval < 1000) {
            throw new IllegalArgumentException("Interval less than 1 second is not allowed.");
        }
        mCheckInterval = interval;
    }

    public void enableDebugLogs(boolean enable) {
        mLogEnabled = enable;
    }

    private static String schedulePolicyToString(@SchedulePolicy int policy) {
        switch (policy) {
            case SchedulePolicy.NO_CHECK: return "NO_CHECK";
            case SchedulePolicy.IGNORE: return "IGNORE";
            case SchedulePolicy.REPLACE: return "REPLACE";
            default: throw new RuntimeException("Unknown policy: " + policy);
        }
    }

    private static void checkSchedulePolicy(@SchedulePolicy int policy) {
        switch (policy) {
            case SchedulePolicy.NO_CHECK:
            case SchedulePolicy.IGNORE:
            case SchedulePolicy.REPLACE:
                return;
            default: throw new RuntimeException("Unknown policy: " + policy);
        }
    }

    public void schedule(@NonNull ITaskExecutor executor, long delayedMs, boolean invokeStack,
            @NonNull Runnable task) {
        schedule(executor, delayedMs, SchedulePolicy.NO_CHECK, invokeStack, task);
    }

    public void schedule(@NonNull ITaskExecutor executor, long delayedMs,
            @SchedulePolicy int policy, boolean invokeStack, @NonNull Runnable task) {
        checkSchedulePolicy(policy);
        TaskInfo taskInfo = new TaskInfo(executor, task, delayedMs);
        if (invokeStack) {
            taskInfo.invokeStack = new Throwable();
        }
//        if (mLogEnabled) {
//            LOG.d("[%s][%s] schedule one-off task: %s, policy: %s", TAG, mOwnerTag,
//                    taskInfo, schedulePolicyToString(policy));
//        }
        scheduleTask(taskInfo, policy);
    }

    public void schedulePeriod(@NonNull ITaskExecutor executor, long delayedMs, long periodMs,
            boolean invokeStack, @NonNull Runnable task) {
        schedulePeriod(executor, delayedMs, periodMs, SchedulePolicy.NO_CHECK, invokeStack, task);
    }

    public void schedulePeriod(@NonNull ITaskExecutor executor, long delayedMs, long periodMs,
            @SchedulePolicy int policy, boolean invokeStack, @NonNull Runnable task) {
        checkSchedulePolicy(policy);
        TaskInfo taskInfo = new TaskInfo(executor, task, delayedMs, periodMs);
        if (invokeStack) {
            taskInfo.invokeStack = new Throwable();
        }
        scheduleTask(taskInfo, policy);
    }

    private void scheduleTask(TaskInfo taskInfo, @SchedulePolicy int policy) {
        if (Looper.myLooper() == scheduleHandler.getLooper()) {
            addTask(taskInfo, policy);
        } else {
            scheduleHandler.obtainMessage(MSG_ADD_TASK, policy, 0, taskInfo).sendToTarget();
        }
    }

    public void cancel(@NonNull Runnable task) {
        if (Looper.myLooper() == scheduleHandler.getLooper()) {
            removeTask(task);
        } else {
            scheduleHandler.obtainMessage(MSG_REMOVE_TASK, task).sendToTarget();
        }
    }

    public void clear() {
        if (Looper.myLooper() == scheduleHandler.getLooper()) {
            clearTasks();
        } else {
            scheduleHandler.sendEmptyMessage(MSG_CLEAR_TASKS);
        }
    }

    public void trigger() {
        if (Looper.myLooper() == scheduleHandler.getLooper()) {
            checkTasks();
        } else {
            scheduleHandler.sendEmptyMessage(MSG_CHECK_TASKS);
        }
    }

    public void dump() {
        HandlerUtils.dumpHandler(scheduleHandler, "TaskScheduler[" + mOwnerTag + "]");
    }

    @MainThread
    private void addTask(TaskInfo task, @SchedulePolicy int policy) {
        boolean taskAdded = false;
        if (policy == SchedulePolicy.NO_CHECK) {
            mTasks.add(task);
            taskAdded = true;
        } else {
            int index = findTaskIndex(task.task);
            if (index == -1) {
                mTasks.add(task);
                taskAdded = true;
            } else {
                if (policy == SchedulePolicy.REPLACE) {
                    mTasks.set(index, task);
                    taskAdded = true;
                } //else: nothing to do for ignore
            }
        }

        if (taskAdded) {
            scheduleCheckTask(task.delay);
        }
    }

    @MainThread
    private int findTaskIndex(@NonNull Runnable task) {
        for (int i = 0; i < mTasks.size(); i++) {
            TaskInfo info = mTasks.get(i);
            if (info.task.equals(task)) {
                return i;
            }
        }
        return -1;
    }

    @MainThread
    private void removeTask(@NonNull Runnable task) {
        for (int i = 0; i < mTasks.size(); /* empty */) {
            TaskInfo info = mTasks.get(i);
            if (info.task.equals(task)) {
                mTasks.remove(i);
            } else {
                i++;
            }
        }
    }

    @MainThread
    private void checkTasks() {
        mCheckCount++; // for test only
        if (mTasks.isEmpty()) {
            scheduleHandler.removeMessages(MSG_CHECK_TASKS);
            return;
        }

        Iterator<TaskInfo> it = mTasks.iterator();
        ArrayList<TaskInfo> pendingTasks = new ArrayList<>();
        long nextEventDelay = mCheckInterval;
        while (it.hasNext()) {
            TaskInfo info = it.next();
            if (SystemClock.elapsedRealtime() >= info.triggerAt) {
                pendingTasks.add(info);
                if (info.period > 0) {
                    info.triggerAt = SystemClock.elapsedRealtime() + info.period;
                } else {
                    it.remove();
                    info = null; // mark it removed from queue
                }
            }

            if (info != null) {
                long timeout = info.triggerAt - SystemClock.elapsedRealtime();
                if (timeout < nextEventDelay) {
                    nextEventDelay = timeout;
                }
            }
        }

        scheduleHandler.removeMessages(MSG_CHECK_TASKS);
        scheduleHandler.sendEmptyMessageDelayed(MSG_CHECK_TASKS, nextEventDelay);

        for (TaskInfo info : pendingTasks) {
            info.executor.post(info.task, info.invokeStack);
        }
    }

    @MainThread
    private void clearTasks() {
        mTasks.clear();
    }

    @MainThread
    private void scheduleCheckTask(long delay) {
        if (delay > mCheckInterval) {
            delay = mCheckInterval;
        }
        scheduleHandler.sendEmptyMessageDelayed(MSG_CHECK_TASKS, delay);
    }

    @SuppressLint("HandlerLeak")
    private class ScheduleHandler extends Handler {
        ScheduleHandler(@NonNull Looper scheduleLooper) {
            super(scheduleLooper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ADD_TASK: {
                    TaskInfo task = (TaskInfo) msg.obj;
                    int policy = msg.arg1;
                    addTask(task, policy);
                    break;
                }

                case MSG_REMOVE_TASK: {
                    Runnable task = (Runnable) msg.obj;
                    removeTask(task);
                    break;
                }

                case MSG_CHECK_TASKS: {
                    checkTasks();
                    break;
                }

                case MSG_CLEAR_TASKS: {
                    clearTasks();
                    break;
                }
            }
        }
    }
}

class TaskInfo {
    private static final AtomicInteger taskIdGenerator = new AtomicInteger(1);

    private final int taskId;
    ITaskExecutor executor;
    Runnable task;
    long delay;
    long period = -1;
    long triggerAt;
    Throwable invokeStack;

    TaskInfo(@NonNull ITaskExecutor executor, @NonNull Runnable task, long delay) {
        this.executor = executor;
        this.taskId = taskIdGenerator.getAndIncrement();
        this.task = task;
        this.delay = delay;
        this.triggerAt = SystemClock.elapsedRealtime() + delay;
    }

    TaskInfo(@NonNull ITaskExecutor executor, @NonNull Runnable task, long delay, long period) {
        this.executor = executor;
        this.taskId = taskIdGenerator.getAndIncrement();
        this.task = task;
        this.delay = delay;
        this.period = period;
        this.triggerAt = SystemClock.elapsedRealtime() + delay;
    }

    @NonNull
    @Override
    public String toString() {
//        long timestamp = System.currentTimeMillis() - (SystemClock.elapsedRealtime() - triggerAt);
        return "TaskInfo[id=" + taskId + ", delay=" + delay
//                + ", triggerAt=" + TimeUtils.getReadableTimeStamp(timestamp)
                + ", period=" + period + "]";
    }
}
