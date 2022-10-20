package im.thebot.chat.ui.adapter;

import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_AUDIO;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_CONTACT_CARD;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_DEBUG_COMMAND;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_DEBUG_LOG;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_DELETED;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_FILE;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_GROUP_CALL;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_GROUP_EVENT;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_IMAGE;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_LOCATION;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_MICROPROGRAM_SHARE_CARD;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_MULTI_MEDIA;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_OFFICIAL;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_OFFICIAL_NOTIFY_OLD;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_P2P_CALL;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_PAYMENT_CASH_NOTIFY;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_PAYMENT_MESSAGE;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_RICH_MEDIA;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_RICH_MEDIAS;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_SERVER_MESSAGE;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_STEPS_LIKE;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_STEPS_RANKING;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_TEXT;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_TEXT_WEB;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_UNKNOWN;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_VIDEO;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_WEB_CLIP;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import im.thebot.api.chat.constant.MessageTypeCode;
import im.thebot.chat.api.chat.message.MessageBeanForUI;
import im.thebot.chat.api.chat.message.TextWebMessageForUI;
import im.thebot.chat.ui.cells.origin.DebugCommandCell;
import im.thebot.chat.ui.cells.origin.DebugLogCell;
import im.thebot.chat.ui.cells.origin.LocationCell;
import im.thebot.chat.ui.cells.origin.SystemEventCell;
import im.thebot.chat.ui.cells.origin.TextCell;
import im.thebot.chat.ui.cells.origin.UnsupportedCell;
import im.thebot.chat.ui.cells.origin.WithdrawCell;
import im.thebot.chat.ui.cells.origin.base.BaseCell;
import im.thebot.chat.ui.cells.origin.base.BaseMessageCell;
import im.thebot.chat.ui.cells.origin.base.BaseReplyCell;
import im.thebot.chat.ui.cells.origin.file.AudioCell;
import im.thebot.chat.ui.cells.origin.file.FileCell;
import im.thebot.chat.ui.cells.origin.file.ImageCell;
import im.thebot.chat.ui.cells.origin.file.VideoCell;
import im.thebot.chat.ui.cells.reply.ReplyTextCell;
import im.turbo.basetools.preconditions.Preconditions;

/**
 * created by zhaoyuntao
 * on 2020/7/7
 * description:
 */
public class MessageCellManager {

    /**
     * Get view type by message type.
     */
    @MessageCellCode
    public static int getItemViewType(@NonNull MessageBeanForUI messageBean) {
        int type = messageBean.getType();
        switch (type) {
            case MessageTypeCode.TYPE_MESSAGE_WITHDRAW:
                return TYPE_CELL_DELETED;
            case MessageTypeCode.TYPE_MESSAGE_DEBUG_LOG:
                return TYPE_CELL_DEBUG_LOG;
            case MessageTypeCode.TYPE_MESSAGE_DEBUG_COMMAND:
                return TYPE_CELL_DEBUG_COMMAND;
            case MessageTypeCode.kChatMsgType_File:
                return TYPE_CELL_FILE;
            //Image.
            case MessageTypeCode.kChatMsgType_Image:
            case MessageTypeCode.kChatMsgType_TextImage:
            case MessageTypeCode.kChatMsgType_OrigImage:
                return TYPE_CELL_IMAGE;
            case MessageTypeCode.TYPE_MESSAGE_MULTI_IMAGE_AND_VIDEO:
                return TYPE_CELL_MULTI_MEDIA;
            //Video.
            case MessageTypeCode.kChatMsgType_TextVideo:
            case MessageTypeCode.kChatMsgType_ShortVideo:
                return TYPE_CELL_VIDEO;
            case MessageTypeCode.kChatMsgType_Webclip:
                return TYPE_CELL_WEB_CLIP;
            //Text.
            case MessageTypeCode.kChatMsgType_Text:
            case MessageTypeCode.kChatMsgType_WrapText:
                if (messageBean instanceof TextWebMessageForUI) {
                    return TYPE_CELL_TEXT_WEB;
                } else {
                    return TYPE_CELL_TEXT;
                }
            case MessageTypeCode.kChatMsgType_ContactCard:
                return TYPE_CELL_CONTACT_CARD;
            case MessageTypeCode.kChatMsgType_Audio:
                return TYPE_CELL_AUDIO;
            case MessageTypeCode.kChatMsgType_GroupVoip:
                return TYPE_CELL_GROUP_CALL;
            case MessageTypeCode.kChatMsgType_System:
                return TYPE_CELL_SERVER_MESSAGE;
            case MessageTypeCode.kChatMsgType_Location:
                return TYPE_CELL_LOCATION;
            case MessageTypeCode.kChatMsgType_OfficialAccount:
                return TYPE_CELL_OFFICIAL;
            case MessageTypeCode.kChatMsgType_OfficialNotify:
                return TYPE_CELL_OFFICIAL_NOTIFY_OLD;
            case MessageTypeCode.kChatMsgType_Richmedia:
                return TYPE_CELL_RICH_MEDIA;
            case MessageTypeCode.kChatMsgType_MutiRichmedia:
                return TYPE_CELL_RICH_MEDIAS;
            //Group event.
            case MessageTypeCode.kChatMsgType_GroupAdd:
            case MessageTypeCode.kChatMsgType_GroupAvatarChange:
            case MessageTypeCode.kChatMsgType_GroupCancelAdmin:
            case MessageTypeCode.kChatMsgType_GroupCreate:
            case MessageTypeCode.kChatMsgType_GroupLeaderChange:
            case MessageTypeCode.kChatMsgType_GroupModifyDesc:
            case MessageTypeCode.kChatMsgType_GroupRefuse:
            case MessageTypeCode.kChatMsgType_GroupRemove:
            case MessageTypeCode.kChatMsgType_GroupRename:
            case MessageTypeCode.kChatMsgType_P2PSys_Chatfirst:
            case MessageTypeCode.kChatMsgType_Group_Update_Desc:
                return TYPE_CELL_GROUP_EVENT;
            case MessageTypeCode.kChatMsgType_RTC:
                return TYPE_CELL_P2P_CALL;
            case MessageTypeCode.kChatMsgType_Share:
                return TYPE_CELL_MICROPROGRAM_SHARE_CARD;
            case MessageTypeCode.kChatMsgType_StepsLike:
                return TYPE_CELL_STEPS_LIKE;
            case MessageTypeCode.kChatMsgType_StepsRanking:
                return TYPE_CELL_STEPS_RANKING;
            case MessageTypeCode.kChatMsgType_CashNotify:
                return TYPE_CELL_PAYMENT_CASH_NOTIFY;
            case MessageTypeCode.kChatMsgType_PayOfficial:
                return TYPE_CELL_PAYMENT_MESSAGE;
            //Unsupported or not ui type.
            case MessageTypeCode.kChatMsgType_Input_Text:
            case MessageTypeCode.kChatMsgType_Input_Voice:
            case MessageTypeCode.kChatMsgType_Clear:
            case MessageTypeCode.kChatMsgType_Empty:
            case MessageTypeCode.kChatMsgType_Expire:
            case MessageTypeCode.TYPE_MESSAGE_UNSUPPORTED:
            case MessageTypeCode.kChatMsgType_TextDraft:
            case MessageTypeCode.kChatMsgType_ALL:
            default:
                return TYPE_CELL_UNKNOWN;
        }
    }

    /**
     * get cell view by message type
     *
     * @param
     * @param viewType
     * @return
     */
    public static BaseMessageCell<?> getChatCellByViewType(@MessageCellCode int viewType) {
        switch (viewType) {
            case TYPE_CELL_DELETED:
                return new WithdrawCell();
            case TYPE_CELL_DEBUG_LOG:
                return new DebugLogCell();
            case TYPE_CELL_DEBUG_COMMAND:
                return new DebugCommandCell();
            case TYPE_CELL_GROUP_EVENT:
                return new SystemEventCell();
            case TYPE_CELL_FILE:
                return new FileCell();
            case TYPE_CELL_IMAGE:
                return new ImageCell();
            case TYPE_CELL_VIDEO:
                return new VideoCell();
            case TYPE_CELL_TEXT:
                return new TextCell();
            case TYPE_CELL_AUDIO:
                return new AudioCell();
            case TYPE_CELL_LOCATION:
                return new LocationCell();
            case TYPE_CELL_UNKNOWN:
            default:
                return new UnsupportedCell();
        }
    }

    /**
     * get reply cell view by message type
     */
    @SuppressWarnings("rawtypes")
    public static BaseReplyCell getReplyCellByViewType(@NonNull MessageBeanForUI messageReply) {
        Preconditions.checkNotNull(messageReply);
        return new ReplyTextCell();
    }

    public static void addCellToContainer(BaseCell<?> baseCell, ViewGroup container) {
        View view = LayoutInflater.from(container.getContext()).inflate(baseCell.setLayout(), container, false);
        baseCell.setRootView(view);
        container.addView(view);
    }

    public static int getItemViewTypeFromOriginType(int originType) {
        return originType & MessageCellFlag.FLAG_MASK_MESSAGE_VALUE;
    }

    public static int getFlagsFromOriginType(int originType) {
        return originType & MessageCellFlag.FLAG_MASK_FLAG_VALUE;
    }

    public static boolean containsFlagInItemViewType(int viewType, int flag) {
        return (viewType & flag) == flag;
    }

    public static int addFlagToItemViewType(int viewType, int flag) {
        return viewType | flag;
    }
}
