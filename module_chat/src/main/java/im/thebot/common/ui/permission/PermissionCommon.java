package im.thebot.common.ui.permission;

import android.content.Context;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.example.module_chat.R;

import im.turbo.baseui.permission.InstallResult;
import im.turbo.baseui.permission.Permission;
import im.turbo.baseui.permission.PermissionResult;
import im.turbo.baseui.permission.PermissionUtils;

/**
 * created by zhaoyuntao
 * on 30/05/2022
 * description:
 */
public class PermissionCommon {
    public static boolean isAlwaysDenied(@Nullable Context context, @NonNull @Permission String... permissions) {
        return PermissionUtils.isAlwaysDenied(context, permissions);
    }

    public static boolean hasPermission(@Nullable Context context, @NonNull @Permission String... permissions) {
        return PermissionUtils.hasPermission(context, permissions);
    }

    public static void requestStoragePermissionWithDialog(Context context, @Nullable final PermissionResult result) {
        requestPermission(context,
                R.string.permission_storage_need_access,
                R.string.permission_storage_need_access,
                result,
                Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE);
    }

    public static void requestTakePhotoPermissionWithDialog(Context context, @Nullable final PermissionResult result) {
        int requestStringRes;
        int guideStringRes;
        if (!hasPermission(context, Permission.CAMERA) && !hasPermission(context, Permission.WRITE_EXTERNAL_STORAGE)) {
            requestStringRes = R.string.permission_storage_cam_on_attaching_photo_request;
            guideStringRes = R.string.permission_storage_cam_on_attaching_photo;
        } else if (!hasPermission(context, Permission.CAMERA)) {
            requestStringRes = R.string.permission_cam_access_request;
            guideStringRes = R.string.permission_cam_access;
        } else if (!hasPermission(context, Permission.WRITE_EXTERNAL_STORAGE)) {
            requestStringRes = R.string.permission_storage_need_access;
            guideStringRes = R.string.permission_storage_need_access;
        } else {
            requestStringRes = R.string.permission_storage_need_access;
            guideStringRes = R.string.permission_storage_need_access;
        }

        requestPermission(context,
                requestStringRes,
                guideStringRes,
                result,
                Permission.CAMERA, Permission.WRITE_EXTERNAL_STORAGE);
    }

    public static void requestSendVideoPermissionWithDialog(Context context, @Nullable final PermissionResult result) {
        int requestStringRes;
        int guideStringRes;
        if (!hasPermission(context, Permission.CAMERA) && !hasPermission(context, Permission.WRITE_EXTERNAL_STORAGE)) {
            requestStringRes = R.string.permission_storage_cam_on_attaching_video_request;
            guideStringRes = R.string.permission_storage_cam_on_attaching_video;
        } else if (!hasPermission(context, Permission.CAMERA)) {
            requestStringRes = R.string.permission_cam_access_request;
            guideStringRes = R.string.permission_cam_access;
        } else if (!hasPermission(context, Permission.WRITE_EXTERNAL_STORAGE)) {
            requestStringRes = R.string.permission_storage_need_access;
            guideStringRes = R.string.permission_storage_need_access;
        } else {
            requestStringRes = R.string.permission_storage_need_access;
            guideStringRes = R.string.permission_storage_need_access;
        }

        requestPermission(context,
                requestStringRes,
                guideStringRes,
                result,
                Permission.CAMERA, Permission.WRITE_EXTERNAL_STORAGE);
    }

    public static void requestSendFilePermissionWithDialog(Context context, @Nullable final PermissionResult result) {
        requestPermission(context,
                R.string.permission_storage_need_write_access_on_sending_media_request,
                R.string.permission_storage_need_write_access_on_sending_media,
                result,
                Permission.READ_EXTERNAL_STORAGE);
    }

    public static void requestGalleryPermissionWithDialog(Context context, @Nullable final PermissionResult result) {
        requestPermission(context,
                R.string.permission_storage_need_write_access_on_attaching_photo_request,
                R.string.permission_storage_need_write_access_on_attaching_photo,
                result,
                Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE);
    }

    public static void requestReadContactPermissionWithDialog(Context context, @Nullable final PermissionResult result) {
        requestPermission(context,
                R.string.permission_contacts_access_on_sending_contact_request,
                R.string.permission_contacts_access_on_sending_contact,
                result,
                Permission.READ_CONTACTS);
    }

    public static void requestWriteContactPermissionWithDialog(Context context, @Nullable final PermissionResult result) {
        requestPermission(context,
                R.string.permission_contacts_access_request,
                R.string.permission_contacts_needed,
                result,
                Permission.WRITE_CONTACTS);
    }

    public static void requestVoiceCallPermissionWithDialog(Context context, @Nullable final PermissionResult result) {
        requestPermission(context,
                R.string.permission_mic_access_request,
                R.string.permission_mic_access,
                result,
                Permission.RECORD_AUDIO);
    }

    public static void requestVideoCallPermissionWithDialog(Context context, @Nullable final PermissionResult result) {
        requestPermission(context,
                R.string.permission_mic_and_cam_on_video_call_request,
                R.string.permission_mic_and_cam_on_video_call,
                result,
                Permission.RECORD_AUDIO, Permission.CAMERA);
    }

    public static void requestLocationPermissionWithDialog(Context context, @Nullable final PermissionResult result) {
        requestPermission(context,
                R.string.permission_location_access_on_sending_location_request,
                R.string.permission_location_access_on_sending_location,
                result, Permission.ACCESS_FINE_LOCATION);
    }

    public static void requestPermission(@Nullable Context context, @StringRes int requestStringRes, @StringRes int guideStringRes, @Nullable final PermissionResult result, @NonNull @Permission final String... permissions) {
        PermissionUtils.requestPermission(context, requestStringRes, guideStringRes, result, permissions);
    }

    public static void requestPermission(@Nullable Context context, @Nullable final PermissionResult result, @NonNull @Permission final String... permissions) {
        PermissionUtils.requestPermission(context, result, permissions);
    }

    public static void requestInstallPermission(@Nullable Context context, @Nullable InstallResult installResult) {
        PermissionUtils.requestInstallPermission(context, installResult);
    }

    public static void requestInstallPermission(@Nullable Context context, @StringRes int requestStringRes, @DrawableRes int drawableRes, @Nullable InstallResult installResult) {
        PermissionUtils.requestInstallPermission(context, requestStringRes, drawableRes, installResult);
    }
}
