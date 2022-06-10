package im.thebot.chat.api.chat.message;

import im.thebot.api.chat.constant.MessageTypeCode;

/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public class FileMessageForUI extends BaseFileMessageForUI {
    public FileMessageForUI() {
        super(MessageTypeCode.kChatMsgType_File);
    }
}
