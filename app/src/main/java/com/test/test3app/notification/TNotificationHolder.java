package com.test.test3app.notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * created by zhaoyuntao
 * on 14/08/2023
 */
class TNotificationHolder {
    private final String channelId;
    private final NotificationCompat.Builder builder;
    private final NotificationCompat.MessagingStyle messageStyle;
    private final List<TNotificationItem> messages = new ArrayList<>();

    public TNotificationHolder(@NonNull String channelId, @NonNull NotificationCompat.Builder builder, @NonNull NotificationCompat.MessagingStyle messageStyle) {
        this.channelId = channelId;
        this.builder = builder;
        this.messageStyle = messageStyle;
    }

    public String getChannelId() {
        return channelId;
    }

    public NotificationCompat.Builder getBuilder() {
        return builder;
    }

    public NotificationCompat.MessagingStyle getMessageStyle() {
        return messageStyle;
    }

    public int getMessageCount() {
        synchronized (messages) {
            return messages.size();
        }
    }

    public void addItem(@NonNull TNotificationItem item) {
        synchronized (messages) {
            if (!this.messages.contains(item)) {
                this.messages.add(item);
            }
        }
    }

    public void removeItem(@NonNull String uuid) {
        synchronized (messages) {
            for (TNotificationItem item : messages) {
                if (uuid.equals(item.getUuid())) {
                    this.messages.remove(item);
                    break;
                }
            }
        }
    }

    public ArrayList<TNotificationItem> getAllItems() {
        synchronized (messages) {
            return new ArrayList<>(messages);
        }
    }

    public void clear() {
        synchronized (messages) {
            messages.clear();
        }
    }

    @Nullable
    public TNotificationItem getItem() {
        return messages.size() == 0 ? null : messages.get(messages.size() - 1);
    }
}
