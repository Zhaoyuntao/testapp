package com.test.test3app.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.test.test3app.notification.constant.NotificationActionKeys;

public class TNotificationActionReceiver extends BroadcastReceiver {
    public static final String KEY_NOTIFY_ID = "notification_key_notify_id";

    @Override
    public void onReceive(Context context, Intent intent) {
        int notifyId = intent.getIntExtra(KEY_NOTIFY_ID, 0);
        if (TextUtils.equals(intent.getAction(), NotificationActionKeys.ACTION_CLEAR)) {
            TNotificationHelper.getInstance().notifyClear(notifyId, intent);
        } else {
            TNotificationHelper.getInstance().notifyActions(notifyId, intent);
        }
    }
}