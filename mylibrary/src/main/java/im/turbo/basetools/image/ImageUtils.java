package im.turbo.basetools.image;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;

import im.turbo.baseui.utils.UiUtils;

/**
 * created by zhaoyuntao
 * on 09/06/2022
 * description:
 */
public class ImageUtils {

    public static final String EVENT_PHOTO_CHANGE = "app.media.photo.change";
    public static final String EVENT_VIDEO_CHANGE = "app.media.video.change";

    @Nullable
    public static String parseImageMimeType(@NonNull File file) {
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        return options.outMimeType;
    }


    public static boolean isImageFile(@NonNull File file) {
        return parseImageMimeType(file) != null;
    }

    @Nullable
    public static byte[] compressToBytes(@NonNull Bitmap bitmap, @NonNull Bitmap.CompressFormat format, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(65536);
        if (!bitmap.compress(format, quality, baos)) {
            return null;
        }
        return baos.toByteArray();
    }


    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        return drawableToBitmap(drawable, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    }

    public static Bitmap drawableToBitmap(Drawable drawable, int width, int height) {
        if (drawable == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);

        return bitmap;
    }

    /**
     * 保存bitmap到本地
     */
    public static File copyBitmapOnly(Bitmap bitmap, File file) {
        if (bitmap == null) {
            return file;
        }
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 保存bitmap到本地
     */
    public static File saveBitmap(Bitmap bitmap, File file) {
        if (bitmap == null) {
            return file;
        }
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void putBitmapToMedia(Context context, String filePath, Bitmap bm) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filePath);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = context.getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        try {
            OutputStream out = context.getContentResolver().openOutputStream(uri);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] Bitmap2JPEGBytes(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return outputStream.toByteArray();
    }

    public static Bitmap getCompressBitmap(Context context, Uri uri, long maxSize) {
        return getCompressBitmap(context, uri, maxSize, Bitmap.Config.ARGB_8888);
    }

    public static Bitmap getCompressBitmap(Context context, Uri uri, long maxSize, Bitmap.Config config) {
        return _getCompressBitmap(context, uri, maxSize, config);
    }

    private static Bitmap _getCompressBitmap(Context context, Uri uri, long maxSize, Bitmap.Config config) {
        int originalWidth;
        int originalHeight;
        try (InputStream input = context.getContentResolver().openInputStream(uri)) {
            BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
            onlyBoundsOptions.inJustDecodeBounds = true;
            onlyBoundsOptions.inDither = true;//optional
            onlyBoundsOptions.inPreferredConfig = config;//optional
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
            if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
                return null;
            }
            originalWidth = onlyBoundsOptions.outWidth;
            originalHeight = onlyBoundsOptions.outHeight;
        } catch (IOException e) {
            return null;
        }


        try (InputStream input = context.getContentResolver().openInputStream(uri)) {
            int inSampleSize = calculateSampleSize(originalWidth, originalHeight, maxSize, config);
            //4.start compress
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = inSampleSize;
            bitmapOptions.inDither = true;//optional
            bitmapOptions.inPreferredConfig = config;//optional
            return BitmapFactory.decodeStream(input, null, bitmapOptions);
        } catch (IOException e) {
            return null;
        }
    }

    public static byte[] bitmap2Bytes(@NonNull Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        return data;
    }

    public static Bitmap getBitmapCopy(Context context, @NonNull Bitmap bitmap) {
        byte[] bytes = bitmap2Bytes(bitmap);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
    }

    public static Bitmap getCompressBitmap(Context context, @NonNull Bitmap bitmap, long maxSize, Bitmap.Config config) {
        int originalWidth;
        int originalHeight;
        byte[] bytes = bitmap2Bytes(bitmap);
        try {

            BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
            onlyBoundsOptions.inJustDecodeBounds = true;
            onlyBoundsOptions.inDither = true;//optional
            onlyBoundsOptions.inPreferredConfig = config;//optional
            BitmapFactory.decodeByteArray(bytes, 0, bytes.length, onlyBoundsOptions);
            if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
                return null;
            }
            originalWidth = onlyBoundsOptions.outWidth;
            originalHeight = onlyBoundsOptions.outHeight;
        } catch (Exception e) {
            return null;
        }


        try {
            int inSampleSize = calculateSampleSize(originalWidth, originalHeight, maxSize, config);
            //4.start compress
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = inSampleSize;
            bitmapOptions.inDither = true;//optional
            bitmapOptions.inPreferredConfig = config;//optional
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, bitmapOptions);
        } catch (Exception e) {
            return null;
        }
    }

    public static int calculateSampleSize(int width, int height, double size, Bitmap.Config config) {
        int[] widthAndHeight = getCompressedWidthAndHeight(width, height, size, config);
        int widthOfCompressed = widthAndHeight[0];
        //3.calculate compress sample size
        float scalePercent = (float) width / widthOfCompressed;
        int inSampleSize;
        if (scalePercent % 1 > 0) {
            inSampleSize = (int) scalePercent + 1;
        } else {
            inSampleSize = (int) scalePercent;
        }
        if (inSampleSize <= 0) {
            inSampleSize = 1;
        }
        return inSampleSize;
    }

    /**
     * calculate width and height for a bitmap witch will be compressed
     *
     * @param width  origin bitmap width
     * @param height origin bitmap height
     * @param size   max size
     * @param config compress formation
     */
    public static int[] getCompressedWidthAndHeight(int width, int height, double size, Bitmap.Config config) {
        int[] widthAndHeight = {width, height};
        //1.proportion of original bitmap
        float ratioOfBitmap = (float) width / height;

        int bytesOfOnePixel = 1;
        switch (config) {
            case ARGB_8888:
                bytesOfOnePixel = 4;
                break;
            case ALPHA_8:
                bytesOfOnePixel = 1;
                break;
            case ARGB_4444:
            case RGB_565:
                bytesOfOnePixel = 2;
                break;
        }

        //2.calculate pixels count of compressed bitmap
        long pixels = (long) (size / bytesOfOnePixel);
        int expectWidth = (int) Math.sqrt(pixels * ratioOfBitmap);
        if (expectWidth < width) {
            widthAndHeight[0] = expectWidth;
            widthAndHeight[1] = (int) (widthAndHeight[0] / ratioOfBitmap);
        }
//        S.s("=======>>>> need[" + size + "]   before compress[" + (width * height * bytesOfOnePixel) + "]:w:" + width + " h:" + height + " after compress[" + (widthAndHeight[0] * widthAndHeight[1] * bytesOfOnePixel) + "]:w" + widthAndHeight[0] + " h:" + widthAndHeight[1]);
        return widthAndHeight;
    }

    private static String insertImageToSystem(Context context, String imagePath, String name) {
        String url = "";
        try {
            url = MediaStore.Images.Media.insertImage(context.getContentResolver(), imagePath, name, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * @return Return [width, height]
     */
    public static int[] adjustImageSize(final int imageWidth, final int imageHeight) {
        int widthScreen = UiUtils.getScreenWidthPixels();

        int viewWidth;
        int viewHeight;

        float minRatio = 0.713f;
        float maxRatio = 4;
        float imageRatio = (imageWidth == 0 || imageHeight == 0) ? 0.7f : (float) imageWidth / imageHeight;

        boolean isVerticalImage = imageRatio < 1;

        if (isVerticalImage) {
            viewWidth = (int) (widthScreen * 0.64f);
        } else {
            viewWidth = (int) (widthScreen * 0.7f);
        }
        if (imageRatio > maxRatio) {
            viewHeight = (int) (viewWidth / maxRatio);
        } else if (imageRatio < minRatio) {
            viewHeight = (int) (viewWidth / minRatio);
        } else {
            viewHeight = (int) (viewWidth / imageRatio);
        }

        return new int[]{viewWidth, viewHeight};
    }

    public static int getImageOrientation(@NonNull String filePath) {
        try {
            return new ExifInterface(filePath).getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
        } catch (Throwable e) {
        }
        return ExifInterface.ORIENTATION_UNDEFINED;
    }

    /**
     * Calculate the largest inSampleSize value that is a power of 2 and keeps both
     * height and width larger than the requested height and width.
     */
    public static int calculateInSampleSize(int width, int height, int reqWidth, int reqHeight) {
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    private static final int MIN_PIC_WIDTH_OR_HEIGHT = 60;

    private static double getScale(int width, int height, int maxWidth,
                                   int maxHeight) {
        double scale = 1;
        if (width <= MIN_PIC_WIDTH_OR_HEIGHT
                && height <= MIN_PIC_WIDTH_OR_HEIGHT)
            return 1;

        if (width > maxWidth || height > maxHeight) {
            double scaleWidth = ((double) width) / maxWidth;
            double scaleHeight = ((double) height) / maxHeight;
            scale = scaleWidth > scaleHeight ? scaleWidth : scaleHeight;
            if (scale < 1)
                return 1;

            int newWidth = (int) (width / scale);
            if (newWidth < MIN_PIC_WIDTH_OR_HEIGHT) {
                return ((double) width) / MIN_PIC_WIDTH_OR_HEIGHT;
            }

            int newHeight = (int) (height / scale);
            if (newHeight < MIN_PIC_WIDTH_OR_HEIGHT) {
                return ((double) height)
                        / MIN_PIC_WIDTH_OR_HEIGHT;
            }
        }

        return scale;
    }

    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        if (TextUtils.isEmpty(mimeType)) {
            mimeType = parseImageMimeType(new File(path));
        }
        return mimeType != null && mimeType.startsWith("image");
    }

    public static boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        if (TextUtils.isEmpty(mimeType)) {
            mimeType = parseImageMimeType(new File(path));
        }
        return mimeType != null && mimeType.startsWith("video");
    }

    public static int[] getImageSize(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;
        return new int[]{imageWidth, imageHeight};
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation, boolean recycle) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_UNDEFINED:
            case ExifInterface.ORIENTATION_NORMAL:
            default:
                return bitmap;
        }

        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (recycle) {
            bitmap.recycle();
        }
        return rotatedBitmap;
    }
}
