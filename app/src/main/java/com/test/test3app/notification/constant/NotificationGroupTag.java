package com.test.test3app.notification.constant;

import androidx.annotation.StringDef;

/**
 * created by zhaoyuntao
 * on 14/08/2023
 * 通知分组tag
 */
@StringDef({
        NotificationGroupTag.CHAT,
        NotificationGroupTag.OTHER,
})
public @interface NotificationGroupTag {
    String CHAT = "TurboGroupChat";
    String OTHER = "TurboGroupOther";
}
