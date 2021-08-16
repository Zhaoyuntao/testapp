package com.zhaoyuntao.myjava;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * created by zhaoyuntao
 * on 2020/8/25
 * description:
 * https://meetings.matrx.us/#/j/989048337/2622d0ab46deddb327df7291391afe503300d8d13326ba49
 * old:
 * https://intl.meeting.huaweicloud.com/#/j/289229034/6401c55f8d3c90b5b727fd58c9e19e571f4bae832fc60e0c
 */
public class PatternUtils {
    /**
     * Meeting url.
     */
    public static final String PATTERN_URL_MEETING = "https://[a-z]+\\.matrx\\..*/#/[a-zA-Z0-9]+/([0-9a-zA-Z]{8,12})/([0-9a-zA-Z]*)";
    /**
     * Email.
     */
    public static final String PATTERN_EMAIL = "[a-z1-9A-Z][a-z0-9A-Z]+@[a-zA-Z0-9]+\\.[a-zA-Z]+";
    /**
     * Phone number.
     */
    public static final String PATTERN_PHONE_NUMBER = "?!\\d\\d{1,4}[- ]?\\d{5,15}";

    // all domain names

    private static final String[] ext = {
            "top", "com.cn", "com", "net", "org", "edu", "gov", "int", "mil", "cn", "tel", "biz", "cc", "tv", "info",
            "name", "hk", "mobi", "asia", "cd", "travel", "pro", "museum", "coop", "aero", "ad", "ae", "af",
            "ag", "ai", "al", "am", "an", "ao", "aq", "ar", "as", "at", "au", "aw", "az", "ba", "bb", "bd",
            "be", "bf", "bg", "bh", "bi", "bj", "bm", "bn", "bo", "br", "bs", "bt", "bv", "bw", "by", "bz",
            "ca", "cc", "cf", "cg", "ch", "ci", "ck", "cl", "cm", "cn", "co", "cq", "cr", "cu", "cv", "cx",
            "cy", "cz", "de", "dj", "dk", "dm", "do", "dz", "ec", "ee", "eg", "eh", "es", "et", "ev", "fi",
            "fj", "fk", "fm", "fo", "fr", "ga", "gb", "gd", "ge", "gf", "gh", "gi", "gl", "gm", "gn", "gp",
            "gr", "gt", "gu", "gw", "gy", "hk", "hm", "hn", "hr", "ht", "hu", "id", "ie", "il", "in", "io",
            "iq", "ir", "is", "it", "jm", "jo", "jp", "ke", "kg", "kh", "ki", "km", "kn", "kp", "kr", "kw",
            "ky", "kz", "la", "lb", "lc", "li", "lk", "lr", "ls", "lt", "lu", "lv", "ly", "ma", "mc", "md",
            "mg", "mh", "ml", "mm", "mn", "mo", "mp", "mq", "mr", "ms", "mt", "mv", "mw", "mx", "my", "mz",
            "na", "nc", "ne", "nf", "ng", "ni", "nl", "no", "np", "nr", "nt", "nu", "nz", "om", "qa", "pa",
            "pe", "pf", "pg", "ph", "pk", "pl", "pm", "pn", "pr", "pt", "pw", "py", "re", "ro", "ru", "rw",
            "sa", "sb", "sc", "sd", "se", "sg", "sh", "si", "sj", "sk", "sl", "sm", "sn", "so", "sr", "st",
            "su", "sy", "sz", "tc", "td", "tf", "tg", "th", "tj", "tk", "tm", "tn", "to", "tp", "tr", "tt",
            "tv", "tw", "tz", "ua", "ug", "uk", "us", "uy", "va", "vc", "ve", "vg", "vn", "vu", "wf", "ws",
            "ye", "yu", "za", "zm", "zr", "zw"
    };
    public static String WEB_URL;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < ext.length; i++) {
            sb.append(ext[i]);
            sb.append("|");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        // final pattern str
        WEB_URL = "((https?|s?ftp|irc[6s]?|git|afp|telnet|smb)://)?((\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})|((www\\.|[a-zA-Z\\.\\-]+\\.)?[a-zA-Z0-9\\-]+\\." + sb.toString() + "(:[0-9]{1,5})?))((/[a-zA-Z0-9\\./,;\\?'\\+&%\\$#=~_\\-]*)|([^\\u4e00-\\u9fa5\\s0-9a-zA-Z\\./,;\\?'\\+&%\\$#=~_\\-]*))";
        // Log.v(TAG, "pattern = " + pattern);
    }

    /**
     * Parent group at 0,child group after parent group.
     *
     * @param text
     * @return
     */
    public static List<BluePatternGroupBean> match(String patternString, String text) {
        if (patternString == null || text == null || text.length() == 0 || text.length() > 1000) {
            return null;
        }
        List<BluePatternGroupBean> patternBeans = new ArrayList<>();
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(text);
        int index = 0;
        while (matcher.find()) {
            int groupCount = matcher.groupCount() + 1;
            BluePatternGroupBean bluePatternGroupBean = new BluePatternGroupBean(groupCount);
            bluePatternGroupBean.setPatternString(patternString);
            //Parent group.
            bluePatternGroupBean.setContent(matcher.group(0));
            bluePatternGroupBean.setEnd(matcher.end());
            bluePatternGroupBean.setIndex(index++);
            bluePatternGroupBean.setStart(matcher.start());
            //Child group.
            for (int i = 1; i < groupCount; i++) {
                String group = matcher.group(i);
                int start = matcher.start(i);
                int end = matcher.end(i);
                BluePatternGroupItemBean childPatternBean = new BluePatternGroupItemBean();
                childPatternBean.setContent(group);
                childPatternBean.setEnd(end);
                childPatternBean.setStart(start);
                childPatternBean.setIndex(i);
                bluePatternGroupBean.addItem(childPatternBean);
            }
            patternBeans.add(bluePatternGroupBean);
        }
        return patternBeans;
    }

}
