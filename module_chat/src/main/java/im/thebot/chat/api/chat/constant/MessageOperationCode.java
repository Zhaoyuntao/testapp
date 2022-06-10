package im.thebot.chat.api.chat.constant;

import androidx.annotation.IntDef;

/**
 * created by zhaoyuntao
 * on 2022/1/19
 */
@IntDef({
        MessageOperationCode.MESSAGE_OPERATION_CODE_ADD,
        MessageOperationCode.MESSAGE_OPERATION_CODE_CHANGE,
        MessageOperationCode.MESSAGE_OPERATION_CODE_DELETE,
        MessageOperationCode.MESSAGE_OPERATION_CODE_CLEAR,
        MessageOperationCode.MESSAGE_OPERATION_CODE_RELOAD,
})
public @interface MessageOperationCode {
    int MESSAGE_OPERATION_CODE_ADD = 0;
    int MESSAGE_OPERATION_CODE_CHANGE = 1;
    int MESSAGE_OPERATION_CODE_DELETE = 2;
    int MESSAGE_OPERATION_CODE_CLEAR = 3;
    int MESSAGE_OPERATION_CODE_RELOAD = 4;
}
