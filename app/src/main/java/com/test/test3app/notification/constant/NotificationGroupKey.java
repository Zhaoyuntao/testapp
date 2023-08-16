package com.test.test3app.notification.constant;

import androidx.annotation.StringDef;

/**
 * created by zhaoyuntao
 * on 14/08/2023
 */
@StringDef({
        NotificationGroupKey.CHAT,
})
public @interface NotificationGroupKey {
    String CHAT = "GroupChat";
}
