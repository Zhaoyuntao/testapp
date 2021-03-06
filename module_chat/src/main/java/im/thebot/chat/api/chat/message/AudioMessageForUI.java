package im.thebot.chat.api.chat.message;

import androidx.annotation.NonNull;

import im.thebot.api.chat.constant.MessageTypeCode;

/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public class AudioMessageForUI extends BaseFileMessageForUI {
    private int audioDuration;
    private long audioPlayedTime;
    private long audioPlayedProgress;

    public AudioMessageForUI() {
        super(MessageTypeCode.kChatMsgType_Audio);
    }

    public int getAudioDuration() {
        return audioDuration;
    }

    public void setAudioDuration(int audioDuration) {
        this.audioDuration = audioDuration;
    }

    public long getAudioPlayedTime() {
        return audioPlayedTime;
    }

    public void setAudioPlayedTime(long audioPlayedTime) {
        this.audioPlayedTime = audioPlayedTime;
    }

    public boolean isPlayed() {
        return audioPlayedTime > 0;
    }

    public long getAudioPlayedProgress() {
        return audioPlayedProgress;
    }

    public void setAudioPlayedProgress(long audioPlayedProgress) {
        this.audioPlayedProgress = audioPlayedProgress;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + "{" +
                "audioDuration=" + audioDuration +
                ", audioPlayedTime=" + audioPlayedTime +
                '}';
    }
}
