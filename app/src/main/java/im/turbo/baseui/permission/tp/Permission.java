/*
 * Copyright © zhaoyuntao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package im.turbo.baseui.permission.tp;

import androidx.annotation.StringDef;

/**
 * <p>Permissions.</p>
 * Created by Zhaoyuntao on 2017/8/4.
 */
@StringDef({
        Permission.READ_CALENDAR,
        Permission.WRITE_CALENDAR,
        Permission.CAMERA,
        Permission.READ_CONTACTS,
        Permission.WRITE_CONTACTS,
        Permission.GET_ACCOUNTS,
        Permission.ACCESS_FINE_LOCATION,
        Permission.ACCESS_COARSE_LOCATION,
        Permission.ACCESS_BACKGROUND_LOCATION,
        Permission.RECORD_AUDIO,
        Permission.READ_PHONE_STATE,
        Permission.CALL_PHONE,
        Permission.ADD_VOICEMAIL,
        Permission.USE_SIP,
        Permission.READ_PHONE_NUMBERS,
        Permission.ANSWER_PHONE_CALLS,
        Permission.PROCESS_OUTGOING_CALLS,
        Permission.BODY_SENSORS,
        Permission.ACTIVITY_RECOGNITION,
        Permission.SEND_SMS,
        Permission.READ_SMS,
        Permission.RECEIVE_SMS,
        Permission.RECEIVE_WAP_PUSH,
        Permission.RECEIVE_MMS,
        Permission.READ_EXTERNAL_STORAGE,
        Permission.WRITE_EXTERNAL_STORAGE
})
public @interface Permission {

    String READ_CALENDAR = "android.permission.READ_CALENDAR";
    String WRITE_CALENDAR = "android.permission.WRITE_CALENDAR";

    String CAMERA = "android.permission.CAMERA";

    String READ_CONTACTS = "android.permission.READ_CONTACTS";
    String WRITE_CONTACTS = "android.permission.WRITE_CONTACTS";
    String GET_ACCOUNTS = "android.permission.GET_ACCOUNTS";

    String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    String ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";
    String ACCESS_BACKGROUND_LOCATION = "android.permission.ACCESS_BACKGROUND_LOCATION";

    String RECORD_AUDIO = "android.permission.RECORD_AUDIO";

    String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    String CALL_PHONE = "android.permission.CALL_PHONE";
    String USE_SIP = "android.permission.USE_SIP";
    String READ_PHONE_NUMBERS = "android.permission.READ_PHONE_NUMBERS";
    String ANSWER_PHONE_CALLS = "android.permission.ANSWER_PHONE_CALLS";
    String ADD_VOICEMAIL = "com.android.voicemail.permission.ADD_VOICEMAIL";

    String PROCESS_OUTGOING_CALLS = "android.permission.PROCESS_OUTGOING_CALLS";

    String BODY_SENSORS = "android.permission.BODY_SENSORS";
    String ACTIVITY_RECOGNITION = "android.permission.ACTIVITY_RECOGNITION";

    String SEND_SMS = "android.permission.SEND_SMS";
    String READ_SMS = "android.permission.READ_SMS";
    String RECEIVE_SMS = "android.permission.RECEIVE_SMS";
    String RECEIVE_WAP_PUSH = "android.permission.RECEIVE_WAP_PUSH";
    String RECEIVE_MMS = "android.permission.RECEIVE_MMS";

    String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
}