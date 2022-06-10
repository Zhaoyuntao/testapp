package im.thebot.chat.api.chat.message.official;


import im.thebot.api.chat.constant.MessageTypeCode;
import im.thebot.chat.api.chat.message.MessageBeanForUI;

/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public class OfficialStepsLikeMessageForUI extends MessageBeanForUI {
    private String stepsLikeUid;
    private String stepsLikeUcid;
    private String stepsLikeName;
    private String stepsLikeAvatar;
    private String stepsLikeStepsDate;
    private String stepsLikeUri;

    public OfficialStepsLikeMessageForUI() {
        super(MessageTypeCode.kChatMsgType_StepsLike);
    }

    public String getStepsLikeUid() {
        return stepsLikeUid;
    }

    public void setStepsLikeUid(String stepsLikeUid) {
        this.stepsLikeUid = stepsLikeUid;
    }

    public String getStepsLikeUcid() {
        return stepsLikeUcid;
    }

    public void setStepsLikeUcid(String stepsLikeUcid) {
        this.stepsLikeUcid = stepsLikeUcid;
    }

    public String getStepsLikeName() {
        return stepsLikeName;
    }

    public void setStepsLikeName(String stepsLikeName) {
        this.stepsLikeName = stepsLikeName;
    }

    public String getStepsLikeAvatar() {
        return stepsLikeAvatar;
    }

    public void setStepsLikeAvatar(String stepsLikeAvatar) {
        this.stepsLikeAvatar = stepsLikeAvatar;
    }

    public String getStepsLikeStepsDate() {
        return stepsLikeStepsDate;
    }

    public void setStepsLikeStepsDate(String stepsLikeStepsDate) {
        this.stepsLikeStepsDate = stepsLikeStepsDate;
    }

    public String getStepsLikeUri() {
        return stepsLikeUri;
    }

    public void setStepsLikeUri(String stepsLikeUri) {
        this.stepsLikeUri = stepsLikeUri;
    }
}
