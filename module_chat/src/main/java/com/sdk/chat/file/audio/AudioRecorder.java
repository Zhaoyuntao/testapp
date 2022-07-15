package com.sdk.chat.file.audio;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.SystemClock;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import com.instanza.cocovoice.AudioUtil;

import java.io.File;
import java.io.IOException;

import im.turbo.basetools.observer.ObjectHolder;
import im.turbo.basetools.preconditions.Preconditions;
import im.turbo.baseui.permission.Permission;
import im.turbo.baseui.permission.PermissionUtils;
import im.turbo.thread.ThreadPool;
import im.turbo.utils.ResourceUtils;

/**
 * created by zhaoyuntao
 * on 27/06/2022
 * description:
 */
public class AudioRecorder {
    private final String uuid;
    private AudioRecord audioRecord;
    private final ObjectHolder<AudioRecordListener> audioRecordListenerObjectHolder;
    private final int sampleRateHZ = 8000;
    private File tmpRecordFile;
    private final long handle;
    private long timeStart;

    @IntDef({
            RecordResult.NON,
            RecordResult.FINISH,
            RecordResult.PAUSE,
            RecordResult.DROP,
    })
    public @interface RecordResult {
        int NON = 0;
        int FINISH = 1;
        int PAUSE = 2;
        int DROP = 3;
    }

    @RecordResult
    private int recordResult;
    private int duration;

    public AudioRecorder(@NonNull String uuid, @NonNull AudioRecordListener audioRecordListener) {
        Preconditions.checkNotEmpty(uuid);
        Preconditions.checkNotNull(audioRecordListener);
        this.uuid = uuid;
        this.tmpRecordFile = new File(ResourceUtils.getApplication().getCacheDir(), "audio-tmp-" + SystemClock.elapsedRealtime() + ".amr");
        handle = AudioUtil.init(tmpRecordFile.getAbsolutePath());
        this.audioRecordListenerObjectHolder = new ObjectHolder<>(audioRecordListener, "audioRecordListener", true);
    }

    public void startRecord() {
        long now = SystemClock.elapsedRealtime();
        if ((timeStart > 0 && (now - timeStart) < 1000) || isRecording()) {
            return;
        }
        timeStart = now;
        if (!PermissionUtils.hasPermission(ResourceUtils.getApplication(), Permission.RECORD_AUDIO)) {
            onError("No permission");
            return;
        }
        ThreadPool.runIO(new Runnable() {
            @Override
            public void run() {
                _startRecord();
            }
        });
    }

    private void _startRecord() {
        if (tmpRecordFile == null) {
            return;
        }
        if (audioRecord != null && audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
            return;
        }
        int bufferSize = AudioRecord.getMinBufferSize(sampleRateHZ, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRateHZ, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
        if (!tmpRecordFile.exists()) {
            try {
                boolean created = tmpRecordFile.createNewFile();
                if (!created) {
                    onError("create file failed");
                    duration = 0;
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (audioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
            onError("audioRecord not initialized");
            return;
        }
        audioRecord.startRecording();
        onRecordStart(new float[]{0}, duration);

        long timeLastTime = 0;
        while (audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
            if (audioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
                break;
            }
            long timeNow = SystemClock.elapsedRealtime();
            if (timeLastTime == 0) {
                timeLastTime = timeNow;
            }
            duration += (timeNow - timeLastTime);
            timeLastTime = timeNow;
            //todo volume.
            onRecording(0, duration);
            byte[] buffer = new byte[bufferSize];
            int readLength = audioRecord.read(buffer, 0, bufferSize);
            int encodeResult = AudioUtil.processAndEncode(handle, buffer, readLength);
            if (encodeResult < 0) {
                audioRecord.stop();
                break;
            }
        }
        processRecordResult();
    }

    private void processRecordResult() {
        if (audioRecord != null) {
            audioRecord.release();
            audioRecord = null;
        }

        AudioFilePacket audioFilePacket = getAudioDraft();
        if (recordResult == RecordResult.FINISH) {
            AudioUtil.destroy(handle);
            onRecordEnd(audioFilePacket);
        } else if (recordResult == RecordResult.PAUSE) {
            onRecordPause(audioFilePacket);
        } else if (recordResult == RecordResult.DROP) {
            AudioUtil.destroy(handle);
            if (tmpRecordFile.exists()) {
                tmpRecordFile.delete();
                tmpRecordFile = null;
            }
            onAudioDropped(audioFilePacket);
        }
    }

    private void onError(String errorMessage) {
        AudioRecordListener audioRecordListener = audioRecordListenerObjectHolder.getObject();
        if (audioRecordListener != null) {
            audioRecordListener.onError(errorMessage);
        }
    }

    private void onRecordStart(float[] volumes, long duration) {
        AudioRecordListener audioRecordListener = audioRecordListenerObjectHolder.getObject();
        if (audioRecordListener != null) {
            audioRecordListener.onRecordStart(volumes, duration);
        }
    }

    private void onRecording(float volume, long duration) {
        AudioRecordListener audioRecordListener = audioRecordListenerObjectHolder.getObject();
        if (audioRecordListener != null) {
            audioRecordListener.onRecording(volume, duration);
        }
    }

    private void onRecordEnd(AudioFilePacket audioFilePacket) {
        AudioRecordListener audioRecordListener = audioRecordListenerObjectHolder.getObject();
        if (audioRecordListener != null) {
            audioRecordListener.onRecordEnd(audioFilePacket);
        }
    }

    private void onRecordPause(AudioFilePacket audioFilePacket) {
        AudioRecordListener audioRecordListener = audioRecordListenerObjectHolder.getObject();
        if (audioRecordListener != null) {
            audioRecordListener.onRecordPause(audioFilePacket);
        }
    }

    private void onAudioDropped(AudioFilePacket audioFilePacket) {
        AudioRecordListener audioRecordListener = audioRecordListenerObjectHolder.getObject();
        if (audioRecordListener != null) {
            audioRecordListener.onAudioDropped(audioFilePacket);
        }
    }

    public void finishRecord() {
        this.recordResult = RecordResult.FINISH;
        if (isRecording()) {
            audioRecord.stop();
        } else {
            processRecordResult();
        }
    }

    public void pauseRecord() {
        this.recordResult = RecordResult.PAUSE;
        if (isRecording()) {
            audioRecord.stop();
        } else {
            processRecordResult();
        }
    }

    public void dropAudio() {
        this.recordResult = RecordResult.DROP;
        if (isRecording()) {
            audioRecord.stop();
        } else {
            processRecordResult();
        }
    }

    public boolean isPaused() {
        return this.recordResult == RecordResult.PAUSE;
    }

    public boolean isRecording() {
        return audioRecord != null && audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING;
    }

    public AudioFilePacket getAudioDraft() {
        AudioFilePacket audioFilePacket = new AudioFilePacket();
        if (tmpRecordFile != null && tmpRecordFile.exists()) {
            audioFilePacket.setFilePath(tmpRecordFile.getAbsolutePath());
            audioFilePacket.setFileSize(tmpRecordFile.length());
            audioFilePacket.setDuration(duration);
            audioFilePacket.setUuid(uuid);
        }
        return audioFilePacket;
    }
}
