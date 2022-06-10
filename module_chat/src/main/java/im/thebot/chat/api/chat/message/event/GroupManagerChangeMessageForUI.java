package im.thebot.chat.api.chat.message.event;

import im.thebot.api.chat.constant.MessageTypeCode;
import im.thebot.chat.api.chat.message.base.event.EventMemberPacket;

/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public class GroupManagerChangeMessageForUI extends BaseEventMessageForUI {
    private EventMemberPacket adminPacket;

    public GroupManagerChangeMessageForUI() {
        super(MessageTypeCode.kChatMsgType_GroupLeaderChange);
    }

    public EventMemberPacket getAdminPacket() {
        return adminPacket;
    }

    public void setAdminPacket(EventMemberPacket adminPacket) {
        this.adminPacket = adminPacket;
    }
}
