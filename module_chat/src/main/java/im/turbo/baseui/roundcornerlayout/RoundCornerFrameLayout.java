package im.turbo.baseui.roundcornerlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;

import com.example.module_chat.R;

import im.turbo.baseui.utils.UiUtils;

/**
 * created by zhaoyuntao
 * on 2021/2/24
 * description:
 */
public class RoundCornerFrameLayout extends FrameLayout {

    public RoundCornerFrameLayout(Context context) {
        super(context);
        init(null);
    }

    public RoundCornerFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RoundCornerFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RoundCornerFrameLayout);
            int cornerRadius = typedArray.getDimensionPixelSize(R.styleable.RoundCornerFrameLayout_RoundCornerFrameLayout_radius, UiUtils.dipToPx(5));
            setCornerRadius(cornerRadius);
            typedArray.recycle();
        }
    }

    public void setCornerRadius(int cornerRadius) {
        setClipToOutline(true);
        setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), cornerRadius);
            }
        });
    }
}
