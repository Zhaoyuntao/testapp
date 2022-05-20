package com.test.test3app.appbase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.zhaoyuntao.androidutils.tools.S;

/**
 * created by zhaoyuntao
 * on 21/01/2022
 * description:
 */
public class TestExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler;

    public TestExceptionHandler(Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler) {
        this.defaultUncaughtExceptionHandler = defaultUncaughtExceptionHandler;
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        S.e(Log.getStackTraceString(e));
        defaultUncaughtExceptionHandler.uncaughtException(t, e);
    }
}
