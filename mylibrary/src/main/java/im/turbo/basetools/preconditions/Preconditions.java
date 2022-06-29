package im.turbo.basetools.preconditions;

import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.doctor.mylibrary.BuildConfig;

import java.util.Collection;

import im.turbo.utils.log.S;

@SuppressWarnings("unused")
public class Preconditions {
    private Preconditions() {
    }

    public static void checkMainThread() {
        if (!isUiThread()) {
            if (BuildConfig.DEBUG) {
                throw new RuntimeException("Not in main thread");
            } else {
                S.e("[Preconditions]", "Preconditions.checkMainThread error:" + Log.getStackTraceString(new Throwable()));
            }
        }
    }

    public static void checkNonMainThread() {
        if (isUiThread()) {
            if (BuildConfig.DEBUG) {
                throw new RuntimeException("In main thread");
            } else {
                S.e("[Preconditions]", "Preconditions.checkNonMainThread error:" + Log.getStackTraceString(new Throwable()));
            }
        }
    }

    public static boolean isUiThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
    public static @NonNull
    <T> T checkNotNull(@Nullable T obj) {
        if (obj == null) {
            if (BuildConfig.DEBUG) {
                throw new IllegalArgumentException();
            } else {
                S.e("[Preconditions]", "Preconditions.checkNotNull error:" + Log.getStackTraceString(new Throwable()));
            }
        }
        return obj;
    }

    public static @NonNull
    <T> T checkNotNull(@Nullable T obj, @NonNull String errorMessage) {
        if (obj == null) {
            if (BuildConfig.DEBUG) {
                throw new IllegalArgumentException(errorMessage);
            } else {
                S.e("[Preconditions]", "Preconditions.checkNotNull error:" + errorMessage + "\n" + Log.getStackTraceString(new Throwable()));
            }
        }
        return obj;
    }

    public static @NonNull
    String checkNotEmpty(@Nullable String str) {
        if (str == null || str.isEmpty()) {
            if (BuildConfig.DEBUG) {
                throw new IllegalArgumentException(str + " is null or empty");
            } else {
                S.e("[Preconditions]", "Preconditions.checkNotEmpty error:" + str + " is null or empty\n" + Log.getStackTraceString(new Throwable()));
            }
        }
        return str;
    }

    public static @NonNull
    String checkNotEmpty(@Nullable String str, @NonNull String errorMessage) {
        if (str == null || str.isEmpty()) {
            if (BuildConfig.DEBUG) {
                throw new IllegalArgumentException(errorMessage);
            } else {
                S.e("[Preconditions]", "Preconditions.checkNotEmpty error:" + errorMessage + "\n" + Log.getStackTraceString(new Throwable()));
            }
        }
        return str;
    }

    public static @NonNull
    int[] checkNotEmpty(@Nullable int[] bytes) {
        if (bytes == null || bytes.length == 0) {
            if (BuildConfig.DEBUG) {
                throw new IllegalArgumentException("Data is null or empty");
            } else {
                S.e("[Preconditions]", "Preconditions.checkNotEmpty error:Data is null or empty:" + bytes + Log.getStackTraceString(new Throwable()));
            }
        }
        return bytes;
    }

    public static @NonNull
    byte[] checkNotEmpty(@Nullable byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            if (BuildConfig.DEBUG) {
                throw new IllegalArgumentException("Data is null or empty");
            } else {
                S.e("[Preconditions]", "Preconditions.checkNotEmpty error:Data is null or empty:" + bytes + " " + Log.getStackTraceString(new Throwable()));
            }
        }
        return bytes;
    }

    public static @NonNull
    char[] checkNotEmpty(@Nullable char[] data) {
        if (data == null || data.length == 0) {
            if (BuildConfig.DEBUG) {
                throw new IllegalArgumentException("Data is null or empty");
            } else {
                S.e("[Preconditions]", "Preconditions.checkNotEmpty error:Data is null or empty:" + data + "\n" + Log.getStackTraceString(new Throwable()));
            }
        }
        return data;
    }

    public static @NonNull
    String[] checkNotEmpty(@Nullable String[] data) {
        if (data == null || data.length == 0) {
            if (BuildConfig.DEBUG) {
                throw new IllegalArgumentException("Data is null or empty");
            } else {
                S.e("[Preconditions]", "Preconditions.checkNotEmpty error:Data is null or empty:" + data + "\n" + Log.getStackTraceString(new Throwable()));
            }
        }
        return data;
    }

    public static @NonNull
    <T> Collection<T> checkNotEmpty(@Nullable Collection<T> data) {
        if (data == null || data.isEmpty()) {
            if (BuildConfig.DEBUG) {
                throw new IllegalArgumentException("Data is null or empty");
            } else {
                S.e("[Preconditions]", "Preconditions.checkNotEmpty error:Data is null or empty:" + data + "\n" + Log.getStackTraceString(new Throwable()));
            }
        }
        return data;
    }

    public static void checkEqual(@Nullable CharSequence str1, @Nullable CharSequence str2) {
        if (!TextUtils.equals(str1, str2)) {
            if (BuildConfig.DEBUG) {
                throw new IllegalArgumentException("[" + str1 + "] not equal to [" + str2 + "]");
            } else {
                S.e("[Preconditions]", "Preconditions.checkEqual error:[" + str1 + "] not equal to [" + str2 + "]\n" + Log.getStackTraceString(new Throwable()));
            }
        }
    }

    public static void checkPositive(int value) {
        if (value <= 0) {
            if (BuildConfig.DEBUG) {
                throw new IllegalArgumentException("Not positive: " + value);
            } else {
                S.e("[Preconditions]", "Preconditions.checkPositive error:Not positive" + value + "\n" + Log.getStackTraceString(new Throwable()));
            }
        }
    }

    public static void checkPositive(long value) {
        if (value <= 0) {
            if (BuildConfig.DEBUG) {
                throw new IllegalArgumentException("Not positive: " + value);
            } else {
                S.e("[Preconditions]", "Preconditions.checkPositive error:Not positive:" + value + "\n" + Log.getStackTraceString(new Throwable()));
            }
        }
    }

    public static void checkNotNegative(long value) {
        if (value < 0) {
            if (BuildConfig.DEBUG) {
                throw new IllegalArgumentException("Negative: " + value);
            } else {
                S.e("[Preconditions]", "Preconditions.checkNotNegative error:Negative:" + value + "\n" + Log.getStackTraceString(new Throwable()));
            }
        }
    }

    public static void checkArgument(boolean expression) {
        if (!expression) {
            if (BuildConfig.DEBUG) {
                throw new IllegalArgumentException();
            } else {
                S.e("[Preconditions]", "Preconditions.checkArgument error:" + Log.getStackTraceString(new Throwable()));
            }
        }
    }

    public static void checkArgument(boolean expression, @NonNull String errorMessage) {
        if (!expression) {
            if (BuildConfig.DEBUG) {
                throw new IllegalArgumentException(errorMessage);
            } else {
                S.e("[Preconditions]", "Preconditions.checkArgument error:" + Log.getStackTraceString(new Throwable()));
            }
        }
    }

    public static void checkState(boolean expression) {
        if (!expression) {
            if (BuildConfig.DEBUG) {
                throw new IllegalStateException();
            } else {
                S.e("[Preconditions]", "Preconditions.checkState[" + expression + "] error:" + Log.getStackTraceString(new Throwable()));
            }
        }
    }

    public static void checkState(boolean expression, @NonNull String errorMessage) {
        if (!expression) {
            if (BuildConfig.DEBUG) {
                throw new IllegalStateException(errorMessage);
            } else {
                S.e("[Preconditions]", "Preconditions.checkState[" + expression + "] error:" + errorMessage + "\n" + Log.getStackTraceString(new Throwable()));
            }
        }
    }
}
