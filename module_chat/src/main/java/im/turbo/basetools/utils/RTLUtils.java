package im.turbo.basetools.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

/**
 * created by zhaoyuntao
 * on 2019-10-06
 * description:
 * Judge system language environments direction is RTL or LTR
 * Judge a strings direction is RTL or LTR
 */
public class RTLUtils {

    public static final int LTR = 0;
    public static final int RTL = 1;

    /**
     * get system layout direction
     *
     * @return
     */
    public static boolean isRTL(Context context) {
        if (context == null) {
            return false;
        }
        return getSystemDirection(context) == View.LAYOUT_DIRECTION_RTL;
    }

    public static int getStringDirection(Context context, CharSequence message) {
        if (context == null || message == null) {
            return LTR;
        }
        //find first character which is RTL or LTR
        for (int i = 0; i < message.length(); i++) {
            char character = message.charAt(i);
            int direction = Character.getDirectionality(character);
            boolean isRTL = (direction == Character.DIRECTIONALITY_RIGHT_TO_LEFT || direction == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC || direction == Character.DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING || direction == Character.DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE);
            boolean isLTR = (direction == Character.DIRECTIONALITY_LEFT_TO_RIGHT || direction == Character.DIRECTIONALITY_LEFT_TO_RIGHT_EMBEDDING || direction == Character.DIRECTIONALITY_LEFT_TO_RIGHT_OVERRIDE);
            if (isRTL) {
                return RTL;
            } else if (isLTR) {
                return LTR;
            }
        }

        return RTLUtils.isRTL(context) ? RTL : LTR;
    }

    public static int getSystemDirection(Context context) {
        if (context == null) {
            return View.LAYOUT_DIRECTION_LOCALE;
        }
        return TextUtils.getLayoutDirectionFromLocale(context.getResources().getConfiguration().locale);
    }
}
