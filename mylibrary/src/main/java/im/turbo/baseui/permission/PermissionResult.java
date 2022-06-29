package im.turbo.baseui.permission;

import androidx.annotation.NonNull;

/**
 * created by zhaoyuntao
 * on 2022/5/28
 */
public interface PermissionResult {
    void onGranted(@NonNull String[] grantedPermissions);

    default void onDenied(@NonNull String[] deniedPermissions) {

    }

    default void onCanceled(@NonNull String[] permissions) {
    }
}
