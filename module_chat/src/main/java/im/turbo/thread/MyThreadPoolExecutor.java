package im.turbo.thread;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class MyThreadPoolExecutor extends ThreadPoolExecutor implements ITaskExecutor {
    private final String name;
    private static final String TAG = "MyThreadPoolExecutor";


    public MyThreadPoolExecutor(@NonNull String name, int priority,
                                int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                new NamedThreadFactory(name, priority));
        this.name = name;
    }

    public MyThreadPoolExecutor(@NonNull String name, int priority,
                                int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                @NonNull MyThreadPoolExecutor fallbackExecutor) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, new SynchronousQueue<>(),
                new NamedThreadFactory(name, priority));
        this.name = name;
        setRejectedExecutionHandler(new FallbackRejectedExecutionHandler(fallbackExecutor));
    }

    @Override
    public void post(@NonNull Runnable task, @Nullable Throwable invokeStack) {
        execute(task);
    }


    public String getName() {
        return name;
    }



    private static class FallbackRejectedExecutionHandler implements RejectedExecutionHandler {
        private final Executor fallbackExecutor;

        public FallbackRejectedExecutionHandler(@NonNull Executor fallbackExecutor) {
            this.fallbackExecutor = fallbackExecutor;
        }

        @Override
        public void rejectedExecution(Runnable task, ThreadPoolExecutor executor) {
            String name;
            if (executor instanceof MyThreadPoolExecutor) {
                name = ((MyThreadPoolExecutor) executor).getName();
            } else {
                name = executor.toString();
            }
            fallbackExecutor.execute(task);
        }
    }
}
