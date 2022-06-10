package im.thebot.chat.api.chat.message;

import androidx.annotation.NonNull;

import im.thebot.api.chat.constant.MessageTypeCode;

/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public class AudioMessageForUI extends BaseFileMessageForUI {
    private long audioDuration;
    private long audioPlayedTime;

    public AudioMessageForUI() {
        super(MessageTypeCode.kChatMsgType_Audio);
    }

    public long getAudioDuration() {
        return audioDuration;
    }

    public void setAudioDuration(long audioDuration) {
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

    @NonNull
    @Override
    public String toString() {
        return super.toString() + "{" +
                "audioDuration=" + audioDuration +
                ", audioPlayedTime=" + audioPlayedTime +
                '}';
    }
}
