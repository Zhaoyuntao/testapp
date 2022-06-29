package im.turbo.basetools.vibrate;

import static android.content.Context.VIBRATOR_SERVICE;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

/**
 * created by zhaoyuntao
 * on 2019-10-23
 * description:a vibrator tool
 */
public class VibratorUtil {

    public static void vibrate(Context context) {
        if (context == null) {
            return;
        }
        long milliseconds = 30;
        Vibrator vib = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        if (vib != null) {
            if (Build.VERSION.SDK_INT >= 26) {
                vib.vibrate(VibrationEffect.createOneShot(milliseconds, 5));
            } else {
                vib.vibrate(milliseconds);
            }
        }
    }

    public static void vibrate(Context context, long milliseconds) {
        if (context == null) {
            return;
        }
        Vibrator vib = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        if (vib != null) {
            if (Build.VERSION.SDK_INT >= 26) {
                vib.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vib.vibrate(milliseconds);
            }
        }
    }
}
