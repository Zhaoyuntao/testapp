package com.test.test3app.observer;

public interface NotifyAction<IListener> {
    void notify(IListener listener);
}
