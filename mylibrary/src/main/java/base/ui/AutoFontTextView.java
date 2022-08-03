package base.ui;

import static android.graphics.Typeface.BOLD_ITALIC;
import static android.graphics.Typeface.ITALIC;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.zhaoyuntao.androidutils.tools.S;

/**
 * created by zhaoyuntao
 * on 27/04/2022
 * description:
 */
public class AutoFontTextView extends AppCompatTextView {

    public AutoFontTextView(@NonNull Context context) {
        super(context);
        set(null);
    }

    public AutoFontTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        set(attrs);
    }

    public AutoFontTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        set(attrs);
    }

    private void set(AttributeSet attrs) {
//        if (attrs != null) {
//            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.AutoFontTextView);
//            String font = typedArray.getString(R.styleable.AutoFontTextView_AutoFontTextView_fontName);
//            S.s("font:"+font);
//            if (!TextUtils.isEmpty(font)) {
//                Typeface face = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + font + ".ttf");
//                setTypeface(face);
//            }
//            typedArray.recycle();
//        }

        SpannableStringBuilder stringBuilder = new SpannableStringBuilder("jabcd");
        stringBuilder.setSpan(new StyleSpan(BOLD_ITALIC), 0, stringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(stringBuilder);
    }
}
