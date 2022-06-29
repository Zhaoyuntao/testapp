package im.turbo.thread;

import androidx.annotation.NonNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {
    private final String nameBase;
    private int priority = -1;
    private final AtomicInteger threadIndex = new AtomicInteger(0);
    private static final String TAG_THREAD="NamedThreadFactory";

    public NamedThreadFactory(@NonNull String nameBase) {
        this.nameBase = nameBase;
    }

    /**
     * @param priority Must between in {@link Thread#MIN_PRIORITY} and {@link Thread#MAX_PRIORITY}.
     */
    public NamedThreadFactory(@NonNull String nameBase, int priority) {
        this.nameBase = nameBase;
        this.priority = priority;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        String threadName = nameBase + '-' + threadIndex.incrementAndGet();
        Thread t = new Thread(runnable, threadName);
        if (priority > 0) {
            t.setPriority(priority);
        }
        return t;
    }
}
