package im.thebot.chat.ui.emoji.category;

import androidx.annotation.StringRes;

import com.example.module_chat.R;

import im.thebot.chat.ui.emoji.EmojiBean;
import im.thebot.chat.ui.emoji.StickerTypes;

public class SmileysAndPeopleCategory implements BaseEmojiCategory {
    @Override
    public String getType() {
        return StickerTypes.SmileysAndPeopleCategory;
    }

    @StringRes
    @Override
    public int getDescRes() {
        return R.string.emoji_smile;
    }

    @Override
    public int getNormalIconRes() {
        return R.drawable.emoji_smile_in_active;
    }

    @Override
    public int getActiveIconRes() {
        return R.drawable.emoji_smile_active;
    }

    @Override
    public EmojiBean[] getData() {
        return DATA;
    }

    public static final EmojiBean[] DATA = new EmojiBean[]{
            EmojiBean.fromCodePoint(0x1F600),
            EmojiBean.fromCodePoint(0x1F603),
            EmojiBean.fromCodePoint(0x1F604),
            EmojiBean.fromCodePoint(0x1F601),
            EmojiBean.fromCodePoint(0x1F606),
            EmojiBean.fromCodePoint(0x1F605),
            EmojiBean.fromCodePoint(0x1F602),
            EmojiBean.fromCodePoint(0x1F642),
            EmojiBean.fromCodePoint(0x1F643),
            EmojiBean.fromCodePoint(0x1F609),
            EmojiBean.fromCodePoint(0x1F60A),
            EmojiBean.fromCodePoint(0x1F607),
            EmojiBean.fromCodePoint(0x1F60D),
            EmojiBean.fromCodePoint(0x1F618),
            EmojiBean.fromCodePoint(0x1F617),
            EmojiBean.fromChars(new int[]{0x263A, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F61A),
            EmojiBean.fromCodePoint(0x1F619),
            EmojiBean.fromCodePoint(0x1F60B),
            EmojiBean.fromCodePoint(0x1F61B),
            EmojiBean.fromCodePoint(0x1F61C),
            EmojiBean.fromCodePoint(0x1F61D),
            EmojiBean.fromCodePoint(0x1F911),
            EmojiBean.fromCodePoint(0x1F917),
            EmojiBean.fromCodePoint(0x1F914),
            EmojiBean.fromCodePoint(0x1F910),
            EmojiBean.fromCodePoint(0x1F611),
            EmojiBean.fromCodePoint(0x1F636),
            EmojiBean.fromCodePoint(0x1F60F),
            EmojiBean.fromCodePoint(0x1F612),
            EmojiBean.fromCodePoint(0x1F644),
            EmojiBean.fromCodePoint(0x1F62C),
            EmojiBean.fromCodePoint(0x1F60C),
            EmojiBean.fromCodePoint(0x1F614),
            EmojiBean.fromCodePoint(0x1F62A),
            EmojiBean.fromCodePoint(0x1F634),
            EmojiBean.fromCodePoint(0x1F637),
            EmojiBean.fromCodePoint(0x1F912),
            EmojiBean.fromCodePoint(0x1F915),
            EmojiBean.fromCodePoint(0x1F635),
            EmojiBean.fromCodePoint(0x1F60E),
            EmojiBean.fromCodePoint(0x1F913),
            EmojiBean.fromCodePoint(0x1F615),
            EmojiBean.fromCodePoint(0x1F61F),
            EmojiBean.fromCodePoint(0x1F641),
            EmojiBean.fromCodePoint(0x1F62E),
            EmojiBean.fromCodePoint(0x1F62F),
            EmojiBean.fromCodePoint(0x1F632),
            EmojiBean.fromCodePoint(0x1F633),
            EmojiBean.fromCodePoint(0x1F626),
            EmojiBean.fromCodePoint(0x1F627),
            EmojiBean.fromCodePoint(0x1F628),
            EmojiBean.fromCodePoint(0x1F630),
            EmojiBean.fromCodePoint(0x1F625),
            EmojiBean.fromCodePoint(0x1F622),
            EmojiBean.fromCodePoint(0x1F62D),
            EmojiBean.fromCodePoint(0x1F631),
            EmojiBean.fromCodePoint(0x1F616),
            EmojiBean.fromCodePoint(0x1F623),
            EmojiBean.fromCodePoint(0x1F61E),
            EmojiBean.fromCodePoint(0x1F613),
            EmojiBean.fromCodePoint(0x1F629),
            EmojiBean.fromCodePoint(0x1F62B),
            EmojiBean.fromCodePoint(0x1F624),
            EmojiBean.fromCodePoint(0x1F621),
            EmojiBean.fromCodePoint(0x1F620),
            EmojiBean.fromCodePoint(0x1F608),
            EmojiBean.fromCodePoint(0x1F47F),
            EmojiBean.fromCodePoint(0x1F480),
            EmojiBean.fromCodePoint(0x1F4A9),
            EmojiBean.fromCodePoint(0x1F44D),
            EmojiBean.fromCodePoint(0x1F44E),
            EmojiBean.fromCodePoint(0x1F44C),
            EmojiBean.fromChars(new int[]{0x270C, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F64F),
            EmojiBean.fromCodePoint(0x1F44F),
            EmojiBean.fromCodePoint(0x1F46B),
            EmojiBean.fromCodePoint(0x1F46A),
            EmojiBean.fromCodePoint(0x1F48F),
            EmojiBean.fromCodePoint(0x1F479),
            EmojiBean.fromCodePoint(0x1F47A),
            EmojiBean.fromCodePoint(0x1F47B),
            EmojiBean.fromCodePoint(0x1F47D),
            EmojiBean.fromCodePoint(0x1F47E),
            EmojiBean.fromCodePoint(0x1F916),
            EmojiBean.fromCodePoint(0x1F63A),
            EmojiBean.fromCodePoint(0x1F638),
            EmojiBean.fromCodePoint(0x1F639),
            EmojiBean.fromCodePoint(0x1F63B),
            EmojiBean.fromCodePoint(0x1F63C),
            EmojiBean.fromCodePoint(0x1F63D),
            EmojiBean.fromCodePoint(0x1F640),
            EmojiBean.fromCodePoint(0x1F63F),
            EmojiBean.fromCodePoint(0x1F63E),
            EmojiBean.fromCodePoint(0x1F648),
            EmojiBean.fromCodePoint(0x1F649),
            EmojiBean.fromCodePoint(0x1F64A),
            EmojiBean.fromCodePoint(0x1F48B),
            EmojiBean.fromCodePoint(0x1F48C),
            EmojiBean.fromCodePoint(0x1F498),
            EmojiBean.fromCodePoint(0x1F49D),
            EmojiBean.fromCodePoint(0x1F496),
            EmojiBean.fromCodePoint(0x1F497),
            EmojiBean.fromCodePoint(0x1F493),
            EmojiBean.fromCodePoint(0x1F49E),
            EmojiBean.fromCodePoint(0x1F495),
            EmojiBean.fromCodePoint(0x1F49F),
            EmojiBean.fromCodePoint(0x1F494),
            EmojiBean.fromChars(new int[]{0x2764, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F49B),
            EmojiBean.fromCodePoint(0x1F49A),
            EmojiBean.fromCodePoint(0x1F499),
            EmojiBean.fromCodePoint(0x1F49C),
            EmojiBean.fromCodePoint(0x1F4AF),
            EmojiBean.fromCodePoint(0x1F4A2),
            EmojiBean.fromCodePoint(0x1F4A5),
            EmojiBean.fromCodePoint(0x1F4AB),
            EmojiBean.fromCodePoint(0x1F4A6),
            EmojiBean.fromCodePoint(0x1F4A8),
            EmojiBean.fromCodePoint(0x1F4A3),
            EmojiBean.fromCodePoint(0x1F4AC),
            EmojiBean.fromChars(new int[]{0x1F5E8, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F5EF, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F4AD),
            EmojiBean.fromCodePoint(0x1F4A4),
            EmojiBean.fromCodePoint(0x1F440),
            EmojiBean.fromChars(new int[]{0x1F441, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F445),
            EmojiBean.fromCodePoint(0x1F444)
    };
}
