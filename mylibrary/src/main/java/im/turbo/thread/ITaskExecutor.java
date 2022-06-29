package im.turbo.thread;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface ITaskExecutor {
    void post(@NonNull Runnable task, @Nullable Throwable invokeStack);
}
