package com.test.test3app.notification;

import android.content.Intent;
import android.net.Uri;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.IconCompat;

import com.test.test3app.notification.actions.BaseAction;
import com.test.test3app.notification.constant.NotificationGroupTag;
import com.test.test3app.notification.content.NotificationClickEvent;
import com.test.test3app.notification.summary.NotificationSummary;

import java.util.Objects;


/**
 * created by zhaoyuntao
 * on 11/08/2023
 */
public class TNotificationItem {
    private final String channelId;
    @NotificationGroupTag
    private String groupTag;

    private final String tag;
    private final int uniqueId;
    private String conversationTitle;
    private String senderName;
    private String senderKey;
    private CharSequence text;
    private IconCompat avatar;
    private String uuid;
    private long time;
    private boolean preview;
    private boolean cancel;
    private Uri imageUri;
    private Uri soundUri;
    @DrawableRes
    private Integer icon;
    @ColorInt
    private Integer iconColor;
    //If show group style.(Something like double avatar)
    private boolean groupConversation;
    //Actions.
    private BaseAction<?>[] actions;
    //Summary.
    @DrawableRes
    private Integer summaryIcon;
    @ColorInt
    private Integer summaryIconColor;
    private int customMessageCount;
    private NotificationSummary summary;
    //Content action.
    private NotificationClickEvent clickEvent;
    //Bubble action.
    private NotificationClickEvent bubbleClickEvent;
    private boolean highLevel;
    private IconCompat bubbleAvatar;

    private ErrorProcessor errorProcessor;
    private long[] vibration;

    public TNotificationItem(String channelId, String tag, int uniqueId) {
        this.channelId = channelId;
        this.tag = tag;
        this.uniqueId = uniqueId;
    }

    public TNotificationItem setGroupTag(@NotificationGroupTag String groupTag) {
        this.groupTag = groupTag;
        return this;
    }

    public TNotificationItem setPerson(String personName, String senderKey, @Nullable IconCompat avatar, @Nullable IconCompat bubbleAvatar) {
        this.senderName = personName;
        this.senderKey = senderKey;
        this.avatar = avatar;
        this.bubbleAvatar = bubbleAvatar;
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

    public TNotificationItem setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
        return this;
    }

    public TNotificationItem setSoundUri(Uri soundUri) {
        this.soundUri = soundUri;
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

    public TNotificationItem setSummary(@NonNull NotificationSummary summary) {
        this.summary = summary;
        return this;
    }

    public TNotificationItem setIcon(@DrawableRes int icon) {
        this.icon = icon;
        return this;
    }

    public TNotificationItem setIconColor(@ColorInt int iconColor) {
        this.iconColor = iconColor;
        return this;
    }

    public TNotificationItem setSummaryIcon(@DrawableRes int summaryIcon) {
        this.summaryIcon = summaryIcon;
        return this;
    }

    public TNotificationItem setSummaryIconColor(@ColorInt int summaryIconColor) {
        this.summaryIconColor = summaryIconColor;
        return this;
    }

    public TNotificationItem setCancel(boolean cancel) {
        this.cancel = cancel;
        return this;
    }

    public TNotificationItem setClickEvent(@Nullable NotificationClickEvent clickEvent) {
        this.clickEvent = clickEvent;
        return this;
    }

    public TNotificationItem setBubbleClickEvent(NotificationClickEvent bubbleClickEvent) {
        this.bubbleClickEvent = bubbleClickEvent;
        return this;
    }

    public TNotificationItem setGroupConversation(boolean groupConversation) {
        this.groupConversation = groupConversation;
        return this;
    }

    public TNotificationItem setPreview(boolean preview) {
        this.preview = preview;
        return this;
    }

    public TNotificationItem setCustomMessageCount(int customMessageCount) {
        this.customMessageCount = customMessageCount;
        return this;
    }

    public TNotificationItem setHighLevel(boolean highLevel) {
        this.highLevel = highLevel;
        return this;
    }

    public TNotificationItem setVibration(long[] vibration) {
        this.vibration = vibration;
        return this;
    }

    public TNotificationItem setErrorProcessor(@Nullable ErrorProcessor errorProcessor) {
        this.errorProcessor = errorProcessor;
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

    public Uri getImageUri() {
        return imageUri;
    }

    public Uri getSoundUri() {
        return soundUri;
    }

    public String getTag() {
        return tag;
    }

    public int getUniqueId() {
        return uniqueId;
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

    @DrawableRes
    public Integer getIcon() {
        return icon;
    }

    @ColorInt
    public Integer getIconColor() {
        return iconColor;
    }

    @DrawableRes
    public Integer getSummaryIcon() {
        return summaryIcon;
    }

    @ColorInt
    public Integer getSummaryIconColor() {
        return summaryIconColor;
    }

    @Nullable
    public NotificationSummary getSummary() {
        return summary;
    }

    public boolean needGroupSummary() {
        return groupTag != null && summary != null;
    }

    public boolean isCancel() {
        return cancel;
    }

    public boolean isPreview() {
        return preview;
    }

    public int getCustomMessageCount() {
        return customMessageCount;
    }

    @Nullable
    public NotificationClickEvent getClickEvent() {
        return clickEvent;
    }

    @Nullable
    public NotificationClickEvent getBubbleClickEvent() {
        return bubbleClickEvent;
    }

    public boolean isGroupConversation() {
        return groupConversation;
    }

    public IconCompat getBubbleAvatar() {
        return bubbleAvatar;
    }

    public boolean isHighLevel() {
        return highLevel;
    }

    public long[] getVibration() {
        return vibration;
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

    public void onError(@NonNull Throwable e) {
        if (errorProcessor != null) {
            errorProcessor.onError(this, e);
        }
    }

    public interface ErrorProcessor {
        void onError(@NonNull TNotificationItem item, @NonNull Throwable e);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TNotificationItem item = (TNotificationItem) o;
        return uuid.equals(item.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    @Override
    public String toString() {
        return "TNotificationItem{" +
                "tag='" + tag + '\'' +
                ", text=" + text +
                '}';
    }
}
