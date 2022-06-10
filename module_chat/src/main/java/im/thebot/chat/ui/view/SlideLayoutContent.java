package im.thebot.chat.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.module_chat.R;

/**
 * created by zhaoyuntao
 * on 10/06/2022
 * description:
 */
public class SlideLayoutContent extends LinearLayout {
    private int marginSpace;

    public SlideLayoutContent(Context context) {
        super(context);
        init(null);
    }

    public SlideLayoutContent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SlideLayoutContent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SlideLayoutContent);
            marginSpace = typedArray.getDimensionPixelSize(R.styleable.SlideLayoutContent_SlideLayoutContent_marginSpace, 0);
            typedArray.recycle();
        } else {
            marginSpace = 0;
        }
    }

    @Override
    public void setGravity(int gravity) {
        if (gravity == Gravity.START) {
            setPaddingRelative(0, 0, marginSpace, 0);
        } else if (gravity == Gravity.END) {
            setPaddingRelative(marginSpace, 0, 0, 0);
        } else {
            setPaddingRelative(0, 0, 0, 0);
        }
        super.setGravity(gravity);
    }
}
