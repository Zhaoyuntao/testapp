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
public class SymbolsCategory implements BaseEmojiCategory {
    @Override
    public String getType() {
        return StickerTypes.SymbolsCategory;
    }

    @StringRes
    @Override
    public int getDescRes() {
        return R.string.emoji_symbol;
    }

    @Override
    public int getNormalIconRes() {
        return R.drawable.emoji_symbol_in_active;
    }

    @Override
    public int getActiveIconRes() {
        return R.drawable.emoji_symbol_active;
    }

    @Override
    public EmojiBean[] getData() {
        return DATA;
    }

    public static final EmojiBean[] DATA = new EmojiBean[]{
            EmojiBean.fromCodePoint(0x1F3E7),
            EmojiBean.fromCodePoint(0x1F6AE),
            EmojiBean.fromCodePoint(0x1F6B0),
            EmojiBean.fromChar((char) 0x267F),
            EmojiBean.fromCodePoint(0x1F6B9),
            EmojiBean.fromCodePoint(0x1F6BA),
            EmojiBean.fromCodePoint(0x1F6BB),
            EmojiBean.fromCodePoint(0x1F6BC),
            EmojiBean.fromCodePoint(0x1F6BE),
            EmojiBean.fromCodePoint(0x1F6C2),
            EmojiBean.fromCodePoint(0x1F6C3),
            EmojiBean.fromCodePoint(0x1F6C4),
            EmojiBean.fromCodePoint(0x1F6C5),
            EmojiBean.fromCodePoint(0x1F6B8),
            EmojiBean.fromChar((char) 0x26D4),
            EmojiBean.fromCodePoint(0x1F6AB),
            EmojiBean.fromCodePoint(0x1F6B3),
            EmojiBean.fromCodePoint(0x1F6AD),
            EmojiBean.fromCodePoint(0x1F6AF),
            EmojiBean.fromCodePoint(0x1F6B1),
            EmojiBean.fromCodePoint(0x1F6B7),
            EmojiBean.fromCodePoint(0x1F4F5),
            EmojiBean.fromCodePoint(0x1F51E),
            EmojiBean.fromCodePoint(0x1F503),
            EmojiBean.fromCodePoint(0x1F504),
            EmojiBean.fromCodePoint(0x1F519),
            EmojiBean.fromCodePoint(0x1F51A),
            EmojiBean.fromCodePoint(0x1F51B),
            EmojiBean.fromCodePoint(0x1F51C),
            EmojiBean.fromCodePoint(0x1F51D),
            EmojiBean.fromCodePoint(0x1F6D0),
            EmojiBean.fromChars(new int[]{0x1F549, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F54E),
            EmojiBean.fromCodePoint(0x1F52F),
            EmojiBean.fromChar((char) 0x2648),
            EmojiBean.fromChar((char) 0x2649),
            EmojiBean.fromChar((char) 0x264A),
            EmojiBean.fromChar((char) 0x264B),
            EmojiBean.fromChar((char) 0x264C),
            EmojiBean.fromChar((char) 0x264D),
            EmojiBean.fromChar((char) 0x264E),
            EmojiBean.fromChar((char) 0x264F),
            EmojiBean.fromChar((char) 0x2650),
            EmojiBean.fromChar((char) 0x2651),
            EmojiBean.fromChar((char) 0x2652),
            EmojiBean.fromChar((char) 0x2653),
            EmojiBean.fromChar((char) 0x26CE),
            EmojiBean.fromCodePoint(0x1F500),
            EmojiBean.fromCodePoint(0x1F501),
            EmojiBean.fromCodePoint(0x1F502),
            EmojiBean.fromChar((char) 0x23E9),
            EmojiBean.fromChar((char) 0x23EA),
            EmojiBean.fromCodePoint(0x1F53C),
            EmojiBean.fromChar((char) 0x23EB),
            EmojiBean.fromCodePoint(0x1F53D),
            EmojiBean.fromChar((char) 0x23EC),
            EmojiBean.fromChars(new int[]{0x23F8, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x23F9, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x23FA, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F3A6),
            EmojiBean.fromCodePoint(0x1F505),
            EmojiBean.fromCodePoint(0x1F506),
            EmojiBean.fromCodePoint(0x1F4F6),
            EmojiBean.fromCodePoint(0x1F4F3),
            EmojiBean.fromCodePoint(0x1F4F4),
            EmojiBean.fromCodePoint(0x1F531),
            EmojiBean.fromCodePoint(0x1F4DB),
            EmojiBean.fromCodePoint(0x1F530),
            EmojiBean.fromChar((char) 0x2B55),
            EmojiBean.fromChar((char) 0x2705),
            EmojiBean.fromChar((char) 0x274C),
            EmojiBean.fromChar((char) 0x274E),
            EmojiBean.fromChar((char) 0x2795),
            EmojiBean.fromChar((char) 0x2796),
            EmojiBean.fromChar((char) 0x2797),
            EmojiBean.fromChar((char) 0x27B0),
            EmojiBean.fromChar((char) 0x27BF),
            EmojiBean.fromChar((char) 0x2753),
            EmojiBean.fromChar((char) 0x2754),
            EmojiBean.fromChar((char) 0x2755),
            EmojiBean.fromChar((char) 0x2757),
            EmojiBean.fromChars(new int[]{0x0023, 0x20E3}),
            EmojiBean.fromChars(new int[]{0x002A, 0x20E3}),
            EmojiBean.fromChars(new int[]{0x0030, 0x20E3}),
            EmojiBean.fromChars(new int[]{0x0031, 0x20E3}),
            EmojiBean.fromChars(new int[]{0x0032, 0x20E3}),
            EmojiBean.fromChars(new int[]{0x0033, 0x20E3}),
            EmojiBean.fromChars(new int[]{0x0034, 0x20E3}),
            EmojiBean.fromChars(new int[]{0x0035, 0x20E3}),
            EmojiBean.fromChars(new int[]{0x0036, 0x20E3}),
            EmojiBean.fromChars(new int[]{0x0037, 0x20E3}),
            EmojiBean.fromChars(new int[]{0x0038, 0x20E3}),
            EmojiBean.fromChars(new int[]{0x0039, 0x20E3}),
            EmojiBean.fromCodePoint(0x1F51F),
            EmojiBean.fromCodePoint(0x1F520),
            EmojiBean.fromCodePoint(0x1F521),
            EmojiBean.fromCodePoint(0x1F522),
            EmojiBean.fromCodePoint(0x1F523),
            EmojiBean.fromCodePoint(0x1F524),
            EmojiBean.fromCodePoint(0x1F18E),
            EmojiBean.fromCodePoint(0x1F191),
            EmojiBean.fromCodePoint(0x1F192),
            EmojiBean.fromCodePoint(0x1F193),
            EmojiBean.fromCodePoint(0x1F194),
            EmojiBean.fromCodePoint(0x1F195),
            EmojiBean.fromCodePoint(0x1F196),
            EmojiBean.fromCodePoint(0x1F197),
            EmojiBean.fromCodePoint(0x1F198),
            EmojiBean.fromCodePoint(0x1F199),
            EmojiBean.fromCodePoint(0x1F19A),
            EmojiBean.fromCodePoint(0x1F201),
            EmojiBean.fromCodePoint(0x1F236),
            EmojiBean.fromCodePoint(0x1F22F),
            EmojiBean.fromCodePoint(0x1F250),
            EmojiBean.fromCodePoint(0x1F239),
            EmojiBean.fromCodePoint(0x1F21A),
            EmojiBean.fromCodePoint(0x1F232),
            EmojiBean.fromCodePoint(0x1F251),
            EmojiBean.fromCodePoint(0x1F238),
            EmojiBean.fromCodePoint(0x1F234),
            EmojiBean.fromCodePoint(0x1F233),
            EmojiBean.fromCodePoint(0x1F23A),
            EmojiBean.fromCodePoint(0x1F235),
            EmojiBean.fromCodePoint(0x1F534),
            EmojiBean.fromCodePoint(0x1F535),
            EmojiBean.fromChar((char) 0x26AB),
            EmojiBean.fromChar((char) 0x26AA),
            EmojiBean.fromChar((char) 0x2B1B),
            EmojiBean.fromChar((char) 0x2B1C),
            EmojiBean.fromChar((char) 0x25FE),
            EmojiBean.fromChar((char) 0x25FD),
            EmojiBean.fromCodePoint(0x1F536),
            EmojiBean.fromCodePoint(0x1F537),
            EmojiBean.fromCodePoint(0x1F538),
            EmojiBean.fromCodePoint(0x1F539),
            EmojiBean.fromCodePoint(0x1F53A),
            EmojiBean.fromCodePoint(0x1F53B),
            EmojiBean.fromCodePoint(0x1F4A0),
            EmojiBean.fromCodePoint(0x1F518),
            EmojiBean.fromCodePoint(0x1F533),
            EmojiBean.fromCodePoint(0x1F532)
    };
}
