package im.turbo.baseui.roundcornerlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.module_chat.R;

import im.turbo.baseui.utils.UiUtils;


/**
 * created by zhaoyuntao
 * on 2021/2/24
 * description:
 */
public class RoundCornerLinearLayout extends LinearLayout {
    protected int cornerRadius;

    public RoundCornerLinearLayout(Context context) {
        super(context);
        init(null);
    }

    public RoundCornerLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RoundCornerLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RoundCornerLinearLayout);
            setCornerRadius(typedArray.getDimensionPixelSize(R.styleable.RoundCornerLinearLayout_RoundCornerLinearLayout_radius, UiUtils.dipToPx(5)));
            getFromAttributeSet(typedArray);
            typedArray.recycle();
        } else {
            getFromAttributeSet(null);
        }
    }

    protected void getFromAttributeSet(@Nullable TypedArray attrs) {

    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        setClipToOutline(true);
        setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), cornerRadius);
            }
        });
    }
}
