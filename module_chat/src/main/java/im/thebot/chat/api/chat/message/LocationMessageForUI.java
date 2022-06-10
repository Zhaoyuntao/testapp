package im.thebot.chat.api.chat.message;

import im.thebot.api.chat.constant.MessageTypeCode;

/**
 * created by zhaoyuntao
 * on 2022/1/19
 */
public class LocationMessageForUI extends MessageBeanForUI {
    private double locationLongitude;
    private double locationLatitude;
    private String locationName;
    private String locationPreviewURL;

    public LocationMessageForUI() {
        super(MessageTypeCode.kChatMsgType_Location);
    }

    public double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationPreviewURL() {
        return locationPreviewURL;
    }

    public void setLocationPreviewURL(String locationPreviewURL) {
        this.locationPreviewURL = locationPreviewURL;
    }
}
