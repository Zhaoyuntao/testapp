package im.turbo.basetools.state;

import androidx.annotation.NonNull;

import im.turbo.basetools.preconditions.Preconditions;

/**
 * created by zhaoyuntao
 * on 30/05/2022
 * description:
 */
@SuppressWarnings("unchecked")
public class State<S extends State<S>> {
    private final String tag;
    private StateFetchListener stateListener;

    public State(int tag) {
        this.tag = String.valueOf(tag);
    }

    public State(@NonNull String tag) {
        Preconditions.checkNotEmpty(tag);
        this.tag = tag;
    }

    public S stateListener(StateFetchListener listener) {
        this.stateListener = listener;
        return (S) this;
    }

    public StateFetchListener getStateListener() {
        return stateListener;
    }


    public String getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return "State{" +
                "mode='" + tag + '\'' +
                '}';
    }
}
