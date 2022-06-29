package im.turbo.basetools.state;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import im.turbo.basetools.observer.ObjectManager;
import im.turbo.basetools.preconditions.Preconditions;

/**
 * created by zhaoyuntao
 * on 30/05/2022
 * description:
 */
public class StateMachine<ST extends State<ST>> extends ObjectManager<ST> {
    private ST currentState;

    public StateMachine() {
        super(false);
    }

    @SafeVarargs
    public final void setStates(@NonNull ST... states) {
        clear();
        for (ST state : states) {
            addObject(state.getTag(), state);
        }
    }

    public boolean setCurrentState(int tag) {
        return setCurrentState(String.valueOf(tag));
    }

    public boolean setCurrentState(@NonNull String tag) {
        Preconditions.checkNotEmpty(tag);
        ST state = getObject(tag);
        return state != null && _setCurrentState(state);
    }

    private boolean _setCurrentState(@NonNull ST state) {
        if (currentState != null && TextUtils.equals(state.getTag(), currentState.getTag())) {
            return false;
        }
        currentState = state;
        StateFetchListener listener = state.getStateListener();
        if (listener != null) {
            listener.onFetched(currentState.getTag());
        }
        return true;
    }

    public ST getCurrentState() {
        return currentState;
    }
}
