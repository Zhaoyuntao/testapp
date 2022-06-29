package im.turbo.baseui.permission;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import im.turbo.baseui.base.bridgefragment.BridgeCallback;
import im.turbo.baseui.base.bridgefragment.BridgeInterface;

public class IntentUtils {
    public static boolean isActivityAvailable(@NonNull Context context, @NonNull Intent intent) {
        return context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null;
    }

    public static void startActivity(@NonNull Context context, @NonNull Intent intent) {
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (!isActivityAvailable(context, intent)) {
        }
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
        }
    }

    public static void startActivity(@NonNull Context context, @NonNull Class<?> cls) {
        startActivity(context, cls, null);
    }

    public static void startActivity(@NonNull Context context, @NonNull Class<?> cls,
                                     @Nullable Bundle args) {
        Intent intent = new Intent(context, cls);
        if (args != null) {
            intent.putExtras(args);
        }
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void startActivityForResult(@NonNull Context context, @NonNull Class<?> cls, @Nullable Bundle args, BridgeCallback callback) {
        Intent intent = new Intent(context, cls);
        if (args != null) {
            intent.putExtras(args);
        }
        startActivityForResult(context, intent, callback);
    }

    public static void startActivityForResult(@NonNull Context context, @NonNull Intent intent, BridgeCallback callback) {
        BridgeInterface.getFragment(context).startActivityForResult(intent, callback);
    }
}
