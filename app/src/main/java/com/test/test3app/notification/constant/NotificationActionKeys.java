package com.test.test3app.notification.constant;

import androidx.annotation.StringDef;

/**
 * created by zhaoyuntao
 * on 14/08/2023
 */
@StringDef({
        NotificationActionKeys.ACTION_REPLY,
        NotificationActionKeys.ACTION_READ,
        NotificationActionKeys.ACTION_CLEAR,
})
public @interface NotificationActionKeys {
    String ACTION_REPLY = "turbo_notification_action_reply";
    String ACTION_READ = "turbo_notification_action_mark_as_read";
    String ACTION_CLEAR = "turbo_notification_action_clear";
}
