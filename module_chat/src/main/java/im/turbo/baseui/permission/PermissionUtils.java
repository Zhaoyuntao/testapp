package im.turbo.baseui.permission;

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
