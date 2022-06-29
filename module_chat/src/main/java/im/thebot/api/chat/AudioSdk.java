package im.thebot.api.chat;

import androidx.annotation.NonNull;

import com.sdk.chat.file.audio.AudioFilePacket;
import com.sdk.chat.file.audio.AudioListener;
import com.sdk.chat.file.audio.AudioNotifier;
import com.sdk.chat.file.audio.AudioRecorder;
import com.sdk.chat.file.audio.AudioStatusBean;
import com.sdk.chat.file.audio.MessageAudioHelper;

/**
 * created by zhaoyuntao
 * on 09/06/2022
 * description:
 */
public class AudioSdk {
    public void startRecord(@NonNull AudioRecorder.AudioRecordListener audioRecordListener) {
        MessageAudioHelper.startRecord(audioRecordListener);
    }

    public void stopRecord() {
        MessageAudioHelper.stopRecord();
    }

    public void startPlay(@NonNull AudioFilePacket audioFilePacket, long position) {
        MessageAudioHelper.playDebug(audioFilePacket, position);
    }

    public void resumeRecord() {
        MessageAudioHelper.resumeRecord();
    }

    public void pausePlayingAudio() {
        MessageAudioHelper.pausePlaying();
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

    public void dropAudio() {
        MessageAudioHelper.dropAudio();
    }

    public AudioFilePacket getAudioDraft() {
        return MessageAudioHelper.getAudioDraft();
    }
}
