package im.thebot.chat.ui.cells.origin.file;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.module_chat.R;

import im.thebot.chat.api.chat.message.ImagesMessageForUI;
import im.thebot.chat.ui.cells.origin.base.BaseFileCell;

/**
 * created by zhaoyuntao
 * on 18/05/2020
 * description:
 */
public class ImagesCell extends BaseFileCell<ImagesMessageForUI> {

    @Override
    public int setLayout() {
        return R.layout.layout_chat_cell_item_file_images;
    }

    @Override
    protected void onInitViewMode(@NonNull ImagesMessageForUI message) {

    }

    @Override
    protected void onClearViewMode() {

    }

    @Override
    protected void initTextTypeView(@NonNull Context context) {
    }

    @Override
    protected void onFileMessageInit(@NonNull ImagesMessageForUI messageBean) {
    }

    @Override
    protected void onFileMessageChanged(@NonNull ImagesMessageForUI messageBean) {
    }

    @Override
    protected void onFileExists(@NonNull ImagesMessageForUI messageBean) {
        loadMedia(messageBean);
    }

    @Override
    protected void onFileNotExists(@NonNull ImagesMessageForUI messageBean) {
        downloadFile();
    }

    @Override
    protected void onDownloadWaiting(@NonNull ImagesMessageForUI message) {

    }

    @Override
    protected void onDownloading(@NonNull ImagesMessageForUI messageBean, long progress, long total, float percent) {

    }

    @Override
    protected void onDownloadSuccess(@NonNull ImagesMessageForUI messageBean) {
        loadMedia(messageBean);
    }

    @Override
    protected void onDownloadFailed(@NonNull ImagesMessageForUI messageBean) {

    }

    @Override
    protected void onDownloadCanceled(@NonNull ImagesMessageForUI messageBean) {

    }

    @Override
    protected void onUploadWaiting(@NonNull ImagesMessageForUI message) {

    }

    @Override
    protected void onUploading(@NonNull ImagesMessageForUI message, float progress, long total, float percent) {

    }

    @Override
    protected void onUploadSuccess(@NonNull ImagesMessageForUI messageBean) {

    }

    @Override
    protected void onUploadFailed(@NonNull ImagesMessageForUI messageBean) {

    }

    @Override
    protected void onUploadCanceled(@NonNull ImagesMessageForUI messageBean) {

    }

    @Override
    protected void loadMedia(@NonNull ImagesMessageForUI message) {

    }

    @Override
    public void onClickMessage(@NonNull ImagesMessageForUI messageBean) {
        //todo lv2
//        Bundle bundle = new Bundle();
//        bundle.putString(AppParams.UID, messageBean.getSessionId());
//        bundle.putString(AppParams.UUID, messageBean.getUUID());
//        IntentUtils.startActivity(getContext(), MediaPreviewActivity.class, bundle);
    }

    @Override
    protected void downloadFile() {
        ImagesMessageForUI imagesMessageForUI = getMessage();

    }
}
