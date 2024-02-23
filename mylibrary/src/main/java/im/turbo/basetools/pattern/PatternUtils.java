package im.turbo.basetools.pattern;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import im.turbo.basetools.util.ValueSafeTransfer;
import im.turbo.utils.log.S;

/**
 * created by zhaoyuntao
 * on 2020/8/25
 * description:
 * https://meetings.a.us/#/j/989048337/2622d0ab46deddb327df7291391afe503300d8d13326ba49
 * old:
 * https://intl.meeting.huaweicloud.com/#/j/289229034/6401c55f8d3c90b5b727fd58c9e19e571f4bae832fc60e0c
 */
public class PatternUtils {
    /**
     * Email.
     */
    public static final String PATTERN_EMAIL = BPattern.AUTOLINK_EMAIL_ADDRESS_REGEX;
    /**
     * Phone number.
     */
    public static final String PATTERN_PHONE_NUMBER = "((?<!(http:|\\d|\\+))(?:\\+?[0-9]{1,3}[ -]?)?([0-9][ -]?){5,12}(?!(\\d)))";
    public static final String PATTERN_MENTION = "(@\\u2068\\d+?\\u2068)";
    public static final String WEB_URL = LinkifyPort.WEB_URL_PATTERN;
    public static final String PATTERN_FILE_NUMBER = "^(.*)\\((\\d+)\\)$";
    private static Pattern PATTERN_WEB_URL;

    static {
        try {
            PATTERN_WEB_URL = Pattern.compile(WEB_URL);
        } catch (Throwable ignore) {
        }
    }


    /**
     * Parent group at 0,child group after parent group.
     */
    public static List<TPatternGroup> match(String patternString, CharSequence text) {
        return match(patternString, text, 1000);
    }

    public static List<TPatternGroup> match(String patternString, CharSequence text, int maxLength) {
        if (TextUtils.isEmpty(patternString) || TextUtils.isEmpty(text) || text.length() > Math.max(maxLength, 1000)) {
            return null;
        }
        List<TPatternGroup> patternBeans = new ArrayList<>();
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(text);
        int index = 0;
        while (matcher.find()) {
            int groupCount = matcher.groupCount() + 1;
            TPatternGroup TPatternGroup = new TPatternGroup(groupCount);
            TPatternGroup.setPatternString(patternString);
            //Parent group.
            TPatternGroup.setContent(matcher.group(0));
            TPatternGroup.setEnd(matcher.end());
            TPatternGroup.setIndex(index++);
            TPatternGroup.setStart(matcher.start());
            //Child group.
            for (int i = 0; i < groupCount; i++) {
                String group = matcher.group(i);
                int start = matcher.start(i);
                int end = matcher.end(i);
                if (start < 0) {
                    continue;
                }
                TPatternItem childPatternBean = new TPatternItem();
                childPatternBean.setContent(group);
                childPatternBean.setEnd(end);
                childPatternBean.setStart(start);
                childPatternBean.setIndex(i);
                TPatternGroup.addItem(childPatternBean);
            }
            patternBeans.add(TPatternGroup);
        }
        return patternBeans;
    }

    public static List<TPatternGroup> matcher(String patternString, String text) {
        if (matched(patternString, text)) {
            return match(patternString, text);
        } else {
            return null;
        }
    }

    public static boolean matched(String patternString, String text) {
        if (patternString == null || text == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(patternString);
        return pattern.matcher(text).matches();
    }

    public static void log(List<TPatternGroup> groups) {
        if (groups != null && groups.size() > 0) {
            for (int groupIndex = 0; groupIndex < groups.size(); groupIndex++) {
                TPatternGroup groupBean = groups.get(groupIndex);
                S.s("------[" + groupIndex + "] group[" + groupBean.getIndex() + "][" + groupBean.getContent() + "][child count:" + groups.size() + "]---------------------------");
                log(groupBean);
            }
        }
    }

    public static void log(TPatternGroup groupBean) {
        if (!groupBean.isEmpty()) {
            groupBean.iterate(new ValueSafeTransfer.ElementIterator<TPatternItem>() {
                @Override
                public TPatternItem element(int position, TPatternItem itemBean) {
                    if (itemBean != null) {
                        S.s("[" + position + "] item[" + itemBean.getIndex() + "]:[" + itemBean.getContent() + "]");
                    }
                    return null;
                }
            });
        }
    }
}
