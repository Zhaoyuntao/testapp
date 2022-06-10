package im.thebot.common.ui.chat.mention;

import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;

import androidx.annotation.NonNull;

/**
 * created by zhaoyuntao
 * on 2020/7/14
 * description:
 */
public class MentionSpan extends ForegroundColorSpan {

    private final String mentionContent;
    private final String uid;
    private int start;
    private int end;

    public MentionSpan(int color, String mentionContent, String uid) {
        super(color);
        this.mentionContent = mentionContent;
        this.uid = uid;
    }

    @Override
    public void updateDrawState(TextPaint textPaint) {
        super.updateDrawState(textPaint);
        textPaint.setUnderlineText(false);
        textPaint.clearShadowLayer();
    }

    public int length() {
        return mentionContent.length();
    }

    @NonNull
    @Override
    public String toString() {
        return mentionContent;
    }

    public String getUid() {
        return uid;
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