package com.test.test3app.notification.content;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.test.test3app.notification.TNotificationItem;


/**
 * created by zhaoyuntao
 * on 15/08/2023
 */
public interface NotificationClickEvent {
    void onSetIntent(@NonNull TNotificationItem item, @NonNull Intent intent);
}
