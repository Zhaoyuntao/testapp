package com.test.test3app.threadpool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface ITaskExecutor {
    void post(@NonNull Runnable task, @Nullable Throwable invokeStack);
}
