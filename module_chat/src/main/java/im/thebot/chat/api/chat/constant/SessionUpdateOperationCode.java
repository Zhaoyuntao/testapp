package im.thebot.chat.api.chat.constant;

import androidx.annotation.IntDef;

/**
 * created by zhaoyuntao
 * on 2022/1/19
 */
@IntDef({
        SessionUpdateOperationCode.OPERATION_HEAD,
        SessionUpdateOperationCode.OPERATION_TAIL,
        SessionUpdateOperationCode.OPERATION_PREVIEW,
        SessionUpdateOperationCode.OPERATION_CLEAR,
        SessionUpdateOperationCode.OPERATION_DRAFT,
        SessionUpdateOperationCode.OPERATION_SILENT,
})
public @interface SessionUpdateOperationCode {
    int OPERATION_HEAD = 1 << 1;
    int OPERATION_TAIL = 1 << 2;
    int OPERATION_PREVIEW = 1 << 3;
    int OPERATION_CLEAR = 1 << 4;
    int OPERATION_DRAFT = 1 << 5;
    int OPERATION_SILENT = 1 << 6;
}
