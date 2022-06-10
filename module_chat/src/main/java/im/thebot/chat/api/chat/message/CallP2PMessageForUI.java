package im.thebot.chat.api.chat.message;

import im.thebot.api.chat.constant.MessageTypeCode;


/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public class CallP2PMessageForUI extends MessageBeanForUI {
    private boolean isMissed;

    public CallP2PMessageForUI() {
        super(MessageTypeCode.kChatMsgType_RTC);
    }


    public boolean isMissed() {
        return isMissed;
    }
}
