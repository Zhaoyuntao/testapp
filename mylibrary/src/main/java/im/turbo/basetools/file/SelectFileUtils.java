package im.turbo.basetools.file;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;

import im.turbo.baseui.base.bridgefragment.BridgeCallback;
import im.turbo.baseui.base.bridgefragment.BridgeInterface;
import im.turbo.baseui.permission.Permission;
import im.turbo.baseui.permission.PermissionResult;
import im.turbo.baseui.permission.PermissionUtils;
import im.turbo.thread.SafeRunnable;
import im.turbo.thread.ThreadPool;
import im.turbo.utils.ResourceUtils;

/**
 * created by zhaoyuntao
 * on 2019-10-04
 * description:
 * To select a file from file system.
 */
public class SelectFileUtils {

    public static void selectPicture(final Context context, final FileGetter fileGetter) {
        _selectFile(context, fileGetter, "image/*");
    }

    public static void selectPictureAndVideo(final Context context, final FileGetter fileGetter) {
        _selectFile(context, fileGetter, "image/*,video/*");
    }

    public static void selectVideo(final Context context, final FileGetter fileGetter) {
        _selectFile(context, fileGetter, "video/*");
    }

    public static void selectFile(final Context context, final FileGetter fileGetter) {
        _selectFile(context, fileGetter, "*/*");
    }

    private static void _selectFile(final Context context, final FileGetter fileGetter, String fileType) {
        PermissionUtils.requestPermission(context, new PermissionResult() {
            @Override
            public void onGranted(@NonNull String[] grantedPermissions) {
                __selectFile(context, fileType, fileGetter);
            }
        }, Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE);
    }

    private static void __selectFile(final Context context, String fileType, final FileGetter fileGetter) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(fileType).addCategory(Intent.CATEGORY_OPENABLE);
        BridgeInterface.getFragment(context).startActivityForResult(Intent.createChooser(intent, "Select Files"), new BridgeCallback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    fileGetter.whenGetFile(uri);
                }
            }
        });
    }

    public interface FileGetter {
        void whenGetFile(Uri uri);
    }

}
