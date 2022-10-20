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
    public SlideLayoutContent(Context context) {
        super(context);
    }

    @Override
    public void setGravity(int gravity) {
        setGravity(gravity, 0);
    }

    public void setGravity(int gravity, int marginSpace) {
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
