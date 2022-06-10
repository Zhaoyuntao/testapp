package im.turbo.basetools.span;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.module_chat.R;

import im.turbo.utils.ResourceUtils;

/**
 * created by zhaoyuntao
 * on 2020/7/14
 * description:
 */
public class TClickableSpan extends ClickableSpan {

    private final String childContent;
    private final View.OnClickListener listener;
    private final View.OnLongClickListener longClickListener;
    private final Integer color;

    public TClickableSpan(String childContent, Integer color, View.OnClickListener listener, View.OnLongClickListener longClickListener) {
        this.childContent = childContent;
        this.color = color;
        this.listener = listener;
        this.longClickListener = longClickListener;
    }

    @Override
    public void updateDrawState(TextPaint textPaint) {
        if (color != null) {
            textPaint.setColor(color);
        } else {
            textPaint.setColor(ResourceUtils.getColor(R.color.indigo05));
        }
        textPaint.setUnderlineText(false);
        textPaint.clearShadowLayer();
    }

    @Override
    public void onClick(@NonNull View view) {
        if (listener != null) {
            listener.onClick(view);
        }
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
}