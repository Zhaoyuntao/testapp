package im.thebot.chat.ui.view;

import static im.thebot.api.chat.constant.FileStatusCode.STATUS_FILE_DOWNLOADING;
import static im.thebot.api.chat.constant.FileStatusCode.STATUS_FILE_DOWNLOAD_CANCELED;
import static im.thebot.api.chat.constant.FileStatusCode.STATUS_FILE_DOWNLOAD_COMPLETED;
import static im.thebot.api.chat.constant.FileStatusCode.STATUS_FILE_DOWNLOAD_FAILED;
import static im.thebot.api.chat.constant.FileStatusCode.STATUS_FILE_DOWNLOAD_WAITING;
import static im.thebot.api.chat.constant.FileStatusCode.STATUS_FILE_NOT_FOUND;
import static im.thebot.api.chat.constant.FileStatusCode.STATUS_FILE_UPLOADING;
import static im.thebot.api.chat.constant.FileStatusCode.STATUS_FILE_UPLOAD_CANCELED;
import static im.thebot.api.chat.constant.FileStatusCode.STATUS_FILE_UPLOAD_COMPLETED;
import static im.thebot.api.chat.constant.FileStatusCode.STATUS_FILE_UPLOAD_FAILED;
import static im.thebot.api.chat.constant.FileStatusCode.STATUS_FILE_UPLOAD_WAITING;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Outline;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.module_chat.R;

import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import im.thebot.SdkFactory;
import im.thebot.api.chat.constant.FileStatusCode;
import im.thebot.api.chat.listener.FileStatusBean;
import im.thebot.chat.api.chat.listener.FileListener;
import im.thebot.chat.api.chat.message.BaseFileMessageForUI;
import im.thebot.chat.api.chat.message.ImageMessageForUI;
import im.thebot.chat.api.chat.message.MessageBeanForUI;
import im.thebot.chat.api.chat.message.VideoMessageForUI;
import im.turbo.basetools.image.ImageUtils;
import im.turbo.thread.SafeRunnable;
import im.turbo.thread.ThreadPool;
import im.turbo.utils.log.S;

/**
 * created by zhaoyuntao
 * on 25/04/2022
 * description:
 */
public class ChatImageView extends AppCompatImageView {
    private final String TAG_CHAT_IMAGE_VIEW = "ChatImageViewTag";
    private int imageWidth, imageHeight;
    private FileListener fileListener;
    private ChatImageStateListener stateListener;
    private MessageBeanForUI message;
    private String tag2;
    public static boolean logOpen = true;

    public ChatImageView(Context context) {
        super(context);
        init(null);
    }

    public ChatImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ChatImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ChatImageView);
            setCornerRadius(typedArray.getDimensionPixelSize(R.styleable.ChatImageView_ChatImageView_radius, 0));
            typedArray.recycle();
        }
    }

    public void setCornerRadius(int cornerRadius) {
        setClipToOutline(true);
        setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), cornerRadius);
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0, height = 0;
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            width += getPaddingLeft() + getPaddingRight();
            width = Math.max(width, getSuggestedMinimumWidth());
            width = resolveSizeAndState(width, widthMeasureSpec, 0);
        } else {
            int[] size = ImageUtils.adjustImageSize(imageWidth, imageHeight);
            width = size[0];
        }

        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            height += getPaddingTop() + getPaddingBottom();
            height = Math.max(height, getSuggestedMinimumHeight());
            height = resolveSizeAndState(height, heightMeasureSpec, 0);
        } else {
            float ratio = Math.max(0.64f, imageWidth / (float) imageHeight);
            height = (int) (width / ratio);
        }

        setMeasuredDimension(width, height);
        load();
    }

    public void bindMessage(@NotNull BaseFileMessageForUI message) {
        bindMessage(message, null);
    }

    public void bindMessage(@NotNull BaseFileMessageForUI message, @Nullable ChatImageStateListener listener) {
        if (this.message == null || !this.message.equals(message) || getDrawable() == null) {
            setCurrentMessage(message);
            load();
        }
        this.stateListener = listener;
        registerListener();
    }

    private void setCurrentMessage(@NotNull MessageBeanForUI message) {
        this.message = message;
        setTag2(TAG_CHAT_IMAGE_VIEW + this.message.getUUID());
    }

    private MessageBeanForUI getCurrentMessage() {
        return message;
    }

    private void registerListener() {
        if (!(message instanceof BaseFileMessageForUI)) {
            return;
        }
        if (fileListener == null) {
            fileListener = new FileListener(TAG_CHAT_IMAGE_VIEW + message.getUUID()) {
                @Override
                public void onFileStatusChanged(@NotNull FileStatusBean fileStatus) {
                    if (fileStatus.getFileStatusCode() == FileStatusCode.STATUS_FILE_DOWNLOAD_COMPLETED) {
                        MessageBeanForUI currentMessage = getCurrentMessage();
                        if (currentMessage != null) {
                            MessageBeanForUI newMessage = SdkFactory.getChatSdk().loadMessage(currentMessage.getSessionId(), currentMessage.getUUID());
                            if (newMessage instanceof BaseFileMessageForUI) {
                                BaseFileMessageForUI baseFileMessageForUI = (BaseFileMessageForUI) newMessage;
                                ThreadPool.runUi(new SafeRunnable(getContext()) {
                                    @Override
                                    protected void runSafely() {
                                        if (getKey().equals(getTag2())) {
                                            setCurrentMessage(baseFileMessageForUI);
                                            updateFileState(baseFileMessageForUI, fileStatus);
                                        }
                                    }
                                });
                            }
                        }
                    }
                }

                @Override
                public boolean match(String key) {
                    return TextUtils.equals(TAG_CHAT_IMAGE_VIEW + key, getKey());
                }
            };
        } else {
            fileListener.setKey(TAG_CHAT_IMAGE_VIEW + message.getUUID());
        }
        if (isAttachedToWindow()) {
            SdkFactory.getFileSdk().registerFileListener(fileListener);
        }
        updateFileState((BaseFileMessageForUI) message, SdkFactory.getFileSdk().getFileStatus(message.getSessionId(), message.getUUID()));
    }

    private void updateFileState(@NonNull BaseFileMessageForUI message, @NonNull FileStatusBean fileStatus) {
        int fileStatusCode = fileStatus.getFileStatusCode();
        switch (fileStatusCode) {
            case STATUS_FILE_NOT_FOUND:
                onFileStateNotFound(message);
                break;
            case STATUS_FILE_DOWNLOAD_WAITING:
                _onDownloadWaiting(message);
                break;
            case STATUS_FILE_DOWNLOADING:
                if (message.getFileState() == STATUS_FILE_UPLOAD_CANCELED) {
                    _onDownloadCanceled(message);
                } else {
                    _onDownloading(message, fileStatus.getProgress(), fileStatus.getTotal(), fileStatus.getPercent());
                }
                break;
            case STATUS_FILE_DOWNLOAD_COMPLETED:
                _onDownloadSuccess(message);
                unregisterListener();
                break;
            case STATUS_FILE_DOWNLOAD_FAILED:
                _onDownloadFailed(message);
                break;
            case STATUS_FILE_DOWNLOAD_CANCELED:
                _onDownloadCanceled(message);
                break;
            case STATUS_FILE_UPLOAD_WAITING:
                _onUploadWaiting(message);
                break;
            case STATUS_FILE_UPLOADING:
                if (message.getFileState() == STATUS_FILE_UPLOAD_CANCELED) {
                    _onUploadCanceled(message);
                } else {
                    _onUploading(message, fileStatus.getProgress(), fileStatus.getTotal(), fileStatus.getPercent());
                }
                break;
            case STATUS_FILE_UPLOAD_COMPLETED:
                _onUploadSuccess(message);
                unregisterListener();
                break;
            case STATUS_FILE_UPLOAD_FAILED:
                _onUploadFailed(message);
                break;
            case STATUS_FILE_UPLOAD_CANCELED:
                _onUploadCanceled(message);
                break;
        }
    }

    protected boolean checkFileExists(@NonNull BaseFileMessageForUI message) {
        return SdkFactory.getFileSdk().isFileExists(message.getFileLocalPath());
    }

    private void onFileStateNotFound(@NonNull BaseFileMessageForUI message) {
        if (message.isSelf()) {
            if (message.isSendCanceled() || message.getFileState() == STATUS_FILE_UPLOAD_CANCELED) {
                _onUploadCanceled(message);
            } else if (message.isSendSuccess()) {
                if (checkFileExists(message)) {
                    _onUploadSuccess(message);
                } else {
                    if (message.getFileState() == STATUS_FILE_NOT_FOUND) {
                        _onFileNotExists(message);
                    } else if (message.getFileState() == STATUS_FILE_DOWNLOAD_CANCELED) {
                        _onDownloadCanceled(message);
                    } else {
                        _onDownloadFailed(message);
                    }
                }
            } else {
                _onUploadFailed(message);
            }
        } else {
            if (checkFileExists(message)) {
                _onDownloadSuccess(message);
            } else {
                if (message.getFileState() == STATUS_FILE_NOT_FOUND) {
                    _onFileNotExists(message);
                } else if (message.getFileState() == STATUS_FILE_DOWNLOAD_CANCELED) {
                    _onDownloadCanceled(message);
                } else {
                    _onDownloadFailed(message);
                }
            }
        }
    }

    private void _onFileNotExists(@NonNull BaseFileMessageForUI message) {
        S.ed(logOpen, "[" + message.getUUID() + "]_onFileNotExists[" + message.getFileLocalPath() + "]", 1);
        if (stateListener != null) {
            stateListener.onFileNotExists(message);
        }
    }

    private void _onDownloadWaiting(@NonNull BaseFileMessageForUI message) {
        S.sd(logOpen, "[" + message.getUUID() + "]_onDownloadWaiting", 1);
        if (stateListener != null) {
            stateListener.onDownloadWaiting(message);
        }
    }

    private void _onDownloading(@NonNull BaseFileMessageForUI message, long progress, long total, float percent) {
        S.sd(logOpen, "[" + message.getUUID() + "]_onDownloading", 1);
        if (stateListener != null) {
            stateListener.onDownloading(message, progress, total, percent);
        }
    }

    private void _onDownloadSuccess(@NonNull BaseFileMessageForUI message) {
        S.sd(logOpen, "[" + message.getUUID() + "]_onDownloadSuccess", 1);
        load();
        if (stateListener != null) {
            stateListener.onDownloadSuccess(message);
        }
    }

    private void _onDownloadFailed(@NonNull BaseFileMessageForUI message) {
        S.ed(logOpen, "[" + message.getUUID() + "]_onDownloadFailed", 1);
        if (stateListener != null) {
            stateListener.onDownloadFailed(message);
        }
    }

    private void _onDownloadCanceled(@NonNull BaseFileMessageForUI message) {
        S.ed(logOpen, "[" + message.getUUID() + "]_onDownloadCanceled", 1);
        if (stateListener != null) {
            stateListener.onDownloadCanceled(message);
        }
    }

    private void _onUploadWaiting(@NonNull BaseFileMessageForUI message) {
        S.sd(logOpen, "[" + message.getUUID() + "]_onUploadWaiting", 1);
        if (stateListener != null) {
            stateListener.onUploadWaiting(message);
        }
    }

    private void _onUploading(@NonNull BaseFileMessageForUI message, float progress, long total, float percent) {
        S.sd(logOpen, "[" + message.getUUID() + "]_onUploading", 1);
        if (stateListener != null) {
            stateListener.onUploading(message, progress, total, percent);
        }
    }

    private void _onUploadSuccess(@NonNull BaseFileMessageForUI message) {
        S.sd(logOpen, "[" + message.getUUID() + "]_onUploadSuccess", 1);
        load();
        if (stateListener != null) {
            stateListener.onUploadSuccess(message);
        }
    }

    private void _onUploadFailed(@NonNull BaseFileMessageForUI message) {
        S.ed(logOpen, "[" + message.getUUID() + "]_onUploadFailed", 1);
        if (stateListener != null) {
            stateListener.onUploadFailed(message);
        }
    }

    private void _onUploadCanceled(@NonNull BaseFileMessageForUI message) {
        S.ed(logOpen, "[" + message.getUUID() + "]_onUploadCanceled", 1);
        if (stateListener != null) {
            stateListener.onUploadCanceled(message);
        }
    }

    private void unregisterListener() {
        stateListener = null;
        if (fileListener != null) {
            SdkFactory.getFileSdk().unregisterFileListener(fileListener);
        }
    }

    private void load() {
        if (message instanceof ImageMessageForUI) {
            ImageMessageForUI imageMessageForUI = (ImageMessageForUI) message;
            setImageSize(imageMessageForUI.getImageWidth(), imageMessageForUI.getImageHeight());
            loadImageLocal(imageMessageForUI);
        } else if (message instanceof VideoMessageForUI) {
            VideoMessageForUI videoMessageForUI = (VideoMessageForUI) message;
            setImageSize(videoMessageForUI.getVideoWidth(), videoMessageForUI.getVideoHeight());
            loadVideoLocal(videoMessageForUI);
        } else {
            setImageDrawable(null);
        }
    }

    private void setImageSize(int imageWidth, int imageHeight) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    private void loadVideoLocal(VideoMessageForUI message) {
        String pathFinal = message.getFileLocalPath();
        if (!SdkFactory.getFileSdk().isFileExists(pathFinal)) {
            setPreviewBase64(message.getVideoPreviewBase64());
            return;
        }
        final String tag2Final = getTag2();
        ThreadPool.runIO(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(pathFinal, MediaStore.Images.Thumbnails.MINI_KIND);
                ThreadPool.runUi(new SafeRunnable(ChatImageView.this) {
                    @Override
                    public void runSafely() {
                        if (TextUtils.equals(tag2Final, getTag2())) {
                            setImageBitmap(bitmap);
                        }
                    }
                });
            }
        });
    }

    private void loadImageLocal(ImageMessageForUI message) {
        String pathFinal = message.getFileLocalPath();
        if (!SdkFactory.getFileSdk().isFileExists(pathFinal)) {
            setPreviewBase64(message.getImagePreviewBase64());
            return;
        }
        float widthCalculate = getMeasuredWidth();
        float heightCalculate = getMeasuredHeight();
        if (widthCalculate <= 0 || heightCalculate <= 0) {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            if (layoutParams.width > 0 && layoutParams.height > 0) {
                widthCalculate = layoutParams.width;
                heightCalculate = layoutParams.height;
            } else if (imageWidth > 0 && imageHeight > 0) {
                widthCalculate = imageWidth;
                heightCalculate = imageHeight;
            } else {
                widthCalculate = 100;
                heightCalculate = 100;
            }
        }
        final float width = widthCalculate;
        final float height = heightCalculate;
        float ratioView = width / height;
        final String tag2Final = getTag2();
        ThreadPool.runIO(new Runnable() {
            @Override
            public void run() {
                FileInputStream stream;
                try {
                    stream = new FileInputStream(pathFinal);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return;
                }
                BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
                onlyBoundsOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(stream, null, onlyBoundsOptions);
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                float widthImage = onlyBoundsOptions.outWidth;
                float heightImage = onlyBoundsOptions.outHeight;
                float ratioImage = widthImage / heightImage;
                BitmapFactory.Options options = new BitmapFactory.Options();
                if (ratioImage > ratioView) {
                    if (heightImage > height) {
                        options.inSampleSize = Math.max(1, Math.round(heightImage / height));
                    } else {
                        options.inSampleSize = 1;
                    }
                } else {
                    if (widthImage > width) {
                        options.inSampleSize = Math.max(1, Math.round(widthImage / width));
                    } else {
                        options.inSampleSize = 1;
                    }
                }
                S.s("w:" + widthImage + " h:" + heightImage + " view w:" + width + " view h:" + height + "  ratio image:" + ratioImage + " ratio view:" + ratioView + " simple:" + options.inSampleSize);
                FileInputStream stream2;
                try {
                    stream2 = new FileInputStream(pathFinal);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return;
                }
                Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, options);
                try {
                    stream2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ThreadPool.runUi(new SafeRunnable(ChatImageView.this) {
                    @Override
                    public void runSafely() {
                        if (TextUtils.equals(tag2Final, getTag2())) {
                            setImageBitmap(bitmap);
                        }
                    }
                });
            }
        });
    }

    private void setPreviewBase64(String previewBase64) {
        if (TextUtils.isEmpty(previewBase64)) {
            setImageDrawable(null);
            return;
        }
        try {
            ByteString byteString = ByteString.decodeBase64(previewBase64);
            if (byteString != null) {
                byte[] blurData = byteString.toByteArray();
                if (blurData != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(blurData, 0, blurData.length);
                    setImageBitmap(bitmap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            S.e(e);
        }
    }

    public String getTag2() {
        return tag2;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    @Override
    protected void onAttachedToWindow() {
        registerListener();
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        unregisterListener();
        super.onDetachedFromWindow();
    }
}
