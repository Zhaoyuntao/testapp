package com.test.test3app.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.test.test3app.notification.constant.NotificationActionKeys;

public class TNotificationActionReceiver extends BroadcastReceiver {
    public static final String KEY_NOTIFY_TAG = "turbo_notification_key_tag";

    @Override
    public void onReceive(Context context, Intent intent) {
        String tag = intent.getStringExtra(KEY_NOTIFY_TAG);
        if (TextUtils.equals(intent.getAction(), NotificationActionKeys.ACTION_CLEAR)) {
            TNotificationHelper.getInstance().notifyClear(tag, intent);
        } else {
            TNotificationHelper.getInstance().notifyActions(tag, intent);
        }
    }
}