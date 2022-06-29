package im.turbo.baseui.expandview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import im.turbo.baseui.utils.UiUtils;

/**
 * created by zhaoyuntao
 * on 19/10/2021
 * description:
 */
public class AnimateTextView extends  View {
    private String text="AAAAAAAAA";
    private Paint paint;
    private float textSize;
    private ValueAnimator animator;
    LinearGradient gradient;
    Matrix matrix = new Matrix();
    private float translate;

    public AnimateTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint=new Paint();
        textSize= UiUtils.spToPx(30);
    }

    @Override
    public void setVisibility(int visibility) {
        boolean needAnimation = visibility != getVisibility();
        super.setVisibility(visibility);
        if (needAnimation) {
            if (visibility == View.VISIBLE) {
                startWaveAnimation();
            } else {
                cancelAnimation();
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        gradient = new LinearGradient(0f,
                0f,
                getMeasuredWidth(),
                0f,
                new int[]{Color.WHITE, Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE},
                null,
                Shader.TileMode.CLAMP);
        paint.setShader(gradient);
        startWaveAnimation();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setShader(gradient);
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
        canvas.drawText(text,getWidth()/2f,getHeight()/2f,paint);
    }

    public void startWaveAnimation() {
        if ((animator != null && animator.isRunning()) || matrix == null || gradient == null) {
            return;
        }
        animator = ValueAnimator.ofFloat(1, 0);
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float percent = (float) animation.getAnimatedValue();
                translate = getMeasuredWidth() * percent;
                matrix.setTranslate(translate, 0f);
                gradient.setLocalMatrix(matrix);
                postInvalidate();
            }
        });
        animator.start();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startWaveAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelAnimation();
    }

    public void cancelAnimation() {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
    }
}
