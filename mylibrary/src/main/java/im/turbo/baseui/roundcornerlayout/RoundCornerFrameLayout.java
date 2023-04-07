package im.turbo.baseui.roundcornerlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;

import com.doctor.mylibrary.R;

import im.turbo.baseui.utils.UiUtils;

/**
 * created by zhaoyuntao
 * on 2021/2/24
 * description:
 */
public class RoundCornerFrameLayout extends FrameLayout {

    private float cornerRadius;

    public RoundCornerFrameLayout(Context context) {
        this(context, null);
    }

    public RoundCornerFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundCornerFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundCornerFrameLayout);
            cornerRadius = typedArray.getDimension(R.styleable.RoundCornerFrameLayout_RoundCornerFrameLayout_radius, UiUtils.dipToPx(5));
            setCornerRadius(cornerRadius);
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

    public float getCornerRadius() {
        return cornerRadius;
    }
}
