package com.sdk.chat.helper;


import im.thebot.api.chat.constant.MessageTypeCode;


/**
 * created by zhaoyuntao
 * on 17/08/2021
 * description:
 */
public class MessageTypeStringHelper {

    public static String toString(@MessageTypeCode int messageType) {
        switch (messageType) {
            case MessageTypeCode.TYPE_MESSAGE_WITHDRAW:
                return "DELETED";
            case MessageTypeCode.TYPE_MESSAGE_DEBUG_LOG:
                return "DEBUG_LOG";
            case MessageTypeCode.TYPE_MESSAGE_DEBUG_COMMAND:
                return "DEBUG_COMMAND";
            case MessageTypeCode.TYPE_MESSAGE_UNSUPPORTED:
                return "UNKNOWN";
            case MessageTypeCode.kChatMsgType_ALL:
                return "ALL";
            case MessageTypeCode.kChatMsgType_Audio:
                return "AUDIO";
            case MessageTypeCode.kChatMsgType_CashCard:
                return "CASH_CARD";
            case MessageTypeCode.kChatMsgType_CashNotify:
                return "CASH_NOTIFY";
            case MessageTypeCode.kChatMsgType_Clear:
                return "CLEAR";
            case MessageTypeCode.kChatMsgType_ContactCard:
                return "CONTACT_CARD";
            case MessageTypeCode.kChatMsgType_Empty:
                return "EMPTY";
            case MessageTypeCode.kChatMsgType_Expire:
                return "EXPIRE";
            case MessageTypeCode.kChatMsgType_File:
                return "FILE";
            case MessageTypeCode.kChatMsgType_GroupAdd:
                return "GROUP_ADD";
            case MessageTypeCode.kChatMsgType_GroupAvatarChange:
                return "GROUP_AVATAR_CHANGE";
            case MessageTypeCode.kChatMsgType_GroupCreate:
                return "GROUP_CREATE";
            case MessageTypeCode.kChatMsgType_GroupLeaderChange:
                return "GROUP_MANAGER_CHANGE";
            case MessageTypeCode.kChatMsgType_GroupCancelAdmin:
                return "GROUP_MANAGER_REMOVE";
            case MessageTypeCode.kChatMsgType_GroupModifyDesc:
                return "GROUP_MODIFY_CHANGE";
            case MessageTypeCode.kChatMsgType_GroupRefuse:
                return "GROUP_REFUSE";
            case MessageTypeCode.kChatMsgType_GroupRemove:
                return "GROUP_REMOVE";
            case MessageTypeCode.kChatMsgType_GroupRename:
                return "GROUP_RENAME";
            case MessageTypeCode.kChatMsgType_GroupVoip:
                return "GROUP_VOIP";
            case MessageTypeCode.kChatMsgType_Image:
                return "IMAGE";
            case MessageTypeCode.kChatMsgType_Input_Text:
                return "INPUT_TEXT";
            case MessageTypeCode.kChatMsgType_Input_Voice:
                return "INPUT_VOICE";
            case MessageTypeCode.kChatMsgType_Location:
                return "LOCATION";
            case MessageTypeCode.kChatMsgType_MutiRichmedia:
                return "MULTI_RICH_MEDIA";
            case MessageTypeCode.kChatMsgType_OfficialAccount:
                return "OA";
            case MessageTypeCode.kChatMsgType_OfficialNotify:
                return "OA_NOTIFY";
            case MessageTypeCode.kChatMsgType_OfficialNotifyTemp:
                return "OA_NOTIFY_TEMP";
            case MessageTypeCode.kChatMsgType_OrigImage:
                return "IMAGE_ORIGIN";
            case MessageTypeCode.kChatMsgType_P2PSys_Chatfirst:
                return "P2P_SYS_CHAT_FIRST";
            case MessageTypeCode.kChatMsgType_PayOfficial:
                return "PAY_OFFICIAL";
            case MessageTypeCode.kChatMsgType_RTC:
                return "RTC";
            case MessageTypeCode.kChatMsgType_Richmedia:
                return "RICH_MEDIA";
            case MessageTypeCode.kChatMsgType_SessionCreatedbySelf:
                return "SESSION_CREATED_BY_SELF";
            case MessageTypeCode.kChatMsgType_Share:
                return "SHARE";
            case MessageTypeCode.kChatMsgType_Share_Card:
                return "SHARE_CARD";
            case MessageTypeCode.kChatMsgType_ShortVideo:
                return "SHORT_VIDEO";
            case MessageTypeCode.kChatMsgType_StepsLike:
                return "STEPS_LIKE";
            case MessageTypeCode.kChatMsgType_StepsRanking:
                return "STEPS_RANKING";
            case MessageTypeCode.kChatMsgType_SysBase:
                return "SYS_BASE";
            case MessageTypeCode.kChatMsgType_System:
                return "SYSTEM";
            case MessageTypeCode.kChatMsgType_Text:
                return "TEXT";
            case MessageTypeCode.kChatMsgType_TextDraft:
                return "TEXT_DRAFT";
            case MessageTypeCode.kChatMsgType_TextImage:
                return "TEXT_IMAGE";
            case MessageTypeCode.kChatMsgType_TextVideo:
                return "TEXT_VIDEO";
            case MessageTypeCode.kChatMsgType_Webclip:
                return "WEB_CLIP";
            case MessageTypeCode.kChatMsgType_WrapText:
                return "WRAP_TEXT";
            case MessageTypeCode.TYPE_MESSAGE_MULTI_IMAGE_AND_VIDEO:
                return "MULTI_IMAGE_AND_VIDEO";
            default:
                return "UNKNOWN-" + messageType;
        }
    }
}
