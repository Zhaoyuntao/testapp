package im.thebot.chat.ui.view;

import androidx.annotation.IntDef;

/**
 * created by zhaoyuntao
 * on 28/06/2022
 * description:
 */
@IntDef({
        ProgressAction.ACTION_NOT_TOUCH,
        ProgressAction.ACTION_PRESS_DOWN,
        ProgressAction.ACTION_MOVE,
        ProgressAction.ACTION_UP,
})
public @interface ProgressAction {
    int ACTION_NOT_TOUCH = 0;
    int ACTION_PRESS_DOWN = 1;
    int ACTION_MOVE = 2;
    int ACTION_UP = 3;
}
