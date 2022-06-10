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
public class TravelAndPlacesCategory implements BaseEmojiCategory {
    @Override
    public String getType() {
        return StickerTypes.TravelAndPlacesCategory;
    }

    @StringRes
    @Override
    public int getDescRes() {
        return R.string.emoji_travel;
    }

    @Override
    public int getNormalIconRes() {
        return R.drawable.emoji_travel_in_active;
    }

    @Override
    public int getActiveIconRes() {
        return R.drawable.emoji_travel_active;
    }

    @Override
    public EmojiBean[] getData() {
        return DATA;
    }

    public static final EmojiBean[] DATA = new EmojiBean[]{
            EmojiBean.fromCodePoint(0x1F30D),
            EmojiBean.fromCodePoint(0x1F30E),
            EmojiBean.fromCodePoint(0x1F30F),
            EmojiBean.fromCodePoint(0x1F310),
            EmojiBean.fromChars(new int[]{0x1F5FA, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F5FE),
            EmojiBean.fromChars(new int[]{0x1F3D4, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F30B),
            EmojiBean.fromCodePoint(0x1F5FB),
            EmojiBean.fromChars(new int[]{0x1F3D5, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F3D6, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F3DC, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F3DD, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F3DE, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F3DF, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F3DB, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F3D7, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F3D8, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F3DA, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F3E0),
            EmojiBean.fromCodePoint(0x1F3E1),
            EmojiBean.fromCodePoint(0x1F3E2),
            EmojiBean.fromCodePoint(0x1F3E3),
            EmojiBean.fromCodePoint(0x1F3E4),
            EmojiBean.fromCodePoint(0x1F3E5),
            EmojiBean.fromCodePoint(0x1F3E6),
            EmojiBean.fromCodePoint(0x1F3E8),
            EmojiBean.fromCodePoint(0x1F3E9),
            EmojiBean.fromCodePoint(0x1F3EA),
            EmojiBean.fromCodePoint(0x1F3EB),
            EmojiBean.fromCodePoint(0x1F3EC),
            EmojiBean.fromCodePoint(0x1F3ED),
            EmojiBean.fromCodePoint(0x1F3EF),
            EmojiBean.fromCodePoint(0x1F3F0),
            EmojiBean.fromCodePoint(0x1F492),
            EmojiBean.fromCodePoint(0x1F5FC),
            EmojiBean.fromCodePoint(0x1F5FD),
            EmojiBean.fromChar((char) 0x26EA),
            EmojiBean.fromCodePoint(0x1F54C),
            EmojiBean.fromCodePoint(0x1F54D),
            EmojiBean.fromCodePoint(0x1F54B),
            EmojiBean.fromChar((char) 0x26F2),
            EmojiBean.fromChar((char) 0x26FA),
            EmojiBean.fromCodePoint(0x1F301),
            EmojiBean.fromCodePoint(0x1F303),
            EmojiBean.fromChars(new int[]{0x1F3D9, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F304),
            EmojiBean.fromCodePoint(0x1F305),
            EmojiBean.fromCodePoint(0x1F306),
            EmojiBean.fromCodePoint(0x1F307),
            EmojiBean.fromCodePoint(0x1F309),
            EmojiBean.fromCodePoint(0x1F3A0),
            EmojiBean.fromCodePoint(0x1F3A1),
            EmojiBean.fromCodePoint(0x1F3A2),
            EmojiBean.fromCodePoint(0x1F488),
            EmojiBean.fromCodePoint(0x1F3AA),
            EmojiBean.fromCodePoint(0x1F682),
            EmojiBean.fromCodePoint(0x1F683),
            EmojiBean.fromCodePoint(0x1F684),
            EmojiBean.fromCodePoint(0x1F685),
            EmojiBean.fromCodePoint(0x1F686),
            EmojiBean.fromCodePoint(0x1F687),
            EmojiBean.fromCodePoint(0x1F688),
            EmojiBean.fromCodePoint(0x1F689),
            EmojiBean.fromCodePoint(0x1F68A),
            EmojiBean.fromCodePoint(0x1F69D),
            EmojiBean.fromCodePoint(0x1F69E),
            EmojiBean.fromCodePoint(0x1F68B),
            EmojiBean.fromCodePoint(0x1F68C),
            EmojiBean.fromCodePoint(0x1F68D),
            EmojiBean.fromCodePoint(0x1F68E),
            EmojiBean.fromCodePoint(0x1F690),
            EmojiBean.fromCodePoint(0x1F691),
            EmojiBean.fromCodePoint(0x1F692),
            EmojiBean.fromCodePoint(0x1F693),
            EmojiBean.fromCodePoint(0x1F694),
            EmojiBean.fromCodePoint(0x1F695),
            EmojiBean.fromCodePoint(0x1F696),
            EmojiBean.fromCodePoint(0x1F697),
            EmojiBean.fromCodePoint(0x1F698),
            EmojiBean.fromCodePoint(0x1F699),
            EmojiBean.fromCodePoint(0x1F69A),
            EmojiBean.fromCodePoint(0x1F69B),
            EmojiBean.fromCodePoint(0x1F69C),
            EmojiBean.fromChars(new int[]{0x1F3CE, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F3CD, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F6B2),
            EmojiBean.fromCodePoint(0x1F68F),
            EmojiBean.fromChars(new int[]{0x1F6E3, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F6E4, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F6E2, 0xFE0F}),
            EmojiBean.fromChar((char) 0x26FD),
            EmojiBean.fromCodePoint(0x1F6A8),
            EmojiBean.fromCodePoint(0x1F6A5),
            EmojiBean.fromCodePoint(0x1F6A6),
            EmojiBean.fromCodePoint(0x1F6A7),
            EmojiBean.fromChar((char) 0x2693),
            EmojiBean.fromChar((char) 0x26F5),
            EmojiBean.fromCodePoint(0x1F6A4),
            EmojiBean.fromChars(new int[]{0x1F6F3, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x26F4, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F6E5, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F6A2),
            EmojiBean.fromChars(new int[]{0x1F6E9, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F6EB),
            EmojiBean.fromCodePoint(0x1F6EC),
            EmojiBean.fromCodePoint(0x1F4BA),
            EmojiBean.fromCodePoint(0x1F681),
            EmojiBean.fromCodePoint(0x1F69F),
            EmojiBean.fromCodePoint(0x1F6A0),
            EmojiBean.fromCodePoint(0x1F6A1),
            EmojiBean.fromChars(new int[]{0x1F6F0, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F680),
            EmojiBean.fromChars(new int[]{0x1F6CE, 0xFE0F}),
            EmojiBean.fromChar((char) 0x231B),
            EmojiBean.fromChar((char) 0x23F3),
            EmojiBean.fromChar((char) 0x231A),
            EmojiBean.fromChar((char) 0x23F0),
            EmojiBean.fromCodePoint(0x1F55B),
            EmojiBean.fromCodePoint(0x1F567),
            EmojiBean.fromCodePoint(0x1F550),
            EmojiBean.fromCodePoint(0x1F55C),
            EmojiBean.fromCodePoint(0x1F551),
            EmojiBean.fromCodePoint(0x1F55D),
            EmojiBean.fromCodePoint(0x1F552),
            EmojiBean.fromCodePoint(0x1F55E),
            EmojiBean.fromCodePoint(0x1F553),
            EmojiBean.fromCodePoint(0x1F55F),
            EmojiBean.fromCodePoint(0x1F554),
            EmojiBean.fromCodePoint(0x1F560),
            EmojiBean.fromCodePoint(0x1F555),
            EmojiBean.fromCodePoint(0x1F561),
            EmojiBean.fromCodePoint(0x1F556),
            EmojiBean.fromCodePoint(0x1F562),
            EmojiBean.fromCodePoint(0x1F557),
            EmojiBean.fromCodePoint(0x1F563),
            EmojiBean.fromCodePoint(0x1F558),
            EmojiBean.fromCodePoint(0x1F564),
            EmojiBean.fromCodePoint(0x1F559),
            EmojiBean.fromCodePoint(0x1F565),
            EmojiBean.fromCodePoint(0x1F55A),
            EmojiBean.fromCodePoint(0x1F566),
            EmojiBean.fromCodePoint(0x1F311),
            EmojiBean.fromCodePoint(0x1F312),
            EmojiBean.fromCodePoint(0x1F313),
            EmojiBean.fromCodePoint(0x1F314),
            EmojiBean.fromCodePoint(0x1F315),
            EmojiBean.fromCodePoint(0x1F316),
            EmojiBean.fromCodePoint(0x1F317),
            EmojiBean.fromCodePoint(0x1F318),
            EmojiBean.fromCodePoint(0x1F319),
            EmojiBean.fromCodePoint(0x1F31A),
            EmojiBean.fromCodePoint(0x1F31B),
            EmojiBean.fromCodePoint(0x1F31C),
            EmojiBean.fromChars(new int[]{0x1F321, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x2600, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F31D),
            EmojiBean.fromCodePoint(0x1F31E),
            EmojiBean.fromChar((char) 0x2B50),
            EmojiBean.fromCodePoint(0x1F31F),
            EmojiBean.fromCodePoint(0x1F320),
            EmojiBean.fromCodePoint(0x1F30C),
            EmojiBean.fromChars(new int[]{0x2601, 0xFE0F}),
            EmojiBean.fromChar((char) 0x26C5),
            EmojiBean.fromChars(new int[]{0x1F324, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F325, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F326, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F327, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F328, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F329, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F32A, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F32B, 0xFE0F}),
            EmojiBean.fromChars(new int[]{0x1F32C, 0xFE0F}),
            EmojiBean.fromCodePoint(0x1F300),
            EmojiBean.fromCodePoint(0x1F308),
            EmojiBean.fromCodePoint(0x1F302),
            EmojiBean.fromChar((char) 0x2614),
            EmojiBean.fromChar((char) 0x26A1),
            EmojiBean.fromChars(new int[]{0x2744, 0xFE0F}),
            EmojiBean.fromChar((char) 0x26C4),
            EmojiBean.fromCodePoint(0x1F525),
            EmojiBean.fromCodePoint(0x1F4A7),
            EmojiBean.fromCodePoint(0x1F30A)
    };
}

