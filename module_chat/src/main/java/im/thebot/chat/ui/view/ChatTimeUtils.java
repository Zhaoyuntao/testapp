package im.thebot.chat.ui.view;

import com.example.module_chat.R;

import java.util.Locale;

import im.turbo.basetools.time.TimeUtils;
import im.turbo.utils.ResourceUtils;

/**
 * created by zhaoyuntao
 * on 14/04/2022
 * description:
 */
public class ChatTimeUtils {
    public static String getTimeTitleString(long time) {
        if (TimeUtils.isSameDay(System.currentTimeMillis(), time)) {
            return "today";// + " " + getTimeString(time, Locale.US);
        } else if (TimeUtils.isYesterday(time)) {
            return ResourceUtils.getString(R.string.yesterday);// + " " + getTimeString(time, Locale.US);
        }else if (TimeUtils.isSameYear(System.currentTimeMillis(), time)) {
            return TimeUtils.getTimeString(time, TimeUtils.TIME_FORMAT_MD);//DateFormat.is24HourFormat(ResourceUtils.getApplication()) ? TIME_FORMAT_MD_HM_24 : TIME_FORMAT_MD_HM, Locale.US);
        } else {
            return TimeUtils.getTimeString(time, TimeUtils.TIME_FORMAT_YMD, Locale.US);
        }
    }
}
