package im.thebot.chat.api.chat.message;

import im.thebot.api.chat.constant.MessageTypeCode;
import im.thebot.chat.api.chat.message.base.sharecardtype.ShareCardType;

public class ShareCardMessageForUI extends MessageBeanForUI {

    @ShareCardType
    private String shareCardType;
    private String shareCardName;
    private String shareCardTitle;
    private String shareCardContent;
    private String shareCardIconUrl;
    private String shareCardCoverUrl;
    private String shareCardActionUrl;
    private String shareCardExt;

    public ShareCardMessageForUI() {
        super(MessageTypeCode.kChatMsgType_Share_Card);
    }

    @ShareCardType
    public String getShareCardType() {
        return shareCardType;
    }

    public void setShareCardType(@ShareCardType String shareCardType) {
        this.shareCardType = shareCardType;
    }

    public String getShareCardName() {
        return shareCardName;
    }

    public void setShareCardName(String shareCardName) {
        this.shareCardName = shareCardName;
    }

    public String getShareCardTitle() {
        return shareCardTitle;
    }

    public void setShareCardTitle(String shareCardTitle) {
        this.shareCardTitle = shareCardTitle;
    }

    public String getShareCardContent() {
        return shareCardContent;
    }

    public void setShareCardContent(String shareCardContent) {
        this.shareCardContent = shareCardContent;
    }

    public String getShareCardIconUrl() {
        return shareCardIconUrl;
    }

    public void setShareCardIconUrl(String shareCardIconUrl) {
        this.shareCardIconUrl = shareCardIconUrl;
    }

    public String getShareCardCoverUrl() {
        return shareCardCoverUrl;
    }

    public void setShareCardCoverUrl(String shareCardCoverUrl) {
        this.shareCardCoverUrl = shareCardCoverUrl;
    }

    public String getShareCardActionUrl() {
        return shareCardActionUrl;
    }

    public void setShareCardActionUrl(String shareCardActionUrl) {
        this.shareCardActionUrl = shareCardActionUrl;
    }

    public String getShareCardExt() {
        return shareCardExt;
    }

    public void setShareCardExt(String shareCardExt) {
        this.shareCardExt = shareCardExt;
    }
}
