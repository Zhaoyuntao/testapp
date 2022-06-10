package im.thebot.chat.api.chat.message.text;


import android.text.TextUtils;

import java.util.List;

import im.thebot.chat.api.chat.message.MessageBeanForUI;
import im.thebot.user.ContactUtil;

/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public abstract class BaseTextMessageForUI extends MessageBeanForUI {
    //mention.
    private List<String> mentionUidList;
    private boolean hasReferenceMe;
    private CharSequence cachedCharSequence;

    public BaseTextMessageForUI(int messageType) {
        super(messageType);
    }

    public CharSequence getCachedCharSequence() {
        return TextUtils.isEmpty(cachedCharSequence) ? getContent() : cachedCharSequence;
    }

    public void setCachedCharSequence(CharSequence cachedCharSequence) {
        this.cachedCharSequence = cachedCharSequence;
    }

    public List<String> getMentionUidList() {
        return mentionUidList;
    }

    public void setMentionUidList(List<String> mentionUidList) {
        this.mentionUidList = mentionUidList;
    }

    public boolean hasMentionMe(boolean includeMentionAll) {
        if (mentionUidList != null && mentionUidList.size() > 0) {
            for (String uid : mentionUidList) {
                if (ContactUtil.isSelf(uid) || (includeMentionAll && ContactUtil.isMentionAll(uid))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isHasReferenceMe() {
        return hasReferenceMe;
    }

    public void setHasReferenceMe(boolean hasReferenceMe) {
        this.hasReferenceMe = hasReferenceMe;
    }
}
