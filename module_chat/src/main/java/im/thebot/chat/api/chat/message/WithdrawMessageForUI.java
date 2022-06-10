package im.thebot.chat.api.chat.message;

import im.thebot.api.chat.constant.MessageTypeCode;

/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public class WithdrawMessageForUI extends MessageBeanForUI {

    private long withdrawTime;

    public WithdrawMessageForUI() {
        super(MessageTypeCode.TYPE_MESSAGE_WITHDRAW);
    }

    public void setWithdrawTime(long withdrawTime) {
        this.withdrawTime = withdrawTime;
    }

    public long getWithdrawTime() {
        return withdrawTime;
    }
}
