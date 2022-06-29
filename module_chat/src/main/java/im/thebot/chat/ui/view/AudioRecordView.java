package im.thebot.chat.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.core.content.ContextCompat;

import com.example.module_chat.R;

import im.turbo.basetools.time.TimeUtils;
import im.turbo.basetools.vibrate.VibratorUtil;
import im.turbo.baseui.utils.UiUtils;
import im.turbo.utils.ResourceUtils;
import im.turbo.utils.log.S;

/**
 * created by zhaoyuntao
 * on 17/06/2022
 * description:
 */
public class AudioRecordView extends View {
    //Common params.
    private float xDown, yDown;
    private boolean canRecord = true;
    private boolean slideHorizontal;
    private boolean slideVertical;
    private boolean slideCanceled;
    private final Paint paint;
    private final PaintFlagsDrawFilter paintFlagsDrawFilter;
    private final float fingerMoveDistanceXMax, fingerMoveDistanceYMax;
    private final float fingerSlideDistanceXMin, fingerSlideDistanceYMin;
    private float fingerMoveDistanceX, fingerMoveDistanceY;
    private AudioRecordViewListener listener;
    //Animators.
    private ValueAnimator translateAnimator;
    private ValueAnimator linearScaleAnimator;
    private ValueAnimator waveAnimator;
    private ValueAnimator lockedShrinkAnimator;
    private ValueAnimator lockedShrinkAlphaAnimator;
    private ValueAnimator timeAnimator;
    //Button circle.
    private final float buttonScalePercentMax = 2.2f;
    private final float buttonRadiusFinal;
    private final Drawable buttonDrawable;
    private final RectF buttonClickRect;
    private final int buttonBackgroundColor;
    private final float buttonDrawablePercent;
    private float buttonScalePercent;
    private boolean downInButton;
    //Button locked.
    private final Drawable buttonLockedDrawable;
    private final float buttonLockedScalePercentMax = 1.4f;
    private final float buttonLockedRadiusFinal;
    private float buttonLockedScalePercent;
    private float buttonLockedAlphaPercent;
    //Slide background.
    private final RectF slideRect;
    private final int slideBackgroundColor;
    private final float slideBackgroundHeightFinal;
    private final float slideBackgroundMarginStartFinal, slideBackgroundMarginEndFinal;
    private float slideBackgroundXAnimateTranslatePercent;
    private float slideBackgroundAlphaPercent;
    //Time duration.
    private long timeDurationMills;
    private final float timeDurationTextSizePx;
    private final int timeDurationTextColor;
    private float timeDurationAlphaPercent;
    //Audio icon.
    private final Drawable audioIconDrawable;
    private float audioIconAlphaPercent;
    private final float audioIconWidthFinal, audioIconHeightFinal, audioIconPaddingFinal;
    //Cancel string.
    private final String cancelStringFinal;
    private final Drawable cancelDrawable;
    private final float cancelDrawableWidthFinal, cancelDrawableHeightFinal;
    private final float cancelStringTextSizePx;
    private final int cancelStringTextColor;
    private final int cancelStringTextColorCenter;
    private float cancelStringAlphaPercent;
    private final Matrix cancelStringAnimateMatrix;
    private LinearGradient cancelStringAnimateGradient;
    private float cancelStringAnimateTranslationPercent;
    //Lock background.
    private final RectF lockRect;
    private final int lockBackgroundColor;
    private final float lockBackgroundWidthFinal, lockBackgroundHeightFinal;
    private final float lockBackgroundMarginBottomFinal;
    private float lockBackgroundYAnimateTranslatePercent;
    private final float lockBackgroundYWaveAnimateTranslateRangeFinal;
    private final float lockBackgroundYWaveAnimateMaxMoveDistance;
    private float lockBackgroundYWaveAnimateTranslatePercent;
    private float lockBackgroundAlphaPercent;
    private final Drawable lockHeadDrawable, lockBodyDrawable;
    private final float lockHeadDrawableWidthFinal, lockHeadDrawableHeightFinal;
    private final float lockBodyDrawableWidthFinal, lockBodyDrawableHeightFinal;
    private final float lockHeadDrawableMarginTop, lockBodyDrawableMarginTop;
    private final Drawable lockArrowDrawable;
    private final float lockArrowDrawableWidthFinal, lockArrowDrawableHeightFinal;
    private final float lockArrowDrawableMarginTop;
    private final float lockArrowDrawableYWaveTranslateRange;
    private float lockArrowDrawableYWaveTranslatePercent;

    public AudioRecordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG);
        //Button circle.
        buttonClickRect = new RectF();
        fingerMoveDistanceYMax = UiUtils.dipToPx(100);
        fingerMoveDistanceXMax = UiUtils.dipToPx(100);
        fingerSlideDistanceXMin = UiUtils.dipToPx(1);
        buttonRadiusFinal = UiUtils.dipToPx(23);
        fingerSlideDistanceYMin = 0;
        buttonScalePercent = 1;
        buttonDrawablePercent = 0.5f;
        buttonDrawable = ContextCompat.getDrawable(getContext(), R.drawable.svg_input_bar_mic);
        buttonBackgroundColor = ContextCompat.getColor(getContext(), R.color.color_chat_input_bar_audio_record);
        //Button locked.
        buttonLockedDrawable = ContextCompat.getDrawable(getContext(), R.drawable.chat_anim_lock_red);
        buttonLockedRadiusFinal = UiUtils.dipToPx(26);
        //Slide background.
        slideRect = new RectF();
        slideBackgroundColor = ContextCompat.getColor(getContext(), R.color.color_chat_record_background);
        slideBackgroundHeightFinal = UiUtils.dipToPx(46);
        slideBackgroundMarginStartFinal = UiUtils.dipToPx(0);
        slideBackgroundMarginEndFinal = UiUtils.dipToPx(5);
        //Time duration.
        timeDurationTextSizePx = UiUtils.spToPx(18);
        timeDurationTextColor = ContextCompat.getColor(getContext(), R.color.color_chat_record_text_color);
        //Audio icon.
        audioIconDrawable = ContextCompat.getDrawable(getContext(), R.drawable.svg_audio_record_mic_red);
        audioIconWidthFinal = UiUtils.dipToPx(46);
        audioIconHeightFinal = UiUtils.dipToPx(46);
        audioIconPaddingFinal = UiUtils.dipToPx(11);
        //Cancel string.
        cancelStringFinal = ResourceUtils.getString(R.string.slide_to_cancel);
        cancelDrawable = ContextCompat.getDrawable(getContext(), R.drawable.svg_audio_record_arrow_left);
        cancelDrawableWidthFinal = UiUtils.dipToPx(20);
        cancelDrawableHeightFinal = UiUtils.dipToPx(20);
        cancelStringTextSizePx = UiUtils.spToPx(16);
        cancelStringAnimateMatrix = new Matrix();
        cancelStringTextColor = ContextCompat.getColor(getContext(), R.color.color_chat_record_text_color);
        cancelStringTextColorCenter = ContextCompat.getColor(getContext(), R.color.color_chat_record_text_color_center);
        //Lock background.
        lockRect = new RectF();
        lockBackgroundColor = ContextCompat.getColor(getContext(), R.color.color_chat_record_background);
        lockBackgroundWidthFinal = UiUtils.dipToPx(52);
        lockBackgroundHeightFinal = UiUtils.dipToPx(160);
        lockBackgroundMarginBottomFinal = UiUtils.dipToPx(20);
        lockBackgroundYWaveAnimateTranslateRangeFinal = UiUtils.dipToPx(6);
        lockBackgroundYWaveAnimateMaxMoveDistance = UiUtils.dipToPx(10);
        lockHeadDrawable = ContextCompat.getDrawable(getContext(), R.drawable.chat_anim_lock_top);
        lockHeadDrawableMarginTop = UiUtils.dipToPx(18);
        lockBodyDrawable = ContextCompat.getDrawable(getContext(), R.drawable.chat_anim_lock_body);
        lockHeadDrawableWidthFinal = UiUtils.dipToPx(15);
        lockHeadDrawableHeightFinal = UiUtils.dipToPx(13);
        lockBodyDrawableWidthFinal = UiUtils.dipToPx(15);
        lockBodyDrawableHeightFinal = UiUtils.dipToPx(13);
        lockBodyDrawableMarginTop = UiUtils.dipToPx(23);
        lockArrowDrawable = ContextCompat.getDrawable(getContext(), R.drawable.svg_audio_record_arrow_top);
        lockArrowDrawableWidthFinal = UiUtils.dipToPx(34);
        lockArrowDrawableHeightFinal = UiUtils.dipToPx(28);
        lockArrowDrawableMarginTop = UiUtils.dipToPx(46);
        lockArrowDrawableYWaveTranslateRange = UiUtils.dipToPx(10);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isCanRecord()) {
            return super.onTouchEvent(event);
        }
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = x;
                yDown = y;
                downInButton = isPointInView(event);
                if (downInButton) {
                    boolean hasPermission = onViewExpand();
                    if (hasPermission) {
                        invalidate();
                        return true;
                    } else {
                        downInButton = false;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (downInButton) {
                    translateWithFingerMove(event);
                    invalidate();
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (downInButton) {
                    onViewClosed(false);
                    invalidate();
                    return true;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean onViewExpand() {
        boolean result = listener.onFingerPressed();
        if (!result) {
            return false;
        }
        cancelTranslateAnimation();
        startTimeAnimator();
        VibratorUtil.vibrate(getContext(), 50);
        startLinearScaleAnimation(true, false);
        startWaveAnimation();
        return true;
    }

    private void onViewClosed(boolean locked) {
        slideHorizontal = false;
        slideVertical = false;
        downInButton = false;

        cancelTimeAnimator();
        cancelWaveAnimation();

        startLinearScaleAnimation(false, locked);
        startTranslateAnimation(locked);

        if (listener != null) {
            if (locked) {
                listener.onFingerSlideLocked(timeDurationMills);
            } else if (!slideCanceled) {
                listener.onFingerRelease(timeDurationMills);
                slideCanceled = false;
            }
        }
    }

    private void translateWithFingerMove(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
//            if (isScaleAnimationRunning()) {
//                xDown = x;
//                yDown = y;
//                return;
//            }
        float distanceMovedY = (y > yDown ? 0 : yDown - y);
        float distanceMovedX;
        if (isRTL()) {
            distanceMovedX = (x < xDown ? 0 : (x - xDown));
        } else {
            distanceMovedX = (x > xDown ? 0 : (xDown - x));
        }

        slideHorizontal = distanceMovedX > distanceMovedY && distanceMovedX > fingerSlideDistanceXMin;
        slideVertical = distanceMovedY > distanceMovedX && distanceMovedY > fingerSlideDistanceYMin;
        if (listener != null) {
            listener.onFingerSliding(slideHorizontal);
        }
        slideCanceled = slideHorizontal && distanceMovedX > fingerMoveDistanceXMax;
        if (slideVertical && distanceMovedY > fingerMoveDistanceYMax) {
            onViewClosed(true);
            startLockedShrinkAnimation();
            return;
        } else if (slideCanceled) {
            VibratorUtil.vibrate(getContext(), 50);
            onViewClosed(false);
            if (listener != null) {
                listener.onFingerSlideCanceled(timeDurationMills);
            }
            return;
        }
        if (slideVertical) {
            xDown = x;
            fingerMoveDistanceX = 0;
            fingerMoveDistanceY = Math.min(fingerMoveDistanceYMax, distanceMovedY);
        } else if (slideHorizontal) {
            yDown = y;
            fingerMoveDistanceX = Math.min(fingerMoveDistanceXMax, distanceMovedX);
            fingerMoveDistanceY = 0;
        } else {
            fingerMoveDistanceY = 0;
            fingerMoveDistanceX = 0;
        }
        postInvalidate();
    }

    private void startTranslateAnimation(boolean locked) {
        cancelTranslateAnimation();
        if (locked) {
            fingerMoveDistanceX = 0;
            fingerMoveDistanceY = 0;
            postInvalidate();
            if (listener != null) {
                listener.onViewClosed();
            }
            return;
        }
        translateAnimator = ValueAnimator.ofFloat(1, 0);
        translateAnimator.setDuration(500);
        translateAnimator.setInterpolator(new LinearInterpolator());
        translateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (!animation.isRunning()) {
                    return;
                }
                float percent = (float) animation.getAnimatedValue();
                fingerMoveDistanceX *= percent;
                fingerMoveDistanceY *= percent;
                postInvalidate();
            }
        });
        translateAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                onAnimationEnd(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                fingerMoveDistanceX = 0;
                fingerMoveDistanceY = 0;
                postInvalidate();
                if (listener != null) {
                    listener.onViewClosed();
                }
            }
        });
        translateAnimator.start();
    }

    private void startWaveAnimation() {
        cancelWaveAnimation();
        long animationDuration = 1600;
        long cancelStringAnimationDuration = 800;
        waveAnimator = ValueAnimator.ofFloat(1, 0);
        waveAnimator.setDuration(animationDuration);
        waveAnimator.setRepeatCount(ValueAnimator.INFINITE);
        waveAnimator.setInterpolator(new LinearInterpolator());
        waveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (!animation.isRunning()) {
                    return;
                }
                float percent = (float) animation.getAnimatedValue();
                float lockPercent = (percent < 0.5f ? percent : 1 - percent) / 0.5f;
                //Lock part.
                if (fingerMoveDistanceY < lockBackgroundYWaveAnimateMaxMoveDistance) {
                    lockBackgroundYWaveAnimateTranslatePercent = (float) Math.pow(lockPercent, 2);
                    if (lockPercent < 0.5) {
                        lockArrowDrawableYWaveTranslatePercent = lockPercent * 2;
                    } else {
                        lockArrowDrawableYWaveTranslatePercent = 1 - ((lockPercent - 0.5f) * 2);
                    }
                } else {
                    lockBackgroundYWaveAnimateTranslatePercent = 0;
                    lockArrowDrawableYWaveTranslatePercent = 0;
                }
                //Cancel string gradient wave percent.
                float cancelStringDurationPercent = cancelStringAnimationDuration / (float) animationDuration;
                if (percent > (1 - cancelStringDurationPercent)) {
                    percent = Math.max(0, 1 - ((1 - percent) / cancelStringDurationPercent));
                    cancelStringAnimateTranslationPercent = percent;
                } else {
                    cancelStringAnimateTranslationPercent = 0;
                }
                postInvalidate();
            }
        });
        waveAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                onAnimationEnd(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                lockBackgroundYWaveAnimateTranslatePercent = 0;
                lockArrowDrawableYWaveTranslatePercent = 0;
                cancelStringAnimateTranslationPercent = 0;
                postInvalidate();
            }
        });
        waveAnimator.start();
    }

    private void cancelWaveAnimation() {
        if (waveAnimator != null && waveAnimator.isRunning()) {
            waveAnimator.cancel();
        }
    }

    private void startLockedShrinkAnimation() {
        buttonLockedAlphaPercent = 1;
        cancelLockedShrinkAnimation();
        cancelLockedShrinkAlphaAnimation();
        lockedShrinkAnimator = ValueAnimator.ofFloat(0, 1);
        lockedShrinkAnimator.setDuration(200);
        lockedShrinkAnimator.setRepeatMode(ValueAnimator.REVERSE);
        lockedShrinkAnimator.setRepeatCount(3);
        lockedShrinkAnimator.setInterpolator(new LinearInterpolator());
        lockedShrinkAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (!animation.isRunning()) {
                    return;
                }
                float percent = (float) animation.getAnimatedValue();
                buttonLockedScalePercent = (buttonLockedScalePercentMax - 1) * percent + 1;
                postInvalidate();
            }
        });
        lockedShrinkAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                buttonLockedScalePercent = 1;
                postInvalidate();
                cancelLockedShrinkAlphaAnimation();
                lockedShrinkAlphaAnimator = ValueAnimator.ofFloat(1, 0);
                lockedShrinkAlphaAnimator.setDuration(200);
                lockedShrinkAlphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (!animation.isRunning()) {
                            return;
                        }
                        buttonLockedAlphaPercent = (float) animation.getAnimatedValue();
                        postInvalidate();
                    }
                });
                lockedShrinkAlphaAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        onAnimationEnd(animation);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        buttonLockedAlphaPercent = 0;
                        postInvalidate();
                    }
                });
                lockedShrinkAlphaAnimator.start();
            }
        });
        lockedShrinkAnimator.start();
    }

    private void cancelLockedShrinkAnimation() {
        if (lockedShrinkAnimator != null && lockedShrinkAnimator.isRunning()) {
            lockedShrinkAnimator.cancel();
        }
    }

    private void cancelLockedShrinkAlphaAnimation() {
        if (lockedShrinkAlphaAnimator != null && lockedShrinkAlphaAnimator.isRunning()) {
            lockedShrinkAlphaAnimator.cancel();
        }
    }

    private void startLinearScaleAnimation(boolean expend, boolean locked) {
        cancelLinearScaleAnimation();
        if (locked) {
            setAllToOriginPosition(expend, true);
            return;
        }
        linearScaleAnimator = expend ? ValueAnimator.ofFloat(0, 1) : ValueAnimator.ofFloat(1, 0);
        linearScaleAnimator.setDuration(100);
        linearScaleAnimator.setInterpolator(new LinearInterpolator());
        linearScaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (!animation.isRunning()) {
                    return;
                }
                float percent = (float) animation.getAnimatedValue();
                //Slide.
                slideBackgroundXAnimateTranslatePercent = expend ? percent : 1;
                slideBackgroundAlphaPercent = expend ? percent : (percent < 0.5f ? 0 : percent);
                //Lock.
                lockBackgroundYAnimateTranslatePercent = expend ? percent : 1;
                lockBackgroundAlphaPercent = percent;
                //Time duration.
                timeDurationAlphaPercent = percent;
                //Cancel string.
                cancelStringAlphaPercent = percent;
                //Button overshoot.
                buttonScalePercent = (buttonScalePercentMax - 1) * getInterpolation(percent) + 1;
                postInvalidate();
            }

            private float getInterpolation(float percent) {
                float tension = 2.0f;
                percent -= 1.0f;
                return percent * percent * ((tension + 1) * percent + tension) + 1.0f;
            }
        });
        linearScaleAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                onAnimationEnd(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setAllToOriginPosition(expend, false);
            }
        });
        linearScaleAnimator.start();
    }

    private void setAllToOriginPosition(boolean expend, boolean locked) {
        //Slide.
        slideBackgroundXAnimateTranslatePercent = locked ? 0 : (expend ? 1 : 0);
        slideBackgroundAlphaPercent = locked ? 0 : (expend ? 1 : 0);
        //Lock.
        lockBackgroundYAnimateTranslatePercent = locked ? 0 : (expend ? 1 : 0);
        lockBackgroundAlphaPercent = locked ? 0 : (expend ? 1 : 0);
        //Time duration.
        timeDurationAlphaPercent = locked ? 0 : (expend ? 1 : 0);
        //Cancel string.
        cancelStringAlphaPercent = locked ? 0 : (expend ? 1 : 0);
        //Button.
        buttonScalePercent = locked ? 1 : (expend ? buttonScalePercentMax : 1);
        postInvalidate();
    }

    private void cancelTranslateAnimation() {
        if (translateAnimator != null && translateAnimator.isRunning()) {
            translateAnimator.cancel();
        }
    }

    private void cancelLinearScaleAnimation() {
        if (linearScaleAnimator != null && linearScaleAnimator.isRunning()) {
            linearScaleAnimator.cancel();
        }
    }

    private boolean isScaleAnimationRunning() {
        return linearScaleAnimator != null && linearScaleAnimator.isRunning();
    }

    private boolean isTransAnimationRunning() {
        return translateAnimator != null && translateAnimator.isRunning();
    }

    private void startTimeAnimator() {
        cancelTimeAnimator();
        timeDurationMills = 0;
        timeAnimator = ValueAnimator.ofFloat(0, 1);
        timeAnimator.setRepeatCount(ValueAnimator.INFINITE);
        timeAnimator.setRepeatMode(ValueAnimator.REVERSE);
        timeAnimator.setDuration(500);
        timeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            private long timeStart;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (!animation.isRunning()) {
                    return;
                }
                float percent = (float) animation.getAnimatedValue();
                long now = SystemClock.elapsedRealtime();
                if (timeStart <= 0) {
                    timeStart = now;
                }
                timeDurationMills = now - timeStart;
                audioIconAlphaPercent = percent;

                postInvalidate();
            }
        });
        timeAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationCancel(Animator animation) {
                onAnimationEnd(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                audioIconAlphaPercent = 0;
                postInvalidate();
            }
        });
        timeAnimator.start();
    }

    private void cancelTimeAnimator() {
        if (timeAnimator != null && timeAnimator.isRunning()) {
            timeAnimator.cancel();
        }
    }

    private boolean isPointInView(MotionEvent event) {
        //Button click rect.
        int buttonBackgroundCircleLeft = (int) (getButtonCenterX() - buttonRadiusFinal);
        int buttonBackgroundCircleRight = (int) (getButtonCenterX() + buttonRadiusFinal);
        int buttonBackgroundCircleTop = (int) (getButtonCenterY() - buttonRadiusFinal);
        int buttonBackgroundCircleBottom = (int) (getButtonCenterY() + buttonRadiusFinal);
        buttonClickRect.set(buttonBackgroundCircleLeft, buttonBackgroundCircleTop, buttonBackgroundCircleRight, buttonBackgroundCircleBottom);
        return buttonClickRect.contains((int) event.getX(), (int) event.getY());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(paintFlagsDrawFilter);

        if (!downInButton && !isScaleAnimationRunning() && !isTransAnimationRunning()) {
            return;
        }
        //Slide background.
        drawSlideBackground(canvas);
        //Audio icon.
        drawAudioIcon(canvas);
        //Time duration.
        drawTimeDuration(canvas);
        //Cancel string.
        drawCancelString(canvas);

        //Lock background.
        drawLockBackground(canvas);

        //Button circle.
        drawButton(canvas);
    }

    private void drawSlideBackground(Canvas canvas) {
        //Slide background.
        float fingerMoveDistanceYPercent = fingerMoveDistanceY / fingerMoveDistanceYMax;
        float fingerMoveDistanceXPercent = fingerMoveDistanceX / fingerMoveDistanceXMax;
        float slideBackgroundWidth = getWidth() - getPaddingStart() - getPaddingEnd() - slideBackgroundMarginStartFinal - slideBackgroundMarginEndFinal - fingerMoveDistanceX - buttonRadiusFinal * 2;
        float slideBackgroundLeft;
        float slideBackgroundRight;
        if (isRTL()) {
            slideBackgroundLeft = getButtonCenterX() + buttonRadiusFinal + slideBackgroundMarginEndFinal + fingerMoveDistanceX - slideBackgroundWidth * (1 - slideBackgroundXAnimateTranslatePercent);
            slideBackgroundRight = slideBackgroundLeft + slideBackgroundWidth;
        } else {
            slideBackgroundRight = getButtonCenterX() - buttonRadiusFinal - slideBackgroundMarginEndFinal - fingerMoveDistanceX + slideBackgroundWidth * (1 - slideBackgroundXAnimateTranslatePercent);
            slideBackgroundLeft = slideBackgroundRight - slideBackgroundWidth;
        }

        float slideBackgroundAlpha = 255 * slideBackgroundAlphaPercent;
        float slideBackgroundTop = getSlideBackgroundY();
        float slideBackgroundBottom = slideBackgroundTop + slideBackgroundHeightFinal;
        slideRect.set(slideBackgroundLeft, slideBackgroundTop, slideBackgroundRight, slideBackgroundBottom);

        float lockBackgroundRadius = slideBackgroundHeightFinal / 2f;
        paint.setColor(slideBackgroundColor);
        paint.setAlpha((int) (slideBackgroundAlpha));
        canvas.drawRoundRect(slideRect, lockBackgroundRadius, lockBackgroundRadius, paint);
        paint.setAlpha(255);
    }

    private void drawAudioIcon(Canvas canvas) {
        //Slide background.
        float slideBackgroundWidth = getWidth() - getPaddingStart() - getPaddingEnd() - slideBackgroundMarginStartFinal - slideBackgroundMarginEndFinal - fingerMoveDistanceX - buttonRadiusFinal * 2;
        float audioIconLeft;
        if (isRTL()) {
            audioIconLeft = getAudioIconX() - audioIconWidthFinal - slideBackgroundWidth * (1 - slideBackgroundXAnimateTranslatePercent);
        } else {
            audioIconLeft = getAudioIconX() + slideBackgroundWidth * (1 - slideBackgroundXAnimateTranslatePercent);
        }

        float audioIconRight = audioIconLeft + audioIconWidthFinal;
        float audioIconTop = getButtonCenterY() - audioIconHeightFinal / 2f;
        float audioIconBottom = audioIconTop + audioIconHeightFinal;

        audioIconDrawable.setAlpha((int) (audioIconAlphaPercent * 255));
        audioIconDrawable.setBounds((int) (audioIconLeft + audioIconPaddingFinal), (int) (audioIconTop + audioIconPaddingFinal), (int) (audioIconRight - audioIconPaddingFinal), (int) (audioIconBottom - audioIconPaddingFinal));
        audioIconDrawable.draw(canvas);
    }

    private void drawTimeDuration(Canvas canvas) {
        //Slide background.
        float slideBackgroundWidth = getWidth() - getPaddingStart() - getPaddingEnd() - slideBackgroundMarginStartFinal - slideBackgroundMarginEndFinal - fingerMoveDistanceX - buttonRadiusFinal * 2;
        paint.setTextSize(timeDurationTextSizePx);
        String timeDurationString = TimeUtils.formatLongToDuration(timeDurationMills);
        float timeDurationLeft;
        float timeDurationWidth = paint.measureText(timeDurationString);
        if (isRTL()) {
            timeDurationLeft = getTimeDurationX() - timeDurationWidth - slideBackgroundWidth * (1 - slideBackgroundXAnimateTranslatePercent);
        } else {
            timeDurationLeft = getTimeDurationX() + slideBackgroundWidth * (1 - slideBackgroundXAnimateTranslatePercent);
        }
        float timeDurationCenterX = timeDurationLeft + timeDurationWidth / 2f;
        float timeDurationCenterY = getButtonCenterY() - (paint.descent() + paint.ascent()) / 2f;

        paint.setColor(timeDurationTextColor);
        paint.setAlpha((int) (timeDurationAlphaPercent * 255));
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(timeDurationString, timeDurationCenterX, timeDurationCenterY, paint);

        paint.setAlpha(255);
    }

    private void drawCancelString(Canvas canvas) {
        //Slide background.
        float slideBackgroundWidth = getWidth() - getPaddingStart() - getPaddingEnd() - slideBackgroundMarginStartFinal - slideBackgroundMarginEndFinal - fingerMoveDistanceX - buttonRadiusFinal * 2;
        paint.setTextSize(cancelStringTextSizePx);
        float cancelStringWidth = paint.measureText(cancelStringFinal);

        float cancelStringCenterX;
        float cancelDrawableLeft;
        float cancelDrawableRight;
        float clipCancelStringLeft;
        float clipCancelStringRight;
        if (isRTL()) {
            float cancelStringLeft = getButtonCenterX() + buttonRadiusFinal * buttonScalePercentMax + slideBackgroundMarginEndFinal + fingerMoveDistanceX - slideBackgroundWidth * (1 - slideBackgroundXAnimateTranslatePercent);
            //Cancel string.
            cancelStringCenterX = cancelStringLeft + cancelStringWidth / 2f;
            //Cancel drawable.
            cancelDrawableLeft = cancelStringCenterX + cancelStringWidth / 2f;
            cancelDrawableRight = cancelDrawableLeft + cancelDrawableWidthFinal;
            //Clip rect.
            clipCancelStringLeft = getButtonCenterX() + buttonRadiusFinal * buttonScalePercentMax + slideBackgroundMarginEndFinal - slideBackgroundWidth * (1 - slideBackgroundXAnimateTranslatePercent);
            clipCancelStringRight = clipCancelStringLeft + cancelStringWidth + cancelDrawableWidthFinal;
        } else {
            float slideBackgroundRight = getButtonCenterX() - buttonRadiusFinal * buttonScalePercentMax - slideBackgroundMarginEndFinal - fingerMoveDistanceX + slideBackgroundWidth * (1 - slideBackgroundXAnimateTranslatePercent);
            //Cancel string.
            cancelStringCenterX = slideBackgroundRight - cancelStringWidth / 2f;
            //Cancel drawable.
            cancelDrawableRight = cancelStringCenterX - cancelStringWidth / 2f;
            cancelDrawableLeft = cancelDrawableRight - cancelDrawableWidthFinal;
            //Clip rect;
            clipCancelStringRight = getButtonCenterX() - buttonRadiusFinal * buttonScalePercentMax - slideBackgroundMarginEndFinal + slideBackgroundWidth * (1 - slideBackgroundXAnimateTranslatePercent);
            clipCancelStringLeft = clipCancelStringRight - cancelStringWidth - cancelDrawableWidthFinal;
        }

        float cancelStringCenterY = getButtonCenterY() - (paint.descent() + paint.ascent()) / 2f;
        float cancelDrawableTop = getButtonCenterY() - cancelDrawableHeightFinal / 2f;
        float cancelDrawableBottom = getButtonCenterY() + cancelDrawableHeightFinal / 2f;
        cancelDrawable.setBounds((int) cancelDrawableLeft, (int) cancelDrawableTop, (int) cancelDrawableRight, (int) cancelDrawableBottom);

        paint.setColor(cancelStringTextColor);
        paint.setTextAlign(Paint.Align.CENTER);

        canvas.save();
        if (cancelStringAnimateGradient == null) {
            float gradientWidth = cancelStringWidth * 2;
            cancelStringAnimateGradient = new LinearGradient(0, 0, cancelStringWidth, 0,
                    new int[]{
                            cancelStringTextColor,
                            cancelStringTextColorCenter,
                            cancelStringTextColor
                    },
                    new float[]{0.2f, 0.5f, 0.8f},
                    Shader.TileMode.CLAMP);
        }
        float cancelStringAnimateOffset = cancelStringCenterX - cancelStringWidth * 1.5f + cancelStringAnimateTranslationPercent * cancelStringWidth * 1.5f;
        cancelStringAnimateMatrix.setTranslate(cancelStringAnimateOffset, 0f);
        cancelStringAnimateGradient.setLocalMatrix(cancelStringAnimateMatrix);
        paint.setShader(cancelStringAnimateGradient);
        float clipCancelStringTop = getButtonCenterY() - slideBackgroundHeightFinal / 2f;
        float clipCancelStringBottom = getButtonCenterY() + slideBackgroundHeightFinal / 2f;
        canvas.clipRect(clipCancelStringLeft, clipCancelStringTop, clipCancelStringRight, clipCancelStringBottom);
        canvas.drawText(cancelStringFinal, cancelStringCenterX, cancelStringCenterY, paint);
        cancelDrawable.setAlpha((int) (cancelStringAlphaPercent * 255));
        cancelDrawable.draw(canvas);
        canvas.restore();
        paint.setShader(null);
    }

    private void drawLockBackground(Canvas canvas) {
        //Lock background.
        float fingerMoveDistanceYPercent = fingerMoveDistanceY / fingerMoveDistanceYMax;
        float fingerMoveDistanceXPercent = fingerMoveDistanceX / fingerMoveDistanceXMax;
        //Expand: 0 -> 1.
        float lockBackgroundYAnimateTranslate = getLockBackgroundY() + (getHeight() - getLockBackgroundY() - lockBackgroundMarginBottomFinal) * (1 - lockBackgroundYAnimateTranslatePercent);

        float randomPercentForYTranslate = 2.5f;
        float lockBackgroundYWithFingerTranslateX = lockBackgroundYAnimateTranslate + fingerMoveDistanceXPercent * lockBackgroundHeightFinal * randomPercentForYTranslate;

        final float lockBackgroundHeightMin = lockBackgroundWidthFinal;
        float lockBackgroundHeight = lockBackgroundHeightMin + (lockBackgroundHeightFinal - lockBackgroundHeightMin) * (1 - fingerMoveDistanceYPercent);

        float lockBackgroundLeft = getLockBackgroundX();
        float lockBackgroundRight = lockBackgroundLeft + lockBackgroundWidthFinal;
        float lockBackgroundTop = lockBackgroundYWithFingerTranslateX - fingerMoveDistanceY;
        float lockBackgroundYWaveAnimateTranslateDistance = lockBackgroundYWaveAnimateTranslateRangeFinal * lockBackgroundYWaveAnimateTranslatePercent;
        float lockBackgroundTopWithWaveDistance = lockBackgroundTop + lockBackgroundYWaveAnimateTranslateDistance;
        float lockBackgroundBottom = lockBackgroundTopWithWaveDistance + lockBackgroundHeight;
        lockRect.set(lockBackgroundLeft, lockBackgroundTopWithWaveDistance, lockBackgroundRight, lockBackgroundBottom);

        float lockBackgroundRadius = lockBackgroundWidthFinal / 2f;
        paint.setColor(lockBackgroundColor);

        float randomPercentForAlpha = 2f;
        float lockBackgroundAlpha = Math.max(0, (1 - fingerMoveDistanceXPercent * randomPercentForAlpha) * 255) * lockBackgroundAlphaPercent;
        paint.setAlpha((int) (lockBackgroundAlpha));

        canvas.drawRoundRect(lockRect, lockBackgroundRadius, lockBackgroundRadius, paint);
        paint.setAlpha(255);

        float lockBackgroundXCenter = lockBackgroundLeft + (lockBackgroundRight - lockBackgroundLeft) / 2f;
        //Lock body.
        float lockBodyLeft = lockBackgroundXCenter - lockBodyDrawableWidthFinal / 2f;
        float lockBodyTop = lockBackgroundTopWithWaveDistance + lockBodyDrawableMarginTop;
        float lockBodyRight = lockBackgroundXCenter + lockBodyDrawableWidthFinal / 2f;
        float lockBodyBottom = lockBodyTop + lockBodyDrawableHeightFinal;
        lockBodyDrawable.setBounds((int) lockBodyLeft, (int) lockBodyTop, (int) lockBodyRight, (int) lockBodyBottom);
        lockBodyDrawable.setAlpha((int) (lockBackgroundAlpha));
        lockBodyDrawable.draw(canvas);
        //Lock head.
        float lockHeadLeft = lockBackgroundXCenter - lockHeadDrawableWidthFinal / 2f;
        float lockHeadTop = lockBackgroundTopWithWaveDistance + lockHeadDrawableMarginTop - lockBackgroundYWaveAnimateTranslateDistance;
        if (fingerMoveDistanceY > lockBackgroundYWaveAnimateMaxMoveDistance) {
            lockHeadTop = lockHeadTop - lockHeadDrawableHeightFinal / 2f * (1 - fingerMoveDistanceYPercent);
        }
        float lockHeadRight = lockBackgroundXCenter + lockHeadDrawableWidthFinal / 2f;
        float lockHeadBottom = lockHeadTop + lockHeadDrawableHeightFinal;
        lockHeadDrawable.setBounds((int) lockHeadLeft, (int) lockHeadTop, (int) lockHeadRight, (int) lockHeadBottom);
        lockHeadDrawable.setAlpha((int) (lockBackgroundAlpha));
        lockHeadDrawable.draw(canvas);
        //Lock arrow.
        float lockArrowLeft = lockBackgroundXCenter - lockArrowDrawableWidthFinal / 2f;
        float lockArrowTop = lockBackgroundTopWithWaveDistance + lockArrowDrawableMarginTop - (lockArrowDrawableYWaveTranslatePercent * lockArrowDrawableYWaveTranslateRange);
        float lockArrowRight = lockBackgroundXCenter + lockArrowDrawableWidthFinal / 2f;
        float lockArrowBottom = lockArrowTop + lockArrowDrawableHeightFinal;
        lockArrowDrawable.setBounds((int) lockArrowLeft, (int) lockArrowTop, (int) lockArrowRight, (int) lockArrowBottom);
        lockArrowDrawable.setAlpha((int) ((1 - fingerMoveDistanceYPercent) * lockBackgroundAlpha));
        lockArrowDrawable.draw(canvas);
    }

    private void drawButton(Canvas canvas) {
        //Button circle.
        float fingerMoveDistanceYPercent = fingerMoveDistanceY / fingerMoveDistanceYMax;
        float fingerMoveDistanceXPercent = fingerMoveDistanceX / fingerMoveDistanceXMax;

        float buttonRadiusScaled = buttonRadiusFinal * buttonScalePercent * (1 - fingerMoveDistanceYPercent);
        float buttonCenterXTranslate = isRTL() ? getButtonCenterX() + fingerMoveDistanceX : getButtonCenterX() - fingerMoveDistanceX;
        float buttonCenterYTranslate = getButtonCenterY() - fingerMoveDistanceY;
        paint.setColor(buttonBackgroundColor);
        canvas.drawCircle(buttonCenterXTranslate, buttonCenterYTranslate, buttonRadiusScaled, paint);
        //Button drawable.
        float buttonDrawableSizeScaled = buttonRadiusScaled * 2 * buttonDrawablePercent;
        int buttonDrawableLeft = (int) (buttonCenterXTranslate - buttonDrawableSizeScaled / 2f);
        int buttonDrawableRight = (int) (buttonDrawableLeft + buttonDrawableSizeScaled);
        int buttonDrawableTop = (int) (buttonCenterYTranslate - buttonDrawableSizeScaled / 2f);
        int buttonDrawableBottom = (int) (buttonDrawableTop + buttonDrawableSizeScaled);
        buttonDrawable.setBounds(buttonDrawableLeft, buttonDrawableTop, buttonDrawableRight, buttonDrawableBottom);
        buttonDrawable.draw(canvas);

        //Button locked.
        float buttonLockedCenterX = getButtonCenterX();
        float buttonLockedCenterY = getLockBackgroundY() - fingerMoveDistanceYMax + buttonLockedRadiusFinal;
        float buttonLockedRadiusScaled = buttonLockedRadiusFinal * buttonLockedScalePercent;
        int buttonLockedDrawableLeft = (int) (buttonLockedCenterX - buttonLockedRadiusScaled);
        int buttonLockedDrawableRight = (int) (buttonLockedCenterX + buttonLockedRadiusScaled);
        int buttonLockedDrawableTop = (int) (buttonLockedCenterY - buttonLockedRadiusScaled);
        int buttonLockedDrawableBottom = (int) (buttonLockedCenterY + buttonLockedRadiusScaled);
        buttonLockedDrawable.setBounds(buttonLockedDrawableLeft, buttonLockedDrawableTop, buttonLockedDrawableRight, buttonLockedDrawableBottom);
        buttonLockedDrawable.setAlpha((int) (buttonLockedAlphaPercent * 255));
        buttonLockedDrawable.draw(canvas);
    }

    private float getButtonCenterX() {
        return isRTL() ? getPaddingStart() + buttonRadiusFinal : getWidth() - getPaddingEnd() - buttonRadiusFinal;
    }

    private float getLockBackgroundX() {
        return getButtonCenterX() - lockBackgroundWidthFinal / 2f;
    }

    private float getButtonCenterY() {
        return getHeight() - getPaddingBottom() - buttonRadiusFinal;
    }

    private float getLockBackgroundY() {
        return getHeight() - getPaddingBottom() - lockBackgroundMarginBottomFinal - lockBackgroundHeightFinal;
    }

    private float getAudioIconX() {
        return isRTL() ? getWidth() - getPaddingEnd() : getPaddingStart();
    }

    private float getTimeDurationX() {
        return isRTL() ? getAudioIconX() - audioIconWidthFinal : getAudioIconX() + audioIconWidthFinal;
    }

    private float getSlideBackgroundY() {
        return getButtonCenterY() - slideBackgroundHeightFinal / 2f;
    }

    private boolean isRTL() {
        return getLayoutDirection() == LAYOUT_DIRECTION_RTL;
    }

    long debugDuration = 500;
    long debugMoveLog;

    void printTouchMove(String s) {
        long now = SystemClock.elapsedRealtime();
        if (now - debugMoveLog > debugDuration) {
            debugMoveLog = now;
            S.s(s);
        }
    }

    long moveInterceptLog;

    void printInterceptTouchMove(String s) {
        long now = SystemClock.elapsedRealtime();
        if (now - moveInterceptLog > debugDuration) {
            moveInterceptLog = now;
            S.e(s);
        }
    }

    public boolean isCanRecord() {
        return canRecord;
    }

    public void setCanRecord(boolean canRecord) {
        this.canRecord = canRecord;
    }

    @Override
    public void destroyDrawingCache() {
        cancelTimeAnimator();
        cancelLinearScaleAnimation();
        cancelTranslateAnimation();
        super.destroyDrawingCache();
    }

    public void setListener(AudioRecordViewListener listener) {
        this.listener = listener;
    }

    public interface AudioRecordViewListener {
        boolean onFingerPressed();

        void onFingerRelease(long durationMills);

        void onFingerSlideLocked(long durationMills);

        void onFingerSliding(boolean horizontal);

        void onFingerSlideCanceled(long durationMills);

        void onViewClosed();
    }
}
