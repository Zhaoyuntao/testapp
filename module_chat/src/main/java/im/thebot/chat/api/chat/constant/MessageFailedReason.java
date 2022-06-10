package im.thebot.chat.api.chat.constant;

import androidx.annotation.IntDef;

/**
 * created by zhaoyuntao
 * on 05/09/2021
 * description:
 */
@IntDef({MessageFailedReason.FAIL_REASON_NETWORK,
        MessageFailedReason.FAIL_REASON_TIMEOUT,
        MessageFailedReason.FAIL_REASON_CANCEL,
        MessageFailedReason.FAIL_REASON_UNKNOWN
})
public @interface MessageFailedReason {
    int FAIL_REASON_NETWORK = 1;
    int FAIL_REASON_TIMEOUT = 2;
    int FAIL_REASON_CANCEL = 3;
    int FAIL_REASON_UNKNOWN = 5;
}
