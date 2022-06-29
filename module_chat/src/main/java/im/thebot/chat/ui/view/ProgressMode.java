package im.thebot.chat.ui.view;

import androidx.annotation.NonNull;

import im.turbo.basetools.state.State;

/**
 * created by zhaoyuntao
 * on 27/04/2022
 * description:
 */
final public class ProgressMode extends State<ProgressMode> {

    public ProgressMode(int tag) {
        super(tag);
    }

    public ProgressMode(@NonNull String tag) {
        super(tag);
    }
}
