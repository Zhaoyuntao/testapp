package im.thebot.chat.ui.adapter;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

/**
 * created by zhaoyuntao
 * on 06/12/2020
 * description:
 */
public class ColorStringUtils {
    public static SpannableString get(String content, int color, int textSize) {
        if (TextUtils.isEmpty(content)) {
            content = "null";
        }
        SpannableString spannableString = new SpannableString(content);
        if (textSize > 0) {
            spannableString.setSpan(new AbsoluteSizeSpan(textSize, true), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        spannableString.setSpan(new ForegroundColorSpan(color), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
