package im.turbo.basetools.span;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;

import com.doctor.mylibrary.R;

import im.turbo.utils.ResourceUtils;

/**
 * created by zhaoyuntao
 * on 28/08/2020
 * description:
 */
public abstract class TCustomClickSpan extends ClickableSpan {
    private static final int DEFAULT_COLOR = R.color.primary06;
    private int colorId = DEFAULT_COLOR;
    @ColorInt
    private int spanColor;
    private boolean underlineText = true;
    private boolean antialias = false;
    private String content;
    private boolean isUserColorInt;

    public TCustomClickSpan() {
    }

    public TCustomClickSpan(boolean underlineText) {
        this.underlineText = underlineText;
    }

    public TCustomClickSpan(boolean underlineText, boolean antialias) {
        this.underlineText = underlineText;
        this.antialias = antialias;
    }

    public TCustomClickSpan(@ColorRes int colorId, boolean underlineText) {
        this.colorId = colorId;
        this.underlineText = underlineText;
    }

    public TCustomClickSpan(@ColorInt int colorInt, boolean underlineText, boolean antialias) {
        this.isUserColorInt = true;
        this.spanColor = colorInt;
        this.underlineText = underlineText;
        this.antialias = antialias;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        if (isUserColorInt) {
            ds.setColor(spanColor);
        } else {
            ds.setColor(ResourceUtils.getColor(colorId));
        }
        ds.setUnderlineText(underlineText);
        ds.setAntiAlias(antialias);
    }

    @Override
    public abstract void onClick(@NonNull View widget);

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}