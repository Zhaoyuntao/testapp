package com.test.test3app.sql;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * created by zhaoyuntao
 * on 27/04/2023
 */
public interface OnDBTaskStateChangeListener {
    void onDump(@NonNull List<DBTaskState> states, @NonNull String log);

    void onWaiting(@NonNull DBTaskState dbTaskState);

    void onStartOP(@NonNull DBTaskState dbTaskState);

    void onEndOP(@NonNull DBTaskState dbTaskState);
}
