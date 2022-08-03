package base.ui;

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

import com.doctor.mylibrary.R;
import com.zhaoyuntao.androidutils.tools.TextMeasure;

/**
 * created by zhaoyuntao
 * on 27/04/2022
 * description:
 */
public class AutoSizeTextView extends AppCompatTextView {
    private boolean singleLine;
    private float radius;

    public AutoSizeTextView(@NonNull Context context) {
        super(context);
        set(null);
    }

    public AutoSizeTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        set(attrs);
    }

    public AutoSizeTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        set(attrs);
    }

    private void set(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.AutoSizeTextView);
            radius = typedArray.getDimensionPixelSize(R.styleable.AutoSizeTextView_autoRadius, 0);
            singleLine = typedArray.getBoolean(R.styleable.AutoSizeTextView_singleLine, true);

            typedArray.recycle();
        } else {
            singleLine = true;
        }
        if (singleLine) {
            setSingleLine();
        }
        if (radius > 0) {
            setCornerRadius(radius);
        }
    }

    public void setCornerRadius(float cornerRadius) {
        this.radius = cornerRadius;
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
        int width = getMeasuredWidth() - getPaddingStart() - getPaddingEnd();
        if (singleLine && width > 0) {
            CharSequence text = getText();
            if (!TextUtils.isEmpty(text)) {
                float textWidth = TextMeasure.measure(text.toString(), getTextSize())[0];
                if (textWidth > width) {
                    float textSize = getTextSize() / (textWidth / width);
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                }
            }
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        int width = getMeasuredWidth() - getPaddingStart() - getPaddingEnd();
        if (singleLine && width > 0) {
            if (!TextUtils.isEmpty(text)) {
                float textWidth = TextMeasure.measure(text.toString(), getTextSize())[0];
                if (textWidth > width) {
                    float textSize = getTextSize() / (textWidth / width);
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                }
            }
        }
    }
}
