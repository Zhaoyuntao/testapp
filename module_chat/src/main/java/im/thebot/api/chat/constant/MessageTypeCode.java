package im.thebot.api.chat.constant;

import androidx.annotation.IntDef;

/**
 * created by zhaoyuntao
 * on 30/12/2021
 * description:
 */
@IntDef({
        MessageTypeCode.kChatMsgType_ALL,
        MessageTypeCode.kChatMsgType_TextDraft,
        MessageTypeCode.kChatMsgType_Text,
        MessageTypeCode.kChatMsgType_Image,
        MessageTypeCode.kChatMsgType_Audio,
        MessageTypeCode.kChatMsgType_OrigImage,
        MessageTypeCode.kChatMsgType_Webclip,
        MessageTypeCode.kChatMsgType_RTC,
        MessageTypeCode.kChatMsgType_Richmedia,
        MessageTypeCode.kChatMsgType_MutiRichmedia,
        MessageTypeCode.kChatMsgType_WrapText,
        MessageTypeCode.kChatMsgType_Location,
        MessageTypeCode.kChatMsgType_ContactCard,
        MessageTypeCode.kChatMsgType_ShortVideo,
        MessageTypeCode.kChatMsgType_File,
        MessageTypeCode.kChatMsgType_Share,
        MessageTypeCode.kChatMsgType_TextImage,
        MessageTypeCode.kChatMsgType_TextVideo,
        MessageTypeCode.kChatMsgType_CashCard,
        MessageTypeCode.kChatMsgType_CashNotify,
        MessageTypeCode.kChatMsgType_PayOfficial,
        MessageTypeCode.TYPE_MESSAGE_WITHDRAW,
        MessageTypeCode.kChatMsgType_OfficialAccount,
        MessageTypeCode.kChatMsgType_OfficialNotify,
        MessageTypeCode.kChatMsgType_Share_Card,
        MessageTypeCode.kChatMsgType_OfficialNotifyTemp,
        MessageTypeCode.kChatMsgType_StepsRanking,
        MessageTypeCode.kChatMsgType_StepsLike,
        MessageTypeCode.kChatMsgType_Clear,
        MessageTypeCode.kChatMsgType_Empty,
        MessageTypeCode.kChatMsgType_Input_Text,
        MessageTypeCode.kChatMsgType_Input_Voice,
        MessageTypeCode.kChatMsgType_Expire,
        MessageTypeCode.kChatMsgType_SysBase,
        MessageTypeCode.kChatMsgType_P2PSys_Chatfirst,
        MessageTypeCode.kChatMsgType_GroupAdd,
        MessageTypeCode.kChatMsgType_GroupRemove,
        MessageTypeCode.kChatMsgType_GroupRename,
        MessageTypeCode.kChatMsgType_GroupAvatarChange,
        MessageTypeCode.kChatMsgType_GroupLeaderChange,
        MessageTypeCode.kChatMsgType_GroupCancelAdmin,
        MessageTypeCode.kChatMsgType_GroupCreate,
        MessageTypeCode.kChatMsgType_GroupRefuse,
        MessageTypeCode.kChatMsgType_GroupVoip,
        MessageTypeCode.kChatMsgType_GroupModifyDesc,
        MessageTypeCode.kChatMsgType_SessionCreatedbySelf,
        MessageTypeCode.kChatMsgType_Group_Update_Desc,
        MessageTypeCode.TYPE_MESSAGE_MULTI_IMAGE_AND_VIDEO,
        MessageTypeCode.TYPE_MESSAGE_DEBUG_LOG,
        MessageTypeCode.TYPE_MESSAGE_DEBUG_COMMAND,
        MessageTypeCode.TYPE_MESSAGE_UNSUPPORTED,
        MessageTypeCode.kChatMsgType_System,
        MessageTypeCode.TYPE_MESSAGE_TEXT_WEB
})
public @interface MessageTypeCode {
    int kChatMsgType_ALL = -1000;
    int kChatMsgType_TextDraft = -1;
    int kChatMsgType_Text = 0;
    int kChatMsgType_Image = 1;
    int kChatMsgType_Audio = 2;
    int kChatMsgType_OrigImage = 4;
    int kChatMsgType_Webclip = 5;
    // 6,7??????????????????geo???namecard?????????????????????????????????2??????????????????
    int kChatMsgType_RTC = 8;
    int kChatMsgType_Richmedia = 9;
    int kChatMsgType_MutiRichmedia = 10;
    int kChatMsgType_WrapText = 11;
    int kChatMsgType_Location = 12;
    int kChatMsgType_ContactCard = 13;
    int kChatMsgType_ShortVideo = 14;
    int kChatMsgType_File = 15;
    int kChatMsgType_Share = 16;
    int kChatMsgType_TextImage = 17;
    int kChatMsgType_TextVideo = 18;
    int kChatMsgType_CashCard = 19;
    int kChatMsgType_CashNotify = 20;
    int kChatMsgType_PayOfficial = 21;
    int TYPE_MESSAGE_WITHDRAW = 22;
    int kChatMsgType_OfficialAccount = 23;
    @Deprecated
    int kChatMsgType_OfficialNotify = 24;
    int kChatMsgType_Share_Card = 25;
    int kChatMsgType_OfficialNotifyTemp = 26; // ?????????????????????????????????
    int kChatMsgType_StepsRanking = 43;
    int kChatMsgType_StepsLike = 44;
    //New type for ui showing, not for db saving.
    int TYPE_MESSAGE_TEXT_WEB = 50;
    // ?????????????????????clear
    int kChatMsgType_Clear = 100;
    // ???????????????session,????????????sessionList
    int kChatMsgType_Empty = 101;
    // ????????????????????????
    int kChatMsgType_Input_Text = 110;
    // ????????????????????????
    int kChatMsgType_Input_Voice = 111;
    // ???????????????????????????????????????
    int kChatMsgType_Expire = 120;
    // ???????????????500?????????
    int kChatMsgType_SysBase = 400;
    // XX?????????SOMA
    int kChatMsgType_P2PSys_Chatfirst = 401;
    // group?????????????????????
    int kChatMsgType_GroupAdd = 501;
    // group?????????????????????
    int kChatMsgType_GroupRemove = 502;
    // group???????????????
    int kChatMsgType_GroupRename = 503;
    // group????????????
    int kChatMsgType_GroupAvatarChange = 504;
    // groupadmin ????????????
    int kChatMsgType_GroupLeaderChange = 505;
    // group ????????????
    int kChatMsgType_GroupCreate = 506;
    // ??????????????????group???block?????????????????????????????????????????????
    int kChatMsgType_GroupRefuse = 507;
    // ???VOIP???????????????
    int kChatMsgType_GroupVoip = 508;
    int kChatMsgType_GroupModifyDesc = 509;
    int kChatMsgType_GroupCancelAdmin = 510;
    //????????????????????????????????????????????????session
    int kChatMsgType_SessionCreatedbySelf = 550;
    int kChatMsgType_Group_Update_Desc = 560;
    // ??????????????????
    int TYPE_MESSAGE_UNSUPPORTED = 1000;
    // ??????????????????
    int kChatMsgType_System = 1001;

    //Aggregation type usually contains multiple messages and will not be saved as a db type.
    int TYPE_MESSAGE_MULTI_IMAGE_AND_VIDEO = 2000;

    //------------ Debug type line ---------------------------------
    int TYPE_MESSAGE_DEBUG_LOG = -1001;
    int TYPE_MESSAGE_DEBUG_COMMAND = -1002;
    //------------ Debug type line ---------------------------------
    //------------ New type line ---------------------------------
    int TYPE_MESSAGE_REPLY_SNAP_SHOT = 1005;
    //------------ New type line ---------------------------------
}
