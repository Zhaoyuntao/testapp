package im.thebot.chat.ui.cells.origin.file;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import android.view.View;
import android.view.View.OnClickListener;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.module_chat.R;

import im.thebot.api.chat.constant.FileStatusCode;
import im.thebot.chat.api.chat.message.FileMessageForUI;
import im.thebot.chat.ui.cells.origin.base.BaseFileCell;
import im.thebot.chat.ui.uiutils.file.FileIconUtils;
import im.turbo.basetools.file.FileUtils;
import im.turbo.baseui.progress.TProgressView;
import im.turbo.baseui.progress.ViewMode;

/**
 * created by zhaoyuntao
 * on 18/05/2020
 * description:
 */
public class FileCell extends BaseFileCell<FileMessageForUI> {
    private TextView fileNameView;
    private TextView fileProgressInfoView;
    private ImageView fileIconView;
    private TextView messageMediaInfoView;
    private View backgroundView;
    private TProgressView progressView;

    public FileCell(Context context) {
        super(context);
    }

    @Override
    public int setLayout() {
        return R.layout.layout_chat_cell_item_file;
    }

    @Override
    public boolean isMaxWidth() {
        return true;
    }

    @Override
    public boolean showMessageForwardView() {
        return true;
    }

    @Override
    protected void initTextTypeView(@NonNull Context context) {
        fileNameView = findViewById(R.id.file_chat_cell_name);
        fileProgressInfoView = findViewById(R.id.file_chat_cell_text_progress);
        fileIconView = findViewById(R.id.file_format_chat_cell);
        messageMediaInfoView = findViewById(R.id.message_type_info_text_view);
        backgroundView = findViewById(R.id.background_file_name_chat_cell);
        progressView = findViewById(R.id.progress_view_file_cell);
    }

    @Override
    protected void onFileMessageInit(@NonNull FileMessageForUI message) {
        fileNameView.setText(message.getFileName());
        String suffixDesc;
        if (!TextUtils.isEmpty(message.getFileName())) {
            String[] suffix = message.getFileName().split("\\.");
            suffixDesc = " · " + suffix[suffix.length - 1].toUpperCase();
        } else {
            suffixDesc = "";
        }
        String mediaInfo = FileUtils.sizeString(message.getFileSize()) + suffixDesc;
        messageMediaInfoView.setText(mediaInfo);
        fileIconView.setImageResource(FileIconUtils.getIconResource(message));
        onMessageChanged(message);
        if (message.isSelf()) {
            backgroundView.setBackgroundResource(R.drawable.background_cell_content_me);
        } else {
            backgroundView.setBackgroundResource(R.drawable.background_cell_content_other);
        }
    }

    @Override
    protected void onInitViewMode(@NonNull FileMessageForUI message) {
        progressView.setViewMode(
                new ViewMode(FileStatusCode.STATUS_FILE_NOT_FOUND)
                        .showProgress(false)
                        .drawable(R.drawable.chat_voice_download_normal)
                        .listener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                downloadFile();
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_DOWNLOADING)
                        .showProgress(true)
                        .drawable(R.drawable.chat_voice_cancel_normal)
                        .listener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelDownloadFile();
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_DOWNLOAD_COMPLETED)
                        .visible(GONE),
                new ViewMode(FileStatusCode.STATUS_FILE_DOWNLOAD_CANCELED)
                        .drawable(R.drawable.chat_voice_download_normal)
                        .listener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                downloadFile();
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_DOWNLOAD_FAILED)
                        .drawable(R.drawable.chat_voice_download_normal)
                        .listener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                downloadFile();
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_UPLOADING)
                        .showProgress(true)
                        .drawable(R.drawable.chat_voice_cancel_normal)
                        .listener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelUploadFile();
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_UPLOAD_COMPLETED)
                        .visible(GONE),
                new ViewMode(FileStatusCode.STATUS_FILE_UPLOAD_CANCELED)
                        .drawable(R.drawable.chat_voice_upload_normal)
                        .listener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                uploadFile();
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_UPLOAD_FAILED)
                        .drawable(R.drawable.chat_voice_upload_normal)
                        .listener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                uploadFile();
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_UPLOAD_WAITING)
                        .drawable(R.drawable.chat_voice_cancel_normal)
                        .useWaveProgress(true)
                        .showProgress(true)
                        .rotate(true)
                        .listener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelUploadFile();
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_DOWNLOAD_WAITING)
                        .drawable(R.drawable.chat_voice_cancel_normal)
                        .useWaveProgress(true)
                        .showProgress(true)
                        .rotate(true)
                        .listener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelDownloadFile();
                            }
                        })
        );
    }

    @Override
    protected void onClearViewMode() {
        progressView.clearVideMode();
    }

    @Override
    protected void onFileMessageChanged(@NonNull FileMessageForUI message) {
    }

    @Override
    public void onClickMessage(@NonNull FileMessageForUI message) {
//        LOG.d("%s onClick, path: %s", LogTag.TAG_CHAT_FILE, message.getAbsolutePath());
    }

    @Override
    protected void onFileExists(@NonNull FileMessageForUI message) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_DOWNLOAD_COMPLETED);
        fileProgressInfoView.setVisibility(GONE);
    }

    @Override
    protected void onFileNotExists(@NonNull FileMessageForUI message) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_NOT_FOUND);
        fileProgressInfoView.setVisibility(GONE);
        if (!message.isSelf()) {
            downloadFile();
        }
    }

    @Override
    protected void onDownloadWaiting(@NonNull FileMessageForUI message) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_DOWNLOAD_WAITING);
        fileProgressInfoView.setVisibility(VISIBLE);
        //todo lv3
        fileProgressInfoView.setText("0 %");
    }

    @Override
    protected void onDownloading(@NonNull FileMessageForUI message, long progress, long total, float percent) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_DOWNLOADING);
        progressView.setProgress((int) (100 * percent), 100);
        fileProgressInfoView.setVisibility(VISIBLE);
        //todo lv3
        fileProgressInfoView.setText((int) (100 * percent) + " %");
    }

    @Override
    protected void onDownloadSuccess(@NonNull FileMessageForUI message) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_DOWNLOAD_COMPLETED);
        fileProgressInfoView.setVisibility(GONE);
    }

    @Override
    protected void onDownloadFailed(@NonNull FileMessageForUI message) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_DOWNLOAD_FAILED);
        fileProgressInfoView.setVisibility(GONE);
    }

    @Override
    protected void onDownloadCanceled(@NonNull FileMessageForUI message) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_DOWNLOAD_CANCELED);
        fileProgressInfoView.setVisibility(GONE);
    }

    @Override
    protected void onUploadWaiting(@NonNull FileMessageForUI message) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_UPLOAD_WAITING);
        fileProgressInfoView.setVisibility(VISIBLE);
        //todo lv3
        fileProgressInfoView.setText("0 %");
    }

    @Override
    protected void onUploading(@NonNull FileMessageForUI message, float progress, long total, float percent) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_UPLOADING);
        progressView.setProgress((int) (100 * percent), 100);
        fileProgressInfoView.setVisibility(VISIBLE);
        //todo lv3
        fileProgressInfoView.setText((int) (100 * percent) + " %");
    }

    @Override
    protected void onUploadSuccess(@NonNull FileMessageForUI message) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_UPLOAD_COMPLETED);
        fileProgressInfoView.setVisibility(GONE);
    }

    @Override
    protected void onUploadFailed(@NonNull FileMessageForUI message) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_UPLOAD_FAILED);
        fileProgressInfoView.setVisibility(GONE);
    }

    @Override
    protected void onUploadCanceled(@NonNull FileMessageForUI message) {
        progressView.setCurrentMode(FileStatusCode.STATUS_FILE_UPLOAD_CANCELED);
        fileProgressInfoView.setVisibility(GONE);
    }

    @Override
    protected void loadMedia(@NonNull FileMessageForUI message) {

    }
}
