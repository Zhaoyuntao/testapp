package im.thebot.chat.api.chat.message.event;

import im.thebot.api.chat.constant.MessageTypeCode;

/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public class ContactJoinMessageForUI extends BaseEventMessageForUI {

    public ContactJoinMessageForUI() {
        super(MessageTypeCode.kChatMsgType_P2PSys_Chatfirst);
    }
}
