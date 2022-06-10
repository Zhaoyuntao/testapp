package im.thebot.chat.api.chat.message;

import im.thebot.api.chat.constant.MessageTypeCode;

public class WebClipMessageForUI extends MessageBeanForUI {
    private String webClipUrl;
    private String webClipTitle;
    private String webClipDescription;
    private String webClipImageUrl;

    public WebClipMessageForUI() {
        super(MessageTypeCode.kChatMsgType_Webclip);
    }

    public String getWebClipUrl() {
        return webClipUrl;
    }

    public void setWebClipUrl(String webClipUrl) {
        this.webClipUrl = webClipUrl;
    }

    public String getWebClipTitle() {
        return webClipTitle;
    }

    public void setWebClipTitle(String webClipTitle) {
        this.webClipTitle = webClipTitle;
    }

    public String getWebClipDescription() {
        return webClipDescription;
    }

    public void setWebClipDescription(String webClipDescription) {
        this.webClipDescription = webClipDescription;
    }

    public String getWebClipImageUrl() {
        return webClipImageUrl;
    }

    public void setWebClipImageUrl(String webClipImageUrl) {
        this.webClipImageUrl = webClipImageUrl;
    }
}
