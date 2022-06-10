package com.test.test3app.observer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import im.turbo.basetools.preconditions.Preconditions;

public class ListenerManager<IListener> extends ObjectManager<IListener> {
    public static final String KEY_ALL = "All";

    public ListenerManager(boolean weakReference) {
        super(weakReference);
    }

    public boolean addListener(@Nullable IListener listener) {
        return super.addObject(listener);
    }

    public void addListener(@NonNull String tag, @Nullable IListener listener) {
        Preconditions.checkNotEmpty(tag);
        super.addObject(tag, listener);
    }

    public void removeListener(@Nullable IListener listener) {
        super.removeObject(listener);
    }

    public void removeListener(@NonNull String tag) {
        Preconditions.checkNotEmpty(tag);
        super.removeObject(tag);
    }

    public IListener getListener(@NonNull String tag) {
        return getObject(tag);
    }

    public void notifyListeners(@NonNull NotifyAction<IListener> action) {
        super.notifyObjects(action);
    }

    public boolean notifyListeners(@NonNull BreakNotifyAction<IListener> action) {
        return super.notifyObjects(action);
    }

    @Override
    final protected void onObjectAdded(@NonNull IListener listener) {
        onListenerAdded(listener);
    }

    protected void onListenerAdded(@NonNull IListener listener) {
        // nothing to do
    }

    @Override
    final protected void onObjectRemoved(@NonNull IListener listener) {
        onListenerRemoved(listener);
    }

    protected void onListenerRemoved(@NonNull IListener listener) {

    }
}
