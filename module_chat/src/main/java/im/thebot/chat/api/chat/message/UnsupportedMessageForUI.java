package im.thebot.chat.api.chat.message;



import im.thebot.api.chat.constant.MessageTypeCode;

/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public class UnsupportedMessageForUI extends MessageBeanForUI {
    public UnsupportedMessageForUI() {
        super(MessageTypeCode.TYPE_MESSAGE_UNSUPPORTED);
    }
}
