package im.thebot.chat.api.chat.message.richmedia;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * created by zhaoyuntao
 * on 12/04/2022
 * description:
 */
public class RichMedia implements Cloneable, RichMediaJsonKey {

    private String richMediaUrl;
    private String richMediaTitle;
    private int richMediaUrlType;
    private int richMediaShowHeight;
    private int richMediaOriginWidth;
    private int richMediaOriginHeight;
    private String richMediaDescription;

    private boolean richMediaPopup;
    private boolean richMediaHasPopped;
    private boolean richMediaFullScreen;

    private String richMediaImageUrl;
    private String richMediaPreviewImageUrl;

    public void parseExtra(@NonNull JSONObject jsonObject) {
        setRichMediaUrl(jsonObject.optString(KEY_RICH_MEDIA_URL));
        setRichMediaTitle(jsonObject.optString(KEY_RICH_MEDIA_TITLE));
        setRichMediaUrlType(jsonObject.optInt(KEY_RICH_MEDIA_URL_TYPE));
        setRichMediaShowHeight(jsonObject.optInt(KEY_RICH_MEDIA_SHOW_HEIGHT));
        setRichMediaOriginWidth(jsonObject.optInt(KEY_RICH_MEDIA_ORIGIN_WIDTH));
        setRichMediaOriginHeight(jsonObject.optInt(KEY_RICH_MEDIA_ORIGIN_HEIGHT));
        setRichMediaDescription(jsonObject.optString(KEY_RICH_MEDIA_DESCRIPTION));

        setRichMediaPopup(jsonObject.optBoolean(KEY_RICH_MEDIA_POPUP));
        setRichMediaHasPopped(jsonObject.optBoolean(KEY_RICH_MEDIA_HAS_POPPED));
        setRichMediaFullScreen(jsonObject.optBoolean(KEY_RICH_MEDIA_FULL_SCREEN));

        setRichMediaImageUrl(jsonObject.optString(KEY_RICH_MEDIA_IMAGE_URL));
        setRichMediaPreviewImageUrl(jsonObject.optString(KEY_RICH_MEDIA_PREVIEW_IMAGE_URL));
    }

    public void buildExtra(@NonNull JSONObject jsonObject) throws JSONException {
        jsonObject.put(KEY_RICH_MEDIA_URL, getRichMediaUrl());
        jsonObject.put(KEY_RICH_MEDIA_TITLE, getRichMediaTitle());
        jsonObject.put(KEY_RICH_MEDIA_URL_TYPE, getRichMediaUrlType());
        jsonObject.put(KEY_RICH_MEDIA_SHOW_HEIGHT, getRichMediaShowHeight());
        jsonObject.put(KEY_RICH_MEDIA_ORIGIN_WIDTH, getRichMediaOriginWidth());
        jsonObject.put(KEY_RICH_MEDIA_ORIGIN_HEIGHT, getRichMediaOriginHeight());
        jsonObject.put(KEY_RICH_MEDIA_DESCRIPTION, getRichMediaDescription());

        jsonObject.put(KEY_RICH_MEDIA_POPUP, isRichMediaPopup());
        jsonObject.put(KEY_RICH_MEDIA_HAS_POPPED, isRichMediaHasPopped());
        jsonObject.put(KEY_RICH_MEDIA_FULL_SCREEN, isRichMediaFullScreen());

        jsonObject.put(KEY_RICH_MEDIA_IMAGE_URL, getRichMediaImageUrl());
        jsonObject.put(KEY_RICH_MEDIA_PREVIEW_IMAGE_URL, getRichMediaPreviewImageUrl());
    }

    @NonNull
    @Override
    public RichMedia clone() {
        try {
            return (RichMedia) super.clone();
        } catch (CloneNotSupportedException e) {
            return new RichMedia();
        }
    }

    public String getRichMediaUrl() {
        return richMediaUrl;
    }

    public void setRichMediaUrl(String richMediaUrl) {
        this.richMediaUrl = richMediaUrl;
    }

    public String getRichMediaTitle() {
        return richMediaTitle;
    }

    public void setRichMediaTitle(String richMediaTitle) {
        this.richMediaTitle = richMediaTitle;
    }

    public int getRichMediaUrlType() {
        return richMediaUrlType;
    }

    public void setRichMediaUrlType(int richMediaUrlType) {
        this.richMediaUrlType = richMediaUrlType;
    }

    public int getRichMediaShowHeight() {
        return richMediaShowHeight;
    }

    public void setRichMediaShowHeight(int richMediaShowHeight) {
        this.richMediaShowHeight = richMediaShowHeight;
    }

    public int getRichMediaOriginWidth() {
        return richMediaOriginWidth;
    }

    public void setRichMediaOriginWidth(int richMediaOriginWidth) {
        this.richMediaOriginWidth = richMediaOriginWidth;
    }

    public int getRichMediaOriginHeight() {
        return richMediaOriginHeight;
    }

    public void setRichMediaOriginHeight(int richMediaOriginHeight) {
        this.richMediaOriginHeight = richMediaOriginHeight;
    }

    public String getRichMediaDescription() {
        return richMediaDescription;
    }

    public void setRichMediaDescription(String richMediaDescription) {
        this.richMediaDescription = richMediaDescription;
    }

    public boolean isRichMediaPopup() {
        return richMediaPopup;
    }

    public void setRichMediaPopup(boolean richMediaPopup) {
        this.richMediaPopup = richMediaPopup;
    }

    public boolean isRichMediaHasPopped() {
        return richMediaHasPopped;
    }

    public void setRichMediaHasPopped(boolean richMediaHasPopped) {
        this.richMediaHasPopped = richMediaHasPopped;
    }

    public boolean isRichMediaFullScreen() {
        return richMediaFullScreen;
    }

    public void setRichMediaFullScreen(boolean richMediaFullScreen) {
        this.richMediaFullScreen = richMediaFullScreen;
    }

    public String getRichMediaImageUrl() {
        return richMediaImageUrl;
    }

    public void setRichMediaImageUrl(String richMediaImageUrl) {
        this.richMediaImageUrl = richMediaImageUrl;
    }

    public String getRichMediaPreviewImageUrl() {
        return richMediaPreviewImageUrl;
    }

    public void setRichMediaPreviewImageUrl(String richMediaPreviewImageUrl) {
        this.richMediaPreviewImageUrl = richMediaPreviewImageUrl;
    }
}
