package im.thebot.chat.api.chat.message;



import im.thebot.api.chat.constant.MessageTypeCode;

/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public class DebugCommandMessageForUI extends MessageBeanForUI {

    public DebugCommandMessageForUI() {
        super(MessageTypeCode.TYPE_MESSAGE_DEBUG_COMMAND);
    }
}
