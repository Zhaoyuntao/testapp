package im.thebot.chat.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.core.content.ContextCompat;

import com.example.module_chat.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import im.turbo.baseui.utils.UiUtils;
import im.turbo.utils.log.S;

/**
 * Created by zhaoyuntao on 2017/5/25.
 */

public class AudioRecordingProgressView extends View {

    private final float dotRadius, dotSpace;
    private final Paint paint;
    private final int dotColor;
    private final float speedPXPerMills;
    private final List<Dot> progressStack;

    private ValueAnimator animator;
    private float volumeValuePercent;
    private float volumePercentMax;
    private float offset;

    private OnDurationChangeListener onDurationChangeListener;

    private long timeDurationMills;
    private float[] initVolumes;

    public AudioRecordingProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        dotColor = ContextCompat.getColor(getContext(), R.color.color_chat_record_draft_progress_foreground);
        dotRadius = UiUtils.dipToPx(1.3f);
        dotSpace = UiUtils.dipToPx(1.5f);
        speedPXPerMills = UiUtils.dipToPx(35) / 1000f;
        progressStack = new ArrayList<>(20);
    }

    @Override
    final protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        volumePercentMax = getMeasuredHeight() / (dotRadius * 2) - 1;
        if (calculateInitData(initVolumes, timeDurationMills, getMeasuredWidth())) {
            this.initVolumes = null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDot(canvas, dotColor, progressStack.size(), offset, false);
    }

    final protected void drawDot(Canvas canvas, int dotColor, int dotDrawCount, float offset, boolean drawEmpty) {
        paint.setAntiAlias(true);
        paint.setColor(dotColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(dotRadius * 2);
        float yCenter = getMeasuredHeight() / 2f;
        for (int i = 0; i < dotDrawCount; i++) {
            float volumePercent = progressStack.get(i).volumePercent;
            float dotX = getPaddingStart() + i * (dotRadius * 2 + dotSpace) - offset;
            if (volumePercent < 0) {
                if (drawEmpty) {
                    volumePercent = 0;
                } else {
                    continue;
                }
            }
            float lineLength = (dotRadius * 2) * Math.min(volumePercentMax, volumePercent);
            float dotLineYStart = yCenter - lineLength / 2f;
            float dotLineYEnd = yCenter + lineLength / 2f;
            canvas.drawLine(dotX, dotLineYStart, dotX, dotLineYEnd, paint);
        }
    }

    final protected int getDotSize() {
        return progressStack == null ? 0 : progressStack.size();
    }

    private void startProgressAnimation() {
        animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            private long time;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                long now = SystemClock.elapsedRealtime();
                if (time <= 0) {
                    time = now;
                }
                long timePass = now - time;
                timeDurationMills += timePass;
                time = now;
                float currentDistance = speedPXPerMills * timePass;
                offset += currentDistance;
                if (offset > (dotRadius * 2 + dotSpace)) {
                    offset = 0;
                    addVolumeValue(volumeValuePercent);
                    volumeValuePercent = 0;
                }
                if (onDurationChangeListener != null) {
                    onDurationChangeListener.onDurationChanged(timeDurationMills);
                }
                postInvalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationCancel(Animator animation) {
                offset = 0;
                volumeValuePercent = 0;
                timeDurationMills = 0;
                if (onDurationChangeListener != null) {
                    onDurationChangeListener.onDurationChanged(timeDurationMills);
                }
                postInvalidate();
            }
        });
        animator.start();
    }

    final protected void addVolumeValue(float volumeValuePercent) {
        if (progressStack != null) {
            for (int i = 0; i < progressStack.size(); i++) {
                Dot dot = progressStack.get(i);
                if (i < progressStack.size() - 1) {
                    dot.volumePercent = progressStack.get(i + 1).volumePercent;
                } else {
                    dot.volumePercent = volumeValuePercent;
                }
            }
        }
    }

    public void setCurrentVolume(float volumeValuePercent) {
        this.volumeValuePercent = volumeValuePercent;
    }

    private void setEmptyData(int dotDrawCount) {
        progressStack.clear();
        for (int i = 0; i < dotDrawCount; i++) {
            progressStack.add(Dot.createEmpty());
        }
    }

    final protected void clearData() {
        for (int i = 0; i < progressStack.size(); i++) {
            progressStack.get(i).volumePercent = -1;
        }
    }

    public void setInitData(float[] volumes, long timeDurationMills) {
        this.timeDurationMills = timeDurationMills;
        this.initVolumes = volumes;
        if (calculateInitData(initVolumes, timeDurationMills, getMeasuredWidth())) {
            this.initVolumes = null;
        }
    }

    private boolean calculateInitData(float[] initVolumes, long timeDurationMills, int viewWidth) {
        if (viewWidth <= 0) {
            return false;
        }
        int maxDrawWidth = viewWidth - getPaddingStart() - getPaddingEnd();
        int dotDrawCount = (int) (maxDrawWidth / (dotRadius * 2 + dotSpace)) + 1;
        if (progressStack.size() != dotDrawCount) {
            setEmptyData(dotDrawCount);
        }
        if (initVolumes == null || initVolumes.length == 0 || timeDurationMills == 0) {
            return false;
        }
        float widthPerDot = dotRadius * 2 + dotSpace;
        long durationPerDot = (long) ((widthPerDot / speedPXPerMills));
        calculateInitData(initVolumes, timeDurationMills, durationPerDot, dotDrawCount);
        postInvalidate();
        return true;
    }

    protected void calculateInitData(float[] initVolumes, long timeDurationMills, long durationPerDot, int dotDrawCount) {
        long durationAllDot = durationPerDot * dotDrawCount;
        float calculatePartPercent = (float) (timeDurationMills / (double) durationAllDot);
        float durationPerInitData = (float) ((double) timeDurationMills / initVolumes.length);
        int calculateDataLength = (int) (calculatePartPercent * initVolumes.length);
        int piece = (int) ((float) durationPerDot / durationPerInitData);
        int startPositionCalculateData = Math.max(0, initVolumes.length - calculateDataLength);
        S.s(
                "=>" +
                        "\n initVolumes:" + Arrays.toString(initVolumes) +
                        "\n timeDurationMills:" + timeDurationMills +
                        "\n durationPerDot:" + durationPerDot +
                        "\n dotDrawCount:" + dotDrawCount +
                        "\n durationAllDot:" + durationAllDot +
                        "\n calculatePartPercent:" + calculatePartPercent +
                        "\n durationPerInitData:" + durationPerInitData +
                        "\n calculateDataLength:" + calculateDataLength +
                        "\n piece:" + piece +
                        "\n startPositionCalculateData:" + startPositionCalculateData
        );
        for (int i = 0; i < progressStack.size(); i++) {
            int indexInitData = startPositionCalculateData + i + i * piece;
            if (indexInitData <= initVolumes.length - 1) {
                addVolumeValue(initVolumes[indexInitData]);
            }
        }
        S.lll();
        S.s("result:" + progressStack);
        S.lll();
    }

    public void start() {
        cancelProgressAnimation();
        startProgressAnimation();
    }

    public void stop() {
        cancelProgressAnimation();
        clearData();
    }

    public void pause() {
        pauseProgressAnimation();
    }

    public void resume() {
        resumeProgressAnimation();
    }

    public boolean isPaused() {
        return animator != null && animator.isPaused();
    }

    private void pauseProgressAnimation() {
        if (animator != null && animator.isRunning() && !animator.isPaused()) {
            animator.pause();
        }
    }

    private void resumeProgressAnimation() {
        if (animator != null && animator.isPaused()) {
            animator.resume();
        }
    }

    private void cancelProgressAnimation() {
        if (animator != null && (animator.isRunning() || animator.isPaused())) {
            animator.cancel();
        }
    }

//    final public float[] debugGetVolumePercent() {
//        if (progressStack == null) {
//            return null;
//        }
//        List<Float> floats = new ArrayList<>(progressStack.size());
//        ValueSafeTransfer.iterate(progressStack, new ValueSafeTransfer.ElementIterator<Dot>() {
//            @Override
//            public Dot element(int position, Dot dot) {
//                if (dot.volumePercent >= 0) {
//                    floats.add(dot.volumePercent);
//                }
//                return null;
//            }
//        });
//        float[] arr = new float[floats.size()];
//        ValueSafeTransfer.iterate(floats, new ValueSafeTransfer.ElementIterator<Float>() {
//            @Override
//            public Float element(int position, Float dot) {
//                arr[position] = dot;
//                return null;
//            }
//        });
//        return arr;
//    }

    public static class Dot {
        public float volumePercent;

        public Dot(float volumePercent) {
            this.volumePercent = volumePercent;
        }

        public static Dot createEmpty() {
            return new Dot(-1);
        }

        @Override
        public String toString() {
            return String.valueOf(volumePercent);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        stop();
        super.onDetachedFromWindow();
    }

    public void setOnDurationChangeListener(OnDurationChangeListener onDurationChangeListener) {
        this.onDurationChangeListener = onDurationChangeListener;
    }

    public interface OnDurationChangeListener {
        void onDurationChanged(long duration);
    }
}
