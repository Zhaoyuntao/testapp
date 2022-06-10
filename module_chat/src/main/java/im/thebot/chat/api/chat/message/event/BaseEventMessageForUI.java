package im.thebot.chat.api.chat.message.event;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import im.thebot.chat.api.chat.message.MessageBeanForUI;
import im.thebot.chat.api.chat.message.base.event.EventMemberPacket;

/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public abstract class BaseEventMessageForUI extends MessageBeanForUI {
    private EventMemberPacket operator;
    private List<EventMemberPacket> members;
    private CharSequence cachedCharSequence;

    public BaseEventMessageForUI(int messageType) {
        super(messageType);
    }

    public void setMembers(@Nullable List<EventMemberPacket> members) {
        this.members = members;
    }

    public void setOperator(EventMemberPacket operator) {
        this.operator = operator;
    }

    @Nullable
    public EventMemberPacket getOperator() {
        return operator;
    }

    public String getOperatorUid() {
        return operator != null ? operator.getUid() : null;
    }

    @Nullable
    public List<EventMemberPacket> getMembers() {
        return members;
    }

    public int getMembersSize() {
        return members == null ? 0 : members.size();
    }

    @Nullable
    public List<String> getMembersUid() {
        if (members == null) {
            return null;
        }
        List<String> uidList = new ArrayList<>(members.size());
        for (EventMemberPacket packet : members) {
            uidList.add(packet.getUid());
        }
        return uidList;
    }

    public String getFirstMemberUid() {
        return (members == null || members.size() == 0) ? null : members.get(0).getUid();
    }

    public CharSequence getCachedCharSequence() {
        return TextUtils.isEmpty(cachedCharSequence) ? getContent() : cachedCharSequence;
    }

    public void setCachedCharSequence(CharSequence cachedCharSequence) {
        this.cachedCharSequence = cachedCharSequence;
    }
}
