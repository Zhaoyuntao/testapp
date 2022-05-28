package im.turbo.baseui.permission.tp;

import androidx.annotation.NonNull;

/**
 * created by zhaoyuntao
 * on 2022/5/28
 */
public interface PermissionResult {
    void onGranted(@NonNull String[] grantedPermissions);

    void onDenied(@NonNull String[] deniedPermissions);

    default void onCanceled(@NonNull String[] permissions) {
    }
}
