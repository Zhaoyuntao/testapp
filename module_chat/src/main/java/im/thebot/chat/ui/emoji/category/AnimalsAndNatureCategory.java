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
public class AnimalsAndNatureCategory implements BaseEmojiCategory {
    @Override
    public String getType() {
        return StickerTypes.AnimalsAndNatureCategory;
    }

    @StringRes
    @Override
    public int getDescRes() {
        return R.string.emoji_animal;
    }

    @Override
    public int getNormalIconRes() {
        return R.drawable.emoji_animal_in_active;
    }

    @Override
    public int getActiveIconRes() {
        return R.drawable.emoji_animal_active;
    }

    @Override
    public EmojiBean[] getData() {
        return DATA;
    }
    public static final EmojiBean[] DATA = new EmojiBean[]{
            EmojiBean.fromCodePoint(0x1F435),
            EmojiBean.fromCodePoint(0x1F412),
            EmojiBean.fromCodePoint(0x1F436),
            EmojiBean.fromCodePoint(0x1F415),
            EmojiBean.fromCodePoint(0x1F429),
            EmojiBean.fromCodePoint(0x1F43A),
            EmojiBean.fromCodePoint(0x1F431),
            EmojiBean.fromCodePoint(0x1F408),
            EmojiBean.fromCodePoint(0x1F981),
            EmojiBean.fromCodePoint(0x1F42F),
            EmojiBean.fromCodePoint(0x1F405),
            EmojiBean.fromCodePoint(0x1F406),
            EmojiBean.fromCodePoint(0x1F434),
            EmojiBean.fromCodePoint(0x1F40E),
            EmojiBean.fromCodePoint(0x1F984),
            EmojiBean.fromCodePoint(0x1F42E),
            EmojiBean.fromCodePoint(0x1F402),
            EmojiBean.fromCodePoint(0x1F403),
            EmojiBean.fromCodePoint(0x1F404),
//            StickerBean.fromCodePoint(0x1F437),
//            StickerBean.fromCodePoint(0x1F416),
//            StickerBean.fromCodePoint(0x1F417),
//            StickerBean.fromCodePoint(0x1F43D),
            EmojiBean.fromCodePoint(0x1F40F),
            EmojiBean.fromCodePoint(0x1F411),
            EmojiBean.fromCodePoint(0x1F410),
            EmojiBean.fromCodePoint(0x1F42A),
            EmojiBean.fromCodePoint(0x1F42B),
            EmojiBean.fromCodePoint(0x1F418),
            EmojiBean.fromCodePoint(0x1F42D),
            EmojiBean.fromCodePoint(0x1F401),
            EmojiBean.fromCodePoint(0x1F400),
            EmojiBean.fromCodePoint(0x1F439),
            EmojiBean.fromCodePoint(0x1F430),
            EmojiBean.fromCodePoint(0x1F407),
            EmojiBean.fromChars(new int[]{0x1F43F, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F43B),
            EmojiBean.fromCodePoint(0x1F428),
            EmojiBean.fromCodePoint(0x1F43C),
            EmojiBean.fromCodePoint(0x1F43E),
            EmojiBean.fromCodePoint(0x1F983),
            EmojiBean.fromCodePoint(0x1F414),
            EmojiBean.fromCodePoint(0x1F413),
            EmojiBean.fromCodePoint(0x1F423),
            EmojiBean.fromCodePoint(0x1F424),
            EmojiBean.fromCodePoint(0x1F425),
            EmojiBean.fromCodePoint(0x1F426),
            EmojiBean.fromCodePoint(0x1F427),
            EmojiBean.fromChars(new int[]{0x1F54A, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F438),
            EmojiBean.fromCodePoint(0x1F40A),
            EmojiBean.fromCodePoint(0x1F422),
            EmojiBean.fromCodePoint(0x1F40D),
            EmojiBean.fromCodePoint(0x1F432),
            EmojiBean.fromCodePoint(0x1F409),
            EmojiBean.fromCodePoint(0x1F433),
            EmojiBean.fromCodePoint(0x1F40B),
            EmojiBean.fromCodePoint(0x1F42C),
            EmojiBean.fromCodePoint(0x1F41F),
            EmojiBean.fromCodePoint(0x1F420),
            EmojiBean.fromCodePoint(0x1F421),
            EmojiBean.fromCodePoint(0x1F419),
            EmojiBean.fromCodePoint(0x1F41A),
            EmojiBean.fromCodePoint(0x1F40C),
            EmojiBean.fromCodePoint(0x1F41B),
            EmojiBean.fromCodePoint(0x1F41C),
            EmojiBean.fromCodePoint(0x1F41D),
            EmojiBean.fromCodePoint(0x1F41E),
            EmojiBean.fromChars(new int[]{0x1F577, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F578, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F982),
            EmojiBean.fromCodePoint(0x1F490),
            EmojiBean.fromCodePoint(0x1F338),
            EmojiBean.fromCodePoint(0x1F4AE),
            EmojiBean.fromChars(new int[]{0x1F3F5, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F339),
            EmojiBean.fromCodePoint(0x1F33A),
            EmojiBean.fromCodePoint(0x1F33B),
            EmojiBean.fromCodePoint(0x1F33C),
            EmojiBean.fromCodePoint(0x1F337),
            EmojiBean.fromCodePoint(0x1F331),
            EmojiBean.fromCodePoint(0x1F332),
            EmojiBean.fromCodePoint(0x1F333),
            EmojiBean.fromCodePoint(0x1F334),
            EmojiBean.fromCodePoint(0x1F335),
            EmojiBean.fromCodePoint(0x1F33E),
            EmojiBean.fromCodePoint(0x1F33F),
            EmojiBean.fromCodePoint(0x1F340),
            EmojiBean.fromCodePoint(0x1F341),
            EmojiBean.fromCodePoint(0x1F342),
            EmojiBean.fromCodePoint(0x1F343)
    };
}
