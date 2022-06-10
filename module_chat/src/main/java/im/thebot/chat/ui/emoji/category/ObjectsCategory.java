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
public class ObjectsCategory implements BaseEmojiCategory {
    @Override
    public String getType() {
        return StickerTypes.ObjectsCategory;
    }

    @StringRes
    @Override
    public int getDescRes() {
        return R.string.emoji_object;
    }

    @Override
    public int getNormalIconRes() {
        return R.drawable.emoji_object_in_active;
    }

    @Override
    public int getActiveIconRes() {
        return R.drawable.emoji_object_active;
    }

    @Override
    public EmojiBean[] getData() {
        return DATA;
    }

    public static final EmojiBean[] DATA = new EmojiBean[]{
            EmojiBean.fromCodePoint(0x1F453),
            EmojiBean.fromChars(new int[]{0x1F576, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F454),
            EmojiBean.fromCodePoint(0x1F455),
            EmojiBean.fromCodePoint(0x1F456),
            EmojiBean.fromCodePoint(0x1F457),
            EmojiBean.fromCodePoint(0x1F458),
            EmojiBean.fromCodePoint(0x1F459),
            EmojiBean.fromCodePoint(0x1F45A),
            EmojiBean.fromCodePoint(0x1F45B),
            EmojiBean.fromCodePoint(0x1F45C),
            EmojiBean.fromCodePoint(0x1F45D),
            EmojiBean.fromChars(new int[]{0x1F6CD, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F392),
            EmojiBean.fromCodePoint(0x1F45E),
            EmojiBean.fromCodePoint(0x1F45F),
            EmojiBean.fromCodePoint(0x1F460),
            EmojiBean.fromCodePoint(0x1F461),
            EmojiBean.fromCodePoint(0x1F462),
            EmojiBean.fromCodePoint(0x1F451),
            EmojiBean.fromCodePoint(0x1F452),
            EmojiBean.fromCodePoint(0x1F3A9),
            EmojiBean.fromCodePoint(0x1F393),
            EmojiBean.fromCodePoint(0x1F4FF),
            EmojiBean.fromCodePoint(0x1F484),
            EmojiBean.fromCodePoint(0x1F48D),
            EmojiBean.fromCodePoint(0x1F48E),
            EmojiBean.fromCodePoint(0x1F507),
            EmojiBean.fromCodePoint(0x1F508),
            EmojiBean.fromCodePoint(0x1F509),
            EmojiBean.fromCodePoint(0x1F50A),
            EmojiBean.fromCodePoint(0x1F4E2),
            EmojiBean.fromCodePoint(0x1F4E3),
            EmojiBean.fromCodePoint(0x1F4EF),
            EmojiBean.fromCodePoint(0x1F514),
            EmojiBean.fromCodePoint(0x1F515),
            EmojiBean.fromCodePoint(0x1F3BC),
            EmojiBean.fromCodePoint(0x1F3B5),
            EmojiBean.fromCodePoint(0x1F3B6),
            EmojiBean.fromChars(new int[]{0x1F399, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F39A, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F39B, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F3A4),
            EmojiBean.fromCodePoint(0x1F3A7),
            EmojiBean.fromCodePoint(0x1F4FB),
            EmojiBean.fromCodePoint(0x1F3B7),
            EmojiBean.fromCodePoint(0x1F3B8),
            EmojiBean.fromCodePoint(0x1F3B9),
            EmojiBean.fromCodePoint(0x1F3BA),
            EmojiBean.fromCodePoint(0x1F3BB),
            EmojiBean.fromCodePoint(0x1F4F1),
            EmojiBean.fromCodePoint(0x1F4F2),
            EmojiBean.fromChars(new int[]{0x260E, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F4DE),
            EmojiBean.fromCodePoint(0x1F4DF),
            EmojiBean.fromCodePoint(0x1F4E0),
            EmojiBean.fromCodePoint(0x1F50B),
            EmojiBean.fromCodePoint(0x1F50C),
            EmojiBean.fromCodePoint(0x1F4BB),
            EmojiBean.fromChars(new int[]{0x1F5A5, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F5A8, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F5B1, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F5B2, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F4BD),
            EmojiBean.fromCodePoint(0x1F4BE),
            EmojiBean.fromCodePoint(0x1F4BF),
            EmojiBean.fromCodePoint(0x1F4C0),
            EmojiBean.fromCodePoint(0x1F3A5),
            EmojiBean.fromChars(new int[]{0x1F39E, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F4FD, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F3AC),
            EmojiBean.fromCodePoint(0x1F4FA),
            EmojiBean.fromCodePoint(0x1F4F7),
            EmojiBean.fromCodePoint(0x1F4F8),
            EmojiBean.fromCodePoint(0x1F4F9),
            EmojiBean.fromCodePoint(0x1F4FC),
            EmojiBean.fromCodePoint(0x1F50D),
            EmojiBean.fromCodePoint(0x1F50E),
            EmojiBean.fromChars(new int[]{0x1F56F, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F4A1),
            EmojiBean.fromCodePoint(0x1F526),
            EmojiBean.fromCodePoint(0x1F3EE),
            EmojiBean.fromCodePoint(0x1F4D4),
            EmojiBean.fromCodePoint(0x1F4D5),
            EmojiBean.fromCodePoint(0x1F4D6),
            EmojiBean.fromCodePoint(0x1F4D7),
            EmojiBean.fromCodePoint(0x1F4D8),
            EmojiBean.fromCodePoint(0x1F4D9),
            EmojiBean.fromCodePoint(0x1F4DA),
            EmojiBean.fromCodePoint(0x1F4D3),
            EmojiBean.fromCodePoint(0x1F4D2),
            EmojiBean.fromCodePoint(0x1F4C3),
            EmojiBean.fromCodePoint(0x1F4DC),
            EmojiBean.fromCodePoint(0x1F4C4),
            EmojiBean.fromCodePoint(0x1F4F0),
            EmojiBean.fromChars(new int[]{0x1F5DE, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F4D1),
            EmojiBean.fromCodePoint(0x1F516),
            EmojiBean.fromChars(new int[]{0x1F3F7, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F4B0),
            EmojiBean.fromCodePoint(0x1F4B4),
            EmojiBean.fromCodePoint(0x1F4B5),
            EmojiBean.fromCodePoint(0x1F4B6),
            EmojiBean.fromCodePoint(0x1F4B7),
            EmojiBean.fromCodePoint(0x1F4B8),
            EmojiBean.fromCodePoint(0x1F4B3),
            EmojiBean.fromCodePoint(0x1F4B9),
            EmojiBean.fromCodePoint(0x1F4B1),
            EmojiBean.fromCodePoint(0x1F4B2),
            EmojiBean.fromCodePoint(0x1F4E7),
            EmojiBean.fromCodePoint(0x1F4E8),
            EmojiBean.fromCodePoint(0x1F4E9),
            EmojiBean.fromCodePoint(0x1F4E4),
            EmojiBean.fromCodePoint(0x1F4E5),
            EmojiBean.fromCodePoint(0x1F4E6),
            EmojiBean.fromCodePoint(0x1F4EB),
            EmojiBean.fromCodePoint(0x1F4EA),
            EmojiBean.fromCodePoint(0x1F4EC),
            EmojiBean.fromCodePoint(0x1F4ED),
            EmojiBean.fromCodePoint(0x1F4EE),
            EmojiBean.fromChars(new int[]{0x1F5F3, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F58B, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F58A, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F58C, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F58D, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F4DD),
            EmojiBean.fromCodePoint(0x1F4BC),
            EmojiBean.fromCodePoint(0x1F4C1),
            EmojiBean.fromCodePoint(0x1F4C2),
            EmojiBean.fromChars(new int[]{0x1F5C2, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F4C5),
            EmojiBean.fromCodePoint(0x1F4C6),
            EmojiBean.fromChars(new int[]{0x1F5D2, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F5D3, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F4C7),
            EmojiBean.fromCodePoint(0x1F4C8),
            EmojiBean.fromCodePoint(0x1F4C9),
            EmojiBean.fromCodePoint(0x1F4CA),
            EmojiBean.fromCodePoint(0x1F4CB),
            EmojiBean.fromCodePoint(0x1F4CC),
            EmojiBean.fromCodePoint(0x1F4CD),
            EmojiBean.fromCodePoint(0x1F4CE),
            EmojiBean.fromChars(new int[]{0x1F587, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F4CF),
            EmojiBean.fromCodePoint(0x1F4D0),
            EmojiBean.fromChars(new int[]{0x1F5C3, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F5C4, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F5D1, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F512),
            EmojiBean.fromCodePoint(0x1F513),
            EmojiBean.fromCodePoint(0x1F50F),
            EmojiBean.fromCodePoint(0x1F510),
            EmojiBean.fromCodePoint(0x1F511),
            EmojiBean.fromChars(new int[]{0x1F5DD, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F528),
            EmojiBean.fromChars(new int[]{0x1F6E0, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F5E1, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F52B),
            EmojiBean.fromCodePoint(0x1F3F9),
            EmojiBean.fromChars(new int[]{0x1F6E1, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F527),
            EmojiBean.fromCodePoint(0x1F529),
            EmojiBean.fromChars(new int[]{0x1F5DC, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F517),
            EmojiBean.fromCodePoint(0x1F52C),
            EmojiBean.fromCodePoint(0x1F52D),
            EmojiBean.fromCodePoint(0x1F4E1),
            EmojiBean.fromCodePoint(0x1F489),
            EmojiBean.fromCodePoint(0x1F48A),
            EmojiBean.fromCodePoint(0x1F6AA),
            EmojiBean.fromChars(new int[]{0x1F6CF, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F6CB, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F6BD),
            EmojiBean.fromCodePoint(0x1F6BF),
            EmojiBean.fromCodePoint(0x1F6C1),
            EmojiBean.fromCodePoint(0x1F6AC),
            EmojiBean.fromCodePoint(0x1F5FF)
    };
}
