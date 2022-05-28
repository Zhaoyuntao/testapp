package im.turbo.baseui.permission.tp;

import android.content.Context;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;


/**
 * created by zhaoyuntao
 * on 2019-11-06
 * description:
 */
public class PermissionUtils {

    public static boolean isAlwaysDenied(@Nullable Context context, @NonNull @Permission String... permissions) {
        return PermissionCore.isDeniedForever(context, permissions);
    }

    public static boolean hasPermission(@Nullable Context context, @NonNull @Permission String... permissions) {
        return PermissionCore.hasPermission(context, permissions);
    }

    public static void requestStoragePermission(Context context, @Nullable final PermissionResult result) {
        requestPermission(context, result, Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE);
    }

    public static void requestStoragePermission(Context context, @StringRes int requestStringRes, @StringRes int guideStringRes, @Nullable final PermissionResult result) {
        requestPermission(context, requestStringRes, guideStringRes, result, Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE);
    }

    public static void requestCameraPermission(Context context, @Nullable final PermissionResult result) {
        requestPermission(context, result, Permission.CAMERA);
    }

    public static void requestCameraPermission(Context context, @StringRes int requestStringRes, @StringRes int guideStringRes, @Nullable final PermissionResult result) {
        requestPermission(context, requestStringRes, guideStringRes, result, Permission.CAMERA);
    }

    public static void requestPhonePermission(Context context, @Nullable final PermissionResult result) {
        requestPermission(context, result, Permission.READ_PHONE_STATE);
    }

    public static void requestPhonePermission(Context context, @StringRes int requestStringRes, @StringRes int guideStringRes, @Nullable final PermissionResult result) {
        requestPermission(context, requestStringRes, guideStringRes, result, Permission.READ_PHONE_STATE);
    }

    public static void requestCalendarPermission(Context context, @Nullable final PermissionResult result) {
        requestPermission(context, result, Permission.READ_CALENDAR, Permission.WRITE_CALENDAR);
    }

    public static void requestCalendarPermission(Context context, @StringRes int requestStringRes, @StringRes int guideStringRes, @Nullable final PermissionResult result) {
        requestPermission(context, requestStringRes, guideStringRes, result, Permission.READ_CALENDAR, Permission.WRITE_CALENDAR);
    }

    public static void requestLocationPermission(Context context, @Nullable final PermissionResult result) {
        requestPermission(context, result, Permission.ACCESS_FINE_LOCATION);
    }

    public static void requestLocationPermission(Context context, @StringRes int requestStringRes, @StringRes int guideStringRes, @Nullable final PermissionResult result) {
        requestPermission(context, requestStringRes, guideStringRes, result, Permission.ACCESS_FINE_LOCATION);
    }

    public static void requestPermission(@Nullable Context context, @StringRes int requestStringRes, @StringRes int guideStringRes, @Nullable final PermissionResult result, @NonNull @Permission final String... permissions) {
        PermissionBridgeFragment.requestPermissions(context, permissions, true, requestStringRes, guideStringRes, result);
    }

    public static void requestPermission(@Nullable Context context, @Nullable final PermissionResult result, @NonNull @Permission final String... permissions) {
        PermissionBridgeFragment.requestPermissions(context, permissions, result);
    }

    public static void requestInstallPermission(@Nullable Context context, @Nullable InstallResult installResult) {
        PermissionBridgeFragment.requestInstall(context, installResult);
    }

    public static void requestInstallPermission(@Nullable Context context, @StringRes int requestStringRes, @DrawableRes int drawableRes, @Nullable InstallResult installResult) {
        PermissionBridgeFragment.requestInstall(context, true, requestStringRes, drawableRes, installResult);
    }
}
