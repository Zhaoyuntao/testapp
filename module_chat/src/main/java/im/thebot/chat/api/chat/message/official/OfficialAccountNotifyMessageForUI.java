package im.thebot.chat.api.chat.message.official;


import im.thebot.api.chat.constant.MessageTypeCode;
import im.thebot.chat.api.chat.message.MessageBeanForUI;

/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public class OfficialAccountNotifyMessageForUI extends MessageBeanForUI {

    public OfficialAccountNotifyMessageForUI() {
        super(MessageTypeCode.kChatMsgType_OfficialNotifyTemp);
    }
}
