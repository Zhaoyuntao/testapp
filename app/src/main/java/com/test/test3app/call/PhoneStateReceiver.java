package com.test.test3app.call;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import im.turbo.utils.log.S;

/**
 * Created by popfisher on 2017/11/6.
 */

public class PhoneStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        S.s("PhoneStateReceiver action: " + action);
//
//        String resultData = this.getResultData();
//        S.s("PhoneStateReceiver getResultData: " + resultData);
//
//        if (action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
//            // 去电，可以用定时挂断
//            // 双卡的手机可能不走这个Action
//            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
//            S.s("PhoneStateReceiver EXTRA_PHONE_NUMBER: " + phoneNumber);
//        } else {
//            // 来电去电都会走
//            // 获取当前电话状态
//            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
//            S.s("PhoneStateReceiver onReceive state: " + state);
//
//            // 获取电话号码
//            String extraIncomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
//            S.s("PhoneStateReceiver onReceive extraIncomingNumber: " + extraIncomingNumber);
//
////            if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {
////                S.s("PhoneStateReceiver onReceive endCall");
////                HangUpTelephonyUtil.endCall(context);
////            }
//        }
    }
}
