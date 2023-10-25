package com.test.test3app.notification;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.IconCompat;

import com.test.test3app.notification.actions.BaseAction;
import com.test.test3app.notification.constant.NotificationGroupTag;
import com.test.test3app.notification.summary.NotificationSummary;

/**
 * created by zhaoyuntao
 * on 11/08/2023
 */
public class TNotificationItem {
    private final String channelId;
    @NotificationGroupTag
    private String groupTag;

    private final String tag;
    private String conversationTitle;
    private String senderName;
    private String senderKey;
    private CharSequence text;
    private IconCompat avatar;
    private String uuid;
    private long time;
    private boolean recall;
    private boolean cancel;
    private Uri uri;
    private BaseAction<?>[] actions;
    private NotificationSummary summary;

    public TNotificationItem(String channelId, String tag) {
        this.channelId = channelId;
        this.tag = tag;
    }

    public TNotificationItem setGroupTag(@NotificationGroupTag String groupTag) {
        this.groupTag = groupTag;
        return this;
    }

    public TNotificationItem setPersonName(String personName, String senderKey, Bitmap avatar) {
        return setPersonName(personName, senderKey, IconCompat.createWithBitmap(avatar));
    }

    public TNotificationItem setPersonName(String personName, String senderKey, IconCompat avatar) {
        this.senderName = personName;
        this.senderKey = senderKey;
        this.avatar = avatar;
        return this;
    }

    public TNotificationItem setConversationTitle(String conversationTitle) {
        this.conversationTitle = conversationTitle;
        return this;
    }

    public TNotificationItem setText(CharSequence text) {
        this.text = text;
        return this;
    }

    public TNotificationItem setUri(Uri uri) {
        this.uri = uri;
        return this;
    }

    public TNotificationItem setTime(long time) {
        this.time = time;
        return this;
    }

    public TNotificationItem setActions(@Nullable BaseAction<?>... actions) {
        this.actions = actions;
        return this;
    }

    public TNotificationItem setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getChannelId() {
        return channelId;
    }

    @NotificationGroupTag
    public String getGroupTag() {
        return groupTag;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getConversationTitle() {
        return conversationTitle;
    }

    public CharSequence getText() {
        return text;
    }

    public Uri getUri() {
        return uri;
    }

    public String getTag() {
        return tag;
    }

    public long getTime() {
        return time;
    }

    public String getSenderKey() {
        return senderKey;
    }

    public IconCompat getAvatar() {
        return avatar;
    }

    public BaseAction<?>[] getActions() {
        return actions;
    }

    public String getUuid() {
        return uuid;
    }

    public void notifyActionCallback(@NonNull Intent intent) {
        if (actions != null && actions.length > 0) {
            for (BaseAction<?> action : actions) {
                if (action.notifyCallback(this, intent)) {
                    return;
                }
            }
        }
    }

    public CharSequence getSummary(int notificationCount, int messageCount) {
        return summary.onGetSummary(this, notificationCount, messageCount);
    }

    @DrawableRes
    public int getSummaryIcon() {
        return summary.getSummaryIcon(this);
    }

    public TNotificationItem setSummary(@NonNull NotificationSummary summary) {
        this.summary = summary;
        return this;
    }

    public boolean needGroupSummary() {
        return groupTag != null && summary != null;
    }

    public boolean isRecall() {
        return recall;
    }

    public TNotificationItem setRecall(boolean recall) {
        this.recall = recall;
        return this;
    }

    public boolean isCancel() {
        return cancel;
    }

    public TNotificationItem setCancel(boolean cancel) {
        this.cancel = cancel;
        return this;
    }
}
