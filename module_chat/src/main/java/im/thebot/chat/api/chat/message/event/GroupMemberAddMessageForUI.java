package im.thebot.chat.api.chat.message.event;

import im.thebot.chat.api.chat.constant.GroupMemberAddType;
import im.thebot.api.chat.constant.MessageTypeCode;

/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public class GroupMemberAddMessageForUI extends BaseEventMessageForUI {
    @GroupMemberAddType
    private int groupMemberAddType;

    public GroupMemberAddMessageForUI() {
        super(MessageTypeCode.kChatMsgType_GroupAdd);
    }

    public int getGroupMemberAddType() {
        return groupMemberAddType;
    }

    public void setGroupMemberAddType(int groupMemberAddType) {
        this.groupMemberAddType = groupMemberAddType;
    }
}
