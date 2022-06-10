package im.thebot.chat.ui.emoji.category;


import androidx.annotation.StringRes;

import com.example.module_chat.R;

import im.thebot.chat.ui.emoji.EmojiBean;
import im.thebot.chat.ui.emoji.StickerTypes;

/**
 * created by zhaoyuntao
 * on 12/09/2021
 * description:
 */
public class ActivitiesCategory implements BaseEmojiCategory {
    @Override
    public String getType() {
        return StickerTypes.ActivitiesCategory;
    }

    @StringRes
    @Override
    public int getDescRes() {
        return R.string.emoji_activity;
    }

    @Override
    public int getNormalIconRes() {
        return R.drawable.emoji_activity_in_active;
    }

    @Override
    public int getActiveIconRes() {
        return R.drawable.emoji_activity_active;
    }

    @Override
    public EmojiBean[] getData() {
        return DATA;
    }

    public static final EmojiBean[] DATA = new EmojiBean[]{
            EmojiBean.fromCodePoint(0x1F383),
            EmojiBean.fromCodePoint(0x1F384),
            EmojiBean.fromCodePoint(0x1F386),
            EmojiBean.fromCodePoint(0x1F387),
            EmojiBean.fromChar((char) 0x2728),
            EmojiBean.fromCodePoint(0x1F388),
            EmojiBean.fromCodePoint(0x1F389),
            EmojiBean.fromCodePoint(0x1F38A),
            EmojiBean.fromCodePoint(0x1F38B),
            EmojiBean.fromCodePoint(0x1F38D),
            EmojiBean.fromCodePoint(0x1F38E),
            EmojiBean.fromCodePoint(0x1F38F),
            EmojiBean.fromCodePoint(0x1F390),
            EmojiBean.fromCodePoint(0x1F391),
            EmojiBean.fromCodePoint(0x1F380),
            EmojiBean.fromCodePoint(0x1F381),
            EmojiBean.fromChars(new int[]{0x1F397, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F39F, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F3AB),
            EmojiBean.fromChars(new int[]{0x1F396, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F3C6),
            EmojiBean.fromCodePoint(0x1F3C5),
            EmojiBean.fromChar((char) 0x26bd),
            EmojiBean.fromChar((char) 0x26be),
            EmojiBean.fromCodePoint(0x1F3C0),
            EmojiBean.fromCodePoint(0x1F3D0),
            EmojiBean.fromCodePoint(0x1F3C8),
            EmojiBean.fromCodePoint(0x1F3C9),
            EmojiBean.fromCodePoint(0x1F3BE),
            EmojiBean.fromCodePoint(0x1F3B3),
            EmojiBean.fromCodePoint(0x1F3CF),
            EmojiBean.fromCodePoint(0x1F3D1),
            EmojiBean.fromCodePoint(0x1F3D2),
            EmojiBean.fromCodePoint(0x1F3D3),
            EmojiBean.fromCodePoint(0x1F3F8),
            EmojiBean.fromChar((char) 0x26F3),
            EmojiBean.fromCodePoint(0x1F3A3),
            EmojiBean.fromCodePoint(0x1F3BD),
            EmojiBean.fromCodePoint(0x1F3BF),
            EmojiBean.fromCodePoint(0x1F3AF),
            EmojiBean.fromCodePoint(0x1F3B1),
            EmojiBean.fromCodePoint(0x1F52E),
            EmojiBean.fromCodePoint(0x1F3AE),
            EmojiBean.fromChars(new int[]{0x1F579, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F3B0),
            EmojiBean.fromCodePoint(0x1F3B2),
            EmojiBean.fromChars(new int[]{0x2660, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x2665, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x2666, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x2663, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F0CF),
            EmojiBean.fromCodePoint(0x1F004),
            EmojiBean.fromCodePoint(0x1F3B4),
            EmojiBean.fromCodePoint(0x1F3AD),
            EmojiBean.fromChars(new int[]{0x1F5BC, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F3A8),
    };

}
