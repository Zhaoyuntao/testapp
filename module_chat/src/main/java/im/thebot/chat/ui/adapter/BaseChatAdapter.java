package im.thebot.chat.ui.adapter;

import android.text.TextUtils;

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

    boolean showReplySourceMessagePart();

    default boolean needShowTopSpace(MessageBeanForUI messageBean, int position) {
        if (messageBean.isSystemEvent()) {
            return false;
        }
        if (position <= 0) {
            return true;
        } else {
            MessageBeanForUI messageBeanOnTop = getMessage(position - 1);
            boolean isSameSender = messageBeanOnTop != null
                    && TextUtils.equals(messageBeanOnTop.getSenderUid(), messageBean.getSenderUid());
            return !isSameSender;
        }
    }

    default boolean needShowTimeTitle(final MessageBeanForUI messageBean, int position) {
        if (position == 0 || messageBean.getType() == MessageTypeCode.kChatMsgType_OfficialAccount) {
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
