package com.test.test3app.notification.actions;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.test.test3app.notification.TNotificationItem;
import com.test.test3app.notification.constant.NotificationActionKeys;


/**
 * created by zhaoyuntao
 * on 15/08/2023
 */
public class ReadAction extends BaseAction<ReadCallback> {

    public ReadAction(@NonNull String title, int icon, @NonNull ReadCallback readCallback) {
        super(NotificationActionKeys.ACTION_READ, title, icon, readCallback);
    }

    @Override
    public void notifyCallback(@NonNull TNotificationItem item, @NonNull ReadCallback readCallback, @NonNull Intent intent) {
        readCallback.onClickRead(item);
    }
}
