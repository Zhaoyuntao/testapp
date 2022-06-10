package im.thebot.chat.ui.emoji.category;

import im.thebot.chat.ui.emoji.EmojiBean;
import im.thebot.chat.ui.emoji.StickerTypes;

/**
 * created by zhaoyuntao
 * on 12/09/2021
 * description:
 */
public interface BaseEmojiCategory {
    @StickerTypes
    String getType();

    int getDescRes();

    int getNormalIconRes();

    int getActiveIconRes();

    EmojiBean[] getData();
}
