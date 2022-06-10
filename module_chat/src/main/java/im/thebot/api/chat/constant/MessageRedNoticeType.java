package im.thebot.api.chat.constant;

import androidx.annotation.IntDef;

/**
 * created by zhaoyuntao
 * on 15/11/2021
 * description:
 */
@IntDef({
        MessageRedNoticeType.TYPE_RED,
        MessageRedNoticeType.TYPE_BLUE
})
public @interface MessageRedNoticeType {
    int TYPE_RED = 0;
    int TYPE_BLUE = 1;
}
