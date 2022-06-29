package im.thebot.chat.ui.cells.origin.file;


import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.module_chat.R;

import im.thebot.api.chat.constant.FileStatusCode;
import im.thebot.chat.api.chat.message.VideoMessageForUI;
import im.thebot.chat.ui.cells.origin.base.BaseFileCell;
import im.thebot.chat.ui.view.ChatImageView;
import im.turbo.basetools.state.StateFetchListener;
import im.turbo.basetools.time.TimeUtils;
import im.turbo.baseui.imageview.AnimateImageView;
import im.turbo.baseui.progress.TProgressView;
import im.turbo.baseui.progress.ViewMode;

/**
 * created by zhaoyuntao
 * on 18/05/2020
 * description:
 */
public class VideoCell extends BaseFileCell<VideoMessageForUI> {
    private ChatImageView imageView;
    private TextView durationView;

    private ViewGroup durationRoot;

    private TProgressView progressView;
    private ViewGroup retryRoot;
    private ViewGroup containerRoot;
    private ImageView retryViewIcon;
    private TextView retryViewText;

    private ViewGroup textContainer;
    private TextView timeViewInVideo;
    private AnimateImageView statusViewInVideo;
    private View shadowView;

    @Override
    public int setLayout() {
        return R.layout.layout_chat_cell_item_file_video;
    }

    @Override
    protected void initTextTypeView(@NonNull Context context) {
        imageView = findViewById(R.id.video_cell_preview);
        durationView = findViewById(R.id.video_cell_text_view_duration);

        durationRoot = findViewById(R.id.video_cell_duration_container);

        progressView = findViewById(R.id.video_cell_progress_view);
        containerRoot = findViewById(R.id.video_cell_center_container);
        retryRoot = findViewById(R.id.video_cell_retry_container);
        retryViewIcon = findViewById(R.id.video_cell_icon_retry);
        retryViewText = findViewById(R.id.video_cell_text_retry);

        textContainer = findViewById(R.id.video_cell_container);
        timeViewInVideo = findViewById(R.id.video_cell_send_time_view);
        statusViewInVideo = findViewById(R.id.video_cell_send_status_view);
        shadowView = findViewById(R.id.video_cell_shadow_view);
    }

    @Override
    protected void onInitViewMode(@NonNull VideoMessageForUI message) {
        progressView.setViewMode(
                new ViewMode(FileStatusCode.STATUS_FILE_NOT_FOUND)
                        .visible(GONE)
                        .stateListener(new StateFetchListener() {
                            @Override
                            public void onFetched(@NonNull String stateTag) {
                                showRetryDownload();
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_UPLOAD_FAILED)
                        .visible(GONE)
                        .stateListener(new StateFetchListener() {
                            @Override
                            public void onFetched(@NonNull String stateTag) {
                                showRetryUpload();
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_UPLOAD_CANCELED)
                        .visible(GONE)
                        .stateListener(new StateFetchListener() {
                            @Override
                            public void onFetched(@NonNull String stateTag) {
                                showRetryUpload();
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_UPLOAD_COMPLETED)
                        .visible(GONE)
                        .stateListener(new StateFetchListener() {
                            @Override
                            public void onFetched(@NonNull String stateTag) {
                                showDurationView();
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_DOWNLOAD_FAILED)
                        .visible(GONE)
                        .stateListener(new StateFetchListener() {
                            @Override
                            public void onFetched(@NonNull String stateTag) {
                                showRetryDownload();
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_DOWNLOAD_CANCELED)
                        .visible(GONE)
                        .stateListener(new StateFetchListener() {
                            @Override
                            public void onFetched(@NonNull String stateTag) {
                                showRetryDownload();
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_DOWNLOAD_COMPLETED)
                        .visible(GONE)
                        .stateListener(new StateFetchListener() {
                            @Override
                            public void onFetched(@NonNull String stateTag) {
                                showDurationView();
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_UPLOADING)
                        .showProgress(true)
                        .drawable(R.drawable.svg_close_white)
                        .listener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelUploadFile();
                            }
                        })
                        .stateListener(new StateFetchListener() {
                            @Override
                            public void onFetched(@NonNull String stateTag) {
                                showProgress();
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_DOWNLOADING)
                        .showProgress(true)
                        .drawable(R.drawable.svg_close_white)
                        .listener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelDownloadFile();
                            }
                        })
                        .stateListener(new StateFetchListener() {
                            @Override
                            public void onFetched(@NonNull String stateTag) {
                                showProgress();
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_UPLOAD_WAITING)
                        .drawable(R.drawable.svg_close_white)
                        .useWaveProgress(true)
                        .showProgress(true)
                        .rotate(true)
                        .listener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelUploadFile();
                            }
                        })
                        .stateListener(new StateFetchListener() {
                            @Override
                            public void onFetched(@NonNull String stateTag) {
                                showProgress();
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_DOWNLOAD_WAITING)
                        .drawable(R.drawable.svg_close_white)
                        .useWaveProgress(true)
                        .showProgress(true)
                        .rotate(true)
                        .listener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelDownloadFile();
                            }
                        })
                        .stateListener(new StateFetchListener() {
                            @Override
                            public void onFetched(@NonNull String stateTag) {
                                showProgress();
                            }
                        })
        );
    }

    @Override
    protected void onClearViewMode() {
        progressView.clearVideMode();
    }

    @Override
    public void onSetStatusDrawable(@NonNull VideoMessageForUI messageBean, int visible, int drawableRes, boolean animate) {
        if (!hasText(messageBean)) {
            statusViewInVideo.setVisibility(visible);
            statusViewInVideo.setImageResource(drawableRes, animate);
        } else {
            statusViewInVideo.setVisibility(GONE);
        }
    }

    @Override
    public void onSetTimestamp(@NonNull VideoMessageForUI messageBean, String timeString) {
        if (!hasText(messageBean)) {
            timeViewInVideo.setVisibility(VISIBLE);
            timeViewInVideo.setText(timeString);
        } else {
            timeViewInVideo.setVisibility(GONE);
        }
    }

    private boolean hasText(@NonNull VideoMessageForUI messageBean) {
        return !TextUtils.isEmpty(messageBean.getContent());
    }

    @Override
    protected void onFileMessageInit(@NonNull VideoMessageForUI message) {
        boolean hasText = hasText(message);
        textContainer.setVisibility(hasText ? VISIBLE : GONE);
        timeViewInVideo.setVisibility(hasText ? GONE : VISIBLE);
        shadowView.setVisibility(hasText ? GONE : VISIBLE);
//        imageView.adjustViewSize(message.getVideoWidth(), message.getVideoHeight());
        imageView.bindMessage(message);
        long duration = message.getVideoDuration();
        String durationString = TimeUtils.formatLongToDuration(duration);
//        S.s("duration:" + duration + " " + durationString);
        durationView.setText(durationString);
    }

    @Override
    protected void onFileMessageChanged(@NonNull VideoMessageForUI message) {

    }

    private void showDurationView() {
        durationRoot.setVisibility(VISIBLE);
        retryRoot.setVisibility(GONE);
        containerRoot.setVisibility(GONE);
    }

    private void showRetryUpload() {
        durationRoot.setVisibility(GONE);
        containerRoot.setVisibility(VISIBLE);
        retryRoot.setVisibility(VISIBLE);

        retryViewIcon.setImageResource(R.drawable.svg_upload_white);
        retryViewText.setText(R.string.retry);
        retryRoot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });
    }

    private void showRetryDownload() {
        VideoMessageForUI message = getMessage();
        durationRoot.setVisibility(GONE);
        containerRoot.setVisibility(VISIBLE);
        retryRoot.setVisibility(VISIBLE);

        retryViewIcon.setImageResource(R.drawable.svg_download_white);
        //todo
        retryViewText.setText(message.getFileSize() / 1024 + " KB");
        retryRoot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile();
            }
        });
    }

    private void showProgress() {
        durationRoot.setVisibility(GONE);
        containerRoot.setVisibility(VISIBLE);
        retryRoot.setVisibility(GONE);
    }

    @Override
    protected void onFileExists(@NonNull VideoMessageForUI message) {
        progressView.setCurrentMode(message.isSelf() ? FileStatusCode.STATUS_FILE_UPLOAD_COMPLETED : FileStatusCode.STATUS_FILE_DOWNLOAD_COMPLETED);
        showDurationView();
    }

    @Override
    protected void onFileNotExists(@NonNull VideoMessageForUI message) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_NOT_FOUND);
        downloadFile();
    }

    @Override
    protected void onDownloadWaiting(@NonNull VideoMessageForUI message) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_DOWNLOAD_WAITING);
    }

    @Override
    protected void onDownloading(@NonNull VideoMessageForUI message, long progress, long total, float percent) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_DOWNLOADING);
        progressView.setProgress((int) (100 * percent), 100);
        durationRoot.setVisibility(GONE);
        retryRoot.setVisibility(GONE);
    }

    @Override
    protected void onDownloadSuccess(@NonNull VideoMessageForUI message) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_DOWNLOAD_COMPLETED);
    }

    @Override
    protected void onDownloadFailed(@NonNull VideoMessageForUI message) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_DOWNLOAD_FAILED);
    }

    @Override
    protected void onDownloadCanceled(@NonNull VideoMessageForUI message) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_DOWNLOAD_CANCELED);
    }

    @Override
    protected void onUploadWaiting(@NonNull VideoMessageForUI message) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_UPLOAD_WAITING);
    }

    @Override
    protected void onUploading(@NonNull VideoMessageForUI message, float progress, long total, float percent) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_UPLOADING);
        progressView.setProgress((int) (100 * percent), 100);
        durationRoot.setVisibility(GONE);
        retryRoot.setVisibility(GONE);
    }

    @Override
    protected void onUploadSuccess(@NonNull VideoMessageForUI message) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_UPLOAD_COMPLETED);
    }

    @Override
    protected void onUploadFailed(@NonNull VideoMessageForUI message) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_UPLOAD_FAILED);
    }

    @Override
    protected void onUploadCanceled(@NonNull VideoMessageForUI message) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_UPLOAD_CANCELED);
    }

    @Override
    public void onClickMessage(@NonNull VideoMessageForUI messageBean) {
    }

    @Override
    protected void loadMedia(@NonNull VideoMessageForUI message) {
//        S.s("message.getVideoWidth(), message.getVideoHeight():" + message.getVideoWidth() + ",   " + message.getVideoHeight());
//        if (TextUtils.isEmpty(message.getFileLocalPath())) {
//            imageView.setImageBase64(message.getVideoPreviewBase64());
//        } else {
//            imageView.setVideoLocal(message.getFileLocalPath());
//        }
    }
}
