package im.thebot.chat.api.chat.message;


import java.util.List;

import im.thebot.api.chat.constant.MessageTypeCode;
import im.thebot.chat.api.chat.message.richmedia.RichMedia;

/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public class RichMediaMultiMessageForUI extends MessageBeanForUI {
    private RichMedia firstRichMedia;
    private List<RichMedia> otherRichMedias;

    public RichMediaMultiMessageForUI() {
        super(MessageTypeCode.kChatMsgType_MutiRichmedia);
    }

    public RichMedia getFirstRichMedia() {
        return firstRichMedia;
    }

    public void setFirstRichMedia(RichMedia firstRichMedia) {
        this.firstRichMedia = firstRichMedia;
    }

    public List<RichMedia> getOtherRichMedias() {
        return otherRichMedias;
    }

    public void setOtherRichMedias(List<RichMedia> otherRichMedias) {
        this.otherRichMedias = otherRichMedias;
    }
}
