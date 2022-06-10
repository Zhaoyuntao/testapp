package im.thebot.chat.api.chat.message.official;


import im.thebot.api.chat.constant.MessageTypeCode;
import im.thebot.chat.api.chat.message.MessageBeanForUI;

/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public class OfficialAccountMessageForUI extends MessageBeanForUI {

    public OfficialAccountMessageForUI() {
        super(MessageTypeCode.kChatMsgType_OfficialAccount);
    }
}
