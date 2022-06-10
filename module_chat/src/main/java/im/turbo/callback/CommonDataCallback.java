package im.turbo.callback;

import androidx.annotation.NonNull;

public interface CommonDataCallback<T> {
    void onSuccess(@NonNull T t);

    default void onFailure(int errorCode, String errorMessage) {
    }

    default void onFailure(@NonNull T t, int errorCode, String errorMessage) {
    }

    default void onCanceled(@NonNull T t) {
    }
}
