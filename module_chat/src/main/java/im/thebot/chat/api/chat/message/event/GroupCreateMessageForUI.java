package im.thebot.chat.api.chat.message.event;

import im.thebot.api.chat.constant.MessageTypeCode;

/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public class GroupCreateMessageForUI extends BaseEventMessageForUI {
    private String groupName;

    public GroupCreateMessageForUI() {
        super(MessageTypeCode.kChatMsgType_GroupCreate);
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
