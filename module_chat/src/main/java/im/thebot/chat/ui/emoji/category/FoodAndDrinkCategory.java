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
public class FoodAndDrinkCategory implements BaseEmojiCategory {
    @Override
    public String getType() {
        return StickerTypes.FoodAndDrinkCategory;
    }

    @StringRes
    @Override
    public int getDescRes() {
        return R.string.emoji_food;
    }

    @Override
    public int getNormalIconRes() {
        return R.drawable.emoji_food_in_active;
    }

    @Override
    public int getActiveIconRes() {
        return R.drawable.emoji_food_active;
    }

    @Override
    public EmojiBean[] getData() {
        return DATA;
    }

    public static final EmojiBean[] DATA = new EmojiBean[]{
            EmojiBean.fromCodePoint(0x1F347),
            EmojiBean.fromCodePoint(0x1F348),
            EmojiBean.fromCodePoint(0x1F349),
            EmojiBean.fromCodePoint(0x1F34A),
            EmojiBean.fromCodePoint(0x1F34B),
            EmojiBean.fromCodePoint(0x1F34C),
            EmojiBean.fromCodePoint(0x1F34D),
            EmojiBean.fromCodePoint(0x1F34E),
            EmojiBean.fromCodePoint(0x1F34F),
            EmojiBean.fromCodePoint(0x1F350),
            EmojiBean.fromCodePoint(0x1F351),
            EmojiBean.fromCodePoint(0x1F352),
            EmojiBean.fromCodePoint(0x1F353),
            EmojiBean.fromCodePoint(0x1F345),
            EmojiBean.fromCodePoint(0x1F346),
            EmojiBean.fromCodePoint(0x1F33D),
            EmojiBean.fromChars(new int[]{0x1F336, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F344),
            EmojiBean.fromCodePoint(0x1F330),
            EmojiBean.fromCodePoint(0x1F35E),
            EmojiBean.fromCodePoint(0x1F9C0),
            EmojiBean.fromCodePoint(0x1F356),
            EmojiBean.fromCodePoint(0x1F357),
            EmojiBean.fromCodePoint(0x1F354),
            EmojiBean.fromCodePoint(0x1F35F),
            EmojiBean.fromCodePoint(0x1F355),
            EmojiBean.fromCodePoint(0x1F32D),
            EmojiBean.fromCodePoint(0x1F32E),
            EmojiBean.fromCodePoint(0x1F32F),
            EmojiBean.fromCodePoint(0x1F373),
            EmojiBean.fromCodePoint(0x1F372),
            EmojiBean.fromCodePoint(0x1F37F),
            EmojiBean.fromCodePoint(0x1F371),
            EmojiBean.fromCodePoint(0x1F358),
            EmojiBean.fromCodePoint(0x1F359),
            EmojiBean.fromCodePoint(0x1F35A),
            EmojiBean.fromCodePoint(0x1F35B),
            EmojiBean.fromCodePoint(0x1F35C),
            EmojiBean.fromCodePoint(0x1F35D),
            EmojiBean.fromCodePoint(0x1F360),
            EmojiBean.fromCodePoint(0x1F362),
            EmojiBean.fromCodePoint(0x1F363),
            EmojiBean.fromCodePoint(0x1F364),
            EmojiBean.fromCodePoint(0x1F365),
            EmojiBean.fromCodePoint(0x1F361),
            EmojiBean.fromCodePoint(0x1F980),
            EmojiBean.fromCodePoint(0x1F366),
            EmojiBean.fromCodePoint(0x1F367),
            EmojiBean.fromCodePoint(0x1F368),
            EmojiBean.fromCodePoint(0x1F369),
            EmojiBean.fromCodePoint(0x1F36A),
            EmojiBean.fromCodePoint(0x1F382),
            EmojiBean.fromCodePoint(0x1F370),
            EmojiBean.fromCodePoint(0x1F36B),
            EmojiBean.fromCodePoint(0x1F36C),
            EmojiBean.fromCodePoint(0x1F36D),
            EmojiBean.fromCodePoint(0x1F36E),
            EmojiBean.fromCodePoint(0x1F36F),
            EmojiBean.fromCodePoint(0x1F37C),
            EmojiBean.fromCodePoint(0x1F375),
            EmojiBean.fromCodePoint(0x1F376),
            EmojiBean.fromCodePoint(0x1F37E),
            EmojiBean.fromCodePoint(0x1F377),
            EmojiBean.fromCodePoint(0x1F378),
            EmojiBean.fromCodePoint(0x1F379),
            EmojiBean.fromCodePoint(0x1F37A),
            EmojiBean.fromCodePoint(0x1F37B),
            EmojiBean.fromChars(new int[]{0x1F37D, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F374),
            EmojiBean.fromCodePoint(0x1F52A),
            EmojiBean.fromCodePoint(0x1F3FA)
    };
}
