package im.thebot.chat.api.chat.message;

import java.util.List;

import im.thebot.api.chat.constant.MessageTypeCode;

/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public class ImagesMessageForUI extends BaseFileMessageForUI {

    private final List<BaseFileMessageForUI> messages;

    public ImagesMessageForUI(List<BaseFileMessageForUI> messages) {
        super(MessageTypeCode.kChatMsgType_Image);
        this.messages = messages;
    }
}
