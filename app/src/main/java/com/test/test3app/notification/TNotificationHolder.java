package com.test.test3app.notification;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * created by zhaoyuntao
 * on 14/08/2023
 */
class TNotificationHolder {
    private final NotificationCompat.Builder builder;
    private final NotificationCompat.MessagingStyle messageStyle;
    private final List<TNotificationItem> messages = new ArrayList<>();
    private TNotificationItem item;

    public TNotificationHolder(@NonNull NotificationCompat.Builder builder, @NonNull NotificationCompat.MessagingStyle messageStyle) {
        this.builder = builder;
        this.messageStyle = messageStyle;
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
            this.messages.add(item);
        }
        this.item = item;
    }

    @NonNull
    public TNotificationItem getItem() {
        return item;
    }
}
