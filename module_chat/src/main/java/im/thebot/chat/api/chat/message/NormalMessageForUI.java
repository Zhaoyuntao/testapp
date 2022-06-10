package im.thebot.chat.api.chat.message;

import im.thebot.api.chat.constant.MessageTypeCode;

public class NormalMessageForUI extends MessageBeanForUI {
    public NormalMessageForUI(@MessageTypeCode int messageType) {
        super(messageType);
    }

}
