package im.thebot.chat.ui.cells.origin.file;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.module_chat.R;

import im.thebot.chat.api.chat.message.AudioMessageForUI;
import im.thebot.chat.ui.cells.origin.base.BaseFileCell;
import im.thebot.chat.ui.view.ChatSlideProgressView;
import im.turbo.baseui.chat.SmoothSwitchFrameLayout;


/**
 * created by zhaoyuntao
 * on 18/05/2020
 * description:
 */
public class AudioCell extends BaseFileCell<AudioMessageForUI> {

    private ImageView playView;
    private ImageView playingView;
    private TextView durationView;
    private ChatSlideProgressView progressView;
    private SmoothSwitchFrameLayout smoothSwitchFrameLayout;

    public AudioCell(Context context) {
        super(context);
    }

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
        progressView = findViewById(R.id.audio_chat_cell_play_progress);
        playView = findViewById(R.id.audio_conversation_cell_play);
        playingView = findViewById(R.id.audio_chat_cell_playing);
        durationView = findViewById(R.id.audio_chat_cell_duration);
        smoothSwitchFrameLayout = findViewById(R.id.audio_chat_cell_play_switch);
    }

    @Override
    protected void onInitViewMode(@NonNull AudioMessageForUI message) {

    }

    @Override
    protected void onClearViewMode() {

    }

    @Override
    protected void onFileMessageInit(@NonNull AudioMessageForUI message) {
        progressView.setCallBack(new ChatSlideProgressView.CallBack() {
            @Override
            public void whenStartDragging(float percent) {

            }

            @Override
            public void whenDragging(float percent) {

            }

            @Override
            public void whenStopDragging(float percent) {

            }
        });
    }

    @Override
    protected void onFileMessageChanged(@NonNull AudioMessageForUI message) {

    }

    @Override
    protected void onFileExists(@NonNull AudioMessageForUI message) {

    }

    @Override
    protected void onFileNotExists(@NonNull AudioMessageForUI message) {

    }

    @Override
    protected void onDownloadWaiting(@NonNull AudioMessageForUI message) {

    }

    @Override
    protected void onDownloading(@NonNull AudioMessageForUI message, long progress, long total, float percent) {

    }

    @Override
    protected void onDownloadSuccess(@NonNull AudioMessageForUI message) {

    }

    @Override
    protected void onDownloadFailed(@NonNull AudioMessageForUI message) {

    }

    @Override
    protected void onDownloadCanceled(@NonNull AudioMessageForUI message) {

    }

    @Override
    protected void onUploadWaiting(@NonNull AudioMessageForUI message) {

    }

    @Override
    protected void onUploading(@NonNull AudioMessageForUI message, float progress, long total, float percent) {

    }

    @Override
    protected void onUploadSuccess(@NonNull AudioMessageForUI message) {

    }

    @Override
    protected void onUploadFailed(@NonNull AudioMessageForUI message) {

    }

    @Override
    protected void onUploadCanceled(@NonNull AudioMessageForUI message) {

    }

    @Override
    protected void loadMedia(@NonNull AudioMessageForUI message) {

    }
}
