package com.test.test3app.notification.summary;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.test.test3app.notification.TNotificationItem;

/**
 * created by zhaoyuntao
 * on 15/08/2023
 */
public interface NotificationSummary {
    CharSequence onGetSummary(@NonNull TNotificationItem item, int notificationCount, int messageCount);

    @DrawableRes
    int getSummaryIcon(@NonNull TNotificationItem item);
}
