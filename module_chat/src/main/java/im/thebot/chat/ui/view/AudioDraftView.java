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
import com.sdk.chat.file.audio.AudioListener;
import com.sdk.chat.file.audio.AudioStatusBean;
import com.sdk.chat.file.audio.AudioStatusCode;

import im.thebot.SdkFactory;
import im.turbo.basetools.preconditions.Preconditions;
import im.turbo.basetools.time.TimeUtils;
import im.turbo.baseui.chat.SmoothSwitchFrameLayout;
import im.turbo.thread.SafeRunnable;
import im.turbo.thread.ThreadPool;

/**
 * created by zhaoyuntao
 * on 23/06/2022
 * description:
 */
public class AudioDraftView extends FrameLayout {
    //Recording.
    private static final int INDEX_PAUSE = SmoothSwitchFrameLayout.INDEX_DEFAULT;
    private static final int INDEX_RECORD = SmoothSwitchFrameLayout.INDEX_SECOND;
    private final ViewGroup audioRecordContainer;
    private final AudioRecordingProgressView audioRecordProgressView;
    private final AudioPlayProgressView audioPlayProgressView;
    private final TextView audioRecordDurationView;
    //Pause.
    private static final int INDEX_PLAY = SmoothSwitchFrameLayout.INDEX_DEFAULT;
    private static final int INDEX_PLAYING = SmoothSwitchFrameLayout.INDEX_SECOND;
    private final ViewGroup audioPlayContainer;
    private final TextView audioPlayDurationView;
    private final SmoothSwitchFrameLayout audioPlaySwitchContainer;
    private final View pausePlayButton;
    private final View playButton;
    //Bottom operators.
    private final View deleteButton;
    private final View sendButton;
    private final View resumeRecordButton;
    private final View pauseRecordButton;
    private final SmoothSwitchFrameLayout recordAndPauseSwitchContainer;
    private AudioListener audioListener;
    private String uuid;

    private AudioDraftListener listener;

    public AudioDraftView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        LayoutInflater.from(context).inflate(R.layout.layout_chat_component_record_draft, this, true);

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
                getListener().onPausePlay();
            }
        });
        audioPlayProgressView.setOnProgressChangedListener(new AudioPlayProgressView.OnProgressChangedListener() {
            private boolean currentPlaying;

            @Override
            public void onProgressChanged(float percent, @ProgressAction int action) {
                audioPlayDurationView.setText(TimeUtils.formatLongToDuration((long) (getListener().getAudioDuration() * audioPlayProgressView.getPercent())));
                if (action == ProgressAction.ACTION_PRESS_DOWN) {
                    currentPlaying = getListener().isPlayingDraft();
                    if (currentPlaying) {
                        getListener().pausePlaying();
                    }
                } else if (action == ProgressAction.ACTION_UP) {
                    if (currentPlaying) {
                        getListener().onPlay(percent);
                        currentPlaying = false;
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
            switchToRecodingState();
            getListener().onResumeRecord();
        });
        pauseRecordButton.setOnClickListener(v -> {
            switchToPauseRecordingState();
            getListener().onPauseRecord();
        });
    }

    private void switchToRecodingState() {
        recordAndPauseSwitchContainer.switchIndex(INDEX_PAUSE);
        if (audioRecordProgressView.isPaused()) {
            audioRecordProgressView.resume();
        } else {
            audioRecordProgressView.start();
        }
        audioRecordContainer.setVisibility(VISIBLE);
        audioPlayContainer.setVisibility(GONE);
    }

    private void switchToPauseRecordingState() {
        updatePlayState(null);
        recordAndPauseSwitchContainer.switchIndex(INDEX_RECORD);
        audioRecordProgressView.pause();
        audioRecordContainer.setVisibility(GONE);
        audioPlayContainer.setVisibility(VISIBLE);
    }

    public void postVolume(float volume) {
        audioRecordProgressView.setCurrentVolume(volume);
    }

    public void start() {
        switchToRecodingState();
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

    public void registerListener(String uuid) {
        this.uuid = uuid;
        if (audioListener == null) {
            audioListener = new AudioListener(uuid) {
                @Override
                public void onAudioStatusChanged(@NonNull AudioStatusBean audioStatus) {
                    ThreadPool.runUi(new SafeRunnable(AudioDraftView.this) {
                        @Override
                        protected void runSafely() {
                            updatePlayState(audioStatus);
                        }
                    });
                }
            };
        } else {
            audioListener.setKey(uuid);
        }
        SdkFactory.getAudioSdk().registerAudioPlayListener(audioListener);
    }

    private void updatePlayState(@Nullable AudioStatusBean audioStatus) {
        if (audioStatus == null) {
            setNotPlaying(SdkFactory.getAudioSdk().getAudioDraft().getDuration());
        } else {
            switch (audioStatus.getAudioStatusCode()) {
                case AudioStatusCode.STATUS_AUDIO_START:
                    setPlaying(audioStatus.getPercent(), audioStatus.getProgress(), false);
                    break;
                case AudioStatusCode.STATUS_AUDIO_NOT_PLAYING:
                    setNotPlaying(audioStatus.getTotal());
                    break;
                case AudioStatusCode.STATUS_AUDIO_PLAYING:
                    setPlaying(audioStatus.getPercent(), audioStatus.getProgress(), true);
                    break;
                case AudioStatusCode.STATUS_AUDIO_PAUSED:
                    setPausePlaying(audioStatus.getPercent(), audioStatus.getProgress());
                    break;
            }
        }
    }

    private void setPlaying(float percent, long currentPosition, boolean animate) {
        if (audioPlaySwitchContainer.getIndex() != SmoothSwitchFrameLayout.INDEX_SECOND) {
            audioPlaySwitchContainer.switchIndex(SmoothSwitchFrameLayout.INDEX_SECOND);
        }
        audioPlayDurationView.setText(TimeUtils.formatLongToDuration(currentPosition));
        audioPlayProgressView.setPercent(percent, animate);
    }

    private void setPausePlaying(float percent, long currentPosition) {
        if (audioPlaySwitchContainer.getIndex() != SmoothSwitchFrameLayout.INDEX_DEFAULT) {
            audioPlaySwitchContainer.switchIndex(SmoothSwitchFrameLayout.INDEX_DEFAULT);
        }
        audioPlayDurationView.setText(TimeUtils.formatLongToDuration(currentPosition));
        audioPlayProgressView.setPercent(percent, false);
    }

    private void setNotPlaying(long duration) {
        if (audioPlaySwitchContainer.getIndex() != INDEX_PLAY) {
            audioPlaySwitchContainer.switchIndex(INDEX_PLAY);
        }
        audioPlayDurationView.setText(TimeUtils.formatLongToDuration(duration));
        audioPlayProgressView.setPercent(0, false);
    }

    private void unregisterListener() {
        if (audioListener != null) {
            SdkFactory.getAudioSdk().unregisterAudioPlayListener(audioListener);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility != VISIBLE) {
            unregisterListener();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unregisterListener();
    }

    public interface AudioDraftListener {
        void onClickSend();

        void onClickDelete();

        void onPauseRecord();

        void onResumeRecord();

        void onPlay(float percent);

        void onPausePlay();

        boolean isPlayingDraft();

        void pausePlaying();

        long getAudioDuration();
    }
}
