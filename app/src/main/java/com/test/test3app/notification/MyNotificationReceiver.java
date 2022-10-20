package com.test.test3app.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

import im.turbo.utils.log.S;

public class MyNotificationReceiver extends BroadcastReceiver {
    public static final String action = "hello123";

    @Override
    public void onReceive(Context context, Intent intent) {
        S.s("onReceive:" + getMessageText(intent));
        S.s("1:"+intent.getStringExtra("hello"));
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.cancel("hello",1);
    }

    private String getMessageText(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getString("HELLO_KEY");
        }
        return null;
    }
}