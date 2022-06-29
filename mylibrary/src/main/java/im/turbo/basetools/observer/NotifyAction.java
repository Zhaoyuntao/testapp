package im.turbo.basetools.observer;

public interface NotifyAction<IListener> {
    void notify(IListener listener);
}
