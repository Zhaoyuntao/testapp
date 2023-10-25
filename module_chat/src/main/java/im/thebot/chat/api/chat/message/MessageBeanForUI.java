package im.thebot.chat.api.chat.message;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.util.Objects;

import im.thebot.api.chat.constant.MessageRedNoticeType;
import im.thebot.api.chat.constant.MessageStatusCode;
import im.thebot.api.chat.constant.MessageTypeCode;
import im.thebot.chat.api.chat.constant.MessageFailedReason;
import im.thebot.chat.api.chat.constant.MessageStatusUtil;
import im.thebot.chat.api.chat.message.event.BaseEventMessageForUI;
import im.thebot.user.ContactUtil;
import im.turbo.basetools.selector.Selectable;

/**
 * created by zhaoyuntao
 * on 18/05/2020
 * description:
 */
public abstract class MessageBeanForUI implements Selectable<String> {

    private String senderUid;
    private String content;
    private String contentConversationListPreview;

    private long timeSend;
    @MessageStatusCode
    private int messageStatus;
    @MessageFailedReason
    private int failReason;
    @MessageTypeCode
    private final int type;
    private String sessionId;
    private String uuid;
    private String errorDescription;
    private String payloadSourceJson;
    //Reply.
    private MessageBeanForUI messageReply;
    private String deviceDetails;
    private String senderName;
    private long installTime;
    private long updateTime;
    private long versionCode;
    private String versionName;
    private String redNotice;
    @MessageRedNoticeType
    private int noticeType;
    private boolean canBeRead;
    private boolean canBeReplied;

    private boolean isForward;
    private int deviceOsId;
    private boolean canBeWithdrew;
    private boolean isOtherDevice;

    public MessageBeanForUI(@MessageTypeCode int messageType) {
        this.type = messageType;
    }

    @Override
    public String getUniqueIdentificationId() {
        return uuid;
    }

    public MessageBeanForUI getMessageReply() {
        return messageReply;
    }

    public void setMessageReply(MessageBeanForUI messageReply) {
        this.messageReply = messageReply;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSelf() {
        return ContactUtil.isSelf(getSenderUid());
    }

    public long getTimeSend() {
        return timeSend;
    }

    public void setTimeSend(long timeSend) {
        this.timeSend = timeSend;
    }

    public boolean isHasRead() {
        return !canBeRead() || isSelf() || isSystemEvent() || messageStatus == MessageStatusCode.STATUS_MESSAGE_SENDER_SEND_SUCCESS_RECEIVER_HAS_READ;
    }

    public boolean canBeRead() {
        return canBeRead;
    }

    @MessageTypeCode
    public int getType() {
        return type;
    }

    @MessageStatusCode
    public int getMessageStatus() {
        return messageStatus;
    }

    public String getMessageStatusStr() {
        return MessageStatusUtil.messageStatusStr(messageStatus);
    }

    public void setMessageStatus(@MessageStatusCode int messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUUID() {
        return uuid;
    }

    public void setUUID(String UUID) {
        this.uuid = UUID;
    }

    public boolean isSystemEvent() {
        return this instanceof BaseEventMessageForUI;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public int getFailReason() {
        return failReason;
    }

    public void setFailReason(int failReason) {
        this.failReason = failReason;
    }

    public String getPayloadSourceJson() {
        if (!TextUtils.isEmpty(payloadSourceJson) && payloadSourceJson.length() > 10000) {
            return payloadSourceJson.substring(0, 10000);
        } else {
            return payloadSourceJson;
        }
    }

    public void setPayloadSourceJson(String payloadSourceJson) {
        if (payloadSourceJson != null && payloadSourceJson.length() > 10000) {
            payloadSourceJson = payloadSourceJson.substring(0, 10000);
        }
        this.payloadSourceJson = payloadSourceJson;
    }

    public String getContentConversationListPreview() {
        return contentConversationListPreview;
    }

    public void setContentConversationListPreview(String contentConversationListPreview) {
        this.contentConversationListPreview = contentConversationListPreview;
    }

    public String getDeviceDetails() {
        return deviceDetails;
    }

    public void setDeviceDetails(String deviceDetails) {
        this.deviceDetails = deviceDetails;
    }

    public boolean isUnsupportedType() {
        return type == MessageTypeCode.TYPE_MESSAGE_UNSUPPORTED;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }


    public String getRedNotice() {
        return redNotice;
    }

    public void setRedNotice(String redNotice) {
        this.redNotice = redNotice;
    }

    public int getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(int noticeType) {
        this.noticeType = noticeType;
    }

    public void setInstallTime(long installTime) {
        this.installTime = installTime;
    }

    public long getInstallTime() {
        return installTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setVersionCode(long versionCode) {
        this.versionCode = versionCode;
    }

    public long getVersionCode() {
        return versionCode;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setForward(boolean forward) {
        isForward = forward;
    }

    public boolean isForward() {
        return isForward;
    }


    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    public boolean isDeletedMessage() {
        return type == MessageTypeCode.TYPE_MESSAGE_WITHDRAW;
    }

    public void setCanBeRead(boolean canBeRead) {
        this.canBeRead = canBeRead;
    }

    public void setDeviceOsId(int deviceOsId) {
        this.deviceOsId = deviceOsId;
    }

    public int getDeviceOsId() {
        return deviceOsId;
    }

    public boolean canBeReplied() {
        return canBeReplied;
    }

    public void setCanBeReplied(boolean canBeReplied) {
        this.canBeReplied = canBeReplied;
    }

    public boolean canBeWithdrew() {
        return canBeWithdrew;
    }

    public void setCanBeWithdrew(boolean canBeWithdrew) {
        this.canBeWithdrew = canBeWithdrew;
    }

    public boolean isTextMessage() {
        return type == MessageTypeCode.kChatMsgType_WrapText;
    }

    public boolean isOtherDevice() {
        return isOtherDevice;
    }

    public void setOtherDevice(boolean otherDevice) {
        this.isOtherDevice = otherDevice;
    }

    public boolean isSendSuccess() {
        return MessageStatusUtil.isSendSuccess(messageStatus);
    }

    public boolean isSendFailed() {
        return MessageStatusUtil.isSendFailed(messageStatus);
    }

    public boolean isSendCanceled() {
        return MessageStatusUtil.isSendCanceled(messageStatus);
    }

    public boolean isSending() {
        return MessageStatusUtil.isSending(messageStatus);
    }

    public boolean isSendingWaiting() {
        return MessageStatusUtil.isSendingWaiting(messageStatus);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageBeanForUI)) return false;
        MessageBeanForUI that = (MessageBeanForUI) o;
        return Objects.equals(uuid, that.uuid);
    }

//    @NonNull
//    @Override
//    public String toString() {
//        return "MessageBeanForUI{" +
//                "senderUid='" + senderUid + '\'' +
//                ", content='" + content + '\'' +
//                ", contentPreview='" + contentConversationListPreview + '\'' +
//                ", isSelf=" + isSelf() +
//                ", timeSend=" + timeSend +
//                ", messageStatus=" + MessageStatusUtil.messageStatusStr(messageStatus) +
//                ", failReason=" + failReason +
//                ", type=" + type +
//                ", conversationId='" + sessionId + '\'' +
//                ", uuid='" + uuid + '\'' +
//                ", messageReply=" + messageReply +
//                '}';
//    }
}
