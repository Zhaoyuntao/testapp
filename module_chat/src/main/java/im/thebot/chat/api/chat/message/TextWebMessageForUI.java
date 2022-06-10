package im.thebot.chat.api.chat.message;


import im.thebot.api.chat.constant.MessageTypeCode;
import im.thebot.chat.api.chat.message.text.BaseTextMessageForUI;

/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public class TextWebMessageForUI extends BaseTextMessageForUI {
    private String textTitle;
    private String textDesc;
    private String textUrl;
    private String textImageUrl;

    public TextWebMessageForUI() {
        super(MessageTypeCode.TYPE_MESSAGE_TEXT_WEB);
    }

    public String getTextTitle() {
        return textTitle;
    }

    public void setTextTitle(String textTitle) {
        this.textTitle = textTitle;
    }

    public String getTextDesc() {
        return textDesc;
    }

    public void setTextDesc(String textDesc) {
        this.textDesc = textDesc;
    }

    public String getTextUrl() {
        return textUrl;
    }

    public void setTextUrl(String textUrl) {
        this.textUrl = textUrl;
    }

    public String getTextImageUrl() {
        return textImageUrl;
    }

    public void setTextImageUrl(String textImageUrl) {
        this.textImageUrl = textImageUrl;
    }
}
