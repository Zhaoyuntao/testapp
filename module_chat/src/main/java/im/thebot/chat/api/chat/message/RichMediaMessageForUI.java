package im.thebot.chat.api.chat.message;


import im.thebot.api.chat.constant.MessageTypeCode;
import im.thebot.chat.api.chat.message.richmedia.RichMedia;

/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public class RichMediaMessageForUI extends MessageBeanForUI {
    private RichMedia richMedia;

    public RichMediaMessageForUI() {
        super(MessageTypeCode.kChatMsgType_Richmedia);
    }

    public RichMedia getRichMedia() {
        return richMedia;
    }

    public void setRichMedia(RichMedia richMedia) {
        this.richMedia = richMedia;
    }
}
