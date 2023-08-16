package com.test.test3app.call;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import im.turbo.utils.log.S;

/**
 * created by zhaoyuntao
 * on 31/05/2023
 */
public class PhoneStateService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        S.s("PhoneStateService onCreated");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        S.s("onStartCommand:" + intent);
        stopSelf(startId);
        getApplicationContext().stopService(new Intent(getApplicationContext(),PhoneStateService.class));
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        S.s("onDestroy:"+getApplicationContext());
        super.onDestroy();
    }
}
