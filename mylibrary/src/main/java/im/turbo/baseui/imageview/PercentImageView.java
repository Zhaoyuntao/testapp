package im.turbo.baseui.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.doctor.mylibrary.R;
import com.zhaoyuntao.androidutils.tools.S;

/**
 * created by zhaoyuntao
 * on 19/10/2021
 * description:
 */
public class PercentImageView extends AppCompatImageView {

    private float percent = 1;

    public PercentImageView(Context context) {
        super(context);
        init(null);
    }

    public PercentImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PercentImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PercentImageView);
            percent = typedArray.getFloat(R.styleable.PercentImageView_PercentImageView_srcPercent, 1f);
            typedArray.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }

        int drawableWidth = drawable.getIntrinsicWidth();
        int drawableHeight = drawable.getIntrinsicHeight();
        int viewWidth = getWidth();
        int viewHeight = getHeight();

        if (drawableWidth == 0 || drawableHeight == 0 || viewWidth == 0 || viewHeight == 0) {
            return;
        }
        drawable.setBounds(0, 0, viewWidth, viewHeight);
        S.s("1:" + percent);
        canvas.save();
        canvas.scale(percent, percent, viewWidth / 2f, viewHeight / 2f);

        drawable.draw(canvas);
        canvas.restore();
    }
}
