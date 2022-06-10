package im.thebot.common.ui.chat;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.module_chat.R;

/**
 * created by zhaoyuntao
 * on 27/04/2022
 * description:
 */
public class AutoSizeTextView extends AppCompatTextView {
    public AutoSizeTextView(@NonNull Context context) {
        super(context);
        setSingleLine();
        set(null);
    }

    public AutoSizeTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setSingleLine();
        set(attrs);
    }

    public AutoSizeTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setSingleLine();
        set(attrs);
    }

    private void set(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.AutoSizeTextView);
            float radius = typedArray.getDimensionPixelSize(R.styleable.AutoSizeTextView_autoRadius, 0);
            if (radius > 0) {
                setCornerRadius(radius);
            }
            typedArray.recycle();
        }
    }

    public void setCornerRadius(float cornerRadius) {
        setClipToOutline(true);
        setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), cornerRadius);
            }
        });
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
        int width = r - l;
        if (width > 0) {
            CharSequence text = getText();
            if (!TextUtils.isEmpty(text)) {
                float textSize = 20f - (text.length() - 8) * 1.5f;
                setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            }
        }
    }
}
