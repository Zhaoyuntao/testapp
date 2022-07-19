package im.thebot.api.chat;

import androidx.annotation.NonNull;

import com.sdk.chat.file.audio.AudioFilePacket;
import com.sdk.chat.file.audio.AudioListener;
import com.sdk.chat.file.audio.AudioNotifier;
import com.sdk.chat.file.audio.AudioRecordListener;
import com.sdk.chat.file.audio.AudioStatusBean;
import com.sdk.chat.file.audio.MessageAudioHelper;

/**
 * created by zhaoyuntao
 * on 09/06/2022
 * description:
 */
public class AudioSdk {
    public void startRecord(@NonNull String uuid,@NonNull AudioRecordListener audioRecordListener) {
        MessageAudioHelper.startRecord(uuid,audioRecordListener);
    }

    public void stopRecord() {
        MessageAudioHelper.finishRecord();
    }

    public void startPlay(@NonNull AudioFilePacket audioFilePacket) {
        MessageAudioHelper.startPlay(audioFilePacket);
    }

    public void resumeRecord() {
        MessageAudioHelper.resumeRecord();
    }

    public void pausePlayingAudio() {
        MessageAudioHelper.stopPlaying();
    }

    public void stopPlayingAudio() {
        MessageAudioHelper.stopPlaying();
    }

    public void registerAudioPlayListener(AudioListener listener) {
        AudioNotifier.getInstance().addListener(listener);
    }

    public void unregisterAudioPlayListener(AudioListener listener) {
        AudioNotifier.getInstance().removeListener(listener);
    }

    public AudioStatusBean getAudioStatus(String sessionId, String uuid) {
        return AudioNotifier.getInstance().getStatus(uuid);
    }

    public boolean isAudioPlaying() {
        return MessageAudioHelper.isPlaying();
    }

    public void pauseRecord() {
        MessageAudioHelper.pauseRecord();
    }

    public void cancelRecord() {
        MessageAudioHelper.cancelRecord();
    }

    public AudioFilePacket getAudioDraft() {
        return MessageAudioHelper.getAudioDraft();
    }
}
