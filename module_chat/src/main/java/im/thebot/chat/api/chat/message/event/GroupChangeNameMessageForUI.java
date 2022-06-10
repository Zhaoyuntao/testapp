package im.thebot.chat.api.chat.message.event;

import im.thebot.api.chat.constant.MessageTypeCode;

/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public class GroupChangeNameMessageForUI extends BaseEventMessageForUI {
    private String groupName;

    public GroupChangeNameMessageForUI() {
        super(MessageTypeCode.kChatMsgType_GroupRename);
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
