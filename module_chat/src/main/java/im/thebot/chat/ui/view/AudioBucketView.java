package im.thebot.chat.ui.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.module_chat.R;

import java.util.ArrayList;
import java.util.List;

import im.turbo.baseui.utils.UiUtils;

/**
 * created by zhaoyuntao
 * on 22/06/2022
 * description:
 */
public class AudioBucketView extends View {
    private final Matrix matrix = new Matrix();

    private final List<AnimateFrame> frames = new ArrayList<>();
    private final Drawable bucketHeadDrawable;
    private final Drawable bucketBodyDrawable;
    private final Drawable micDrawable;
    private final float micMaxMoveDistance;
    private final float micDrawableWidth;
    private final float micDrawableHeight;

    private final float bucketHeadDrawableWidth;
    private final float bucketHeadDrawableHeight;
    private final float bucketBodyDrawableWidth;
    private final float bucketBodyDrawableHeight;
    private final float bucketHeadOffsetFromBodyAfterOpen;

    private final float drawableCenterX;
    private final float marginBottom;

    private float timeLine;
    private boolean overturn;

    public AudioBucketView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        micDrawable = ContextCompat.getDrawable(getContext(), R.drawable.svg_input_bar_mic_red2);
        bucketHeadDrawable = ContextCompat.getDrawable(getContext(), R.drawable.chat_anim_bucket_lid);
        bucketBodyDrawable = ContextCompat.getDrawable(getContext(), R.drawable.chat_anim_bucket_body);
        micMaxMoveDistance = UiUtils.dipToPx(155);
        drawableCenterX = UiUtils.dipToPx(31);
        micDrawableWidth = UiUtils.dipToPx(32);
        micDrawableHeight = UiUtils.dipToPx(32);

        bucketHeadDrawableWidth = UiUtils.dipToPx(18);
        bucketHeadDrawableHeight = UiUtils.dipToPx(4);
        bucketBodyDrawableWidth = UiUtils.dipToPx(16);
        bucketBodyDrawableHeight = UiUtils.dipToPx(18);
        bucketHeadOffsetFromBodyAfterOpen = UiUtils.dipToPx(15);

        marginBottom = UiUtils.dipToPx(31);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension((int) (drawableCenterX * 2), (int) (micMaxMoveDistance + micDrawableHeight + marginBottom));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int viewHeight = getMeasuredHeight();

        float bucketSpaceBetweenHeadAndBody = bucketBodyDrawableHeight / 2f - UiUtils.dipToPx(1);

        float micUpStart = 0;
        float micUpDuration = 800;
        AnimateFrame micUp = new AnimateFrame("micUp", micUpStart, micUpDuration) {
            private final float angleStart = 180;
            private final float angleEnd = 360;
            private final float xStart = drawableCenterX;
            private final float xEnd = drawableCenterX;
            private final float yStart = viewHeight - marginBottom;
            private final float yEnd = viewHeight - micMaxMoveDistance;

            @Override
            public void onDraw(Canvas canvas, float percent) {
                percent = (float) (float) (1.0f - Math.pow((1.0f - percent), 4));
                int centerX = (int) (xStart + (xEnd - xStart) * percent);
                int centerY = (int) (yStart + (yEnd - yStart) * percent);
                float angle = angleStart + (angleEnd - angleStart) * percent;
                canvas.save();
                canvas.rotate(angle, centerX, centerY);
                int left = (int) (centerX - micDrawableWidth / 2f);
                int right = (int) (centerX + micDrawableWidth / 2f);
                int top = (int) (centerY - micDrawableHeight);
                int bottom = (int) (centerY);
                micDrawable.setAlpha((int) (percent > 0.2 ? (percent * 255) : 0));
                micDrawable.setBounds(left, top, right, bottom);
                micDrawable.draw(canvas);

                canvas.restore();
            }
        };
        float micDropStart = micUpStart + micUpDuration;
        float micDropDuration = 700;
        AnimateFrame micDrop = new AnimateFrame("micDrop", micDropStart, micDropDuration) {
            private final float angleStart = 0;
            private final float angleEnd = 0;
            private final float xStart = drawableCenterX;
            private final float xEnd = drawableCenterX;
            private final float yStart = viewHeight - micMaxMoveDistance;
            private final float yEnd = viewHeight - marginBottom;

            @Override
            public void onDraw(Canvas canvas, float percent) {
                percent = (float) Math.pow(percent, 1.6);
                int centerX = (int) (xStart + (xEnd - xStart) * percent);
                int centerY = (int) (yStart + (yEnd - yStart) * percent);
                float angle = angleStart + (angleEnd - angleStart) * percent;
                canvas.save();
                canvas.rotate(angle, centerX, centerY);

                float wScale = micDrawableWidth * (1 - percent / 2);
                float hScale = micDrawableHeight * (1 - percent / 2);

                int left = (int) (centerX - wScale / 2f);
                int right = (int) (centerX + wScale / 2f);
                int top = (int) (centerY - hScale);
                int bottom = (int) (centerY);
                micDrawable.setBounds(left, top, right, bottom);
                micDrawable.draw(canvas);

                canvas.restore();
            }
        };
        float bucketUpStart = micUpStart + micUpDuration;
        float bucketUpDuration = 100;
        AnimateFrame bucketUp = new AnimateFrame("bucketUp", bucketUpStart, bucketUpDuration) {
            private final float bodyXStart = drawableCenterX;
            private final float bodyXEnd = drawableCenterX;
            private final float bodyYStart = viewHeight + bucketBodyDrawableHeight;
            private final float bodyYEnd = viewHeight - bucketBodyDrawableHeight;

            private final float headXStart = bodyXStart;
            private final float headXEnd = bodyXEnd;
            private final float headYStart = bodyYStart - bucketSpaceBetweenHeadAndBody - bucketHeadDrawableHeight;
            private final float headYEnd = bodyYEnd - bucketSpaceBetweenHeadAndBody - bucketHeadDrawableHeight;

            @Override
            public void onDraw(Canvas canvas, float percent) {
                //head.
                int headCenterX = (int) (headXStart + (headXEnd - headXStart) * percent);
                int headCenterY = (int) (headYStart + (headYEnd - headYStart) * percent);
                int headLeft = (int) (headCenterX - bucketHeadDrawableWidth / 2f);
                int headRight = (int) (headCenterX + bucketHeadDrawableWidth / 2f);
                int headTop = (int) (headCenterY - bucketHeadDrawableHeight / 2f);
                int headBottom = (int) (headCenterY + bucketHeadDrawableHeight / 2f);
                bucketHeadDrawable.setBounds(headLeft, headTop, headRight, headBottom);
                bucketHeadDrawable.draw(canvas);

                //body.
                int bodyCenterX = (int) (bodyXStart + (bodyXEnd - bodyXStart) * percent);
                int bodyCenterY = (int) (bodyYStart + (bodyYEnd - bodyYStart) * percent);

                int bodyLeft = (int) (bodyCenterX - bucketBodyDrawableWidth / 2f);
                int bodyRight = (int) (bodyCenterX + bucketBodyDrawableWidth / 2f);
                int bodyTop = (int) (bodyCenterY - bucketBodyDrawableHeight / 2f);
                int bodyBottom = (int) (bodyCenterY + bucketBodyDrawableHeight / 2f);
                bucketBodyDrawable.setBounds(bodyLeft, bodyTop, bodyRight, bodyBottom);
                bucketBodyDrawable.draw(canvas);
            }
        };
        float bucketUpAndOpenStart = bucketUpStart + bucketUpDuration;
        float bucketUpAndOpenDuration = 150;
        AnimateFrame bucketUpAndOpen = new AnimateFrame("bucketUpAndOpen", bucketUpAndOpenStart, bucketUpAndOpenDuration) {
            private final float bodyXStart = drawableCenterX;
            private final float bodyXEnd = drawableCenterX;
            private final float bodyYStart = viewHeight - bucketBodyDrawableHeight;
            private final float bodyYEnd = viewHeight - marginBottom;

            private final float headAngleStart = 0;
            private final float headAngleEnd = -60;
            private final float headXStart = bodyXStart;
            private final float headXEnd = bodyXEnd - bucketHeadOffsetFromBodyAfterOpen;
            private final float headYStart = bodyYStart - bucketSpaceBetweenHeadAndBody - bucketHeadDrawableHeight;
            private final float headYEnd = bodyYEnd - bucketSpaceBetweenHeadAndBody - bucketHeadDrawableHeight;

            @Override
            public void onDraw(Canvas canvas, float percent) {
                //head.
                int headCenterX = (int) (headXStart + (headXEnd - headXStart) * percent);
                int headCenterY = (int) (headYStart + (headYEnd - headYStart) * percent);
                float angle = headAngleStart + (headAngleEnd - headAngleStart) * percent;
                canvas.save();
                canvas.rotate(angle, headCenterX, headCenterY);
                int left2 = (int) (headCenterX - bucketHeadDrawableWidth / 2f);
                int right2 = (int) (headCenterX + bucketHeadDrawableWidth / 2f);
                int top2 = (int) (headCenterY - bucketHeadDrawableHeight / 2f);
                int bottom2 = (int) (headCenterY + bucketHeadDrawableHeight / 2f);
                bucketHeadDrawable.setBounds(left2, top2, right2, bottom2);
                bucketHeadDrawable.draw(canvas);
                canvas.restore();

                //body.
                int bodyCenterX = (int) (bodyXStart + (bodyXEnd - bodyXStart) * percent);
                int centerY = (int) (bodyYStart + (bodyYEnd - bodyYStart) * percent);

                int bodyLeft = (int) (bodyCenterX - bucketBodyDrawableWidth / 2f);
                int bodyRight = (int) (bodyCenterX + bucketBodyDrawableWidth / 2f);
                int bodyTop = (int) (centerY - bucketBodyDrawableHeight / 2f);
                int bodyBottom = (int) (centerY + bucketBodyDrawableHeight / 2f);
                bucketBodyDrawable.setBounds(bodyLeft, bodyTop, bodyRight, bodyBottom);
                bucketBodyDrawable.draw(canvas);
            }
        };
        float bucketOpenWaitingStart = bucketUpAndOpenStart + bucketUpAndOpenDuration;
        float bucketOpenWaitingDuration = 650;
        AnimateFrame bucketOpenWaiting = new AnimateFrame("bucketOpenWaiting", bucketOpenWaitingStart, bucketOpenWaitingDuration) {
            private final float bodyXEnd = drawableCenterX;
            private final float bodyYEnd = viewHeight - marginBottom;

            private final float headAngleEnd = -60;
            private final float headXEnd = bodyXEnd - bucketHeadOffsetFromBodyAfterOpen;
            private final float headYEnd = bodyYEnd - bucketSpaceBetweenHeadAndBody - bucketHeadDrawableHeight;

            @Override
            public void onDraw(Canvas canvas, float percent) {
                //head.
                int headCenterX = (int) (headXEnd);
                int headCenterY = (int) (headYEnd);
                canvas.save();
                canvas.rotate(headAngleEnd, headCenterX, headCenterY);
                int left2 = (int) (headCenterX - bucketHeadDrawableWidth / 2f);
                int right2 = (int) (headCenterX + bucketHeadDrawableWidth / 2f);
                int top2 = (int) (headCenterY - bucketHeadDrawableHeight / 2f);
                int bottom2 = (int) (headCenterY + bucketHeadDrawableHeight / 2f);
                bucketHeadDrawable.setBounds(left2, top2, right2, bottom2);
                bucketHeadDrawable.draw(canvas);
                canvas.restore();

                //body.
                int bodyCenterX = (int) (bodyXEnd);
                int centerY = (int) (bodyYEnd);

                int bodyLeft = (int) (bodyCenterX - bucketBodyDrawableWidth / 2f);
                int bodyRight = (int) (bodyCenterX + bucketBodyDrawableWidth / 2f);
                int bodyTop = (int) (centerY - bucketBodyDrawableHeight / 2f);
                int bodyBottom = (int) (centerY + bucketBodyDrawableHeight / 2f);
                bucketBodyDrawable.setBounds(bodyLeft, bodyTop, bodyRight, bodyBottom);
                bucketBodyDrawable.draw(canvas);
            }
        };
        float bucketCloseStart = bucketOpenWaitingStart + bucketOpenWaitingDuration;
        float bucketCloseDuration = 150;
        AnimateFrame bucketClose = new AnimateFrame("bucketClose", bucketCloseStart, bucketCloseDuration) {
            private final float bodyXEnd = drawableCenterX;
            private final float bodyYEnd = viewHeight - marginBottom;

            private final float headAngleStart = -60;
            private final float headAngleEnd = 0;
            private final float headXStart = bodyXEnd - bucketHeadOffsetFromBodyAfterOpen;
            private final float headXEnd = bodyXEnd;
            private final float headYEnd = bodyYEnd - bucketSpaceBetweenHeadAndBody - bucketHeadDrawableHeight;

            @Override
            public void onDraw(Canvas canvas, float percent) {
                //head.
                int headCenterX = (int) (headXStart + (headXEnd - headXStart) * percent);
                int headCenterY = (int) (headYEnd);
                float angle = headAngleStart + (headAngleEnd - headAngleStart) * percent;
                canvas.save();
                canvas.rotate(angle, headCenterX, headCenterY);
                int left2 = (int) (headCenterX - bucketHeadDrawableWidth / 2f);
                int right2 = (int) (headCenterX + bucketHeadDrawableWidth / 2f);
                int top2 = (int) (headCenterY - bucketHeadDrawableHeight / 2f);
                int bottom2 = (int) (headCenterY + bucketHeadDrawableHeight / 2f);
                bucketHeadDrawable.setBounds(left2, top2, right2, bottom2);
                bucketHeadDrawable.draw(canvas);
                canvas.restore();

                //body.
                int bodyCenterX = (int) (bodyXEnd);
                int bodyCenterY = (int) (bodyYEnd);

                int bodyLeft = (int) (bodyCenterX - bucketBodyDrawableWidth / 2f);
                int bodyRight = (int) (bodyCenterX + bucketBodyDrawableWidth / 2f);
                int bodyTop = (int) (bodyCenterY - bucketBodyDrawableHeight / 2f);
                int bodyBottom = (int) (bodyCenterY + bucketBodyDrawableHeight / 2f);
                bucketBodyDrawable.setBounds(bodyLeft, bodyTop, bodyRight, bodyBottom);
                bucketBodyDrawable.draw(canvas);
            }
        };
        float bucketCloseWaitingStart = bucketCloseStart + bucketCloseDuration;
        float bucketCloseWaitingDuration = 100;
        AnimateFrame bucketCloseWaiting = new AnimateFrame("bucketCloseWaiting", bucketCloseWaitingStart, bucketCloseWaitingDuration) {
            private final float bodyXEnd = drawableCenterX;
            private final float bodyYEnd = viewHeight - marginBottom;

            private final float headXEnd = bodyXEnd;
            private final float headYEnd = bodyYEnd - bucketSpaceBetweenHeadAndBody - bucketHeadDrawableHeight;

            @Override
            public void onDraw(Canvas canvas, float percent) {
                //head.
                int headCenterX = (int) (headXEnd);
                int headCenterY = (int) (headYEnd);
                int headLeft = (int) (headCenterX - bucketHeadDrawableWidth / 2f);
                int headRight = (int) (headCenterX + bucketHeadDrawableWidth / 2f);
                int headTop = (int) (headCenterY - bucketHeadDrawableHeight / 2f);
                int headBottom = (int) (headCenterY + bucketHeadDrawableHeight / 2f);
                bucketHeadDrawable.setBounds(headLeft, headTop, headRight, headBottom);
                bucketHeadDrawable.draw(canvas);

                //body.
                int bodyCenterX = (int) (bodyXEnd);
                int bodyCenterY = (int) (bodyYEnd);

                int bodyLeft = (int) (bodyCenterX - bucketBodyDrawableWidth / 2f);
                int bodyRight = (int) (bodyCenterX + bucketBodyDrawableWidth / 2f);
                int bodyTop = (int) (bodyCenterY - bucketBodyDrawableHeight / 2f);
                int bodyBottom = (int) (bodyCenterY + bucketBodyDrawableHeight / 2f);
                bucketBodyDrawable.setBounds(bodyLeft, bodyTop, bodyRight, bodyBottom);
                bucketBodyDrawable.draw(canvas);
            }
        };
        float bucketDropStart = bucketCloseWaitingStart + bucketCloseWaitingDuration;
        float bucketDropDuration = 100;
        AnimateFrame bucketDrop = new AnimateFrame("bucketDrop", bucketDropStart, bucketDropDuration) {
            private final float bodyXStart = drawableCenterX;
            private final float bodyXEnd = drawableCenterX;
            private final float bodyYStart = viewHeight - marginBottom;
            private final float bodyYEnd = viewHeight + bucketBodyDrawableHeight;

            private final float headXStart = bodyXStart;
            private final float headXEnd = bodyXEnd;
            private final float headYStart = bodyYStart - bucketSpaceBetweenHeadAndBody - bucketHeadDrawableHeight;
            private final float headYEnd = bodyYEnd - bucketSpaceBetweenHeadAndBody - bucketHeadDrawableHeight;

            @Override
            public void onDraw(Canvas canvas, float percent) {
                //head.
                int headCenterX = (int) (headXStart + (headXEnd - headXStart) * percent);
                int headCenterY = (int) (headYStart + (headYEnd - headYStart) * percent);
                int headLeft = (int) (headCenterX - bucketHeadDrawableWidth / 2f);
                int headRight = (int) (headCenterX + bucketHeadDrawableWidth / 2f);
                int headTop = (int) (headCenterY - bucketHeadDrawableHeight / 2f);
                int headBottom = (int) (headCenterY + bucketHeadDrawableHeight / 2f);
                bucketHeadDrawable.setBounds(headLeft, headTop, headRight, headBottom);
                bucketHeadDrawable.draw(canvas);

                //body.
                int bodyCenterX = (int) (bodyXStart + (bodyXEnd - bodyXStart) * percent);
                int bodyCenterY = (int) (bodyYStart + (bodyYEnd - bodyYStart) * percent);

                int bodyLeft = (int) (bodyCenterX - bucketBodyDrawableWidth / 2f);
                int bodyRight = (int) (bodyCenterX + bucketBodyDrawableWidth / 2f);
                int bodyTop = (int) (bodyCenterY - bucketBodyDrawableHeight / 2f);
                int bodyBottom = (int) (bodyCenterY + bucketBodyDrawableHeight / 2f);
                bucketBodyDrawable.setBounds(bodyLeft, bodyTop, bodyRight, bodyBottom);
                bucketBodyDrawable.draw(canvas);
            }
        };

        frames.clear();
        frames.add(micUp);
        frames.add(micDrop);
        frames.add(bucketUp);
        frames.add(bucketUpAndOpen);
        frames.add(bucketOpenWaiting);
        frames.add(bucketClose);
        frames.add(bucketCloseWaiting);
        frames.add(bucketDrop);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
//        canvas.drawColor(Color.argb(60, 0, 0, 0));
        if (!overturn && getLayoutDirection() == LAYOUT_DIRECTION_RTL) {
            overturn = true;
            matrix.preScale(-1, 1, getWidth() / 2f, getHeight() / 2f);
            canvas.concat(matrix);
        }
        for (AnimateFrame frame : frames) {
            frame.draw(canvas, timeLine);
        }
        canvas.restore();
    }

    public void debugTimeLine(float percent) {
        timeLine = percent * getWholeTimeLine();
        postInvalidate();
    }

    public float getWholeTimeLine() {
        float timeLineWhole = 0;
        for (AnimateFrame frame : frames) {
            float timeLineCurrent = frame.getStart() + frame.getDuration();
            if (timeLineCurrent > timeLineWhole) {
                timeLineWhole = timeLineCurrent;
            }
        }
        return timeLineWhole;
    }

    ValueAnimator animator;

    private void cancelAnimation() {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
    }

    public void start(Animator.AnimatorListener listener) {
        cancelAnimation();
        animator = ValueAnimator.ofFloat(getWholeTimeLine());
        animator.setDuration((long) getWholeTimeLine());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                timeLine = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.addListener(listener);
        animator.start();
    }
}
