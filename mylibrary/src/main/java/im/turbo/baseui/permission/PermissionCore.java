package im.turbo.baseui.permission;

import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.doctor.mylibrary.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import im.turbo.baseui.lifecircle.LifeCycleHelper;

/**
 * created by zhaoyuntao
 * on 2022/5/28
 */
class PermissionCore {
    static final int MODE_ASK = 4;
    static final int MODE_COMPAT = 5;
    private static final String STORAGE_PERMISSION = "PermissionCore";

    static boolean isDeniedForever(Context context, @Permission String... permissions) {
        for (String permission : permissions) {
            if (!hasPermission(context, permission) && PermissionCore.isNotAskPermission(context, permission) && alwaysDeniedPermissionBefore(context, permission)) {
                return true;
            }
        }
        return false;
    }

    static boolean isNotAskPermission(Context context, @Permission String permission) {
        return !isShowRationalePermission(context, permission);
    }

    static String[] filterPermissions(@Permission String... permissions) {
        List<String> arrayList = new ArrayList<>(new HashSet<>(Arrays.asList(permissions)));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            arrayList.remove(Permission.READ_PHONE_NUMBERS);
            arrayList.remove(Permission.ANSWER_PHONE_CALLS);
        }

        if (Build.VERSION.SDK_INT < 29) {
            arrayList.remove(Permission.ACTIVITY_RECOGNITION);
            arrayList.remove(Permission.ACCESS_BACKGROUND_LOCATION);
        }
        return arrayList.toArray(new String[0]);
    }

    @NonNull
    @Permission
    static String[] getDeniedPermissions(Context context, @Permission String... permissions) {
        List<String> deniedList = new ArrayList<>(1);
        for (String permission : permissions) {
            if (!hasPermission(context, permission)) {
                deniedList.add(permission);
            }
        }
        return deniedList.toArray(new String[0]);
    }

    static boolean isShowRationalePermission(Context context, @Permission String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false;
        Activity activity = LifeCycleHelper.findActivity(context);
        if (activity != null) {
            return activity.shouldShowRequestPermissionRationale(permission);
        } else {
            PackageManager packageManager = context.getPackageManager();
            Class<?> pkManagerClass = packageManager.getClass();
            try {
                Method method = pkManagerClass.getMethod("shouldShowRequestPermissionRationale", String.class);
                if (!method.isAccessible()) method.setAccessible(true);
                return (boolean) method.invoke(packageManager, permission);
            } catch (Exception ignored) {
                return false;
            }
        }
    }

    static boolean hasPermission(Context context, @Permission String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            int result = context.checkPermission(permission, android.os.Process.myPid(), android.os.Process.myUid());
            if (result == PackageManager.PERMISSION_DENIED) {
                return false;
            }
            String op = AppOpsManager.permissionToOp(permission);
            if (!TextUtils.isEmpty(op)) {
                AppOpsManager opsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                result = opsManager.checkOpNoThrow(op, android.os.Process.myUid(), context.getPackageName());
                if (result != AppOpsManager.MODE_ALLOWED && result != MODE_ASK) {
                    return false;
                }
            }
        }
        return true;
    }

    static int getPermissionDrawable(@Permission String... permissions) {
        String permissionKey;
        if (permissions.length == 1) {
            permissionKey = permissions[0];
        } else if (permissions.length >= 2) {
            Arrays.sort(permissions);
            permissionKey = permissions[0] + permissions[1];
        } else {
            permissionKey = Manifest.permission.READ_PHONE_STATE;
        }
        switch (permissionKey) {
            case Manifest.permission.RECORD_AUDIO:
                return R.drawable.permission_ic_audio;
            case Manifest.permission.CAMERA:
                return R.drawable.permission_ic_camera;
            case Manifest.permission.READ_CONTACTS:
            case Manifest.permission.WRITE_CONTACTS:
                return R.drawable.permission_ic_contact;
            case Manifest.permission.ACCESS_COARSE_LOCATION:
            case Manifest.permission.ACCESS_FINE_LOCATION:
                return R.drawable.permission_ic_location;
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                return R.drawable.permission_ic_storage;
            case Manifest.permission.CAMERA + Manifest.permission.RECORD_AUDIO:
                return R.drawable.permission_ic_audio_camera;
            case Manifest.permission.CAMERA + Manifest.permission.READ_EXTERNAL_STORAGE:
            case Manifest.permission.CAMERA + Manifest.permission.WRITE_EXTERNAL_STORAGE:
                return R.drawable.permission_ic_file_camera;
            case Manifest.permission.READ_CONTACTS + Manifest.permission.READ_EXTERNAL_STORAGE:
            case Manifest.permission.READ_CONTACTS + Manifest.permission.WRITE_EXTERNAL_STORAGE:
            case Manifest.permission.WRITE_CONTACTS + Manifest.permission.WRITE_EXTERNAL_STORAGE:
            case Manifest.permission.READ_EXTERNAL_STORAGE + Manifest.permission.WRITE_CONTACTS:
                return R.drawable.permission_ic_storage_contact;
            case Manifest.permission.READ_EXTERNAL_STORAGE + Manifest.permission.RECORD_AUDIO:
            case Manifest.permission.RECORD_AUDIO + Manifest.permission.WRITE_EXTERNAL_STORAGE:
                return R.drawable.permission_ic_audio_storage;
            case Manifest.permission.ACTIVITY_RECOGNITION:
                return R.drawable.permission_ic_steps;
            default:
            case Manifest.permission.READ_PHONE_STATE:
                return R.drawable.permission_ic_phone;
        }
    }

    static boolean canRequestPackageInstalls(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (context.getApplicationInfo().targetSdkVersion < Build.VERSION_CODES.O) {
                int uid = context.getApplicationInfo().uid;
                try {
                    Class<AppOpsManager> appOpsClass = AppOpsManager.class;
                    Method method = appOpsClass.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class);
                    Field opField = appOpsClass.getDeclaredField("OP_REQUEST_INSTALL_PACKAGES");
                    int opValue = (int) opField.get(Integer.class);
                    int result = (int) method.invoke((AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE), opValue, uid, context.getApplicationContext().getPackageName());
                    return result == AppOpsManager.MODE_ALLOWED || result == MODE_ASK || result == MODE_COMPAT;
                } catch (Throwable e) {
                    return true;
                }
            }
            return context.getPackageManager().canRequestPackageInstalls();
        }
        return true;
    }

    static boolean alwaysDeniedPermissionBefore(Context context, @Permission String deniedPermission) {
        int mode = Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS;
        SharedPreferences pref = context.getSharedPreferences(STORAGE_PERMISSION, mode);
        return pref.getBoolean(deniedPermission, false);
    }

    static void setAlwaysDeniedPermissionBefore(Context context, @Permission String[] deniedPermissions, boolean alwaysDeniedPermissionBefore) {
        int mode = Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS;
        SharedPreferences pref = context.getSharedPreferences(STORAGE_PERMISSION, mode);
        for (String permission : deniedPermissions) {
            pref.edit().putBoolean(permission, alwaysDeniedPermissionBefore).apply();
        }
    }
}
