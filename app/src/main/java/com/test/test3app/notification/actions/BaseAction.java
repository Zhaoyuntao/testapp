package com.test.test3app.notification.actions;

import android.content.Intent;
import android.text.TextUtils;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.test.test3app.notification.TNotificationItem;
import com.test.test3app.notification.constant.NotificationActionKeys;

/**
 * created by zhaoyuntao
 * on 15/08/2023
 */
public abstract class BaseAction<T extends BaseActionCallback> {
    @NotificationActionKeys
    private final String action;
    private final String title;
    private final int icon;
    private final T callback;

    public BaseAction(@NotificationActionKeys String action, @NonNull String title, @DrawableRes int icon, @NonNull T t) {
        this.action = action;
        this.title = title;
        this.icon = icon;
        this.callback = t;
    }

    public String getTitle() {
        return title;
    }

    public int getIcon() {
        return icon;
    }

    @NotificationActionKeys
    public String getAction() {
        return action;
    }

    public boolean isEnable(@NonNull TNotificationItem item) {
        return true;
    }

    final public boolean notifyCallback(@NonNull TNotificationItem item, @NonNull Intent intent) {
        if (TextUtils.equals(action, intent.getAction())) {
            notifyCallback(item, callback, intent);
            return true;
        }
        return false;
    }

    public abstract void notifyCallback(@NonNull TNotificationItem item, @NonNull T t, @NonNull Intent intent);
}
