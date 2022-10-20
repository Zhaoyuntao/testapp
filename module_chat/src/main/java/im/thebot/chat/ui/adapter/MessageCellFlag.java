package im.thebot.chat.ui.adapter;

import androidx.annotation.IntDef;

/**
 * created by zhaoyuntao
 * on 28/03/2022
 * description:
 */
@IntDef({
        MessageCellFlag.FLAG_MASK_MESSAGE_VALUE,
        MessageCellFlag.FLAG_MASK_FLAG_VALUE,
        MessageCellFlag.FLAG_GRAVITY_LEFT,
        MessageCellFlag.FLAG_GRAVITY_RIGHT,
        MessageCellFlag.FLAG_LAYOUT_SHOW_TIME,
        MessageCellFlag.FLAG_LAYOUT_SHOW_TOP_SPACE,
        MessageCellFlag.FLAG_LAYOUT_SHOW_BOTTOM_SPACE,
        MessageCellFlag.FLAG_LAYOUT_SHOW_SENDER_NAME,
        MessageCellFlag.FLAG_LAYOUT_SHOW_NEW_MESSAGE_LINE,
        MessageCellFlag.FLAG_LAYOUT_SHOW_FORWARD_HEAD,
        MessageCellFlag.FLAG_LAYOUT_SHOW_REPLY,
})
public @interface MessageCellFlag {
    int FLAG_MASK_MESSAGE_VALUE = (1 << 11) - 1;
    int FLAG_MASK_FLAG_VALUE = (1 << 31) - 1 - FLAG_MASK_MESSAGE_VALUE;
    int FLAG_GRAVITY_LEFT = 1 << 13;
    int FLAG_GRAVITY_RIGHT = 1 << 14;
    int FLAG_LAYOUT_SHOW_TIME = 1 << 15;
    int FLAG_LAYOUT_SHOW_TOP_SPACE = 1 << 16;
    int FLAG_LAYOUT_SHOW_BOTTOM_SPACE = 1 << 17;
    int FLAG_LAYOUT_SHOW_SENDER_NAME = 1 << 18;
    int FLAG_LAYOUT_SHOW_NEW_MESSAGE_LINE = 1 << 19;
    int FLAG_LAYOUT_SHOW_FORWARD_HEAD = 1 << 20;
    int FLAG_LAYOUT_SHOW_REPLY = 1 << 21;
}
