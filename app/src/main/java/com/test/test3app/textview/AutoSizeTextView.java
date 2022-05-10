package com.test.test3app.textview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * created by zhaoyuntao
 * on 27/04/2022
 * description:
 */
public class AutoSizeTextView extends AppCompatTextView {
    public AutoSizeTextView(@NonNull Context context) {
        super(context);
    }

    public AutoSizeTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoSizeTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
