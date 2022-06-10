package im.thebot.chat.api.chat.message;

import im.thebot.api.chat.constant.MessageTypeCode;

public class ShareLinkMessageForUI extends MessageBeanForUI {

    private String shareCanForward;
    private String shareContentUrl;
    private String shareLink;
    private String shareSubtitle;
    private String shareTitle;
    private String shareTitleIconUrl;

    public ShareLinkMessageForUI() {
        super(MessageTypeCode.kChatMsgType_Share);
    }

    public String getShareCanForward() {
        return shareCanForward;
    }

    public void setShareCanForward(String shareCanForward) {
        this.shareCanForward = shareCanForward;
    }

    public String getShareContentUrl() {
        return shareContentUrl;
    }

    public void setShareContentUrl(String shareContentUrl) {
        this.shareContentUrl = shareContentUrl;
    }

    public String getShareLink() {
        return shareLink;
    }

    public void setShareLink(String shareLink) {
        this.shareLink = shareLink;
    }

    public String getShareSubtitle() {
        return shareSubtitle;
    }

    public void setShareSubtitle(String shareSubtitle) {
        this.shareSubtitle = shareSubtitle;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareTitleIconUrl() {
        return shareTitleIconUrl;
    }

    public void setShareTitleIconUrl(String shareTitleIconUrl) {
        this.shareTitleIconUrl = shareTitleIconUrl;
    }
}
