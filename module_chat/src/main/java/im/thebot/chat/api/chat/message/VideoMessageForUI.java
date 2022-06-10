package im.thebot.chat.api.chat.message;

import im.thebot.api.chat.constant.MessageTypeCode;

/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public class VideoMessageForUI extends BaseFileMessageForUI {
    private int videoType;// 0 mp4
    private long videoStartTime;
    private long videoEndTime;
    private long videoDuration;
    private long videoOriginalSize;

    private String videoMimeType;
    private String videoPreviewPath;
    private String videoLocalClipPath;
    private String videoPreviewBase64;

    private int videoWidth;
    private int videoHeight;
    private int videoBitrate;
    private int videoRotation;
    private int videoClipWidth;
    private int videoClipHeight;
    private int videoClipQuality; // value is 1, 2 or 3;  (1:qp=23, 2:qp=26, 3:qp=29)
    private boolean videoCanCompress;
    private float videoCompressionRate;
    private double videoOriginalDuration;

    public VideoMessageForUI() {
        super(MessageTypeCode.kChatMsgType_ShortVideo);
    }

    public int getVideoType() {
        return videoType;
    }

    public void setVideoType(int videoType) {
        this.videoType = videoType;
    }

    public long getVideoStartTime() {
        return videoStartTime;
    }

    public void setVideoStartTime(long videoStartTime) {
        this.videoStartTime = videoStartTime;
    }

    public long getVideoEndTime() {
        return videoEndTime;
    }

    public void setVideoEndTime(long videoEndTime) {
        this.videoEndTime = videoEndTime;
    }

    public long getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(long videoDuration) {
        this.videoDuration = videoDuration;
    }

    public long getVideoOriginalSize() {
        return videoOriginalSize;
    }

    public void setVideoOriginalSize(long videoOriginalSize) {
        this.videoOriginalSize = videoOriginalSize;
    }

    public String getVideoMimeType() {
        return videoMimeType;
    }

    public void setVideoMimeType(String videoMimeType) {
        this.videoMimeType = videoMimeType;
    }

    public String getVideoPreviewPath() {
        return videoPreviewPath;
    }

    public void setVideoPreviewPath(String videoPreviewPath) {
        this.videoPreviewPath = videoPreviewPath;
    }

    public String getVideoLocalClipPath() {
        return videoLocalClipPath;
    }

    public void setVideoLocalClipPath(String videoLocalClipPath) {
        this.videoLocalClipPath = videoLocalClipPath;
    }

    public String getVideoPreviewBase64() {
        return videoPreviewBase64;
    }

    public void setVideoPreviewBase64(String videoPreviewBase64) {
        this.videoPreviewBase64 = videoPreviewBase64;
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }

    public int getVideoBitrate() {
        return videoBitrate;
    }

    public void setVideoBitrate(int videoBitrate) {
        this.videoBitrate = videoBitrate;
    }

    public int getVideoRotation() {
        return videoRotation;
    }

    public void setVideoRotation(int videoRotation) {
        this.videoRotation = videoRotation;
    }

    public int getVideoClipWidth() {
        return videoClipWidth;
    }

    public void setVideoClipWidth(int videoClipWidth) {
        this.videoClipWidth = videoClipWidth;
    }

    public int getVideoClipHeight() {
        return videoClipHeight;
    }

    public void setVideoClipHeight(int videoClipHeight) {
        this.videoClipHeight = videoClipHeight;
    }

    public int getVideoClipQuality() {
        return videoClipQuality;
    }

    public void setVideoClipQuality(int videoClipQuality) {
        this.videoClipQuality = videoClipQuality;
    }

    public boolean isVideoCanCompress() {
        return videoCanCompress;
    }

    public void setVideoCanCompress(boolean videoCanCompress) {
        this.videoCanCompress = videoCanCompress;
    }

    public float getVideoCompressionRate() {
        return videoCompressionRate;
    }

    public void setVideoCompressionRate(float videoCompressionRate) {
        this.videoCompressionRate = videoCompressionRate;
    }

    public double getVideoOriginalDuration() {
        return videoOriginalDuration;
    }

    public void setVideoOriginalDuration(double videoOriginalDuration) {
        this.videoOriginalDuration = videoOriginalDuration;
    }
}
