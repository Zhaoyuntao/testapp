package im.turbo.basetools.observer;

public interface BreakNotifyAction<IListener> {
    boolean notifyIfNeedBreak(IListener listener);
}
