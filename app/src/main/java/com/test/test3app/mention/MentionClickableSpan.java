package com.test.test3app.mention;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.NonNull;

import com.test.test3app.R;
import com.test.test3app.threadpool.ResourceUtils;

/**
 * created by zhaoyuntao
 * on 2020/7/14
 * description:
 */
public class MentionClickableSpan extends ClickableSpan {

    private final String childContent;
    private final String uid;
    private final MentionSpannableString spannableString;
    private final View.OnClickListener listener;
    private final View.OnLongClickListener longClickListener;
    private final Integer color;
    private int start;
    private int end;

    public MentionClickableSpan(String childContent, Integer color, String uid, MentionSpannableString spannableString, View.OnClickListener listener, View.OnLongClickListener longClickListener) {
        this.childContent = childContent;
        this.color = color;
        this.uid = uid;
        this.spannableString = spannableString;
        this.listener = listener;
        this.longClickListener = longClickListener;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        if (color != null) {
            ds.setColor(color);
        } else {
            ds.setColor(ResourceUtils.getColor(R.color.color_primary));
        }
        ds.setUnderlineText(false);
        ds.clearShadowLayer();
    }

    @Override
    public void onClick(@NonNull View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public int length() {
        return childContent.length();
    }

    public boolean onLongClick(@NonNull View view) {
        if (longClickListener != null) {
            return longClickListener.onLongClick(view);
        }
        return false;
    }

    public boolean canClick(@NonNull View view) {
        return listener != null;
    }

    @NonNull
    @Override
    public String toString() {
        return childContent;
    }

    public String getUid() {
        return uid;
    }

    public MentionSpannableString getSpannableString() {
        return spannableString;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}