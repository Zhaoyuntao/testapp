package im.thebot.chat.ui.view;

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

import androidx.appcompat.widget.AppCompatImageView;


import com.example.module_chat.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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
    private String path;
    private int viewWidth, viewHeight;

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
        if (viewWidth > 0 && viewHeight > 0) {
            int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
            if (viewWidth > 0 && this.viewWidth > viewWidth) {
                this.viewWidth = viewWidth;
            }
            setMeasuredDimension(this.viewWidth, this.viewHeight);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setImageBase64(String previewBase64) {
    }

    public void setImageLocal(String path) {
        this.path = path;
        loadBitmap();
    }

    public void setVideoLocal(String path) {
        this.path = path;
        loadVideoThumb();
    }

    public void adjustViewSize(int imageWidth, int imageHeight) {
        int[] size = ImageUtils.adjustImageSize(imageWidth, imageHeight);
        this.viewWidth = size[0];
        this.viewHeight = size[1];
    }

    private void loadVideoThumb() {
        String pathFinal = getLocalImagePath();
        if (TextUtils.isEmpty(pathFinal)) {
            setImageDrawable(null);
            return;
        }
        ThreadPool.runIO(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(pathFinal, MediaStore.Images.Thumbnails.MINI_KIND);
                ThreadPool.runUi(new SafeRunnable(ChatImageView.this) {
                    @Override
                    public void runSafely() {
                        String path = getLocalImagePath();
                        if (TextUtils.equals(path, pathFinal)) {
                            setImageBitmap(bitmap);
                        }
                    }
                });
            }
        });
    }

    private void loadBitmap() {
        String pathFinal = getLocalImagePath();
        if (TextUtils.isEmpty(pathFinal)) {
            setImageDrawable(null);
            return;
        }
        float width;
        float height;
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (viewWidth > 0 && viewHeight > 0) {
            width = viewWidth;
            height = viewHeight;
        } else if (layoutParams.width > 0 && layoutParams.height > 0) {
            width = layoutParams.width;
            height = layoutParams.height;
        } else {
            width = 100;
            height = 100;
        }
        float ratioView = width / height;
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
                        String path = getLocalImagePath();
                        if (!TextUtils.equals(path, pathFinal)) {
                            return;
                        }
                        setImageBitmap(bitmap);
                    }
                });
            }
        });
    }

    public String getLocalImagePath() {
        return path;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        path = null;
    }
}
