package im.thebot.chat.ui.adapter;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import im.thebot.api.chat.constant.MessageTypeCode;
import im.thebot.chat.api.chat.message.MessageBeanForUI;
import im.thebot.user.ContactUtil;
import im.turbo.basetools.time.TimeUtils;

/**
 * created by zhaoyuntao
 * on 25/04/2022
 * description:
 */
interface BaseChatAdapter {
    int messageSize();

    MessageBeanForUI getMessage(int position);

    boolean forceShowSenderDetails(MessageBeanForUI messageBean, int position);

    default int getMessageCellGravityFlag(MessageBeanForUI messageBean, int position) {
        return ContactUtil.isSelf(messageBean.getSenderUid()) ? MessageCellFlag.FLAG_GRAVITY_RIGHT : MessageCellFlag.FLAG_GRAVITY_LEFT;
    }

    default boolean needShowTopSpace(MessageBeanForUI messageBean, int position) {
        if (messageBean.isSystemEvent()) {
            return false;
        }
        if (position <= 0) {
            return true;
        } else {
            MessageBeanForUI messageBeanOnTop = getMessage(position - 1);
            if (messageBeanOnTop != null) {
                return messageBeanOnTop.isSystemEvent() || !TextUtils.equals(messageBeanOnTop.getSenderUid(), messageBean.getSenderUid());
            }
        }
        return false;
    }

    default boolean needShowBottomSpace(MessageBeanForUI messageBean, int position) {
        if (messageBean.isSystemEvent()) {
            return false;
        }
        if (position >= messageSize() - 1) {
            return false;
        } else {
            MessageBeanForUI messageBeanOnBottom = getMessage(position + 1);
            if (messageBeanOnBottom != null) {
                return messageBeanOnBottom.isSystemEvent()
                        || !TextUtils.equals(messageBeanOnBottom.getSenderUid(), messageBean.getSenderUid())
                        || !TimeUtils.isSameDay(messageBeanOnBottom.getTimeSend(), messageBean.getTimeSend())
                        || messageBeanOnBottom.getType() == MessageTypeCode.kChatMsgType_OfficialAccount;
            }
        }
        return false;
    }

    default boolean needShowForwardHead(@NonNull MessageBeanForUI messageBean, int position) {
        return messageBean.isForward();
    }

    default boolean needShowTimeTitle(final MessageBeanForUI messageBean, int position) {
        if (messageBean.getType() == MessageTypeCode.kChatMsgType_OfficialAccount
                || messageBean.getType() == MessageTypeCode.kChatMsgType_StepsRanking
                || messageBean.getType() == MessageTypeCode.kChatMsgType_StepsLike) {
            return true;
        }
        if (position == 0) {
            return true;
        } else {
            MessageBeanForUI messageBeanEarlier = getMessage(position - 1);
            if (messageBeanEarlier != null) {
                return !TimeUtils.isSameDay(messageBeanEarlier.getTimeSend(), messageBean.getTimeSend());
            } else {
                return false;
            }
        }
    }

    default boolean needShowSenderName(final MessageBeanForUI messageBean, int position, boolean needShowNewMessageLine, boolean needShowTimeTitle) {
        if (ContactUtil.isUser(messageBean.getSessionId())
                || ContactUtil.isOAAccount(messageBean.getSenderUid())
                || ContactUtil.isPayOfficial(messageBean.getSenderUid())
                || messageBean.isSelf()
                || messageBean.isSystemEvent()
                || TextUtils.isEmpty(messageBean.getSenderUid())) {
            return false;
        }
        if (needShowNewMessageLine
                || needShowTimeTitle
                || forceShowSenderDetails(messageBean, position)
                || messageBean.isDeletedMessage()
                || !messageBean.isTextMessage()
                || position <= 0) {
            return true;
        }
        MessageBeanForUI messageBeanEarlier = getMessage(position - 1);
        if (messageBeanEarlier == null) {
            return true;
        }
        if (messageBeanEarlier.isSystemEvent()) {
            return true;
        }
        if (!TextUtils.equals(messageBeanEarlier.getSenderUid(), messageBean.getSenderUid())) {
            return true;
        }
        if (!TimeUtils.isSameDay(messageBeanEarlier.getTimeSend(), messageBean.getTimeSend())) {
            return true;
        }
        return false;
    }

    default boolean needShowTail(MessageBeanForUI messageBean, int position) {
        if (messageBean.isSystemEvent()) {
            return false;
        }
        if (position == 0) {
            return true;
        }
        MessageBeanForUI messageBeanEarlier = getMessage(position - 1);
        if (messageBeanEarlier == null) {
            return true;
        }
        if (!TimeUtils.isSameDay(messageBeanEarlier.getTimeSend(), messageBean.getTimeSend())) {
            return true;
        }
        boolean isDifferentSenderWithEarlierMessage = !TextUtils.equals(messageBeanEarlier.getSenderUid(), messageBean.getSenderUid());
        if (isDifferentSenderWithEarlierMessage) {
            return true;
        }
        return messageBeanEarlier.isSystemEvent();
    }
}
