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
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_OFFICIAL;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_OFFICIAL_NOTIFY;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_RICH_MEDIA;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_SERVER_MESSAGE;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_TEXT;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_TEXT_WEB;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_UNKNOWN;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_VIDEO;
import static im.thebot.chat.ui.adapter.MessageCellCode.TYPE_CELL_WEB_CLIP;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import im.thebot.api.chat.constant.MessageTypeCode;
import im.thebot.chat.api.chat.message.MessageBeanForUI;
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
import im.thebot.chat.ui.cells.reply.ReplyAudioCell;
import im.thebot.chat.ui.cells.reply.ReplyDeletedCell;
import im.thebot.chat.ui.cells.reply.ReplyEventCell;
import im.thebot.chat.ui.cells.reply.ReplyFileCell;
import im.thebot.chat.ui.cells.reply.ReplyImageCell;
import im.thebot.chat.ui.cells.reply.ReplyNameCardCell;
import im.thebot.chat.ui.cells.reply.ReplyTextCell;
import im.thebot.chat.ui.cells.reply.ReplyUnsupportedCell;
import im.thebot.chat.ui.cells.reply.ReplyVideoCell;
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
    public static int getItemViewType(MessageBeanForUI messageBean) {
        return getItemViewTypeByMessageType(messageBean.getType());
    }

    @MessageCellCode
    private static int getItemViewTypeByMessageType(@MessageTypeCode int type) {
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
            //Video.
            case MessageTypeCode.kChatMsgType_TextVideo:
            case MessageTypeCode.kChatMsgType_ShortVideo:
                return TYPE_CELL_VIDEO;
            case MessageTypeCode.kChatMsgType_Webclip:
                return TYPE_CELL_WEB_CLIP;
            //Text.
            case MessageTypeCode.kChatMsgType_Text:
            case MessageTypeCode.kChatMsgType_WrapText:
                return TYPE_CELL_TEXT;
            case MessageTypeCode.TYPE_MESSAGE_TEXT_WEB:
                return TYPE_CELL_TEXT_WEB;
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
            case MessageTypeCode.kChatMsgType_OfficialNotifyTemp:
                return TYPE_CELL_OFFICIAL_NOTIFY;
            case MessageTypeCode.kChatMsgType_Richmedia:
                return TYPE_CELL_RICH_MEDIA;
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
                return TYPE_CELL_GROUP_EVENT;
            //Unsupported yet.
            case MessageTypeCode.kChatMsgType_Input_Text:
            case MessageTypeCode.kChatMsgType_Input_Voice:
            case MessageTypeCode.kChatMsgType_MutiRichmedia:
            case MessageTypeCode.kChatMsgType_PayOfficial:
            case MessageTypeCode.kChatMsgType_RTC:
            case MessageTypeCode.kChatMsgType_SessionCreatedbySelf:
            case MessageTypeCode.kChatMsgType_Share:
            case MessageTypeCode.kChatMsgType_Share_Card:
            case MessageTypeCode.kChatMsgType_StepsLike:
            case MessageTypeCode.kChatMsgType_StepsRanking:
            case MessageTypeCode.kChatMsgType_CashCard:
            case MessageTypeCode.kChatMsgType_CashNotify:
            case MessageTypeCode.kChatMsgType_Clear:
            case MessageTypeCode.kChatMsgType_Empty:
            case MessageTypeCode.kChatMsgType_Expire:
            case MessageTypeCode.TYPE_MESSAGE_UNSUPPORTED:
            default:
                return TYPE_CELL_UNKNOWN;
        }
    }

    /**
     * get cell view by message type
     *
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
    public static BaseReplyCell getReplyCellByViewType(Context context, @NonNull MessageBeanForUI messageReply) {
        Preconditions.checkNotNull(messageReply);
        if (messageReply.getType() == MessageTypeCode.TYPE_MESSAGE_WITHDRAW) {
            return new ReplyDeletedCell(context);
        }
        int viewType = MessageCellManager.getItemViewType(messageReply);
        switch (viewType) {
            case TYPE_CELL_FILE:
                return new ReplyFileCell(context);
            case TYPE_CELL_IMAGE:
                return new ReplyImageCell(context);
            case TYPE_CELL_TEXT:
                return new ReplyTextCell(context);
            case TYPE_CELL_CONTACT_CARD:
                return new ReplyNameCardCell(context);
            case TYPE_CELL_AUDIO:
                return new ReplyAudioCell(context);
            case TYPE_CELL_DELETED:
                return new ReplyDeletedCell(context);
            case TYPE_CELL_GROUP_EVENT:
                return new ReplyEventCell(context);
            case TYPE_CELL_VIDEO:
                return new ReplyVideoCell(context);
            case TYPE_CELL_UNKNOWN:
            default:
                return new ReplyUnsupportedCell(context);
        }
    }

    public static void addCellToContainer(BaseCell<?> baseCell, ViewGroup container) {
        container.removeAllViews();
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
