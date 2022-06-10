package im.turbo.basetools.observer;

import androidx.annotation.NonNull;

import im.turbo.basetools.preconditions.Preconditions;

public class BaseListener {

    protected String observeKey;

    public BaseListener(){

    }

    public BaseListener(@NonNull String observeKey) {
        Preconditions.checkNotEmpty(observeKey);
        this.observeKey = observeKey;
    }

    public boolean match(@NonNull String observeKey) {
        return this.observeKey.equals(ListenerManager.KEY_ALL) || this.observeKey.equals(observeKey);
    }

    final public void init(String observeKey) {
        Preconditions.checkNotEmpty(observeKey);
        this.observeKey = observeKey;
    }



}
