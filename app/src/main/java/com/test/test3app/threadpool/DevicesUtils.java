
package com.test.test3app.threadpool;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.zhaoyuntao.androidutils.tools.S;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.regex.Pattern;

public class DevicesUtils {
    public static final String HUAWEI = "huawei";
    public static final String SAMSUNG = "samsung";
    public static final String XIAOMI = "xiaomi";
    public static final String OPPO = "oppo";
    public static final String VIVO = "vivo";

    public static boolean legacyDevices() {
        return false;
    }

    public static boolean honeyCombDevices() {
        return true;
    }

    public static boolean lollipopDevices() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean kitkatDevices() {
        return true;
    }

    public static boolean jellyBeanMR1Devices() {
        return true;
    }

    public static boolean jellyBeanMR2Devices() {
        return true;
    }

    public static boolean jellyBeanDevices() {
        return true;
    }

    public static boolean marshmallowDevices() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean oreoDevices() {
        // return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
        return Build.VERSION.SDK_INT >= 26;
    }

    private static int numOfCpuCores = -1;

    /**
     * Gets the number of cores available in this device, across all processors.
     * Requires: Ability to peruse the filesystem at "/sys/devices/system/cpu"
     *
     * @return The number of cores, or 1 if failed to get result
     */
    public static int getNumCores() {
        if (numOfCpuCores > 0) {
            return numOfCpuCores;
        }

        //Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //Check if filename is "cpu", followed by a single digit number
                if (Pattern.matches("cpu[0-9]+", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            //Return the number of cores (virtual CPU devices)
            numOfCpuCores = files.length > 0 ? files.length : 1;
        } catch (Throwable e) {
            numOfCpuCores = -1;
        }

        if (numOfCpuCores <= 0) {
            numOfCpuCores = Runtime.getRuntime().availableProcessors();
        }

        if (numOfCpuCores <= 0) {
            //Default to return 1 core
            numOfCpuCores = 1;
        }

        return numOfCpuCores;
    }

    private static int memoryMB = -1;

    public static boolean lowPhysicalMemoryDevices() {
        if (lowRamDevice()) {
            return true;
        }
        if (memoryMB == -1) {
            memoryMB = (int) (getPhysicalMemoryKBs() / 1024);
        }

        return (memoryMB < 300);
    }

    public static boolean lowPhysicalMemoryDevicesmLessThan3G() {
        if (lowPhysicalMemoryDevices()) {
            return true;
        }

        return (memoryMB < 3000);
    }

    private static Boolean sLowRamDevice;

    private static boolean lowRamDevice() {
        if (sLowRamDevice == null) {
            if (kitkatDevices()) {
                ActivityManager am = (ActivityManager) ResourceUtils.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
                sLowRamDevice = am.isLowRamDevice();
            } else {
                sLowRamDevice = false;
            }
        }

        return sLowRamDevice;
    }

    private static long sPhysicalMemory = 0L;

    public static Long getPhysicalMemoryKBs() {
        // read /proc/meminfo to find MemTotal 'MemTotal: 711480 kB'
        // This operation would complete in fixed time

        if (sPhysicalMemory == 0L) {
            final String PATTERN = "MemTotal:";

            InputStream inStream = null;
            InputStreamReader inReader = null;
            BufferedReader inBuffer = null;

            try {
                inStream = new FileInputStream("/proc/meminfo");
                inReader = new InputStreamReader(inStream);
                inBuffer = new BufferedReader(inReader);

                String s;
                while ((s = inBuffer.readLine()) != null && s.length() > 0) {
                    if (s.startsWith(PATTERN)) {
                        String memKBs = s.substring(PATTERN.length()).trim();
                        memKBs = memKBs.substring(0, memKBs.indexOf(' '));
                        sPhysicalMemory = Long.parseLong(memKBs);
                        break;
                    }
                }
            } catch (Exception e) {
            } finally {
                ResourceUtils.silentlyClose(inStream);
                ResourceUtils.silentlyClose(inReader);
                ResourceUtils.silentlyClose(inBuffer);
            }
        }

        return sPhysicalMemory;
    }

    public static boolean lowMemoryDevices() {
        return getHeapSize() < 30;
    }

    private static int sHeapSize = -1;

    public static int getHeapSize() {
        if (sHeapSize <= 0) {
            ActivityManager am = (ActivityManager) ResourceUtils.getApplicationContext().getSystemService(
                    Context.ACTIVITY_SERVICE);
            sHeapSize = am.getMemoryClass();
            S.s("heapSize=" + sHeapSize);
        }
        return sHeapSize;
    }

    private static String deviceDesc = null;

    public static String getDeviceDescription() {
        if (deviceDesc == null) {
            StringBuffer sb = new StringBuffer();
            sb.append('\n');
            sb.append('\t').append("Build.MANUFACTURER\t").append(Build.MANUFACTURER).append('\n');
            sb.append('\t').append("Build.MODEL\t").append(Build.MODEL).append('\n');
            sb.append('\t').append("Build.PRODUCT\t").append(Build.PRODUCT).append('\n');
            sb.append('\t').append("Build.DEVICE\t").append(Build.DEVICE).append('\n');
            sb.append('\t').append("Build.BOARD\t").append(Build.BOARD).append('\n');
            sb.append('\t').append("Build.BRAND\t").append(Build.BRAND).append('\n');
            sb.append('\t').append("Build.CPU_ABI\t").append(Build.CPU_ABI).append('\n');
            sb.append('\t').append("Build.DISPLAY\t").append(Build.DISPLAY).append('\n');
            sb.append('\t').append("Build.FINGERPRINT\t").append(Build.FINGERPRINT).append('\n');
            sb.append('\t').append("Build.HARDWARE\t").append(Build.HARDWARE).append('\n');
            sb.append('\t').append("Build.RADIO\t").append(Build.RADIO).append('\n');
            sb.append('\t').append("Build.SERIAL\t").append(getSN()).append('\n');
            sb.append('\t').append("Build.TAGS\t").append(Build.TAGS).append('\n');
            sb.append('\t').append("Build.TYPE\t").append(Build.TYPE).append('\n');
            sb.append('\t').append("Build.SDK_INT\t").append(Build.VERSION.SDK_INT).append('\n');
            deviceDesc = sb.toString();
        }

        return deviceDesc;
    }

    private static Boolean mIsHuaWeiDevice = null;
    public static boolean isHuaWei() {
        if (mIsHuaWeiDevice == null) {
            mIsHuaWeiDevice = Build.MANUFACTURER.toLowerCase().contains(HUAWEI) || Build.FINGERPRINT.toLowerCase(Locale.US).contains(HUAWEI);
        }
        return mIsHuaWeiDevice;
    }

    public static boolean isHuaWei10Above() {
        return Build.VERSION.SDK_INT >= 29 && isHuaWei();
    }

    private static Boolean isSamsungDevice = null;
    public static boolean isSamsung() {
        if (isSamsungDevice == null) {
            isSamsungDevice = Build.FINGERPRINT.toLowerCase().contains(SAMSUNG);
        }

        return isSamsungDevice;
    }

    private static Boolean sIsXiaomi = null;
    // detects mi-phone or miui rom
    public static boolean isXiaomi() {
        if (sIsXiaomi == null) {
            boolean isXiaomi = false;

            if (Build.FINGERPRINT != null) {
                if (Build.FINGERPRINT.toLowerCase().contains(XIAOMI)) {
                    isXiaomi = true;
                }
            }

            if (Build.PRODUCT != null) {
                if (Build.PRODUCT.toLowerCase().contains(XIAOMI)) {
                    isXiaomi = true;
                }
            }

            if (Build.MANUFACTURER != null) {
                if (Build.MANUFACTURER.toLowerCase().contains(XIAOMI)) {
                    isXiaomi = true;
                }
            }

            sIsXiaomi = isXiaomi;
        }

        return sIsXiaomi;
    }

    private static Boolean isOppoDevice = null;
    public static boolean isOppo() {
        if (isOppoDevice == null) {
            isOppoDevice = Build.FINGERPRINT.toLowerCase().contains(OPPO);
        }
        return isOppoDevice;
    }

    private static Boolean isVivoDevice = null;
    public static boolean isVivo() {
        if (isVivoDevice == null) {
            isVivoDevice = Build.FINGERPRINT.toLowerCase().contains(VIVO);
        }
        return isVivoDevice;
    }

    private static Boolean sIsLenovoK900 = null;
    public static boolean isLenovoK900() {
        //        [ro.build.fingerprint]: [Lenovo/K900/K900:4.2.1/JOP40D/K900_1_S_2_009_0145_130508:user/releasekey]
        //        [ro.product.device]: [K900]
        //        [ro.product.manufacturer]: [LENOVO]

        if (sIsLenovoK900 == null) {
            if (Build.FINGERPRINT != null && Build.FINGERPRINT.contains("Lenovo/K900")) {
                sIsLenovoK900 = true;
            } else if ("K900".equalsIgnoreCase(Build.DEVICE) && "LENOVO".equalsIgnoreCase(Build.MANUFACTURER)) {
                sIsLenovoK900 = true;
            } else {
                sIsLenovoK900 = false;
            }
        }

        return sIsLenovoK900;
    }

    private static Boolean sIsMX4Pro = null;
    public static boolean isMeizuMX4Pro() {
        if (sIsMX4Pro == null) {
            if ("mx4pro".equalsIgnoreCase(Build.DEVICE) && "Meizu".equalsIgnoreCase(Build.MANUFACTURER)) {
                sIsMX4Pro = true;
            } else {
                sIsMX4Pro = false;
            }
        }
        return sIsMX4Pro;
    }

    public static boolean isMeizuMX2() {
        final String fingerprint = Build.FINGERPRINT;
        return fingerprint != null && (fingerprint.contains("meizu_mx2"));
    }

    private static String sMainIMEI = null;
    public static String getIMEI() {
        if (sMainIMEI == null || sMainIMEI.length() < 4) {
            TelephonyManager tm = (TelephonyManager) ResourceUtils.getApplicationContext().getSystemService(
                    Context.TELEPHONY_SERVICE);

            try {
                // need android.permission.READ_PHONE_STATE
                sMainIMEI = tm.getDeviceId();
            } catch (Exception e) {
                sMainIMEI = "No-Permission-IMEI: " + Build.FINGERPRINT;
            }
        }

        return sMainIMEI;
    }

    private static String sMainIMSI = null;
    public static String getIMSI() {
        if (sMainIMSI == null || sMainIMSI.length() < 4) {
            TelephonyManager tm = (TelephonyManager) ResourceUtils.getApplicationContext().getSystemService(
                    Context.TELEPHONY_SERVICE);

            try {
                // need android.permission.READ_PHONE_STATE
                sMainIMSI = tm.getSubscriberId();
            } catch (Exception e) {
                sMainIMSI = "No-Permission-IMSI: " + Build.FINGERPRINT;
            }
        }

        return sMainIMSI;
    }

    private static String sSerialNumber = null;
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static String getSN() {
        if (TextUtils.isEmpty(sSerialNumber)) {
            try {
                sSerialNumber = Build.SERIAL;
            } catch (Exception e) {
                sSerialNumber = "Serial-Not-Available";
            }
        }

        return sSerialNumber;
    }

    private static String sPhoneNumber = null;
    public static String getPhoneNumber() {
        if (TextUtils.isEmpty(sPhoneNumber)) {
            TelephonyManager tm = (TelephonyManager) ResourceUtils.getApplicationContext().getSystemService(
                    Context.TELEPHONY_SERVICE);

            try {
                // need android.permission.READ_PHONE_STATE
                sPhoneNumber = tm.getLine1Number();
            } catch (Exception e) {
            }

            if (TextUtils.isEmpty(sPhoneNumber)) {
                sPhoneNumber = "";
            }
        }

        return sPhoneNumber;
    }

    private static String sSimContryCode = null;
    public static String getSimCountryCode() {
        if (sSimContryCode == null) {
            TelephonyManager tm = (TelephonyManager) ResourceUtils.getApplicationContext().getSystemService(
                    Context.TELEPHONY_SERVICE);
            try {
                // need android.permission.READ_PHONE_STATE
                sSimContryCode = tm.getSimCountryIso();
            } catch (Exception e) {
            }
            if (TextUtils.isEmpty(sSimContryCode)) {
                sSimContryCode = "";
            }
        }
        return sSimContryCode;
    }

    /**
     * 得到屏幕的物理尺寸，由于该尺寸是在出厂时，厂商写死的，所以仅供参考
     * 计算方法：获取到屏幕的分辨率:point.x和point.y，再取出屏幕的DPI（每英寸的像素数量），
     * 计算长和宽有多少英寸，即：point.x / dm.xdpi，point.y / dm.ydpi，屏幕的长和宽算出来了，
     * 再用勾股定理，计算出斜角边的长度，即屏幕尺寸。
     *
     * @param context
     * @return
     */
    public static double getPhysicsScreenSize(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        manager.getDefaultDisplay().getRealSize(point);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        double x = Math.pow(point.x / dm.xdpi, 2);//dm.xdpi是屏幕x方向的真实密度值，比上面的densityDpi真实。
        double y = Math.pow(point.y / dm.ydpi, 2);//dm.xdpi是屏幕y方向的真实密度值，比上面的densityDpi真实。
        return Math.sqrt(x + y);
    }

    public static double getScaledPhysicsScreenSize(Context context, double thumbnailSubScale) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        manager.getDefaultDisplay().getRealSize(point);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        double mScreenWidth = point.x / dm.xdpi;//dm.xdpi是屏幕x方向的真实密度值，比上面的densityDpi真实。
        double mThumbnailWidth = mScreenWidth / thumbnailSubScale;
        double mThumbnailHeight = mScreenWidth * (1f / thumbnailSubScale) * (4f / 3f);
        return Math.sqrt(Math.pow(mThumbnailWidth, 2) + Math.pow(mThumbnailHeight, 2));
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }
}
