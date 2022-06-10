package im.thebot.chat.api.chat.message.event;

import im.thebot.api.chat.constant.MessageTypeCode;
import im.thebot.chat.api.chat.message.base.event.EventMemberPacket;

/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public class GroupManagerRemoveMessageForUI extends BaseEventMessageForUI {
    private EventMemberPacket adminPacket;

    public GroupManagerRemoveMessageForUI() {
        super(MessageTypeCode.kChatMsgType_GroupCancelAdmin);
    }

    public EventMemberPacket getAdminPacket() {
        return adminPacket;
    }

    public void setAdminPacket(EventMemberPacket adminPacket) {
        this.adminPacket = adminPacket;
    }
}
