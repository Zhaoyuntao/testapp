package im.thebot.chat.api.chat.message;


import im.thebot.api.chat.constant.MessageTypeCode;

/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public class DebugLogMessageForUI extends MessageBeanForUI {

    private boolean error;

    public DebugLogMessageForUI() {
        super(MessageTypeCode.TYPE_MESSAGE_DEBUG_LOG);
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean getError() {
        return error;
    }
}
