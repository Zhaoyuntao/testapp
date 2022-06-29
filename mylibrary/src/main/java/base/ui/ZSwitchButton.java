package base.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.core.content.ContextCompat;

import com.doctor.mylibrary.R;

import im.turbo.baseui.utils.UiUtils;

/**
 * created by zhaoyuntao
 * on 2020/7/16
 * description:
 */
public class ZSwitchButton extends View {

    private final Paint paint;
    private final int colorSlider;
    private final int colorProgressBackground;
    private final int colorProgressBackgroundChecked;
    private final float paddingSlider;
    private final int shadow;
    private final int shadowColor;
    private final RectF rectBack;

    private OnCheckedChangeListener listener;
    private boolean checked;
    private float percent;

    public ZSwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        shadow = UiUtils.dipToPx(1);
        paddingSlider = UiUtils.dipToPx(3);
        shadowColor = Color.argb(255, 0, 0, 0);
        colorSlider = ContextCompat.getColor(getContext(), R.color.color_white_white);
        colorProgressBackgroundChecked = ContextCompat.getColor(getContext(), R.color.color_main2);
        colorProgressBackground = ContextCompat.getColor(getContext(), R.color.color_white_grey);
        rectBack = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int wView = getWidth();
        int hView = getHeight();
        if (wView == 0 || hView == 0) {
            return;
        }
        int left = getPaddingStart();
        int top = getPaddingTop();
        int right = wView - getPaddingEnd();
        int bottom = hView - getPaddingBottom();
        float radiusRect = (bottom - top) / 2f;
        rectBack.set(left, top, right, bottom);
        //back.
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.clearShadowLayer();
        paint.setColor(colorProgressBackground);
        canvas.drawRoundRect(rectBack, radiusRect, radiusRect, paint);

        paint.setColor(colorProgressBackgroundChecked);
        paint.setAlpha((int) ((checked ? percent : (1 - percent)) * 255));
        canvas.drawRoundRect(rectBack, radiusRect, radiusRect, paint);
        paint.setAlpha(255);

        //Draw slider.
        float y = top + (bottom - top) / 2f;
        float x = checked ? (left + radiusRect + percent * (right - left - radiusRect * 2)) : (right - radiusRect - percent * (right - left - radiusRect * 2));

        paint.setColor(colorSlider);
        paint.setStyle(Paint.Style.FILL);
        paint.setShadowLayer(shadow, shadow / 2f, shadow / 2f, shadowColor);
        canvas.drawCircle(x, y, radiusRect - paddingSlider, paint);
    }

    public void setChecked(boolean checked) {
        _setChecked(checked, false);
    }

    private void _setChecked(boolean checked, boolean performCheckedChangeEvent) {
        this.checked = checked;
        if (performCheckedChangeEvent && listener != null) {
            listener.onCheckedChanged(this, this.checked);
        }
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(200);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                percent = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                postInvalidate();
            }
        });
        animator.start();
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.listener = listener;
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                _setChecked(!checked, true);
            }
        });
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(ZSwitchButton buttonView, boolean isChecked);
    }
}
