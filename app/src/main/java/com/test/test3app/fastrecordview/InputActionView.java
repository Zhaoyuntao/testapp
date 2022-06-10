package com.test.test3app.fastrecordview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

import com.test.test3app.R;
import com.test.test3app.fastrecordviewnew.LanguageUtils;
import com.test.test3app.interpolator.ZLoopThread;
import im.turbo.basetools.utils.BitmapUtils;
import com.zhaoyuntao.androidutils.tools.S;
import com.zhaoyuntao.androidutils.tools.TextMeasure;


/**
 * created by zhaoyuntao
 * on 2019-10-23
 * description:
 * rect part of audio record view :show duration and cancel record.
 */
public class InputActionView extends View implements OprationEvent {

    private String timeString;
    private String timeStringDefault;
    private String cancelString1;
    private String cancelString2;

    private int x_rect_start, x_rect_end;
    private float x_offset_rect;

    private int w_rect, h_rect;
    private float y_text1_cancelTouch_start, y_text2_cancelTouch_start, y_bitmap_cancelTouch_start;
    private float y_text1_cancelTouch_end, y_text2_cancelTouch_end, y_bitmap_cancelTouch_end;
    private int w_area_cancel_back, h_area_cancel_back;
    private int w_cancelTouch_icon, h_bitmap_cancelTouch;

    private int w_during, h_during;

    //start when start record
    private ValueAnimator animator_circle;
    //start when AudioRecordView locked
    private ValueAnimator animator_lock;
    //start when view disappear
    private ValueAnimator animator_dispear;
    //start when view appear
    private ValueAnimator animator_appear;

    private int distance_move_x_max;
    private int distance_move_x;

    private int marginX_start_during;

    private float radius;

    private float textSize_during;
    private float textSize_cancel1;
    private float textSize_cancel2;

    private int alpha_cancelTouch;
    private int alpha_cancelTouch2;
    private int alpha_circle;
    private int alpha_all;

    private long time_startTimer;

    private ZLoopThread loopThread;
    private float w_text_during, h_text_during;
    private float w_text1_cancel, h_text1_cancel;
    private float w_text2_cancel, h_text2_cancel;

    private int color_circle;
    private int color_back;
    private int color_back_during;
    private int color_text_during;
    private int color_text_cancel1;
    private int color_text_cancel2;
    private int color_back_cancel;
    private boolean lockedRecord;

    private Paint paint;
    private PaintFlagsDrawFilter paintFlagsDrawFilter;

    private Bitmap bitmap_cancelTouch;

    private boolean isRecording;
    private String duration_formation;

    private boolean autoUpdateTime;

    //press time
    private long time_down;

    private int margin_circle_cancelTouch;
    private int margin_icon_cancelTouch;

    private int y_offset_slide_cancel_bitmap;
    private int y_offset_slide_cancel;
    private int y_offset_cancel;

    private Rect rect = new Rect();
    private Rect rectCancelTouch = new Rect();
    private Rect rect_cancel_icon_src = new Rect();
    private Rect rect_cancel_icon_des = new Rect();
    private Rect rectTime = new Rect();
    private int w_tail;

    public InputActionView(Context context) {
        super(context);
        init();
    }

    public InputActionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InputActionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int left_Pause = (int) (x_rect_start + x_offset_rect + w_tail / 2f + (w_rect - w_area_cancel_back) / 2f + (w_area_cancel_back - w_text2_cancel) / 2f);
        int right_Pause = (int) (left_Pause + w_text2_cancel);
        int top_Pause = 0;
        int bottom_Pause = top_Pause + h_rect;
        if (lockedRecord) {
            long time_now = System.currentTimeMillis();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    time_down = time_now;
                    break;
                case MotionEvent.ACTION_UP:
                    long during = time_now - time_down;
                    if (during <= 200) {
                        if (x >= left_Pause && x <= right_Pause && y >= top_Pause && y <= bottom_Pause) {
                            cancelRecord();
                        }
                    }
                    break;
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void init() {
        setWillNotDraw(false);

        timeStringDefault = getContext().getResources().getString(R.string.string_fastrecord_duration_default);
        timeString = timeStringDefault;
        cancelString1 = getContext().getResources().getString(R.string.string_fastrecord_cancel1);
        cancelString2 = getContext().getResources().getString(R.string.string_fastrecord_cancel2);
        duration_formation = getContext().getResources().getString(R.string.string_fastrecord_duration_formation);

        alpha_cancelTouch = 255;
        alpha_all = 255;
        alpha_circle = 255;

        distance_move_x_max = BitmapUtils.dip2px(getContext(), 80);
        h_rect = BitmapUtils.dip2px(getContext(), 48);

        //margin of during
        marginX_start_during = BitmapUtils.dip2px(getContext(), 40);

        //size of touch cancel
        w_area_cancel_back = BitmapUtils.dip2px(getContext(), 130);
        h_area_cancel_back = BitmapUtils.dip2px(getContext(), 40);

        radius = BitmapUtils.dip2px(getContext(), 5);

        textSize_during = BitmapUtils.sp2px(getContext(), 15);
        textSize_cancel1 = BitmapUtils.sp2px(getContext(), 13);
        textSize_cancel2 = BitmapUtils.sp2px(getContext(), 16);

        float[] textWH_during = TextMeasure.measure(timeString, textSize_during);

        w_text_during = textWH_during[0];
        h_text_during = textWH_during[1];

        w_during = (int) w_text_during;
        h_during = BitmapUtils.dip2px(getContext(), 40);

        float[] textWH_cancelString1 = TextMeasure.measure(cancelString1, textSize_cancel1);

        w_text1_cancel = textWH_cancelString1[0];
        h_text1_cancel = textWH_cancelString1[1];

        float[] textWH_cancelString2 = TextMeasure.measure(cancelString2, textSize_cancel2);

        w_text2_cancel = textWH_cancelString2[0];
        h_text2_cancel = textWH_cancelString2[1];

        color_circle = Color.RED;
        color_back = Color.WHITE;
        color_back_during = Color.WHITE;
        color_text_during = Color.argb(100, 0, 0, 0);

        color_back_cancel = Color.WHITE;
        color_text_cancel1 = getContext().getResources().getColor(R.color.yc_color_808080_CMG);
        color_text_cancel2 = getContext().getResources().getColor(R.color.yc_color_4378F2_CPN);

        //paint and canvas
        paint = new Paint();
        paint.setAntiAlias(true);
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        bitmap_cancelTouch = BitmapUtils.getBitmapById(getContext(), R.drawable.ic_arrow_left);

        h_bitmap_cancelTouch = (int) (h_area_cancel_back * 0.5f);

        int w_bitmap = bitmap_cancelTouch.getWidth();
        int h_bitmap = bitmap_cancelTouch.getHeight();

        float bitmapRatioOfCancel = (float) w_bitmap / h_bitmap;
        w_cancelTouch_icon = (int) (h_bitmap_cancelTouch * bitmapRatioOfCancel);

        margin_circle_cancelTouch = BitmapUtils.dip2px(getContext(), 36);
        margin_icon_cancelTouch = BitmapUtils.dip2px(getContext(), 5);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w_max = MeasureSpec.getSize(widthMeasureSpec);
        int h_max = MeasureSpec.getSize(heightMeasureSpec);

        h_rect = h_max;
        w_tail = (int) (h_rect * 0.8f);
        w_rect = w_max - w_tail;

        x_rect_start = w_rect;
        x_rect_end = 0;

        int y_area_cancel_back = (h_rect - h_area_cancel_back) / 2;

        y_text1_cancelTouch_start = y_area_cancel_back + (h_area_cancel_back - h_text1_cancel) / 2f;
        y_bitmap_cancelTouch_start = y_area_cancel_back + (h_area_cancel_back - h_bitmap_cancelTouch) / 2f;
        y_text2_cancelTouch_start = y_area_cancel_back;

        y_text1_cancelTouch_end = h_max;
        y_bitmap_cancelTouch_end = y_text1_cancelTouch_end;
        y_text2_cancelTouch_end = y_area_cancel_back + (h_area_cancel_back - h_text2_cancel) / 2;

        setMeasuredDimension(w_rect, h_max);
    }

    private synchronized void startLockAnim() {
        if (!isRecording) {
            return;
        }
        if (animator_lock == null) {
            animator_lock = ValueAnimator.ofFloat(1000, 0);
            animator_lock.setDuration(400);
            animator_lock.setInterpolator(new AccelerateInterpolator());
            animator_lock.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float distance = (float) animation.getAnimatedValue();
                    float percent = distance / 1000;
                    if (percent < 0.1) {
                        percent = 0;
                    } else if (percent > 0.9) {
                        percent = 1;
                    }
                    if (isRecording) {
                        y_offset_slide_cancel_bitmap = (int) ((y_bitmap_cancelTouch_end - y_bitmap_cancelTouch_start) * (1 - percent));
                        y_offset_slide_cancel = (int) ((y_text1_cancelTouch_end - y_text1_cancelTouch_start) * (1 - percent));
                        y_offset_cancel = (int) ((y_text2_cancelTouch_end - y_text2_cancelTouch_start) * (1 - percent));
                        alpha_cancelTouch = (int) (255 * percent);
                        alpha_cancelTouch2 = (int) (255 * (1 - percent));
                    } else {
                        alpha_cancelTouch = 0;
                        alpha_cancelTouch2 = 0;
                    }
                    postInvalidate();
                }
            });

        }
        if (!animator_lock.isRunning()) {
            animator_lock.start();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(paintFlagsDrawFilter);
        //parent position
        int x_rect = (int) (x_rect_start + x_offset_rect);
        int y_rect = 0;
        //-------------- background
        paint.setColor(color_back);
        paint.setAlpha(alpha_all);

        int left = x_rect;
        int top = y_rect;
        int right = left + w_rect;
        int bottom = top + h_rect;
        rect.set(left, top, right, bottom);
        canvas.drawRect(rect, paint);

        float y_text1_cancelTouch = y_text1_cancelTouch_start + y_offset_slide_cancel;
        float y_bitmap_cancelTouch = y_bitmap_cancelTouch_start + y_offset_slide_cancel_bitmap;

        //-------------- cancel touch

        paint.setColor(color_back_cancel);
        int left_cancel = x_rect + (w_rect - w_area_cancel_back) / 2;
        int top_cancel = y_rect + (h_rect - h_area_cancel_back) / 2;
        int right_cancel = left_cancel + w_area_cancel_back;
        int bottom_cancel = top_cancel + h_area_cancel_back;
        rectCancelTouch.set(left_cancel, top_cancel, right_cancel, bottom_cancel);
        paint.setAlpha(alpha_cancelTouch);
        canvas.drawRect(rectCancelTouch, paint);
        paint.setAlpha(alpha_all);
        //------------- debug:test click area
//        if (S.DEBUG) {
//            Rect rectDebug = new Rect();
//            int left_Pause = x_cancelTouch;
//            int right_Pause = x_cancelTouch + w_cancelTouch;
//            int top_Pause = y_cancelTouch;
//            int bottom_Pause = y_cancelTouch + h_cancelTouch;
//            rectDebug.set(left_Pause, top_Pause, right_Pause, bottom_Pause);
//            paint.setColor(Color.RED);
//            canvas.drawRect(rectDebug, paint);
//        }
        //-------------- cancel text2: CANCEL
        String cancelString2 = this.cancelString2;
        if (!TextUtils.isEmpty(cancelString2)) {
            paint.setTextSize(textSize_cancel2);
            paint.setColor(color_text_cancel2);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAlpha(alpha_cancelTouch2);
            float x_text2_cancelTouch = left_cancel + w_tail / 2f + (w_area_cancel_back - w_text2_cancel) / 2f;
            canvas.drawText(cancelString2, x_text2_cancelTouch, y_text2_cancelTouch_start + y_offset_cancel + h_text2_cancel, paint);
        }
        //-------------- cancel icon: arrow
        paint.setAlpha(alpha_cancelTouch);

        int left_cancel_icon_src = 0;
        int top_cancel_icon_src = 0;
        int right_cancel_icon_src = bitmap_cancelTouch.getWidth();
        int bottom_cancel_icon_src = bitmap_cancelTouch.getHeight();
        rect_cancel_icon_src.set(left_cancel_icon_src, top_cancel_icon_src, right_cancel_icon_src, bottom_cancel_icon_src);

        int left_cancel_icon = (int) (x_rect + w_rect - w_text1_cancel - margin_circle_cancelTouch - w_cancelTouch_icon - margin_icon_cancelTouch + distance_move_x);
        int top_cancel_icon = (int) (y_bitmap_cancelTouch);
        int right_cancel_icon = left_cancel_icon + w_cancelTouch_icon;
        int bottom_cancel_icon = top_cancel_icon + h_bitmap_cancelTouch;
        rect_cancel_icon_des.set(left_cancel_icon, top_cancel_icon, right_cancel_icon, bottom_cancel_icon);
        canvas.drawBitmap(bitmap_cancelTouch, rect_cancel_icon_src, rect_cancel_icon_des, paint);
//        if (S.DEBUG) {
//            //debug rect
//            paint.setColor(Color.RED);
//            paint.setAlpha(alpha_cancelTouch);
//            paint.setStyle(Paint.Style.STROKE);
//            canvas.drawRect(rect_cancel_icon_des, paint);
//            paint.setStyle(Paint.Style.FILL);
//        }
        //-------------- cancel text: SLIDE TO CANCEL
        String cancelString1 = this.cancelString1;
        if (!TextUtils.isEmpty(cancelString1)) {
            paint.setTextSize(textSize_cancel1);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setColor(color_text_cancel1);
            paint.setAlpha(alpha_cancelTouch);
            float x_text1_cancelTouch = x_rect + w_rect - w_text1_cancel - margin_circle_cancelTouch + distance_move_x;
            S.s("y:" + (y_text1_cancelTouch + h_text1_cancel) + "    al:" + alpha_cancelTouch);
            canvas.drawText(cancelString1, x_text1_cancelTouch, y_text1_cancelTouch + h_text1_cancel, paint);
        }

        //-------------- during circle
        int x_circle = (int) (x_rect + (marginX_start_during - radius * 2) / 2 + radius);
        int y_circle = (int) (y_rect + (h_rect - radius * 2) / 2 + radius);
        paint.setColor(color_circle);
        paint.setAlpha(alpha_circle);
        canvas.drawCircle(x_circle, y_circle, radius, paint);
        paint.setAlpha(alpha_all);

        //-------------- during time background
        int x_during = x_rect + marginX_start_during;
        int y_during = y_rect + (h_rect - h_during) / 2;
        paint.setColor(color_back_during);
        paint.setAlpha(alpha_all);

        int left_during = x_during;
        int top_during = y_during;
        int right_during = left_during + w_during;
        int bottom_during = top_during + h_during;
        rectTime.set(left_during, top_during, right_during, bottom_during);
        canvas.drawRect(rectTime, paint);

        //-------------- during time
        String during = timeString;
        if (!TextUtils.isEmpty(during)) {
            paint.setTextSize(textSize_during);
            paint.setColor(color_text_during);
            paint.setTypeface(Typeface.DEFAULT);
            paint.setAlpha(alpha_all);
            float x_text = x_during + ((rectTime.right - rectTime.left) - w_text_during) / 2;
            float y_text = y_during + ((rectTime.bottom - rectTime.top) - h_text_during) / 2 + h_text_during;
            canvas.drawText(during, x_text, y_text, paint);
        }
//        if (S.DEBUG) {
//            //-------------- debug line
//            paint.setColor(Color.BLACK);
//            paint.setAlpha(255);
//            paint.setStrokeWidth(1);
//            float x_line = w_rect - distance_move_x_max;
//            canvas.drawLine(x_line, 0, x_line, canvas.getHeight(), paint);
//        }
    }

    private void startAppearAnim() {
        stopDispearAnim();
        if (animator_appear == null) {
            animator_appear = ValueAnimator.ofFloat(0, 1000);
            animator_appear.setDuration(200);
            animator_appear.setInterpolator(new DecelerateInterpolator());
            animator_appear.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float distance = (float) animation.getAnimatedValue();
                    float percent = distance / 1000;
                    if (percent < 0.1) {
                        percent = 0;
                    } else if (percent > 0.9) {
                        percent = 1;
                    }
                    x_offset_rect = (x_rect_end - x_rect_start) * percent;
                    y_offset_slide_cancel = 0;
                    y_offset_slide_cancel_bitmap = 0;
                    y_offset_cancel = 0;
                    postInvalidate();
                }
            });
        }
        if (!animator_appear.isRunning()) {
            animator_appear.start();
        }
    }

    private void stopAppearAnim() {
        if (animator_appear != null && animator_appear.isRunning()) {
            animator_appear.cancel();
            animator_appear.end();
        }
    }

    public void startTimer() {
        if (autoUpdateTime) {
            closeTimer();
            loopThread = new ZLoopThread(100) {
                @Override
                protected void init() {
                    time_startTimer = System.currentTimeMillis();
                }

                @Override
                protected void todo() {
                    long now = System.currentTimeMillis();
                    long duration = now - time_startTimer;

                    duration = duration >= 0 ? duration : 0;
                    long mills = duration % 1000 / 10;
                    long seconds = duration / 1000 % 60;
                    long minutes = duration / 1000 / 60;

                    String millString = String.format(LanguageUtils.getApplyLocale(), "%02d", mills);
                    String secondString = String.format(LanguageUtils.getApplyLocale(), "%02d", seconds);
                    String minuteString = String.format(LanguageUtils.getApplyLocale(), "%02d", minutes);

                    setTimeString(String.format(LanguageUtils.getApplyLocale(), duration_formation, minuteString, secondString, millString));

                    postInvalidate();
                }
            };
            loopThread.start();
        }
    }

    public void restartTimer() {
        startTimer();
    }

    private void closeTimer() {
        if (loopThread != null) {
            loopThread.close();
        }
    }

    private void startCircleAnim() {
//        startTimer();
        if (animator_circle == null) {
            animator_circle = ValueAnimator.ofFloat(0, 1000);
            animator_circle.setDuration(500);
            animator_circle.setRepeatCount(ValueAnimator.INFINITE);
            animator_circle.setRepeatMode(ValueAnimator.REVERSE);
            animator_circle.setInterpolator(new BounceInterpolator());
            animator_circle.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float distance = (float) animation.getAnimatedValue();
                    float percent = distance / 1000;
                    alpha_circle = (int) (255 * percent);
                    postInvalidate();
                }
            });

        }
        if (!animator_circle.isRunning()) {
            animator_circle.start();
        }
    }

    public void updateTime(long duration) {
        if (autoUpdateTime) {
            return;
        }
        long mills = duration % 1000 / 10;
        long seconds = duration / 1000 % 60;
        long minutes = duration / 1000 / 60;
        String millString = String.format(LanguageUtils.getApplyLocale(), "%02d", mills);
        String secondString = String.format(LanguageUtils.getApplyLocale(), "%02d", seconds);
        String minuteString = String.format(LanguageUtils.getApplyLocale(), "%02d", minutes);
        setTimeString(String.format(LanguageUtils.getApplyLocale(), duration_formation, minuteString, secondString, millString));
    }

    private void stopCircleAnim() {
        if (animator_circle != null && animator_circle.isRunning()) {
            animator_circle.cancel();
            animator_circle.end();
        }
        closeTimer();
    }

    private void startDisappearAnim() {
        if (!isRecording) {
            resetAllState();
            return;
        }
        isRecording = false;
        stopCircleAnim();
        stopAppearAnim();
        if (animator_dispear == null) {
            animator_dispear = ValueAnimator.ofFloat(1000, 0);
            animator_dispear.setDuration(200);
            animator_dispear.setInterpolator(new AccelerateInterpolator());
            animator_dispear.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float distance = (float) animation.getAnimatedValue();
                    float percent = distance / 1000;
                    if (percent < 0.1) {
                        percent = 0;
                    } else if (percent > 0.9) {
                        percent = 1;
                    }
                    x_offset_rect = (x_rect_end - x_rect_start) * percent;
                    alpha_all = (int) (255 * percent);
                    alpha_circle = (int) (255 * percent);
                    alpha_cancelTouch = 0;
                    postInvalidate();
                }
            });
            animator_dispear.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    resetAllState();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        if (!animator_dispear.isRunning()) {
            animator_dispear.start();
        }
    }

    private void resetAllState() {
        timeString = timeStringDefault;
        postInvalidate();
    }

    private void stopDispearAnim() {
        if (animator_dispear != null && animator_dispear.isRunning()) {
            animator_dispear.cancel();
            animator_dispear.end();
        }
    }


    private void stopLockAnim() {
        if (animator_lock != null && animator_lock.isRunning()) {
            animator_lock.cancel();
            animator_lock.end();
        }
    }

    private void cancelRecord() {
        lockedRecord = false;
        distance_move_x = 0;
        startDisappearAnim();
        if (userOperations != null) {
            userOperations.whenCanceled();
        }
    }

    private void stopRecord(boolean callBack) {
        lockedRecord = false;
        distance_move_x = 0;
        startDisappearAnim();
        if (callBack && userOperations != null) {
            userOperations.whenStopRecord();
        }
    }

    private void appear() {
        isRecording = true;
        distance_move_x = 0;
        alpha_all = 255;
        alpha_cancelTouch = 255;
        alpha_cancelTouch2 = 0;
        startAppearAnim();
        startCircleAnim();
    }

    private void lockRecord() {
        lockedRecord = true;
        startLockAnim();
    }

    @Override
    public void destroyDrawingCache() {
        stopDispearAnim();
        stopAppearAnim();
        stopLockAnim();
        stopCircleAnim();
        super.destroyDrawingCache();
    }

    @Override
    public void whenAppear() {
        appear();
        postInvalidate();
    }


    @Override
    public void whenLockRecord() {
        lockRecord();
    }

    @Override
    public void moveLeft(float distance_move) {
        if (Math.abs(distance_move) < distance_move_x_max) {
            distance_move_x = (int) distance_move;
            //cancel touch position
            float percent = 1 - Math.abs(distance_move) / distance_move_x_max;
            alpha_cancelTouch = (int) (255 * percent);
        } else {
            cancelRecord();
        }

        postInvalidate();
    }

    @Override
    public void whenStopRecord() {
        stopRecord(false);
        postInvalidate();
    }

    private UserOperations userOperations;

    protected void setUserOperations(UserOperations userOperations) {
        this.userOperations = userOperations;
    }

    private void setTimeString(String timeString) {
        this.timeString = timeString;
        if (userOperations != null) {
            userOperations.whenTimeUpdate(timeString);
        }
        postInvalidate();
    }

    public void setAutoUpdateTime(boolean autoUpdateTime) {
        this.autoUpdateTime = autoUpdateTime;
    }

}
