package im.thebot.chat.api.chat.message.event;

import im.thebot.api.chat.constant.MessageTypeCode;

/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public class GroupChangeAvatarMessageForUI extends BaseEventMessageForUI {

    public GroupChangeAvatarMessageForUI() {
        super(MessageTypeCode.kChatMsgType_GroupAvatarChange);
    }
}
