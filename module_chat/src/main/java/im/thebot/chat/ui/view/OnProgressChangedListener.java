package im.thebot.chat.ui.view;

/**
 * created by zhaoyuntao
 * on 14/07/2022
 * description:
 */
public interface OnProgressChangedListener {
    default void onStartDragging() {
    }

    default void onDragging(float percent) {
    }

    void onProgressChanged(float percent, boolean dragByUser);
}
