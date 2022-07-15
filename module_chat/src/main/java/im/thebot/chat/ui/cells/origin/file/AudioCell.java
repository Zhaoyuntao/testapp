package im.thebot.chat.ui.cells.origin.file;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.module_chat.R;
import com.sdk.chat.file.audio.AudioListener;
import com.sdk.chat.file.audio.AudioStatusBean;
import com.sdk.chat.file.audio.AudioPlayStatusCode;

import im.thebot.SdkFactory;
import im.thebot.api.chat.constant.FileStatusCode;
import im.thebot.chat.api.chat.message.AudioMessageForUI;
import im.thebot.chat.ui.cells.origin.base.BaseFileCell;
import im.thebot.chat.ui.view.AudioPlayProgressView;
import im.thebot.chat.ui.view.OnProgressChangedListener;
import im.thebot.common.UserFaceView;
import im.turbo.basetools.state.StateFetchListener;
import im.turbo.basetools.time.TimeUtils;
import im.turbo.baseui.chat.SmoothScaleFrameLayout;
import im.turbo.baseui.permission.Permission;
import im.turbo.baseui.permission.PermissionResult;
import im.turbo.baseui.permission.PermissionUtils;
import im.turbo.baseui.progress.TProgressView;
import im.turbo.baseui.progress.ViewMode;
import im.turbo.thread.SafeRunnable;
import im.turbo.thread.ThreadPool;
import im.turbo.utils.log.S;

/**
 * created by zhaoyuntao
 * on 18/05/2020
 * description:
 */
public class AudioCell extends BaseFileCell<AudioMessageForUI> {

    private ImageView playView;
    private ImageView playingView;
    private TextView durationView;
    private ImageView micIconView;
    private UserFaceView userFaceView;
    private SmoothScaleFrameLayout playContainer;
    private AudioPlayProgressView playProgressView;
    private TProgressView downLoadProgressView;

    private AudioListener listener;
    private int colorSliderPlayed, colorSliderNotPlayed, colorSliderSelf;

    @Override
    public int setLayout() {
        return R.layout.layout_chat_cell_item_file_audio;
    }

    @Override
    public boolean isMaxWidth() {
        return true;
    }

    @Override
    protected void initTextTypeView(@NonNull Context context) {
        colorSliderPlayed = ContextCompat.getColor(context, R.color.color_chat_audio_cell_progress_slider_played);
        colorSliderNotPlayed = ContextCompat.getColor(context, R.color.color_chat_audio_cell_progress_slider);
        colorSliderSelf = ContextCompat.getColor(context, R.color.color_chat_audio_cell_progress_slider_me);
        playProgressView = findViewById(R.id.audio_chat_cell_play_progress);
        userFaceView = findViewById(R.id.audio_chat_cell_face_view);
        micIconView = findViewById(R.id.audio_chat_cell_mic_icon);

        playContainer = findViewById(R.id.audio_chat_cell_play_container);
        playView = findViewById(R.id.audio_cell_play_view);
        playingView = findViewById(R.id.audio_cell_playing_view);

        durationView = findViewById(R.id.audio_chat_cell_duration);
        downLoadProgressView = findViewById(R.id.audio_cell_download_progress_view);
    }

    @Override
    protected void onFileMessageInit(@NonNull AudioMessageForUI message) {
        userFaceView.bindUid(message.getSenderUid());
        durationView.setText(TimeUtils.formatLongToDuration(message.getAudioDuration()));
        playProgressView.setOnProgressChangedListener(new OnProgressChangedListener() {
            private boolean isPlayingBeforePause;

            @Override
            public void onStartDragging() {
                isPlayingBeforePause = SdkFactory.getAudioSdk().getAudioStatus(message.getSessionId(), message.getUUID()).getAudioStatusCode() == AudioPlayStatusCode.STATUS_AUDIO_PLAYING;
            }

            @Override
            public void onProgressChanged(float percent, boolean dragByUser) {
                if (dragByUser) {
                    if (isPlayingBeforePause) {
                        isPlayingBeforePause = false;
                        getPresenter().playAudio(getMessage(), (int) (getMessage().getAudioDuration() * percent));
                    }
                }
            }
        });


        playView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionUtils.requestPermission(getContext(), new PermissionResult() {
                    @Override
                    public void onGranted(@NonNull String[] grantedPermissions) {
                        getPresenter().playAudio(getMessage(), calculateCurrentPlayingProgress());
                    }

                    @Override
                    public void onDenied(@NonNull String[] deniedPermissions) {
                        S.s("onDenied");
                    }

                    @Override
                    public void onCanceled(@NonNull String[] permissions) {
                        S.e("onCanceled");
                    }
                }, Permission.READ_EXTERNAL_STORAGE);
            }
        });
        playingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().pausePlayingAudio();
            }
        });
    }

    private int calculateCurrentPlayingProgress() {
        return (int) (getMessage().getAudioDuration() * playProgressView.getPercent());
    }

    @Override
    protected void onRegisterListener() {
        if (listener == null) {
            listener = new AudioListener(getMessage().getUUID()) {
                @Override
                public void onAudioStatusChanged(@NonNull AudioStatusBean status) {
                    if (status.getAudioStatusCode() == AudioPlayStatusCode.STATUS_AUDIO_NOT_PLAYING) {
                        S.s("onAudioStatusChanged:" + status.getAudioStatusCode() + " " + status.getPercent() + " " + status.getErrorMessage());
                    }
                    ThreadPool.runUi(new SafeRunnable(getContext()) {
                        @Override
                        protected void runSafely() {
                            if (!status.getKey().equals(getTag())) {
                                return;
                            }
                            updateAudioPlayState(status);
                        }
                    });
                }
            };
        } else {
            listener.setKey(getMessage().getUUID());
        }
        if (isAttachedToWindow()) {
            SdkFactory.getAudioSdk().registerAudioPlayListener(listener);
        }
        updateAudioPlayState(null);
    }

    @Override
    protected void onUnregisterListener() {
        SdkFactory.getAudioSdk().unregisterAudioPlayListener(listener);
    }

    private void updateAudioPlayState(@Nullable AudioStatusBean audioStatus) {
        if (audioStatus == null) {
            audioStatus = SdkFactory.getAudioSdk().getAudioStatus(getMessage().getSessionId(), getMessage().getUUID());
        }
        switch (audioStatus.getAudioStatusCode()) {
            case AudioPlayStatusCode.STATUS_AUDIO_START:
                setPlaying(audioStatus.getPercent(), false);
                break;
            case AudioPlayStatusCode.STATUS_AUDIO_NOT_PLAYING:
                setNotPlaying();
                break;
            case AudioPlayStatusCode.STATUS_AUDIO_PLAYING:
                setPlaying(audioStatus.getPercent(), true);
                break;
            case AudioPlayStatusCode.STATUS_AUDIO_PAUSED:
                setPausePlaying(audioStatus.getPercent());
                break;
        }
    }

    @Override
    protected void onFileMessageChanged(@NonNull AudioMessageForUI message) {
        updateAudioPlayState(null);
    }

    private void setPlaying(float percent, boolean animate) {
        if (playContainer.getVisibility() != VISIBLE) {
            playContainer.setVisibility(View.VISIBLE);
        }
        if (playContainer.getIndex() != SmoothScaleFrameLayout.INDEX_SECOND) {
            playContainer.switchIndex(SmoothScaleFrameLayout.INDEX_SECOND);
        }
        playProgressView.setPercent(percent, animate);
        AudioMessageForUI message = getMessage();
        int color = message.isSelf() ? colorSliderSelf : colorSliderPlayed;
        micIconView.setImageTintList(ColorStateList.valueOf(color));
    }

    private void setPausePlaying(float percent) {
        if (playContainer.getVisibility() != VISIBLE) {
            playContainer.setVisibility(View.VISIBLE);
        }
        if (playContainer.getIndex() != SmoothScaleFrameLayout.INDEX_DEFAULT) {
            playContainer.switchIndex(SmoothScaleFrameLayout.INDEX_DEFAULT);
        }
        playProgressView.setPercent(percent, false);
    }

    private void setNotPlaying() {
        if (playContainer.getVisibility() != VISIBLE) {
            playContainer.setVisibility(View.VISIBLE);
        }
        if (playContainer.getIndex() != SmoothScaleFrameLayout.INDEX_DEFAULT) {
            playContainer.switchIndex(SmoothScaleFrameLayout.INDEX_DEFAULT);
        }
        AudioMessageForUI message = getMessage();
        int color;
        if (message.isSelf()) {
            color = colorSliderSelf;
        } else {
            color = (message.getAudioPlayedTime() > 0 || message.getAudioPlayedProgress() > 0) ? colorSliderPlayed : colorSliderNotPlayed;
        }
        micIconView.setImageTintList(ColorStateList.valueOf(color));
        playProgressView.setPercent(0, false);
    }

    @Override
    protected void onFileExists(@NonNull AudioMessageForUI message) {

    }

    @Override
    protected void onInitViewMode(@NonNull AudioMessageForUI message) {
        downLoadProgressView.setViewMode(
                new ViewMode(FileStatusCode.STATUS_FILE_NOT_FOUND)
                        .showProgress(false)
                        .drawable(R.drawable.chat_voice_download_normal)
                        .listener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                downloadFile();
                            }
                        })
                        .stateListener(new StateFetchListener() {
                            @Override
                            public void onFetched(@NonNull String stateTag) {
                                setFileNotExits();
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_DOWNLOADING)
                        .showProgress(true)
                        .drawable(R.drawable.chat_voice_cancel_normal)
                        .listener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelDownloadFile();
                            }
                        })
                        .stateListener(new StateFetchListener() {
                            @Override
                            public void onFetched(@NonNull String stateTag) {
                                setFileNotExits();
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_DOWNLOAD_COMPLETED)
                        .visible(GONE)
                        .stateListener(new StateFetchListener() {
                            @Override
                            public void onFetched(@NonNull String stateTag) {
                                updateAudioPlayState(null);
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_DOWNLOAD_CANCELED)
                        .drawable(R.drawable.chat_voice_download_normal)
                        .listener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                downloadFile();
                            }
                        })
                        .stateListener(new StateFetchListener() {
                            @Override
                            public void onFetched(@NonNull String stateTag) {
                                setFileNotExits();
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_DOWNLOAD_FAILED)
                        .drawable(R.drawable.chat_voice_download_normal)
                        .listener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                downloadFile();
                            }
                        })
                        .stateListener(new StateFetchListener() {
                            @Override
                            public void onFetched(@NonNull String stateTag) {
                                setFileNotExits();
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_UPLOADING)
                        .showProgress(true)
                        .drawable(R.drawable.chat_voice_cancel_normal)
                        .listener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelUploadFile();
                            }
                        })
                        .stateListener(new StateFetchListener() {
                            @Override
                            public void onFetched(@NonNull String stateTag) {
                                setFileNotExits();
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_UPLOAD_COMPLETED)
                        .visible(GONE)
                        .stateListener(new StateFetchListener() {
                            @Override
                            public void onFetched(@NonNull String stateTag) {
                                updateAudioPlayState(null);
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_UPLOAD_CANCELED)
                        .drawable(R.drawable.chat_voice_upload_normal)
                        .listener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                uploadFile();
                            }
                        })
                        .stateListener(new StateFetchListener() {
                            @Override
                            public void onFetched(@NonNull String stateTag) {
                                setFileNotExits();
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_UPLOAD_FAILED)
                        .drawable(R.drawable.chat_voice_upload_normal)
                        .listener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                uploadFile();
                            }
                        })
                        .stateListener(new StateFetchListener() {
                            @Override
                            public void onFetched(@NonNull String stateTag) {
                                setFileNotExits();
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_UPLOAD_WAITING)
                        .drawable(R.drawable.chat_voice_cancel_normal)
                        .useWaveProgress(true)
                        .showProgress(true)
                        .rotate(true)
                        .listener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelUploadFile();
                            }
                        })
                        .stateListener(new StateFetchListener() {
                            @Override
                            public void onFetched(@NonNull String stateTag) {
                                setFileNotExits();
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_DOWNLOAD_WAITING)
                        .drawable(R.drawable.chat_voice_cancel_normal)
                        .useWaveProgress(true)
                        .showProgress(true)
                        .rotate(true)
                        .listener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelDownloadFile();
                            }
                        })
                        .stateListener(new StateFetchListener() {
                            @Override
                            public void onFetched(@NonNull String stateTag) {
                                setFileNotExits();
                            }
                        })
        );
    }

    private void setFileNotExits() {
        playContainer.setVisibility(View.GONE);
        playProgressView.setPercent(0, false);
    }

    @Override
    protected void onClearViewMode() {

    }

    @Override
    protected void onFileNotExists(@NonNull AudioMessageForUI message) {
        downLoadProgressView.setCurrentMode(FileStatusCode.STATUS_FILE_NOT_FOUND);
        if (!message.isSelf()) {
            downloadFile();
        }
    }

    @Override
    protected void onDownloadWaiting(@NonNull AudioMessageForUI message) {
        downLoadProgressView.setCurrentMode(FileStatusCode.STATUS_FILE_DOWNLOAD_WAITING);
    }

    @Override
    protected void onDownloading(@NonNull AudioMessageForUI message, long progress, long total, float percent) {
        downLoadProgressView.setCurrentMode(FileStatusCode.STATUS_FILE_DOWNLOADING);
        downLoadProgressView.setProgress((int) (100 * percent), 100);
    }

    @Override
    protected void onDownloadSuccess(@NonNull AudioMessageForUI message) {
        downLoadProgressView.setCurrentMode(FileStatusCode.STATUS_FILE_DOWNLOAD_COMPLETED);
    }

    @Override
    protected void onDownloadFailed(@NonNull AudioMessageForUI message) {
        downLoadProgressView.setCurrentMode(FileStatusCode.STATUS_FILE_DOWNLOAD_FAILED);
    }

    @Override
    protected void onDownloadCanceled(@NonNull AudioMessageForUI message) {
        downLoadProgressView.setCurrentMode(FileStatusCode.STATUS_FILE_DOWNLOAD_CANCELED);
    }

    @Override
    protected void onUploadWaiting(@NonNull AudioMessageForUI message) {
        downLoadProgressView.setCurrentMode(FileStatusCode.STATUS_FILE_UPLOAD_WAITING);
    }

    @Override
    protected void onUploading(@NonNull AudioMessageForUI message, float progress, long total, float percent) {
        downLoadProgressView.setCurrentMode(FileStatusCode.STATUS_FILE_UPLOADING);
        downLoadProgressView.setProgress((int) (100 * percent), 100);
    }

    @Override
    protected void onUploadSuccess(@NonNull AudioMessageForUI message) {
        downLoadProgressView.setCurrentMode(FileStatusCode.STATUS_FILE_UPLOAD_COMPLETED);
    }

    @Override
    protected void onUploadFailed(@NonNull AudioMessageForUI message) {
        downLoadProgressView.setCurrentMode(FileStatusCode.STATUS_FILE_UPLOAD_FAILED);
    }

    @Override
    protected void onUploadCanceled(@NonNull AudioMessageForUI message) {
        downLoadProgressView.setCurrentMode(FileStatusCode.STATUS_FILE_UPLOAD_CANCELED);
    }

    @Override
    protected void loadMedia(@NonNull AudioMessageForUI message) {

    }
}
