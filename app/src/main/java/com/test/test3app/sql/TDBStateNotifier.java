package com.test.test3app.sql;

import androidx.annotation.NonNull;

import com.test.test3app.observer.ListenerManager;

import java.util.List;

/**
 * created by zhaoyuntao
 * on 28/04/2023
 */
public class TDBStateNotifier extends ListenerManager<OnDBTaskStateChangeListener> {

    private volatile static TDBStateNotifier notifier;

    private TDBStateNotifier() {
        super(false);
    }

    public static TDBStateNotifier getInstance() {
        if (notifier == null) {
            synchronized (TDBStateNotifier.class) {
                if (notifier == null) {
                    notifier = new TDBStateNotifier();
                }
            }
        }
        return notifier;
    }

    void notifyWaiting(@NonNull DBTaskState state) {
        notifyListeners(listener -> {
            listener.onWaiting(state);
        });
    }

    void notifyStartOP(@NonNull DBTaskState state) {
        notifyListeners(listener -> {
            listener.onStartOP(state);
        });
    }

    void notifyEnded(@NonNull DBTaskState state) {
        notifyListeners(listener -> {
            listener.onEndOP(state);
        });
    }

    void notifyDumpStates(@NonNull List<DBTaskState> dbStatesDump, @NonNull String log) {
        notifyListeners(listener -> {
            listener.onDump(dbStatesDump, log);
        });
    }
}
