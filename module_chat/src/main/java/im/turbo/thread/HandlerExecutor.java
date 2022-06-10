package im.turbo.thread;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

@SuppressWarnings({"unused", "WeakerAccess"})
public class HandlerExecutor implements ITaskExecutor {
    private Handler mTaskHandler;

    public HandlerExecutor(@NonNull Handler handler) {
        mTaskHandler = handler;
    }

    @Override
    public void post(@NonNull Runnable task, @Nullable Throwable invokeStack) {
        mTaskHandler.post(task);
    }

    public static HandlerExecutor withMainLooper() {
        return new HandlerExecutor(new Handler(Looper.getMainLooper()));
    }
}
