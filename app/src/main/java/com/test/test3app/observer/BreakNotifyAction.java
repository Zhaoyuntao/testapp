package com.test.test3app.observer;

public interface BreakNotifyAction<IListener> {
    boolean notifyIfNeedBreak(IListener listener);
}
