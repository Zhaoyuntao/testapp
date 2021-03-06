package im.turbo.utils;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import androidx.core.text.TextUtilsCompat;
import androidx.core.view.ViewCompat;

import java.lang.reflect.Method;
import java.util.Locale;

import im.turbo.baseui.utils.UiUtils;

/**
 * Description:
 * <p>
 * Created by ____lamer on 2019/4/11 11:36 AM
 */
public class ScreenUtils {

    //Generated code from Code Templates. Do not modify!
    private static final String TAG = "ScreenUtils";

    public static int getScreenWidthPX() {
        Context context = ResourceUtils.getApplicationContext();
        if (context != null) {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            if (dm != null) {
                return dm.widthPixels;
            }
        }
        return -1;
    }

    public static int getScreenHeightPX() {
        Context context = ResourceUtils.getApplicationContext();
        if (context != null) {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            if (dm != null) {
                return dm.heightPixels;
            }
        }
        return -1;
    }

    public static int getScreenWidthDP() {
        Context context = ResourceUtils.getApplicationContext();
        if (context != null) {
            Configuration configuration = context.getResources().getConfiguration();
            if (configuration != null) {
                return configuration.screenWidthDp;
            }
        }
        return -1;
    }

    public static int getScreenHeightDP() {
        Context context = ResourceUtils.getApplicationContext();
        if (context != null) {
            Configuration configuration = context.getResources().getConfiguration();
            if (configuration != null) {
                return configuration.screenHeightDp;
            }
        }
        return -1;
    }


    public static boolean isRTLLanguage() {
        int direction = TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault());
        return direction == ViewCompat.LAYOUT_DIRECTION_RTL;
    }

    /**
     * ?????????????????????
     */
    public static int getStatusBarHeight() {
        Resources resources = ResourceUtils.getApplicationContext().getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    /**
     * ??????????????????????????????????????????????????????????????????meetFull????????????????????????
     *
     * @return
     */
    public static int getScreenShorterWidthPX() {
        return Math.min(getScreenWidthPX(), getScreenHeightPX());
    }

    public static boolean isScreenOn() {
        // return m_isScreenOn;
        Context context = ResourceUtils.getApplicationContext();
        KeyguardManager km = (KeyguardManager) context
                .getSystemService(Context.KEYGUARD_SERVICE);
        if (km.inKeyguardRestrictedInputMode()) {
            return false;
        }
        return true;
    }

    /**
     * Comment by shangkai
     * ?????????????????????isSreenLock=false???????????????locked????????????true
     * ?????????lock????????????????????????
     *
     * @return
     */
    public static boolean isScreenLock() {
        KeyguardManager mKeyguardManager = (KeyguardManager) ResourceUtils.getApplicationContext()
                .getSystemService(Context.KEYGUARD_SERVICE);
        return mKeyguardManager.inKeyguardRestrictedInputMode();
    }

    public static boolean hasNavigationBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");

        if (resourceId != 0) {
            String sNavBarOverride = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                try {
                    Class c = Class.forName("android.os.SystemProperties");
                    //noinspection unchecked
                    Method m = c.getDeclaredMethod("get", String.class);
                    m.setAccessible(true);
                    sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
                } catch (Throwable e) {
                    sNavBarOverride = "";
                }
            }

            boolean hasNav = res.getBoolean(resourceId);

            // Check override flag (see static block)
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }

            return hasNav;
        } else {
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    /**
     * ????????????????????????????????????????????????????????????????????????????????????
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(final Context context) {
        if (!checkDeviceHasNavigationBar(context)) {
            return 0;
        }
        int size = 0;
        Resources resources = context.getResources();

        boolean isPortrait = true;
        int resourceId;
        if (isTablet(context)) {
            resourceId = resources.getIdentifier(isPortrait ? "navigation_bar_height" : "navigation_bar_height_landscape", "dimen", "android");
        } else {
            resourceId = resources.getIdentifier(isPortrait ? "navigation_bar_height" : "navigation_bar_width", "dimen", "android");
        }

        if (resourceId > 0) {
            size = resources.getDimensionPixelSize(resourceId);
        }

        if (size <= 0) {
            size = UiUtils.dipToPx(48);
        }

        return size;
    }

    private static boolean isTablet(final Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean checkDeviceHasNavigationBar(Context context) {
        int navigationBarIsMin = 0;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            navigationBarIsMin = Settings.System.getInt(context.getContentResolver(),
                    getDeviceInfo(), 0);
        } else {
            if (Build.BRAND.equalsIgnoreCase("VIVO") || Build.BRAND.equalsIgnoreCase("OPPO")) {
                navigationBarIsMin = Settings.Secure.getInt(context.getContentResolver(),
                        getDeviceInfo(), 0);
            } else {
                navigationBarIsMin = Settings.Global.getInt(context.getContentResolver(),
                        getDeviceInfo(), 0);
            }
        }
        return navigationBarIsMin != 1;
    }

    private static String getDeviceInfo() {
        String mDeviceInfo;
        String brand = Build.BRAND;
        if (brand.equalsIgnoreCase("HUAWEI")) {
            mDeviceInfo = "navigationbar_is_min";
        } else if (brand.equalsIgnoreCase("XIAOMI")) {
            mDeviceInfo = "force_fsg_nav_bar";
        } else if (brand.equalsIgnoreCase("VIVO")) {
            mDeviceInfo = "navigation_gesture_on";
        } else if (brand.equalsIgnoreCase("OPPO")) {
            mDeviceInfo = "navigation_gesture_on";
        } else {
            mDeviceInfo = "navigationbar_is_min";
        }

        return mDeviceInfo;
    }

    /**
     * ??????????????????????????????
     * ??????????????????????????????????????????????????????????????????
     * @param context
     * @return
     */
    public static int getNavigationBarSize(Context context) {
        Point appUsableSize = getAppUsableScreenSize(context);
        Point realScreenSize = getRealScreenSize(context);

        Point point = new Point();
        int size = 0;

        // navigation bar on the right
        if (appUsableSize.x < realScreenSize.x) {
            point = new Point(realScreenSize.x - appUsableSize.x, appUsableSize.y);
        }

        // navigation bar at the bottom
        if (appUsableSize.y < realScreenSize.y) {
            point = new Point(appUsableSize.x, realScreenSize.y - appUsableSize.y);
        }
        int y = point.y;
        if (y < 0) {
            size = getNavigationBarHeight(context);
        } else {
            size = y;
        }
        // navigation bar is not present
        return size;
    }

    public static Point getAppUsableScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static Point getRealScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();

        display.getRealSize(size);

        return size;
    }

    @SuppressLint("InvalidWakeLockTag")
    public static void wakeUpScreen() {
        Context context = ResourceUtils.getApplicationContext();
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
//        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //??????
//        kl.disableKeyguard();
        //???????????????????????????
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //??????PowerManager.WakeLock??????,???????????????|???????????????????????????,????????????LogCat?????????Tag
         PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        //????????????
        wl.acquire();
        //??????
        wl.release();
    }
}
