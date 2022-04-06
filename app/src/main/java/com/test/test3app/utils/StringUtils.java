
package com.test.test3app.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.test.test3app.Hanzi2Pinyin;
import com.test.test3app.threadpool.ResourceUtils;

import java.text.BreakIterator;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

@SuppressLint("DefaultLocale")
public class StringUtils {
    public static boolean isEmpty(String string) {
        return string == null || "".equals(string.trim());
    }

    public static boolean isDigitsOnly(@NonNull String str, int startIndex, int endIndex) {
        if (startIndex >= endIndex || startIndex >= str.length() || endIndex > str.length()) {
            return false; // return 'false' for empty string
        }
        for (int i = startIndex; i < endIndex; i++) {
            char ch = str.charAt(i);
            if (ch < '0' || ch > '9') {
                return false;
            }
        }
        return true;
    }

    public static boolean isDigitsOnly(@NonNull String str) {
        return isDigitsOnly(str, 0, str.length());
    }

    /***
     * 判断是否纯数字
     * @param str
     * @return
     */
    public static boolean isPhone(String str) {

        //使用正则表达式
        String regex = "[0-9]+";
        return str.matches(regex) && str.length() >= 7 && str.length() <= 20;
    }

    public static String transMapToString(Map<String, Long> map) {
        StringBuilder sb = new StringBuilder();
        for (String key : map.keySet()) {
            Long value = map.get(key);
            String vs = value == null ? "0" : value.toString();
            sb.append(key).append(",").append(vs).append("$");
        }
        if (sb.length() >= 1) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public static Map<String, Long> transStringToMap(String mapString) {
        Map<String, Long> map = new HashMap<String, Long>();

        if (TextUtils.isEmpty(mapString)) {
            return map;
        }
        StringTokenizer items;
        StringTokenizer entries = new StringTokenizer(mapString, "$");

        while (entries.hasMoreTokens()) {
            items = new StringTokenizer(entries.nextToken(), ",");
            try {
                map.put(items.nextToken(), Long.valueOf(items.nextToken()));
            } catch (NumberFormatException ignore) {
            }
        }
        return map;
    }

    public static boolean isEmojiCharacter(int codePoint) {

        return (0x0080 <= codePoint && codePoint <= 0x00A1) ||
                (0x00A9 <= codePoint && codePoint <= 0x00AE) ||
                (0x0300 <= codePoint && codePoint <= 0x03FF) ||
                (0x0600 <= codePoint && codePoint <= 0x06FF) ||
                (0x0C00 <= codePoint && codePoint <= 0x0C7F) ||
                (0x1DC0 <= codePoint && codePoint <= 0x1DFF) ||
                (0x1E00 <= codePoint && codePoint <= 0x1EFF) ||
                (0x2000 <= codePoint && codePoint <= 0x200F) ||
                (0x203c <= codePoint && codePoint <= 0x2049) ||
                //for 'TM'.
                (0x2122 == codePoint) ||
                (0x2190 <= codePoint && codePoint <= 0x23FF) ||
                (0x2460 <= codePoint && codePoint <= 0x25FF) ||
                (0x2600 <= codePoint && codePoint <= 0x27EF) ||
                (0x2900 <= codePoint && codePoint <= 0x29FF) ||
                (0x2B00 <= codePoint && codePoint <= 0x2BFF) ||
                (0x2C60 <= codePoint && codePoint <= 0x2C7F) ||
                (0x2E00 <= codePoint && codePoint <= 0x2E7F) ||
                (0xA490 <= codePoint && codePoint <= 0xA4CF) ||
                (0xE000 <= codePoint && codePoint <= 0xF8FF) ||
                (0xFE00 <= codePoint && codePoint <= 0xFE0F) ||
                (0xFE30 <= codePoint && codePoint <= 0xFE4F) ||
                (0x1F000 <= codePoint && codePoint <= 0x1F02F) ||
                (0x1F0A0 <= codePoint && codePoint <= 0x1F0FF) ||
                (0x1F100 <= codePoint && codePoint <= 0x1F64F) ||
                (0x1F680 <= codePoint && codePoint <= 0x1F6FF) ||
                (0x1F910 <= codePoint && codePoint <= 0x1F96B) ||
                (0x1F980 <= codePoint && codePoint <= 0x1F9E0);
    }

    /**
     * version:2020-02-26
     *
     * @param source
     * @return
     */
    public static int getCharCount(String source, CharIterator charIterator) {
        BreakIterator breakIterator = BreakIterator.getCharacterInstance();
        breakIterator.setText(source);
        int start = breakIterator.first();
        int count = 0;
        for (int end = breakIterator.next(); end != BreakIterator.DONE; start = end, end = breakIterator.next()) {
            count++;
            if (charIterator != null) {
                String oneChar = source.substring(start, end);
                if (TextUtils.isEmpty(oneChar)) {
                    continue;
                }
                if (!charIterator.getCharString(source.substring(start, end), Character.codePointAt(source, start))) {
                    break;
                }
            }
        }
        return count;
    }

    /**
     * version:2020-10-12
     *
     * @param source
     * @return
     */
    public static int getEmojiCount(String source) {
        BreakIterator breakIterator = BreakIterator.getCharacterInstance();
        breakIterator.setText(source);
        int start = breakIterator.first();
        int count = 0;
        for (int end = breakIterator.next(); end != BreakIterator.DONE; start = end, end = breakIterator.next()) {
            if (isEmojiCharacter(Character.codePointAt(source, start))) {
                count++;
            }
        }
        return count;
    }

    public static int getCharCount(String source) {
        return getCharCount(source, null);
    }

    public interface CharIterator {
        boolean getCharString(String item, int codePoint);
    }

    public static String hideMiddleChar(String content) {
        if (TextUtils.isEmpty(content) || content.length() <= 1) {
            return content;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(content.charAt(0));
        for (int i = 0; i < content.length() - 2; i++) {
            stringBuilder.append('*');
        }
        stringBuilder.append(content.charAt(content.length() - 1));
        return stringBuilder.toString();
    }

    /**
     * Return the similarity of two string.
     *
     * @return The lower the number, the higher the similarity.0 means totally different.
     */
    public static int matchStringWithPinyin(String srcString, String descString) {
        int similarity = matchString(srcString, descString);
        if (similarity == 0) {
            similarity = matchString(srcString, Hanzi2Pinyin.getInstance(ResourceUtils.getApplicationContext()).getAsString(descString));
            if (similarity > 0) {
                similarity += 10000;
            }
        }
        return similarity;
    }

    /**
     * Return the similarity of two string.
     * For ex:input 'ren', this is the orders:
     * 1.renxxxx
     * 2.xxxxren
     * 3.rxxxexxxnxxx
     *
     * @param descString
     * @param srcString
     * @return The lower the number, the higher the similarity.0 means totally different.
     */
    public static int matchString(String srcString, String descString) {
        if (srcString == null) {
            if (descString == null) {
                return 1;
            } else {
                return 0;
            }
        } else {
            if (descString == null) {
                return 0;
            }
        }
        srcString = srcString.toLowerCase();
        descString = descString.toLowerCase();
        int similarity = 0;
        //Check name and input char one by one.
        char[] arrDesc = descString.toCharArray();
        char[] arrSrc = srcString.toCharArray();
        int differentPoint = -1;
        int startPosition = 0;

        for (int srcCharIndex = 0, descCharIndex = 0; srcCharIndex < arrSrc.length; descCharIndex++) {
            //If input length over than name.
            if (descCharIndex >= arrDesc.length) {
                similarity = 0;
                break;
            }
            char descChar = arrDesc[descCharIndex];
            char srcChar = arrSrc[srcCharIndex];
            if (descChar == srcChar) {
                similarity += (descCharIndex + 1);
                srcCharIndex++;
                if (differentPoint < 0) {
                    differentPoint++;
                }
            } else {
                if (differentPoint >= 0) {
                    differentPoint++;
                }
                if (similarity == 0) {
                    startPosition++;
                }
            }
        }
        if (similarity > 0) {
            if (startPosition > 0) {
                similarity *= (startPosition * 10);
            }
            if (differentPoint > 0) {
                similarity *= (differentPoint * 10000);
            }
        }
        return similarity;
    }
}
