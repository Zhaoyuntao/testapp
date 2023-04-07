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
import im.thebot.chat.api.chat.message.ImageMessageForUI;
import im.thebot.chat.ui.cells.origin.base.BaseFileCell;
import im.thebot.chat.ui.view.ChatImageView;
import im.turbo.basetools.state.StateFetchListener;
import im.turbo.baseui.imageview.AnimateImageView;
import im.turbo.baseui.progress.TProgressView;
import im.turbo.baseui.progress.ViewMode;

/**
 * created by zhaoyuntao
 * on 18/05/2020
 * description:
 */
public class ImageCell extends BaseFileCell<ImageMessageForUI> {
    private ChatImageView imageView;
    private TextView timeViewInImage;
    private AnimateImageView statusViewInImage;
    private ViewGroup textContainer;
    private View shadowView;
    private TProgressView progressView;
    private View centerContainer;
    private View retryContainer;
    private ImageView retryViewIcon;
    private TextView retryViewText;

    @Override
    public int setLayout() {
        return R.layout.layout_chat_cell_item_file_image;
    }

    @Override
    protected void initTextTypeView(@NonNull Context context) {
        imageView = findViewById(R.id.image_cell_image_view);
        timeViewInImage = findViewById(R.id.image_cell_send_time_view);
        statusViewInImage = findViewById(R.id.image_cell_send_status_view);
        textContainer = findViewById(R.id.image_cell_text_container);
        shadowView = findViewById(R.id.image_cell_shadow_view);
        progressView = findViewById(R.id.image_cell_progress_view);
        centerContainer = findViewById(R.id.image_cell_center_container);
        retryContainer = findViewById(R.id.image_cell_retry_view_container);
        retryViewIcon = findViewById(R.id.image_cell_retry_view_icon);
        retryViewText = findViewById(R.id.image_cell_retry_view_text);
    }

    @Override
    public void onSetStatusDrawable(@NonNull ImageMessageForUI message, int visible, int drawableRes, boolean animate) {
        if (!hasText(message)) {
            statusViewInImage.setVisibility(visible);
            statusViewInImage.setImageResource(drawableRes, animate);
        } else {
            statusViewInImage.setVisibility(GONE);
        }
    }

    @Override
    protected void onInitViewMode(@NonNull ImageMessageForUI message) {
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
                                hideCenterContainer();
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
                                hideCenterContainer();
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
                                hideRetryContainer();
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
                                hideRetryContainer();
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_UPLOAD_WAITING)
                        .drawable(R.drawable.svg_close_white)
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
                                hideRetryContainer();
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_DOWNLOAD_WAITING)
                        .drawable(R.drawable.svg_close_white)
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
                                hideRetryContainer();
                            }
                        })
        );
    }

    @Override
    protected void onClearViewMode() {
        progressView.clearVideMode();
    }

    @Override
    public void onSetTimestamp(@NonNull ImageMessageForUI message, String timeString) {
        if (!hasText(message)) {
            timeViewInImage.setVisibility(VISIBLE);
            timeViewInImage.setText(timeString);
        } else {
            timeViewInImage.setVisibility(GONE);
        }
    }

    private boolean hasText(@NonNull ImageMessageForUI message) {
        return !TextUtils.isEmpty(message.getContent());
    }

    @Override
    protected void onFileMessageInit(@NonNull ImageMessageForUI message) {
        boolean hasText = hasText(message);
        textContainer.setVisibility(hasText ? VISIBLE : GONE);
        timeViewInImage.setVisibility(hasText ? GONE : VISIBLE);
        shadowView.setVisibility(hasText ? GONE : VISIBLE);
        imageView.bindMessage(message);
//        imageView.adjustViewSize(message.getImageWidth(), message.getImageHeight());
    }

    private void showRetryUpload() {
        centerContainer.setVisibility(VISIBLE);
        retryContainer.setVisibility(VISIBLE);
        retryViewIcon.setImageResource(R.drawable.svg_upload_white);
        retryViewText.setText(R.string.retry);
        retryContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });
    }

    private void showRetryDownload() {
        ImageMessageForUI message = getMessage();
        centerContainer.setVisibility(VISIBLE);
        retryContainer.setVisibility(VISIBLE);
        retryViewIcon.setImageResource(R.drawable.svg_download_white);
        retryViewText.setText(message.getFileSize() / 1024L + " KB");
        retryContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile();
            }
        });
    }

    private void hideCenterContainer() {
        centerContainer.setVisibility(GONE);
    }

    private void hideRetryContainer() {
        retryContainer.setVisibility(GONE);
    }

    @Override
    protected void onFileMessageChanged(@NonNull ImageMessageForUI message) {
    }

    @Override
    protected void onFileExists(@NonNull ImageMessageForUI message) {
        progressView.setCurrentMode(message.isSelf() ? FileStatusCode.STATUS_FILE_UPLOAD_COMPLETED : FileStatusCode.STATUS_FILE_DOWNLOAD_COMPLETED);
    }

    @Override
    protected void onFileNotExists(@NonNull ImageMessageForUI message) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_NOT_FOUND);
        if (!message.isSelf()) {
            downloadFile();
        }
    }

    @Override
    protected void onDownloadWaiting(@NonNull ImageMessageForUI message) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_DOWNLOAD_WAITING);
    }

    @Override
    protected void onDownloading(@NonNull ImageMessageForUI message, long progress, long total, float percent) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_DOWNLOADING);
        progressView.setProgress((int) (100 * percent), 100);
    }

    @Override
    protected void onDownloadSuccess(@NonNull ImageMessageForUI message) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_DOWNLOAD_COMPLETED);
    }

    @Override
    protected void onDownloadFailed(@NonNull ImageMessageForUI message) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_DOWNLOAD_FAILED);
    }

    @Override
    protected void onDownloadCanceled(@NonNull ImageMessageForUI message) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_DOWNLOAD_CANCELED);
    }

    @Override
    protected void onUploadWaiting(@NonNull ImageMessageForUI message) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_UPLOAD_WAITING);
    }

    @Override
    protected void onUploading(@NonNull ImageMessageForUI message, float progress, long total, float percent) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_UPLOADING);
        progressView.setProgress((int) (100 * percent), 100);
    }

    @Override
    protected void onUploadSuccess(@NonNull ImageMessageForUI message) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_UPLOAD_COMPLETED);
    }

    @Override
    protected void onUploadFailed(@NonNull ImageMessageForUI message) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_UPLOAD_FAILED);
    }

    @Override
    protected void onUploadCanceled(@NonNull ImageMessageForUI message) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_UPLOAD_CANCELED);
    }

    @Override
    public void onClickMessage(@NonNull ImageMessageForUI message) {
        //todo lv2
//        Bundle bundle = new Bundle();
//        bundle.putString(AppParams.UID, message.getSessionId());
//        bundle.putString(AppParams.UUID, message.getUUID());
//        IntentUtils.startActivity(getContext(), MediaPreviewActivity.class, bundle);
    }

    @Override
    protected void loadMedia(@NonNull ImageMessageForUI message) {
//        if (TextUtils.isEmpty(message.getFileLocalPath())) {
//            imageView.setImageBase64(message.getImagePreviewBase64());
//        } else {
//            imageView.setImageLocal(message.getFileLocalPath());
//        }
    }
}
