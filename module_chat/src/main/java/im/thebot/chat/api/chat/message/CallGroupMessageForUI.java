package im.thebot.chat.api.chat.message;

import im.thebot.api.chat.constant.MessageTypeCode;


/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public class CallGroupMessageForUI extends MessageBeanForUI {
    private int groupVoipType;
    private int groupVoipRoomState;
    private long groupVoipGroupId;
    private String groupVoipRoomId;
    private String groupVoipDuration;
    private String groupVoipGroupMemberIds;
    private boolean groupVoipIsMissCall;
    private boolean groupVoipIsCaller;
    private boolean groupVoipDisturb;

    public CallGroupMessageForUI() {
        super(MessageTypeCode.kChatMsgType_GroupVoip);
    }

    public int getGroupVoipType() {
        return groupVoipType;
    }

    public void setGroupVoipType(int groupVoipType) {
        this.groupVoipType = groupVoipType;
    }

    public int getGroupVoipRoomState() {
        return groupVoipRoomState;
    }

    public void setGroupVoipRoomState(int groupVoipRoomState) {
        this.groupVoipRoomState = groupVoipRoomState;
    }

    public long getGroupVoipGroupId() {
        return groupVoipGroupId;
    }

    public void setGroupVoipGroupId(long groupVoipGroupId) {
        this.groupVoipGroupId = groupVoipGroupId;
    }

    public String getGroupVoipRoomId() {
        return groupVoipRoomId;
    }

    public void setGroupVoipRoomId(String groupVoipRoomId) {
        this.groupVoipRoomId = groupVoipRoomId;
    }

    public String getGroupVoipDuration() {
        return groupVoipDuration;
    }

    public void setGroupVoipDuration(String groupVoipDuration) {
        this.groupVoipDuration = groupVoipDuration;
    }

    public String getGroupVoipGroupMemberIds() {
        return groupVoipGroupMemberIds;
    }

    public void setGroupVoipGroupMemberIds(String groupVoipGroupMemberIds) {
        this.groupVoipGroupMemberIds = groupVoipGroupMemberIds;
    }

    public boolean isGroupVoipIsMissCall() {
        return groupVoipIsMissCall;
    }

    public void setGroupVoipIsMissCall(boolean groupVoipIsMissCall) {
        this.groupVoipIsMissCall = groupVoipIsMissCall;
    }

    public boolean isGroupVoipIsCaller() {
        return groupVoipIsCaller;
    }

    public void setGroupVoipIsCaller(boolean groupVoipIsCaller) {
        this.groupVoipIsCaller = groupVoipIsCaller;
    }

    public boolean isGroupVoipDisturb() {
        return groupVoipDisturb;
    }

    public void setGroupVoipDisturb(boolean groupVoipDisturb) {
        this.groupVoipDisturb = groupVoipDisturb;
    }
}
