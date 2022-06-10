package im.thebot.chat.api.chat.message;

import im.thebot.api.chat.constant.MessageTypeCode;

public class CashCardMessageForUI extends MessageBeanForUI {
    private int cardType;
    private String subject;
    private String outTradeNo;
    private String payload;
    private String payUid;
    private Long payPhone;
    private String receiveUid;
    private Long receivePhone;
    private String ext;
    private float amount;
    private String current;
    private int status;// 0 - initial, 1 - accept, 2 - reject

    public CashCardMessageForUI() {
        super(MessageTypeCode.kChatMsgType_CashCard);
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getPayUid() {
        return payUid;
    }

    public void setPayUid(String payUid) {
        this.payUid = payUid;
    }

    public Long getPayPhone() {
        return payPhone;
    }

    public void setPayPhone(Long payPhone) {
        this.payPhone = payPhone;
    }

    public String getReceiveUid() {
        return receiveUid;
    }

    public void setReceiveUid(String receiveUid) {
        this.receiveUid = receiveUid;
    }

    public Long getReceivePhone() {
        return receivePhone;
    }

    public void setReceivePhone(Long receivePhone) {
        this.receivePhone = receivePhone;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
