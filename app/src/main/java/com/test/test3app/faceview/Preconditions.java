package com.test.test3app.faceview;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.test.test3app.threadpool.ThreadUtils;

@SuppressWarnings("unused")
public class Preconditions {
    private Preconditions() {
    }

    public static void checkMainThread() {
        if (!ThreadUtils.isUiThread()) {
            throw new RuntimeException("Not in main thread");
        }
    }

    public static void checkNonMainThread() {
        if (ThreadUtils.isUiThread()) {
            throw new RuntimeException("In main thread");
        }
    }

    public static @NonNull <T> T checkNotNull(@Nullable T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }

    public static @NonNull <T> T checkNotNull(@Nullable T obj, @NonNull String errorMessage) {
        if (obj == null) {
            throw new NullPointerException(errorMessage);
        }
        return obj;
    }

    public static @NonNull
    String checkNotEmpty(@Nullable String str) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException(str + " is null or empty");
        }
        return str;
    }

    public static @NonNull
    String checkNotEmpty(@Nullable String str, @NonNull String errorMessage) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
        return str;
    }

    public static @NonNull byte[] checkNotEmpty(@Nullable byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("Data is null or empty");
        }
        return bytes;
    }

    public static void checkEqual(@Nullable CharSequence str1, @Nullable CharSequence str2) {
        if (!TextUtils.equals(str1, str2)) {
            throw new IllegalArgumentException("[" + str1 + "] not equal to [" + str2 + "]");
        }
    }

    public static void checkPositive(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Not positive: " + value);
        }
    }

    public static void checkPositive(long value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Not positive: " + value);
        }
    }

    public static void checkArgument(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }

    public static void checkArgument(boolean expression, @NonNull String errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void checkState(boolean expression) {
        if (!expression) {
            throw new IllegalStateException();
        }
    }

    public static void checkState(boolean expression, @NonNull String errorMessage) {
        if (!expression) {
            throw new IllegalStateException(errorMessage);
        }
    }
}
