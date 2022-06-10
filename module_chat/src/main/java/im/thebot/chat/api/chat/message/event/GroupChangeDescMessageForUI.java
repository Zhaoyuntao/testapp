package im.thebot.chat.api.chat.message.event;

import im.thebot.api.chat.constant.MessageTypeCode;

/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public class GroupChangeDescMessageForUI extends BaseEventMessageForUI {
    private String groupDesc;

    public GroupChangeDescMessageForUI() {
        super(MessageTypeCode.kChatMsgType_GroupModifyDesc);
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }
}
