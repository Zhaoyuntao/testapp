package im.thebot.chat.api.chat.constant;

import androidx.annotation.IntDef;

/**
 * created by zhaoyuntao
 * on 11/04/2022
 * description:
 * 0：普通加入；1：二维码加入 2：share link 加入
 */
@IntDef({
        GroupMemberAddType.ADD_TYPE_NULL,
        GroupMemberAddType.ADD_TYPE_NORMAL,
        GroupMemberAddType.ADD_TYPE_QRCODE,
        GroupMemberAddType.ADD_TYPE_SHARE_LINK,
})
public @interface GroupMemberAddType {
    int ADD_TYPE_NULL = -1;
    int ADD_TYPE_NORMAL = 0;
    int ADD_TYPE_QRCODE = 1;
    int ADD_TYPE_SHARE_LINK = 2;
}
