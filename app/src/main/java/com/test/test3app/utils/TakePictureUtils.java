package com.test.test3app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import com.zhaoyuntao.androidutils.permission.PermissionSettings;
import com.zhaoyuntao.androidutils.tools.S;
import com.zhaoyuntao.androidutils.tools.ZP;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * created by zhaoyuntao
 * on 2019-10-04
 * description:
 * To take picture via system camera
 */
public class TakePictureUtils {
    private static final int REQUESTCODE_SELECT_PICTURE = 1003;
    //max size of bitmap is 1mb;
    private static final int MAX_SIZE = 1024 * 1024;

    /**
     * take photo via system camera
     *
     * @param activity
     * @param photoGetter
     */
    public static void takePhoto(final Activity activity, final PhotoGetter photoGetter) {
        if (photoGetter == null) {
            return;
        }
        ZP.requestCameraAndAudioAndWriteExternalPermissionWithNotice(activity, new ZP.RequestResultWithNotice() {
            @Override
            public void onShowNotice(PermissionSettings permissionSettings) {
                permissionSettings.whenContinue();
            }

            @Override
            public void onGranted(List<String> permissions) {
                _takePhoto(activity, photoGetter);
            }

            @Override
            public void onDenied(List<String> permissions) {
                photoGetter.whenNoPermission(permissions);
            }

            @Override
            public void onDeniedNotAsk(PermissionSettings permissionSettings) {
                photoGetter.whenForbidPermission(permissionSettings);
            }
        });
    }

    public static void _takePhoto(final Activity activity, final PhotoGetter photoGetter) {
        if (activity == null) {
            return;
        }
//        final File file = new File(activity.getExternalFilesDir("abc_test"), "test_" + System.currentTimeMillis() + ".jpg");
        final File dir = activity.getExternalFilesDir("abc_test1");
        final File file = new File(dir, "test_" + System.currentTimeMillis() + ".jpg");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        final Uri uriImg = FileProvider.getUriForFile(activity, "com.test.photo", file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriImg);
        AvoidActivityResultFragment.getFragment(activity).startActivityForResult(intent, REQUESTCODE_SELECT_PICTURE, new AvoidActivityResultFragment.CallBack() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        int degree = BitmapUtils.getBitmapDegree(file.getAbsolutePath());
                        Bitmap bitmap;
                        if (degree != 0) {
                            bitmap = BitmapUtils.rotateBitmap(MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uriImg), degree);
                            BitmapUtils.saveBitmap(bitmap, file.getAbsoluteFile());
                        } else {
                            bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uriImg);
                        }
//                        S.s("file.size:" + file.length() + "byte," + file.length() / 1024f / 1024f + "Mb");
                        if (bitmap != null) {
                            photoGetter.whenGetPhoto(file.getAbsolutePath(), uriImg, bitmap);
                        } else {
                            photoGetter.whenPictureNotFound();
                        }
                    } catch (IOException e) {
                        S.e(e);
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static Bitmap getCompressBitmap(Context context, String filepath) {
        Uri uri = Uri.fromFile(new File(filepath));
        return getCompressBitmap(context, uri);
    }

    public static Bitmap getCompressBitmap(Context context, Uri uri) {
        return getCompressBitmap(context, uri, MAX_SIZE);
    }

    public static Bitmap getCompressBitmap(Context context, Uri uri, long maxSize) {
        return getCompressBitmap(context, uri, maxSize, Bitmap.Config.ARGB_8888);
    }

    public static Bitmap getCompressBitmap(Context context, Uri uri, long maxSize, Bitmap.Config config) {
        return _getCompressBitmap(context, uri, maxSize, config);
    }

    public static Bitmap getCompressBitmapARGB4444(Context context, Uri uri) {
        return _getCompressBitmap(context, uri, MAX_SIZE, Bitmap.Config.ARGB_4444);
    }

    private static Bitmap _getCompressBitmap(Context context, Uri uri, long maxSize, Bitmap.Config config) {
        InputStream input;
        try {
            input = context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            S.e(e.getMessage());
            return null;
        }
        if (input == null) {
            return null;
        }
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = config;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        try {
            input.close();
        } catch (IOException e) {
            S.e(e.getMessage());
        }
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1)) {
            return null;
        }

        try {
            input = context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            S.e(e.getMessage());
            return null;
        }
        int inSampleSize = getSampleSizeKeepPropertion(originalWidth, originalHeight, maxSize, config);
        //4.start compress
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = inSampleSize;
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = config;//optional
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
//        S.s("size:" + (bitmap != null ? bitmap.getAllocationByteCount()/1024f/1024 : 0));
        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException e) {
            S.e(e.getMessage());
        }
        return bitmap;
    }

    public static int getSampleSizeKeepPropertion(int width, int height, double size, Bitmap.Config config) {
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
//        S.s("inSampleSize:" + inSampleSize);
        return inSampleSize;
    }

    public static int[] getCompressedWidthAndHeight(int width, int height, double size, Bitmap.Config config) {
        int[] widthAndHeight = {width, height};
        //1.proportion of original bitmap
        float proportionOfBitmap = (float) width / height;

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
        widthAndHeight[0] = (int) Math.sqrt(pixels * proportionOfBitmap);
        widthAndHeight[1] = (int) (widthAndHeight[0] / proportionOfBitmap);
//        S.s("w_c:" + widthAndHeight[0] + " h_c:" + widthAndHeight[1] + " total size:" + widthAndHeight[0] * widthAndHeight[1] * 4f / 1024 / 1024);
        return widthAndHeight;
    }

    public interface PhotoGetter {
        void whenGetPhoto(String absolutePath, Uri uri, Bitmap bitmap);

        void whenPictureNotFound();

        void whenNoPermission(List<String> permissions);

        void whenForbidPermission(PermissionSettings permissionSettings);
    }
}
