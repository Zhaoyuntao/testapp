package im.thebot.chat.api.chat.message.base.sharecardtype;

import androidx.annotation.StringDef;

/**
 * created by zhaoyuntao
 * on 07/04/2022
 * description:
 */
@StringDef({
        ShareCardType.SHARE_TYPE_IMAGE_LEFT,
        ShareCardType.SHARE_TYPE_IMAGE_RIGHT,
        ShareCardType.SHARE_TYPE_IMAGE_LARGE,
        ShareCardType.SHARE_TYPE_VIDEO,
        ShareCardType.SHARE_TYPE_AUDIO,
})
public @interface ShareCardType {
    String SHARE_TYPE_IMAGE_LEFT = "1";
    String SHARE_TYPE_IMAGE_RIGHT = "4";
    String SHARE_TYPE_IMAGE_LARGE = "5";
    String SHARE_TYPE_VIDEO = "2";
    String SHARE_TYPE_AUDIO = "3";
}
