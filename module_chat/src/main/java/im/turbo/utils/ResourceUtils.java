package im.turbo.utils;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.ArrayRes;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import java.io.InputStream;
import java.util.Arrays;

import im.thebot.user.ContactUtil;

/**
 * created by zhaoyuntao
 * on 14/12/2021
 * description:
 */
public class ResourceUtils {
    private static Application application;
    private static final String TAG = "ResourceUtils";

    public static void initApplication(Application application) {
        ResourceUtils.application = application;
    }

    public static Application getApplication() {
        if (application == null) {
            throw new IllegalStateException("Common library ResourceUtils is used before initialize!");
        }
        return application;
    }

    public static Object getSystemService(String name) {
        if (application == null) {
            throw new IllegalStateException("Common library ResourceUtils is used before initialize!");
        }
        return application.getSystemService(name);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static <T> T getSystemService(Class<T> clazz) {
        if (application == null) {
            throw new IllegalStateException("Common library ResourceUtils is used before initialize!");
        }
        return application.getSystemService(clazz);
    }

    public static String getString(@StringRes int stringId, Object... formatArgs) {
        if (formatArgs != null && formatArgs.length > 0) {
            try {
                return getApplication().getString(stringId, formatArgs);
            } catch (Exception e) {
                try {
                    return getApplication().getString(stringId);
                } catch (Exception e2) {
                    return "?";
                }
            }
        } else {
            try {
                return getApplication().getString(stringId);
            } catch (Exception e2) {
                return "?";
            }
        }
    }

    public static String[] getStringArray(@ArrayRes int res) {
        return getApplication().getResources().getStringArray(res);
    }

    @ColorInt
    public static int getColor(@ColorRes int colorResourceId) {
        if (application != null) {
            return ContextCompat.getColor(application, colorResourceId);
        }
        return 0;
    }

    public static Drawable getDrawable(@DrawableRes int drawableResourceId) {
        if (application != null) {
            return ContextCompat.getDrawable(application, drawableResourceId);
        }
        return null;
    }

    public static ContentResolver getContentResolver() {
        return getApplication().getContentResolver();
    }

    public static String getPackageName() {
        return application.getPackageName();
    }

    public static Context getApplicationContext() {
        return application;
    }
}
