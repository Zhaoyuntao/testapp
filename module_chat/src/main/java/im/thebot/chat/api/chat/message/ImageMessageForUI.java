package im.thebot.chat.api.chat.message;

import im.thebot.api.chat.constant.MessageTypeCode;

/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public class ImageMessageForUI extends BaseFileMessageForUI {

    //picture
    private int imageWidth;
    private int imageHeight;
    private int originImageWidth;
    private int originImageHeight;
    private String originImageDownloadUrl;
    private String originImageLocalPath;
    private String imagePreviewBase64;

    public ImageMessageForUI() {
        super(MessageTypeCode.kChatMsgType_Image);
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public int getOriginImageWidth() {
        return originImageWidth;
    }

    public void setOriginImageWidth(int originImageWidth) {
        this.originImageWidth = originImageWidth;
    }

    public int getOriginImageHeight() {
        return originImageHeight;
    }

    public void setOriginImageHeight(int originImageHeight) {
        this.originImageHeight = originImageHeight;
    }

    public void setOriginImageDownloadUrl(String originImageDownloadUrl) {
        this.originImageDownloadUrl = originImageDownloadUrl;
    }

    public String getOriginImageDownloadUrl() {
        return originImageDownloadUrl;
    }

    public void setOriginImageLocalPath(String originImageLocalPath) {
        this.originImageLocalPath = originImageLocalPath;
    }

    public String getOriginImageLocalPath() {
        return originImageLocalPath;
    }

    public void setImagePreviewBase64(String imagePreviewBase64) {
        this.imagePreviewBase64 = imagePreviewBase64;
    }

    public String getImagePreviewBase64() {
        return imagePreviewBase64;
    }
}
