package im.turbo.basetools.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import im.turbo.basetools.file.SFileProvider;
import im.turbo.basetools.image.ImageUtils;
import im.turbo.baseui.base.bridgefragment.BridgeCallback;
import im.turbo.baseui.base.bridgefragment.BridgeInterface;
import im.turbo.baseui.lifecircle.LifeCycleHelper;
import im.turbo.baseui.permission.Permission;
import im.turbo.baseui.permission.PermissionResult;
import im.turbo.baseui.permission.PermissionUtils;
import im.turbo.utils.log.S;


/**
 * created by zhaoyuntao
 * on 2019-10-04
 * description:
 * To take picture via system camera
 */
public class TakePictureUtils {
    //file max size： 10M
    private static int bitmapSize = 10 * 1024 * 1024;

    /**
     * take photo via system camera
     *
     * @param context
     * @param photoGetter
     */
    public static void takePhoto(final Context context, final PhotoGetter photoGetter) {
        takePhoto(context, photoGetter, bitmapSize);
    }

    /**
     * take photo via system camera
     *
     * @param context
     * @param photoGetter
     * @param maxSize     photo max size
     */
    public static void takePhoto(final Context context, final PhotoGetter photoGetter, final int maxSize) {
        if (photoGetter == null) {
            return;
        }
        //Because you need to save the picture after taking the picture, you need to apply for file writing permission
        PermissionUtils.requestPermission(context, new PermissionResult() {
            @Override
            public void onGranted(@NonNull String[] grantedPermissions) {
                _takePhoto(context, photoGetter, maxSize);
            }

            @Override
            public void onDenied(@NonNull String[] deniedPermissions, boolean deniedForever) {
                photoGetter.whenNoPermission(deniedPermissions);
            }
        }, Permission.CAMERA, Permission.WRITE_EXTERNAL_STORAGE);
    }

    public static void _takePhoto(final Context context, final PhotoGetter photoGetter, final int maxSize) {
        if (!LifeCycleHelper.checkLifeCircle(context)) {
            return;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        final File file = new File(getRootFileDir(context), "img_" + timeStamp + ".jpg");
        final Uri uriImg = SFileProvider.getUriForFile(context, file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriImg);
        BridgeInterface.getFragment(context).startActivityForResult(intent, new BridgeCallback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    int orientation = ImageUtils.getImageOrientation(file.getAbsolutePath());
                    Bitmap bitmap = ImageUtils.getCompressBitmap(context, uriImg, maxSize);
                    bitmap = ImageUtils.rotateBitmap(bitmap, orientation, true);
                    File realFile = ImageUtils.saveBitmap(bitmap, file);
                    S.s("bitmap:" + bitmap + " readFile:" + realFile);
                    if (bitmap != null && realFile != null) {
                        photoGetter.whenGetPhoto(realFile.getAbsolutePath(), uriImg, bitmap);
                    } else {
                        photoGetter.whenPictureNotFound();
                    }
                }
            }
        });
    }

    public static File getRootFileDir(Context context) {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ?
                getExternalFilesDir(context) : context.getFilesDir();
    }

    private static File getExternalFilesDir(Context context) {
        File dir = context.getExternalFilesDir((String) null);
        if (dir != null) {
            return dir;
        }
        return context.getFilesDir();
    }

    public interface PhotoGetter {
        void whenGetPhoto(String absolutePath, Uri uri, Bitmap bitmap);

        default void whenPictureNotFound() {
        }

        default void whenNoPermission(String[] permissions) {
        }
    }
}
