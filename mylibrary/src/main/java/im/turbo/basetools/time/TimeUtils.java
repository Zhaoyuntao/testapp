package im.turbo.basetools.time;

import static java.lang.Math.abs;

import android.text.TextUtils;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.StringDef;

import com.doctor.mylibrary.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import im.turbo.utils.ResourceUtils;

public class TimeUtils {

    @StringDef({
            TIME_FORMAT_HM_12,
            TIME_FORMAT_HM_12_CH,
            TIME_FORMAT_HMS,
            TIME_FORMAT_HM_24,
            TIME_FORMAT_YMD,
            TIME_FORMAT_MD,
            TIME_FORMAT_DDMM,
            TIME_FORMAT_MD_HM,
            TIME_FORMAT_MD_HMS,
            TIME_FORMAT_Y_M_D_HM_12,
            TIME_FORMAT_YMD_HM_12,
            TIME_FORMAT_YMD_HM_24,
            TIME_FORMAT_MD_HM_12,
            TIME_FORMAT_MD_HM_24,
            TIME_FORMAT_YMD_HMS_FILENAME,
            TIME_FORMAT_YMD_HMS_MILLISECOND,
            TIME_FORMAT_YYYY_MM,
            TIME_FORMAT_YMD_LOG,
            TIME_FORMAT_YYYYMMDD,
            TIME_FORMAT_YMD_HM,
            TIME_FORMAT_YMD_HMS,
            TIME_FORMAT_YMD_HMSS,
            TIME_FORMAT_YMD_HM_FOR_ISSUE,
            TIME_FORMAT_DETAIL,
            TIME_FORMAT_EN,
            TIME_FORMAT_DD_MM_YYYY,
            TIME_FORMAT_LOG
    })
    public @interface TIME_FORMAT {

    }

    public static final String TIME_FORMAT_HM_12 = "h:mm a";
    public static final String TIME_FORMAT_HM_12_CH = "ah:mm";
    public static final String TIME_FORMAT_HMS = "h:mm:ss a";
    public static final String TIME_FORMAT_HM_24 = "HH:mm";
    public static final String TIME_FORMAT_YMD = "d/M/y";
    public static final String TIME_FORMAT_MD = "d/M";
    public static final String TIME_FORMAT_DDMM = "dd MM";
    public static final String TIME_FORMAT_YMD_LOG = "y_M_d";
    public static final String TIME_FORMAT_MD_HM = "d/M h:mm a";
    public static final String TIME_FORMAT_MD_HMS = "d/M h:mm:ss a";
    public static final String TIME_FORMAT_Y_M_D_HM_12 = "y-M-d h:mm a";
    public static final String TIME_FORMAT_YYYYMMDD = "yyyy-MM-dd";
    public static final String TIME_FORMAT_YMD_HM = "yyyy-MM-dd HH:mm";
    public static final String TIME_FORMAT_YMD_HMS = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_FORMAT_LOG = "yyyy-MM-dd_HH:mm:ss";
    public static final String TIME_FORMAT_YMD_HMSS = "yyyy-MM-dd HH:mm:ss:SSS";
    public static final String TIME_FORMAT_YMD_HM_12 = "d/M/y  h:mm a";
    public static final String TIME_FORMAT_YMD_HM_24 = "d/M/y  HH:mm";
    public static final String TIME_FORMAT_MD_HM_12 = "d/M  h:mm a";
    public static final String TIME_FORMAT_MD_HM_24 = "d/M  HH:mm";
    public static final String TIME_FORMAT_DD_MM_YYYY = "dd MM yyyy";

    public static final String TIME_FORMAT_YMD_HMS_FILENAME = "yy-MM-dd_hh_mm_ss_a";
    public static final String TIME_FORMAT_YMD_HMS_MILLISECOND = "yy-MM-dd_hh_mm_ss_SSS";
    public static final String TIME_FORMAT_YYYYMMDD_HMS_MILLISECOND = "yyyyMMdd-HH:mm:ss:SSS";
    public static final String TIME_FORMAT_YYYYMMDD_HMS_MILLISECOND_2 = "HH:mm:ss:SSS";
    public static final String TIME_FORMAT_YYYY_MM = "yyyy-MM";
    public static final String TIME_FORMAT_YMD_HM_FOR_ISSUE = "yyyy/MM/dd,HH:mm";
    public static final String TIME_FORMAT_DETAIL = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String TIME_FORMAT_EN = "HH:mm MM/dd/yyyy";
    public static final long ONE_MINUTE = 60 * 1000;
    public static final long FIVE_MINUTES_MS = 5 * ONE_MINUTE;
    public static final long ONE_DAY = 24 * 60 * 60 * 1000;
    public static final long ONE_YEAR = 365 * ONE_DAY;
    public static final int YEAR = 365;

    public static int SECOND_TO_MILLSECONDS = 1;
    public static int MINUTE_TO_MILLSECONDS = 1 * 60;
    public static int HOUR_TO_MILLSECONDS = 1 * 60 * 60;

    public static String getTimeString(long time) {
        if (time == 0) {
            // only draft message and no save time
            return "";
        } else if (isSameDay(System.currentTimeMillis(), time)) {
            return getTimeString(time, Locale.US);
        } else if (isYesterday(time)) {
            return ResourceUtils.getString(R.string.yesterday);
        } else if (isSameYear(System.currentTimeMillis(), time)) {
            return getTimeString(time, TIME_FORMAT_MD, Locale.US);
        } else {
            return getTimeString(time, TIME_FORMAT_YMD, Locale.US);
        }
    }

    private static long toSeconds(long data) {
        return data / 1000L;
    }

    private static long toMinutes(long data) {
        return toSeconds(data) / 60L;
    }

    public static String getTimeTitleString(long time) {
        if (isSameDay(System.currentTimeMillis(), time)) {
            return ResourceUtils.getString(R.string.today);// + " " + getTimeString(time, Locale.US);
        } else if (isYesterday(time)) {
            return ResourceUtils.getString(R.string.yesterday);// + " " + getTimeString(time, Locale.US);
        }
//        else if (isInWeek(time)) {
//            return getWeekString(time);
//        }
        else if (isSameYear(System.currentTimeMillis(), time)) {
            return getTimeString(time, TIME_FORMAT_MD);//DateFormat.is24HourFormat(ResourceUtils.getApplication()) ? TIME_FORMAT_MD_HM_24 : TIME_FORMAT_MD_HM, Locale.US);
        } else {
            return getTimeString(time, TIME_FORMAT_YMD, Locale.US);
        }
    }
//    private static String getWeekString(long time) {
//        Calendar c2 = Calendar.getInstance();
//        c2.setTimeInMillis(time); // actual date
//        int weekIndex = c2.get(Calendar.DAY_OF_WEEK);
//        switch (weekIndex) {
//            case 1:
//                return ResourceUtils.getString(R.string.string_week_1);
//            case 2:
//                return ResourceUtils.getString(R.string.string_week_2);
//            case 3:
//                return ResourceUtils.getString(R.string.string_week_3);
//            case 4:
//                return ResourceUtils.getString(R.string.string_week_4);
//            case 5:
//                return ResourceUtils.getString(R.string.string_week_5);
//            case 6:
//                return ResourceUtils.getString(R.string.string_week_6);
//            case 7:
//                return ResourceUtils.getString(R.string.string_week_7);
//        }
//        return getTimeString(time, TIME_FORMAT_YMD);
//    }

    public static String getTimeStringHMS(long time) {
        return getTimeString(time, Locale.getDefault());
    }

    public static String getTimeString(long time, Locale locale) {
        if (DateFormat.is24HourFormat(ResourceUtils.getApplication())) {
            return getTimeString(time, TIME_FORMAT_HM_24, locale);
        } else {
            if (locale.getLanguage().equals(Locale.CHINA.getLanguage())) {
                return getTimeString(time, TIME_FORMAT_HM_12_CH, locale);
            } else {
                return getTimeString(time, TIME_FORMAT_HM_12, locale);
            }
        }
    }

    public static String getTimeStringHM(long time) {
        if (DateFormat.is24HourFormat(ResourceUtils.getApplication())) {
            return getTimeString(time, TIME_FORMAT_HM_24, Locale.ENGLISH);
        } else {
            return getTimeString(time, TIME_FORMAT_HM_12, Locale.ENGLISH);
        }
    }

    public static boolean is24HourFormat() {
        return DateFormat.is24HourFormat(ResourceUtils.getApplication());
    }

    public static String getDateString(long time, Locale locale) {
        return getTimeString(time, TIME_FORMAT_YMD, locale);
    }

    public static String getTimeString(long time, @TIME_FORMAT String formation) {
        return getTimeString(time, formation, Locale.US);
    }

    public static String getTimeStringDefaultLocale(long time, @TIME_FORMAT String formation) {
        return getTimeString(time, formation, Locale.getDefault());
    }

    public static String getTimeString(long time, @TIME_FORMAT String formation, Locale locale) {
        return getTimeString(time, formation, locale, TimeZone.getDefault());
    }

    public static String getTimeString(long time, @TIME_FORMAT String formation, TimeZone timeZone) {
        return getTimeString(time, formation, Locale.ENGLISH, timeZone);
    }

    public static String getTimeString(long time, @TIME_FORMAT String formation, Locale locale, TimeZone timeZone) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formation, locale);
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.format(time);
    }

    public static String formatLongToDuration(long timeMills) {
        timeMills = abs(timeMills);
        long totalSeconds = (timeMills + 500) / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        Formatter formatter = new Formatter();
        return hours > 0
                ? formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
                : formatter.format("%02d:%02d", minutes, seconds).toString();
    }


    public static String getCurrentTimeString() {
        return getTimeString(System.currentTimeMillis(), TIME_FORMAT_Y_M_D_HM_12);
    }

    public static String getFileNameOfTime() {
        return getTimeString(System.currentTimeMillis(), TIME_FORMAT_YMD_HMS_FILENAME);
    }

    public static String getCurrentMilliSecondTime() {
        return getTimeString(System.currentTimeMillis(), TIME_FORMAT_YMD_HMS_MILLISECOND);
    }

    public static long getWeekStartTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static long getMonthStartTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static boolean isSameDay(long time1, long time2) {
        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();
        date1.setTimeInMillis(time1);
        date2.setTimeInMillis(time2);
        return date1.get(Calendar.ERA) == date2.get(Calendar.ERA) && date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR) && date1.get(Calendar.DAY_OF_YEAR) == date2.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isToday(long time) {
        return isSameDay(System.currentTimeMillis(), time);
    }

    public static boolean isSameHour(long time1, long time2) {
        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();
        date1.setTimeInMillis(time1);
        date2.setTimeInMillis(time2);
        return date1.get(Calendar.ERA) == date2.get(Calendar.ERA) && date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR) && date1.get(Calendar.DAY_OF_YEAR) == date2.get(Calendar.DAY_OF_YEAR) && date1.get(Calendar.HOUR_OF_DAY) == date2.get(Calendar.HOUR_OF_DAY);
    }

    public static boolean isSameMinute(long time1, long time2) {
        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();
        date1.setTimeInMillis(time1);
        date2.setTimeInMillis(time2);
        return date1.get(Calendar.ERA) == date2.get(Calendar.ERA) && date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR) && date1.get(Calendar.DAY_OF_YEAR) == date2.get(Calendar.DAY_OF_YEAR) && date1.get(Calendar.HOUR_OF_DAY) == date2.get(Calendar.HOUR_OF_DAY) && date1.get(Calendar.MINUTE) == date2.get(Calendar.MINUTE);
    }

    public static boolean isSameYear(long time1, long time2) {
        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();
        date1.setTimeInMillis(time1);
        date2.setTimeInMillis(time2);
        return date1.get(Calendar.ERA) == date2.get(Calendar.ERA) && date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR);
    }

    public static boolean isYesterday(long timeMills) {
        Calendar c1 = Calendar.getInstance(); // today
        c1.add(Calendar.DAY_OF_YEAR, -1); // yesterday
        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(timeMills); // actual date
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isTomorrow(long timeMills) {
        Calendar c1 = Calendar.getInstance(); // today
        c1.add(Calendar.DAY_OF_YEAR, 1); // yesterday
        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(timeMills); // actual date
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isInWeek(long timeMills) {
        Calendar c1 = Calendar.getInstance(); // today
        c1.add(Calendar.DAY_OF_YEAR, -7); // week
        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(timeMills); // actual date
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) <= c2.get(Calendar.DAY_OF_YEAR);
    }

    // 中国为+08:00
    public static String getTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        Calendar cal = GregorianCalendar.getInstance(tz);
        int offsetInMillis = tz.getOffset(cal.getTimeInMillis());
        return (offsetInMillis >= 0 ? "+" : "-")
                + String.format(Locale.US, "%02d:%02d", Math.abs(offsetInMillis / 3600000), Math.abs((offsetInMillis / 60000) % 60));
    }

    public static int getTimeZoneOffset() {
        TimeZone tz = TimeZone.getDefault();
        Calendar cal = GregorianCalendar.getInstance(tz);
        int offsetInMillis = tz.getOffset(cal.getTimeInMillis());
        return offsetInMillis / 3600000;
    }

    public static String getTimeZoneOffsetStringValue() {
        int offset = getTimeZoneOffset();
        return offset > 0 ? "+" + offset : String.valueOf(offset);
    }

    public static String getTimeZoneGMT() {
        TimeZone tz = TimeZone.getDefault();
        Calendar cal = GregorianCalendar.getInstance(tz);
        int offsetInMillis = tz.getOffset(cal.getTimeInMillis());
        return "GMT" + (offsetInMillis >= 0 ? "+" : "-")
                + String.format(Locale.US, "%02d:%02d", Math.abs(offsetInMillis / 3600000), Math.abs((offsetInMillis / 60000) % 60));
    }

    public static long getTimeLongValue(long timeMills) {
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeMills);
        int cYear = c.get(Calendar.YEAR);
        int cMonth = c.get(Calendar.MONTH);
        int cDay = c.get(Calendar.DAY_OF_MONTH);
        int cHour = c.get(Calendar.HOUR_OF_DAY);
        int cMinute = c.get(Calendar.MINUTE);
        String timeStr = "" + cYear + (cMonth > 9 ? cMonth : "0" + cMonth) + (cDay > 9 ? cDay : "0" + cDay) + (cHour > 9 ? cHour : "0" + cHour) + (cMinute > 9 ? cMinute : "0" + cMinute);
        return Long.parseLong(timeStr);
    }

    public static long getBookingMeetingDefaultStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        //Get point time.
        hour = hour + (minute + 30) / 60;
        minute = minute >= 30 ? 0 : 30;
        calendar.set(year, month, day, hour, minute);
        return calendar.getTimeInMillis();
    }

    public static long getBookMeetingDefaultRepeatEndTime() {
        return getBookingMeetingDefaultStartTime() + ONE_YEAR;
    }

    public static long calculateDistanceOfCurrentToLast(long lastTime) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTimeInMillis(System.currentTimeMillis());

        Calendar toCalendar = Calendar.getInstance();
        fromCalendar.setTimeInMillis(lastTime);

        long dayDistance = (toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / ONE_DAY;
        dayDistance = Math.abs(dayDistance);

        return dayDistance;
    }

    public static long calculateLimitDay(long lastTime) {
        if (calculateDistanceOfCurrentToLast(lastTime) > YEAR) {
            return System.currentTimeMillis() + ONE_DAY;
        }
        return lastTime;
    }

    public static String getMeetingHistoryTimeString(long time, int timeZoneOffset, boolean showYear) {
        if (is24HourFormat()) {
            return getTimeString(time, showYear ? TIME_FORMAT_YMD_HM_24 : TIME_FORMAT_MD_HM_24, getTimeZoneByOffset(timeZoneOffset));
        } else {
            return getTimeString(time, showYear ? TIME_FORMAT_YMD_HM_12 : TIME_FORMAT_MD_HM_12, getTimeZoneByOffset(timeZoneOffset));
        }
    }

    public static String getBookingMeetingTimeString(long time, int timeZoneOffset) {
        return getBookingMeetingTimeString(time, getTimeZoneByOffset(timeZoneOffset));
    }

    public static String getBookingMeetingRepeatEndDateString(long time, int timeZoneOffset) {
        return getTimeString(time, TIME_FORMAT_YMD, getTimeZoneByOffset(timeZoneOffset));
    }

    public static String getMeetingRepeatEndDateString(long time, int timeZoneOffset) {
        return getTimeString(time, TIME_FORMAT_YYYYMMDD, getTimeZoneByOffset(timeZoneOffset));
    }

    public static TimeZone getTimeZoneByOffset(int timeZoneOffset) {
        return TimeZone.getTimeZone("GMT" + (timeZoneOffset > 0 ? "+" : "") + timeZoneOffset);
    }

    public static String getBookingMeetingTimeString(long time, TimeZone timeZone) {
        if (is24HourFormat()) {
            return getTimeString(time, TIME_FORMAT_YMD_HM_24, timeZone);
        } else {
            return getTimeString(time, TIME_FORMAT_YMD_HM_12, timeZone);
        }
    }

    /**
     * Parse system time from string in the format "yyyyMMdd-HH:mm:ss:SSS",
     *
     * @param timeStr Time string in the format "yyyyMMdd-HH:mm:ss:SSS"
     * @param format  default yyyyMMdd-HH:mm:ss:SSS
     * @return return 0 if something wrong
     */
    public static long parseReadableTimeStamp(@NonNull String timeStr, String format) {
        try {
            Date date;
            if (!TextUtils.isEmpty(format)) {
                date = new SimpleDateFormat(format, Locale.US).parse(timeStr);
            } else {
                date = new SimpleDateFormat().parse(timeStr);
            }
            if (date != null) {
                return date.getTime();
            }
        } catch (ParseException e) {
        }
        return 0;
    }

    public static long parseReadableTimeStamp(@NonNull String timeStr) {
        return parseReadableTimeStamp(timeStr, TIME_FORMAT_YYYYMMDD_HMS_MILLISECOND);
    }

    public static long parseDateTimeStamp(@NonNull String date) {
        try {
            return new Date(date).getTime();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Generate file name from system time in the format "yyyyMMdd-HH:mm:ss:SSS",
     *
     * @param timeStamp System time in milliseconds
     */
    @NonNull
    public static String getReadableTimeStamp(long timeStamp) {
        return new SimpleDateFormat(TIME_FORMAT_YYYYMMDD_HMS_MILLISECOND, Locale.US).format(new Date(timeStamp));
    }

    public static String getReadableTimeStamp(long timeStamp, String format) {
        return new SimpleDateFormat(format, Locale.US).format(new Date(timeStamp));
    }

    public static String formatDate(String date, String originalFormat, String targetFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(originalFormat);
        Date d2 = null;
        String format = null;
        try {
            d2 = simpleDateFormat.parse(date);
            SimpleDateFormat sdf = new SimpleDateFormat(targetFormat, Locale.ENGLISH);
            format = sdf.format(d2);
            if (format == null) {
                return date;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
        return format;
    }

    /**
     * Format the time usage to string like "1d17h37m3s728ms"
     */
    @NonNull
    public static String getReadableTimeUsage(long timeUsageMs) {
        long millisecondsLeft = timeUsageMs % 1000;
        if (timeUsageMs == millisecondsLeft) {
            return millisecondsLeft + "ms";
        }

        long seconds = timeUsageMs / 1000;
        long secondsLeft = seconds % 60;
        if (secondsLeft == seconds) {
            return secondsLeft + "s" + millisecondsLeft + "ms";
        }

        long minutes = seconds / 60;
        long minutesLeft = minutes % 60;
        if (minutesLeft == minutes) {
            return minutesLeft + "m" + secondsLeft + "s" + millisecondsLeft + "ms";
        }

        long hours = minutes / 60;
        long hoursLeft = hours % 24;
        if (hoursLeft == hours) {
            return hoursLeft + "h" + minutesLeft + "m" + secondsLeft + "s" + millisecondsLeft + "ms";
        }

        long days = hours / 24;
        return days + "d" + hoursLeft + "h" + minutesLeft + "m" + secondsLeft + "s" + millisecondsLeft + "ms";
    }

    @NonNull
    public static long timeToLong(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT_YMD_HMS);
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return System.currentTimeMillis();
        }
        return date.getTime();
    }

    // 中国为+08:00
    public static String formatSeconds(long time) {
        String offset = String.format(Locale.US, "%02d:%02d:%02d", Math.abs(time / 3600000), Math.abs((time / 60000) % 60), Math.abs((time / 1000) % 60));
        return offset;
    }


    public static Date getZeroTime(Date date) {
        int year = date.getYear();
        int month = date.getMonth();
        int day = date.getDate();
        return new Date(year, month, day);
    }
}
