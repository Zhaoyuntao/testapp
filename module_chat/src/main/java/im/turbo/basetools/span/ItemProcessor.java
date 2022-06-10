package im.turbo.basetools.span;

import android.text.SpannableStringBuilder;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

/**
 * created by zhaoyuntao
 * on 18/05/2022
 * description:
 */
public class ItemProcessor {
    private final String patternString;

    public ItemProcessor(String patternString) {
        this.patternString = patternString;
    }

    public void onClick(@NonNull View v, String contentOrigin, CharSequence contentClick) {
    }

    public boolean onLongClick(@NonNull View v, String contentOrigin, CharSequence contentClick) {
        return false;
    }

    public boolean canClick(@NonNull View v) {
        return false;
    }

    public CharSequence onReplace(SpannableStringBuilder stringBuilder, int start, int end, String childContent) {
        return null;
    }

    public String getPatternString() {
        return patternString;
    }

    @ColorInt
    public Integer getColor() {
        return null;
    }

    public Object generateSpan(String contentOrigin, @NonNull String contentClick) {
        return null;
    }
}
