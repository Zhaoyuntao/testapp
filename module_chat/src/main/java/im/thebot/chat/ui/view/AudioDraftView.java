package im.thebot.chat.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.module_chat.R;

import im.turbo.basetools.preconditions.Preconditions;
import im.turbo.basetools.time.TimeUtils;
import im.turbo.baseui.chat.SmoothScaleFrameLayout;

/**
 * created by zhaoyuntao
 * on 23/06/2022
 * description:
 */
public class AudioDraftView extends FrameLayout {
    //Recording.
    private static final int INDEX_PAUSE = SmoothScaleFrameLayout.INDEX_DEFAULT;
    private static final int INDEX_RECORD = SmoothScaleFrameLayout.INDEX_SECOND;
    private final ViewGroup audioRecordContainer;
    private final AudioRecordingProgressView audioRecordProgressView;
    private final AudioPlayProgressView audioPlayProgressView;
    private final TextView audioRecordDurationView;
    //Pause.
    private static final int INDEX_PLAY = SmoothScaleFrameLayout.INDEX_DEFAULT;
    private static final int INDEX_PLAYING = SmoothScaleFrameLayout.INDEX_SECOND;
    private final ViewGroup audioPlayContainer;
    private final TextView audioPlayDurationView;
    private final SmoothScaleFrameLayout audioPlaySwitchContainer;
    private final View pausePlayButton;
    private final View playButton;
    //Bottom operators.
    private final View deleteButton;
    private final View sendButton;
    private final View resumeRecordButton;
    private final View pauseRecordButton;
    private final SmoothScaleFrameLayout recordAndPauseSwitchContainer;

    private AudioDraftListener listener;

    public AudioDraftView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        LayoutInflater.from(context).inflate(R.layout.layout_record_draft_view, this, true);

        //Recording.
        audioRecordContainer = findViewById(R.id.audio_draft_view_container_recording);
        audioRecordContainer.setVisibility(VISIBLE);
        audioRecordProgressView = findViewById(R.id.audio_draft_view_progress_view_recording);
        audioRecordDurationView = findViewById(R.id.audio_draft_view_draft_duration_recording);

        //Pause.
        audioPlayProgressView = findViewById(R.id.audio_draft_view_draft_progress_view);
        audioPlayContainer = findViewById(R.id.audio_draft_view_container_paused);
        audioPlayContainer.setVisibility(GONE);
        audioPlayDurationView = findViewById(R.id.audio_draft_view_draft_duration_paused);
        audioPlaySwitchContainer = findViewById(R.id.audio_draft_view_play_switch_container);
        pausePlayButton = findViewById(R.id.audio_draft_view_playing);
        playButton = findViewById(R.id.audio_draft_view_play);

        //Bottom operators.
        deleteButton = findViewById(R.id.audio_draft_view_delete_button);
        sendButton = findViewById(R.id.audio_draft_view_send_button);
        resumeRecordButton = findViewById(R.id.audio_draft_view_start_record);
        pauseRecordButton = findViewById(R.id.audio_draft_view_pause_record);
        recordAndPauseSwitchContainer = findViewById(R.id.audio_draft_view_switch_container);
    }

    private AudioDraftListener getListener() {
        Preconditions.checkNotNull(listener, "You have to set listener before you click this view");
        return listener;
    }

    public void setListener(@NonNull AudioDraftListener listener) {
        Preconditions.checkNotNull(listener);
        this.listener = listener;
        playButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getListener().onPlay(audioPlayProgressView.getPercent());
            }
        });
        pausePlayButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getListener().pausePlaying();
            }
        });
        audioPlayProgressView.setOnProgressChangedListener(new OnProgressChangedListener() {
            private boolean isPlayingBeforePause;

            @Override
            public void onStartDragging() {
                isPlayingBeforePause = getListener().isPlayingDraft();
            }

            @Override
            public void onProgressChanged(float percent, boolean dragByUser) {
                if (dragByUser) {
                    audioPlayDurationView.setText(TimeUtils.formatLongToDuration((long) (getListener().getAudioDuration() * audioPlayProgressView.getPercent())));
                    if (isPlayingBeforePause) {
                        isPlayingBeforePause = false;
                        getListener().onPlay(percent);
                    }
                }
            }
        });
        deleteButton.setOnClickListener(v -> {
            getListener().onClickDelete();
            audioRecordProgressView.stop();
        });
        sendButton.setOnClickListener(v -> {
            getListener().onClickSend();
            audioRecordProgressView.stop();
        });
        resumeRecordButton.setOnClickListener(v -> {
            performStartRecoding();
        });
        pauseRecordButton.setOnClickListener(v -> {
            performPauseRecording();
        });
    }

    public void performStartRecoding() {
        getListener().onResumeRecord();
        recordAndPauseSwitchContainer.switchIndex(INDEX_PAUSE);
        if (audioRecordProgressView.isPaused()) {
            audioRecordProgressView.resume();
        } else {
            audioRecordProgressView.start();
        }
        audioRecordContainer.setVisibility(VISIBLE);
        audioPlayContainer.setVisibility(GONE);
    }

    public void performPauseRecording() {
        getListener().onPauseRecord();
        recordAndPauseSwitchContainer.switchIndex(INDEX_RECORD);
        audioRecordProgressView.pause();
        audioRecordContainer.setVisibility(GONE);
        audioPlayContainer.setVisibility(VISIBLE);
    }

    public void postVolume(float volume) {
        audioRecordProgressView.setCurrentVolume(volume);
    }

    public void debugStop() {
        audioRecordProgressView.stop();
    }

    public void setRecordingInitData(float[] volume, long duration) {
        audioRecordProgressView.setInitData(volume, duration);
    }

    public void setSlidePlayDraftInitData(float[] volume, long duration) {
        audioPlayProgressView.setInitData(volume, duration);
        audioPlayProgressView.setPercent(0);
    }

    public void setCurrentVolume(float volume) {
        audioRecordProgressView.setCurrentVolume(volume);
    }

    public void setRecordDuration(long duration) {
        audioRecordDurationView.setText(TimeUtils.formatLongToDuration(duration));
    }

    public void setPlaying(float percent, long currentPosition, boolean animate) {
        if (audioPlaySwitchContainer.getIndex() != SmoothScaleFrameLayout.INDEX_SECOND) {
            audioPlaySwitchContainer.switchIndex(SmoothScaleFrameLayout.INDEX_SECOND);
        }
        audioPlayDurationView.setText(TimeUtils.formatLongToDuration(currentPosition));
        audioPlayProgressView.setPercent(percent, animate);
    }

    public void performPausePlaying() {
        if (audioPlaySwitchContainer.getIndex() != INDEX_PLAY) {
            audioPlaySwitchContainer.switchIndex(INDEX_PLAY);
        }
    }

    public void setNotPlaying(float percent, long currentPosition, long duration) {
        if (audioPlaySwitchContainer.getIndex() != INDEX_PLAY) {
            audioPlaySwitchContainer.switchIndex(INDEX_PLAY);
        }
        audioPlayDurationView.setText(TimeUtils.formatLongToDuration(duration));
        audioPlayProgressView.setPercent(percent, false);
    }

    public interface AudioDraftListener {
        void onClickSend();

        void onClickDelete();

        void onPauseRecord();

        void onResumeRecord();

        void onPlay(float percent);

        boolean isPlayingDraft();

        void pausePlaying();

        long getAudioDuration();
    }
}
