package com.test.test3app.fastrecordviewnew;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

import com.test.test3app.R;
import com.test.test3app.interpolator.BounceInterpolator;
import com.test.test3app.interpolator.ZLoopThread;
import im.turbo.basetools.utils.BitmapUtils;
import im.turbo.basetools.vibrate.VibratorUtil;
import com.zhaoyuntao.androidutils.tools.S;
import com.zhaoyuntao.androidutils.tools.TextMeasure;

/**
 * created by zhaoyuntao
 * on 2019-10-23
 * description:
 * the main part of audio record view, include a clickable circle and a lock area.
 */
public class AudioRecordViewOld extends View implements TouchEvent {


    //------- circle part
    private float x_circle, y_circle;
    private float w_bitmap_circle, h_bitmap_circle;
    private float w_hide, h_hide;
    //first appear position of circle
    private float x_start_circle, y_start_circle;
    private float radius_circle_max;
    private float radius;

    //------- lock part
    private int x_lock, y_lock;
    private int w_lock, h_lock;
    private int h_lock_max;
    private int h_lock_min;
    //appear position
    private int y_start_lock;
    //appear animator ended position
    private int y_end_lock;
    //size of pause part
    private int w_lock_pauseRect, h_lock_pauseRect;
    //radius of round rect of pause part
    private float radius_pauseRect;

    //start when finger up before over max distance
    private ValueAnimator animator_disappear;
    //start when finger move to over max distance
    private ValueAnimator animator_lock;
    //start when finger long click ChangeIconButton
    private ValueAnimator animator_appear;
    //start when close waitsend
    private ValueAnimator animator_disappear_waitsend;
    private ValueAnimator animator_appear_waitsend;

    //alphaForAll for all part.
    private int alphaForAll;
    //alphaForAll only for lock part,it's different from alphaForAll for all,because lock is disappear earlier than circle.
    private int alpha_lock;

    //finger move distance
    private float distance_move_y;
    //max distance for finger move
    private float distance_move_y_max;
    //if over the max distance, lock the record operation.
    private boolean lockedRecord;

    //for calculate distance of finger moving
    private float x_press, y_press;

    //callback
    private CallBack callBack;
    //canceledByHand by InputActionView
    private boolean canceledByHand;

    private Paint paint;
    private PaintFlagsDrawFilter paintFlagsDrawFilter;

    private int color_circle;
    private int color_back_lock;
    private int color_border_lock;
    private int color_pauseRect_lock;

    //lock part before locked
    private Bitmap bitmap_lock_part1;
    private Bitmap bitmap_lock_part2;
    private Bitmap bitmap_lock_part3;
    //bitmap in circle
    private Bitmap bitmap_center_circle_mic;
    private Bitmap bitmap_center_circle_send;
    private int w_lock_part1, h_lock_part1;
    private int w_lock_part2, h_lock_part2;
    private int w_lock_part3, h_lock_part3;
    private int margin_bottom_lock_part3;

    //margin between circle and lock;
    private float margin_c_l_vertical;

    private boolean isRecording;
    private int color_circle_normal;
    private int color_circle_click;
    private int color_hide;

    //wait send part
    private int x_rect_waitSend, y_rect_waitSend;
    private int w_rect_waitSend, h_rect_waitSend;
    private int color_waitSend_back;
    private boolean waitSend;
    private Bitmap bitmap_waitsend_delete, bitmap_waitsend_send;
    private float textSizeOfDuration;
    private int color_duration;
    private int left_delete, top_delete, right_delete, bottom_delete;
    private int left_send, top_send, right_send, bottom_send;
    private String textOfDuration;
    private int h_view;
    private boolean showWaitSend;

    public AudioRecordViewOld(Context context) {
        super(context);
        init();
        init2();
    }

    public AudioRecordViewOld(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        init2();
    }

    public AudioRecordViewOld(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        init2();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (lockedRecord) {
            S.s("lock");
            int left_Pause = x_lock;
            int right_Pause = x_lock + w_lock;
            int top_Pause = y_lock;
            int bottom_Pause = y_lock + h_lock;
            if (x >= left_Pause && x <= right_Pause && y >= top_Pause && y <= bottom_Pause) {
                S.s("stop");
                stopRecord(true, false);
            } else if (Math.pow(x - x_circle, 2) + Math.pow(y - y_circle, 2) < Math.pow(radius, 2)) {
                S.s("stop2");
                stopRecord(true, true);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        color_circle = color_circle_click;
                        postInvalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        color_circle = color_circle_normal;
                        postInvalidate();
                        break;
                }
            }

            int left_Pause2 = x_cancelTouch;
            int right_Pause2 = x_cancelTouch + w_cancelTouch;
            int top_Pause2 = y_cancelTouch;
            int bottom_Pause2 = y_cancelTouch + h_cancelTouch;
            long time_now = System.currentTimeMillis();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    time_down = time_now;
                    break;
                case MotionEvent.ACTION_UP:
                    long during = time_now - time_down;
                    if (during <= 200) {
                        if (x >= left_Pause2 && x <= right_Pause2 && y >= top_Pause2 && y <= bottom_Pause2) {
                            cancelRecord();
                        }
                    }
                    break;
            }
            return true;
        } else if (waitSend) {
            S.s("wait send");
            int left = x_rect_waitSend;
            int right = left + w_rect_waitSend;
            int top = y_rect_waitSend;
            int bottom = top + h_rect_waitSend;
            //position in wait send rect
            if (x >= left && x <= right && y >= top && y <= bottom) {
                if (x >= left_delete && x <= right_delete && y >= top_delete && y <= bottom_delete) {
                    S.s("click delete");
                    startWaitSendDisappearAnim();
                    abandonedVoice();
                } else if (x >= left_send && x <= right_send && y >= top_send && y <= bottom_send) {
                    S.s("click send");
                    startWaitSendDisappearAnim();
                    sendVoice();
                }
                return true;
            }
        }

        //------------

        return super.onTouchEvent(event);
    }

    public void showSendAndDeleteBar() {
        waitSend = true;
        startWaitSendAppearAnim();
    }

    private void abandonedVoice() {
        waitSend = false;
        if (callBack != null) {
            callBack.whenAbandonedVoice();
        }
    }

    private void sendVoice() {
        waitSend = false;
        if (callBack != null) {
            callBack.whenSendClick();
        }
    }

    private void init() {
        //max move distance in vertical
        distance_move_y_max = BitmapUtils.dip2px(getContext(), 50);
        radius_circle_max = BitmapUtils.dip2px(getContext(), 40);

        w_bitmap_circle = BitmapUtils.dip2px(getContext(), 30);
        h_bitmap_circle = BitmapUtils.dip2px(getContext(), 30);
        w_hide = BitmapUtils.dip2px(getContext(), 24);
        h_hide = BitmapUtils.dip2px(getContext(), 24);

        w_lock = BitmapUtils.dip2px(getContext(), 28);
        h_lock_max = (int) (w_lock * 2.2f);
        h_lock_min = w_lock;

        w_lock_part1 = (int) (w_lock * 0.7f);
        h_lock_part1 = (int) (w_lock * 0.7f);

        w_lock_part2 = (int) (w_lock * 0.7f);
        h_lock_part2 = (int) (w_lock * 0.7f);


        margin_c_l_vertical = w_lock * 0.8f;
        w_lock_pauseRect = (int) (w_lock * 0.4f);
        h_lock_pauseRect = w_lock_pauseRect;
        radius_pauseRect = BitmapUtils.dip2px(getContext(), 1);
        h_lock = h_lock_max;

        //colors
        color_back_lock = Color.WHITE;
        color_border_lock = Color.rgb(244, 244, 244);
        color_circle_normal = getContext().getResources().getColor(R.color.yc_color_4378F2_CPN);
        color_circle_click = getContext().getResources().getColor(R.color.yc_color_275edb_CPP);
        color_hide = color_back_lock;
        color_circle = color_circle_normal;
        color_pauseRect_lock = Color.RED;

        //paint and canvas
        paint = new Paint();
        paint.setAntiAlias(true);
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        bitmap_lock_part1 = BitmapUtils.getBitmapById(getContext(), R.drawable.ic_lock_top);
        bitmap_lock_part2 = BitmapUtils.getBitmapById(getContext(), R.drawable.ic_lock_btm);
        bitmap_lock_part3 = BitmapUtils.getBitmapById(getContext(), R.drawable.ic_arrow_up);
        bitmap_center_circle_mic = BitmapUtils.getBitmapById(getContext(), R.drawable.ic_mic);
        bitmap_center_circle_send = BitmapUtils.getBitmapById(getContext(), R.drawable.ic_send);

        w_lock_part3 = (int) (w_lock * 0.5f);
        h_lock_part3 = (int) (w_lock_part3 / ((float) bitmap_lock_part3.getWidth() / bitmap_lock_part3.getHeight()));
        margin_bottom_lock_part3 = BitmapUtils.dip2px(getContext(), 5);

        //wait send
        h_rect_waitSend = BitmapUtils.dip2px(getContext(), 48);
        color_waitSend_back = Color.WHITE;
        bitmap_waitsend_delete = BitmapUtils.getBitmapById(getContext(), R.drawable.ic_messages_delete);
        bitmap_waitsend_send = BitmapUtils.getBitmapById(getContext(), R.drawable.ic_send);
        textSizeOfDuration = BitmapUtils.sp2px(getContext(), 15);
        color_duration = Color.BLACK;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w_max = MeasureSpec.getSize(widthMeasureSpec);
        int h_max = MeasureSpec.getSize(heightMeasureSpec);
        h_view = h_max;
        x_start_circle = (float) (w_max - BitmapUtils.dip2px(getContext(), 24));
        y_start_circle = (float) (h_max - BitmapUtils.dip2px(getContext(), 24));

        x_circle = x_start_circle;
        y_circle = y_start_circle;

        x_lock = (int) (x_circle - w_lock / 2);
        y_start_lock = (int) (y_start_circle);

        y_end_lock = (int) (y_start_circle - radius_circle_max - h_lock_max - margin_c_l_vertical);

        if (lockedRecord) {
            y_lock = (int) (y_start_circle - radius_circle_max - h_lock - margin_c_l_vertical);
        } else {
            y_lock = y_end_lock;
        }

        w_rect_waitSend = w_max;

        x_rect_waitSend = 0;
        if (waitSend) {
            y_rect_waitSend = h_view - h_rect_waitSend;
        } else {
            y_rect_waitSend = h_view;
        }

        //---------------------------------------------
        int padding_start = getPaddingLeft();

        w_rect_back = w_max - w_tail;

        x_start = w_max - w_tail;
        int y_start = h_max - h_rect_back;

        x_rect = x_start;
        y_rect = y_start;

        x_start_cancelTouch = padding_start + (w_max - w_cancelTouch) / 2;
//        x_start_cancelTouch = padding_start + marginX_start_during + w_during + marginX_end_during;

        float y_start_cancelTouch = y_rect + (h_rect_back - h_cancelTouch) / 2;
        y_end_cancelTouch = y_start_cancelTouch + h_cancelTouch;

        y_text1_cancelTouch_start = y_cancelTouch + (h_cancelTouch - h_text1_cancel) / 2 + h_text1_cancel;
        y_text2_cancelTouch_start = y_cancelTouch + (h_cancelTouch - h_text2_cancel) / 2 + h_text2_cancel;
        y_bitmap_cancelTouch_start = y_cancelTouch + (h_cancelTouch - h_bitmap_cancelTouch) / 2;

        calculateAllPosition();
//        setMeasuredDimension(w_rect_back, h_max);

        setMeasuredDimension(w_max, h_max);//UiUtils.dipToPx(getContext(),48));
    }

    private void calculateAllPosition() {
        alpha_all = (int) (255 * percent_position);
        alpha_cancelTouch = (int) (255 * percent_position);
        //parent position
        x_rect = (int) (x_start - w_rect_back * percent_position);

        //during position
        x_during = x_rect + marginX_start_during;
        y_during = y_rect + (h_rect_back - h_during) / 2;

        //cancel touch position
//        x_cancelTouch = x_during + w_during + marginX_end_during + distance_move_x;
        x_cancelTouch = (int) (x_rect + x_start_cancelTouch + distance_move_x);
        y_cancelTouch = y_rect + (h_rect_back - h_cancelTouch) / 2;

        x_redPoint = (int) (x_rect + (marginX_start_during - radius_redPoint * 2) / 2 + radius_redPoint);
        y_redPoint = (int) (y_rect + (h_rect_back - radius_redPoint * 2) / 2 + radius_redPoint);

        y_text1_cancelTouch = y_cancelTouch + (h_cancelTouch - h_text1_cancel) / 2 + h_text1_cancel;
        S.s("y:"+y_text1_cancelTouch);
        y_text2_cancelTouch = y_cancelTouch + (h_cancelTouch - h_text2_cancel) / 2 + h_text2_cancel;
        y_bitmap_cancelTouch = y_cancelTouch + (h_cancelTouch - h_bitmap_cancelTouch) / 2;
    }

    private void calculateAllPosition2() {
        alpha_all = (int) (255 * percent_position);
        alpha_cancelTouch = 0;
        //parent position
        x_rect = (int) (x_start - w_rect_back * percent_position);

        //during position
        x_during = x_rect + marginX_start_during;
        y_during = y_rect + (h_rect_back - h_during) / 2;

        //cancel touch position
//        x_cancelTouch = x_during + w_during + marginX_end_during + distance_move_x;
        x_cancelTouch = (int) (x_rect + x_start_cancelTouch + distance_move_x);
        y_cancelTouch = y_rect + (h_rect_back - h_cancelTouch) / 2;

        x_redPoint = (int) (x_rect + (marginX_start_during - radius_redPoint * 2) / 2 + radius_redPoint);
        y_redPoint = (int) (y_rect + (h_rect_back - radius_redPoint * 2) / 2 + radius_redPoint);

        y_text1_cancelTouch = y_cancelTouch + (h_cancelTouch - h_text1_cancel) / 2 + h_text1_cancel;
        S.s("y:"+y_text1_cancelTouch);
        y_text2_cancelTouch = y_cancelTouch + (h_cancelTouch - h_text2_cancel) / 2 + h_text2_cancel;
        y_bitmap_cancelTouch = y_cancelTouch + (h_cancelTouch - h_bitmap_cancelTouch) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(paintFlagsDrawFilter);

        //----------
        //-------------- background
        paint.setColor(color_back);
        paint.setAlpha(alpha_all);
        Rect rect = new Rect();
        int left_rect = (int) x_rect;
        int top_rect = y_rect;
        int right_rect = left_rect + w_rect_back;
        int bottom_rect = top_rect + h_rect_back;
        rect.set(left_rect, top_rect, right_rect, bottom_rect);
        canvas.drawRect(rect, paint);

        //-------------- cancel touch
        Rect rectCancelTouch = new Rect();
        paint.setColor(color_back_cancel);
        int left_cancel = x_cancelTouch;
        int top_cancel = y_cancelTouch;
        int right_cancel = left_cancel + w_cancelTouch;
        int bottom_cancel = top_cancel + h_cancelTouch;
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
            float x_text2_cancelTouch = x_cancelTouch + (w_cancelTouch - w_text2_cancel) / 2;
            canvas.drawText(cancelString2, x_text2_cancelTouch, y_text2_cancelTouch, paint);
        }
        //-------------- cancel icon: arrow
        paint.setAlpha(alpha_cancelTouch);
        Rect rect_cancel_icon_src = new Rect();
        int left_cancel_icon_src = 0;
        int top_cancel_icon_src = 0;
        int right_cancel_icon_src = bitmap_cancelTouch.getWidth();
        int bottom_cancel_icon_src = bitmap_cancelTouch.getHeight();
        rect_cancel_icon_src.set(left_cancel_icon_src, top_cancel_icon_src, right_cancel_icon_src, bottom_cancel_icon_src);
        Rect rect_cancel_icon_des = new Rect();
        int left_cancel_icon = (int) (x_rect + w_rect_back - w_text1_cancel - margin_circle_cancelTouch - w_cancelTouch_icon - margin_icon_cancelTouch + distance_move_x);
        ;
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
            float x_text1_cancelTouch = x_rect + w_rect_back - w_text1_cancel - margin_circle_cancelTouch + distance_move_x;
            canvas.drawText(cancelString1, x_text1_cancelTouch, y_text1_cancelTouch, paint);

        }

        //-------------- during circle
        paint.setColor(color_redPoint);
        paint.setAlpha(alpha_redPoint);
        canvas.drawCircle(x_redPoint, y_redPoint, radius_redPoint, paint);
        paint.setAlpha(alpha_all);

        //-------------- during time background
        paint.setColor(color_back_during);
        paint.setAlpha(alpha_all);
        Rect rectDuring = new Rect();
        int left_during = x_during;
        int top_during = y_during;
        int right_during = left_during + w_during;
        int bottom_during = top_during + h_during;
        rectDuring.set(left_during, top_during, right_during, bottom_during);
        canvas.drawRect(rectDuring, paint);

        //-------------- during time
        String during = timeString;
        if (!TextUtils.isEmpty(during)) {
            paint.setTextSize(textSize_during);
            paint.setColor(color_text_during);
            paint.setTypeface(Typeface.DEFAULT);
            paint.setAlpha(alpha_all);
            float x_text = x_during + ((rectDuring.right - rectDuring.left) - w_text_during) / 2;
            float y_text = y_during + ((rectDuring.bottom - rectDuring.top) - h_text_during) / 2 + h_text_during;
            canvas.drawText(during, x_text, y_text, paint);
        }
//        if (S.DEBUG) {
//            //-------------- debug line
//            paint.setColor(Color.BLACK);
//            paint.setAlpha(255);
//            paint.setStrokeWidth(1);
//            float x_line = w_rect_back - distance_move_x_max;
//            canvas.drawLine(x_line, 0, x_line, canvas.getHeight(), paint);
//        }

        //---------------------------------------------------------------
        //------------ hide button in the back
        Rect rect_hide = new Rect();
        int left_hide = (int) (x_start_circle - w_hide);
        int top_hide = (int) (y_start_circle - h_hide);
        int right_hide = (int) (left_hide + w_hide * 2);
        int bottom_hide = (int) (top_hide + h_hide * 2);
        rect_hide.set(left_hide, top_hide, right_hide, bottom_hide);
        paint.setColor(color_hide);
        paint.setAlpha(alphaForAll);
        canvas.drawRect(rect_hide, paint);

        //------------- lock
        paint.setColor(color_back_lock);
        paint.setAlpha(alpha_lock);
        RectF rectFOfLock = new RectF();
        rectFOfLock.set(x_lock, y_lock, x_lock + w_lock, y_lock + h_lock);
        float radiusOfRect = w_lock / 2;
        canvas.drawRoundRect(rectFOfLock, radiusOfRect, radiusOfRect, paint);
        //------------- lock border
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setColor(color_border_lock);
        paint.setAlpha(alpha_lock);
        RectF rectFOfBorder = new RectF();
        rectFOfBorder.set(x_lock + 1, y_lock + 1, x_lock + w_lock - 2, y_lock + h_lock - 2);
        canvas.drawRoundRect(rectFOfBorder, radiusOfRect, radiusOfRect, paint);


        if (!lockedRecord) {
            //------------- lock icon part1
            paint.setAlpha(alpha_lock);
            int x_bitmap_lock_part1 = x_lock + (w_lock - w_lock_part1) / 2;
            int y_bitmap_lock_part1 = (int) (y_lock + h_lock * 0.13f);
            Rect rect_lockIcon_part1_des = new Rect();
            int left_lockIcon = x_bitmap_lock_part1;
            int top_lockIcon = y_bitmap_lock_part1;
            int right_lockIcon = x_bitmap_lock_part1 + w_lock_part1;
            int bottom_lockIcon = y_bitmap_lock_part1 + h_lock_part1;
            rect_lockIcon_part1_des.set(left_lockIcon, top_lockIcon, right_lockIcon, bottom_lockIcon);

            Rect rect_lockIcon_src = new Rect();
            rect_lockIcon_src.set(0, 0, bitmap_lock_part1.getWidth(), bitmap_lock_part1.getHeight());
            canvas.drawBitmap(bitmap_lock_part1, rect_lockIcon_src, rect_lockIcon_part1_des, paint);

//            if (S.DEBUG) {
//                // debug rect 1
//                paint.setStyle(Paint.Style.STROKE);
//                paint.setColor(Color.RED);
//                paint.setAlpha(alpha_lock);
//                paint.setStrokeWidth(1);
//                canvas.drawRect(rect_lockIcon_part1_des, paint);
//                paint.setStyle(Paint.Style.FILL);
//            }


            //------------- lock icon part2
            int x_bitmap_lock_part2 = x_lock + (w_lock - w_lock_part2) / 2;
            float percent = 1 - ((float) h_lock_max - h_lock) / (h_lock_max - h_lock_min);
            int y_bitmap_lock_part2 = (int) (y_bitmap_lock_part1 + (h_lock_part1 / 2f * percent) + h_lock_part1 / 2f);
            Rect rect_lockIcon_part2_des = new Rect();
            int left_lockIcon_part2 = x_bitmap_lock_part2;
            int top_lockIcon_part2 = y_bitmap_lock_part2;
            int right_lockIcon_part2 = x_bitmap_lock_part2 + w_lock_part2;
            int bottom_lockIcon_part2 = y_bitmap_lock_part2 + h_lock_part2;
            rect_lockIcon_part2_des.set(left_lockIcon_part2, top_lockIcon_part2, right_lockIcon_part2, bottom_lockIcon_part2);

            Rect rect_lockIcon_part2_src = new Rect();
            rect_lockIcon_part2_src.set(0, 0, bitmap_lock_part2.getWidth(), bitmap_lock_part2.getHeight());
            canvas.drawBitmap(bitmap_lock_part2, rect_lockIcon_part2_src, rect_lockIcon_part2_des, paint);
//            if (S.DEBUG) {
//                // debug rect 2
//                paint.setStyle(Paint.Style.STROKE);
//                paint.setColor(Color.RED);
//                paint.setAlpha(alpha_lock);
//                paint.setStrokeWidth(1);
//                canvas.drawRect(rect_lockIcon_part2_des, paint);
//                paint.setStyle(Paint.Style.FILL);
//            }

            //------------- lock icon part3
            int x_bitmap_lock_part3 = x_lock + (w_lock - w_lock_part3) / 2;
            float percent_alpha = alpha_lock == 0 ? 0 : (1 - ((float) h_lock_max - h_lock) / (h_lock_max - h_lock_min) * 2);
            if (percent_alpha < 0) {
                percent_alpha = 0;
            }
            int alpha_part3 = (int) (255 * percent_alpha);
            int y_bitmap_lock_part3 = y_lock + h_lock - h_lock_part3 - margin_bottom_lock_part3;
            Rect rect_lockIcon_part3_des = new Rect();
            int left_lockIcon_part3 = x_bitmap_lock_part3;
            int top_lockIcon_part3 = y_bitmap_lock_part3;
            int right_lockIcon_part3 = x_bitmap_lock_part3 + w_lock_part3;
            int bottom_lockIcon_part3 = y_bitmap_lock_part3 + h_lock_part3;
            rect_lockIcon_part3_des.set(left_lockIcon_part3, top_lockIcon_part3, right_lockIcon_part3, bottom_lockIcon_part3);

            paint.setAlpha(alpha_part3);
            Rect rect_lockIcon_part3_src = new Rect();
            rect_lockIcon_part3_src.set(0, 0, bitmap_lock_part3.getWidth(), bitmap_lock_part3.getHeight());
            canvas.drawBitmap(bitmap_lock_part3, rect_lockIcon_part3_src, rect_lockIcon_part3_des, paint);
//            if (S.DEBUG) {
//                // debug rect 3
//                paint.setStyle(Paint.Style.STROKE);
//                paint.setColor(Color.RED);
//                paint.setAlpha(alpha_part3);
//                paint.setStrokeWidth(1);
//                canvas.drawRect(rect_lockIcon_part3_des, paint);
//                paint.setStyle(Paint.Style.FILL);
//            }

        } else {
            //------------- locked pause button
            RectF rect_pause = new RectF();
            int left_pause = x_lock + (w_lock - w_lock_pauseRect) / 2;
            int top_pause = y_lock + (h_lock - h_lock_pauseRect) / 2;
            int right_pause = left_pause + w_lock_pauseRect;
            int bottom_pause = top_pause + h_lock_pauseRect;
            rect_pause.set(left_pause, top_pause, right_pause, bottom_pause);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(color_pauseRect_lock);
            paint.setAlpha(alpha_lock);
            canvas.drawRoundRect(rect_pause, radius_pauseRect, radius_pauseRect, paint);
        }


        //------------- circle
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color_circle);
        paint.setAlpha(alphaForAll);
        canvas.drawCircle(x_circle, y_circle, radius, paint);

        //------------- icon in circle
        Rect rect_icon_circle_des = new Rect();
        int left_icon_circle = (int) (x_circle - w_bitmap_circle / 2);
        int top_icon_circle = (int) (y_circle - h_bitmap_circle / 2);
        int right_icon_circle = (int) (left_icon_circle + w_bitmap_circle);
        int bottom_icon_circle = (int) (top_icon_circle + h_bitmap_circle);
        rect_icon_circle_des.set(left_icon_circle, top_icon_circle, right_icon_circle, bottom_icon_circle);
        Rect rect_icon_circle_src = new Rect();
        Bitmap bitmap_circle;
        if (lockedRecord) {
            bitmap_circle = bitmap_center_circle_send;
        } else {
            bitmap_circle = bitmap_center_circle_mic;
        }
        paint.setAlpha(alphaForAll);
        rect_icon_circle_src.set(0, 0, bitmap_circle.getWidth(), bitmap_circle.getHeight());
        canvas.drawBitmap(bitmap_circle, rect_icon_circle_src, rect_icon_circle_des, paint);

//        if (S.DEBUG) {
//            //------------- debug line
//            paint.setColor(Color.BLACK);
//            paint.setStrokeWidth(1);
//            float y_line = y_start_circle - distance_move_y_max;
//            canvas.drawLine(0, y_line, canvas.getWidth(), y_line, paint);
//        }
        String text_during = AudioRecordViewOld.this.textOfDuration;

        //-------------- wait send,contains 3 part:delete,duration,send.
        if (showWaitSend && !TextUtils.isEmpty(text_during)) {
            paint.setColor(color_waitSend_back);
            Rect rect_waitSend = new Rect();
            int left = (int) x_rect_waitSend;
            int top = y_rect_waitSend;
            int right = left + w_rect_waitSend;
            int bottom = top + h_rect_waitSend;
            rect_waitSend.set(left, top, right, bottom);
            canvas.drawRect(rect_waitSend, paint);

            int size_button_draw_delete = (int) (h_rect_waitSend / 2f);
            int padding_waitsend = (h_rect_waitSend - size_button_draw_delete) / 2;
            int padding_waitsend_start = (int) (h_rect_waitSend / 2.2f);
            int padding_waitsend_end = padding_waitsend_start;
            //delete button
            Rect rect_delete_des = new Rect();
            left_delete = x_rect_waitSend + padding_waitsend_start;
            top_delete = y_rect_waitSend + padding_waitsend;
            right_delete = left_delete + size_button_draw_delete;
            bottom_delete = top_delete + size_button_draw_delete;
            rect_delete_des.set(left_delete, top_delete, right_delete, bottom_delete);

            Rect rect_delete_src = new Rect();
            rect_delete_src.set(0, 0, bitmap_waitsend_delete.getWidth(), bitmap_waitsend_delete.getHeight());
            canvas.drawBitmap(bitmap_waitsend_delete, rect_delete_src, rect_delete_des, paint);

            //send button
            int size_button_draw_send = (int) (h_rect_waitSend / 2.3f);
            Rect rect_send_des = new Rect();
            left_send = x_rect_waitSend + w_rect_waitSend - padding_waitsend_end - size_button_draw_send;
            int padding_waitsend_send = (h_rect_waitSend - size_button_draw_send) / 2;
            top_send = y_rect_waitSend + padding_waitsend_send;
            right_send = left_send + size_button_draw_send;
            bottom_send = top_send + size_button_draw_send;
            rect_send_des.set(left_send, top_send, right_send, bottom_send);

            float radius_circle_waitsend = h_rect_waitSend * 0.8f / 2f;
            float x_circle_waitsend = left_send + size_button_draw_send / 2f;
            float y_circle_waitsend = top_send + size_button_draw_send / 2f;
            paint.setColor(color_circle);
            canvas.drawCircle(x_circle_waitsend, y_circle_waitsend, radius_circle_waitsend, paint);
//            if (S.DEBUG) {
//                paint.setColor(Color.WHITE);
//                paint.setStyle(Paint.Style.STROKE);
//                paint.setStrokeWidth(1);
//                canvas.drawRect(rect_send_des, paint);
//                paint.setStyle(Paint.Style.FILL);
//            }
            Rect rect_send_src = new Rect();
            rect_send_src.set(0, 0, bitmap_waitsend_send.getWidth(), bitmap_waitsend_send.getHeight());
            canvas.drawBitmap(bitmap_waitsend_send, rect_send_src, rect_send_des, paint);

            paint.setColor(color_duration);
            paint.setTextSize(textSizeOfDuration);
            float[] textWH = TextMeasure.measure(text_during, textSizeOfDuration);
            float w_duration = textWH[0];
            float h_duration = textWH[1];
            float x_duration = x_rect_waitSend + (w_rect_waitSend - w_duration) / 2f;
            float y_duration = y_rect_waitSend + (h_rect_waitSend - h_duration) / 2f + h_duration;
            canvas.drawText(text_during, x_duration, y_duration, paint);
        }


    }

    /**
     * when long click
     */
    private void startAppearAnim() {
        stopDisappearAnim();
        if (animator_appear == null) {
            animator_appear = ValueAnimator.ofFloat(0, 1000);
            animator_appear.setDuration(200);
            animator_appear.setInterpolator(new BounceInterpolator());
            animator_appear.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float distance = (float) animation.getAnimatedValue();
                    if (distance > 900) {
                        distance = 1000;
                    }
                    float percent = distance / 1000;
                    alphaForAll = (int) (255 * percent);
                    if (alphaForAll < 100) {
                        alpha_lock = 0;
                    } else {
                        alpha_lock = alphaForAll;
                    }
                    radius = radius_circle_max * percent;
                    y_lock = (int) (y_start_lock - (y_start_lock - y_end_lock) * percent);

                    percent_position = percent;
                    calculateAllPosition();
                    postInvalidate();
                }
            });
        }
        if (!animator_appear.isRunning()) {
            animator_appear.start();
        }
    }

    /**
     * when finger up
     */
    private void startDisappearAnim() {
        if (!isRecording) {
            resetAllState();
            return;
        }
        isRecording = false;
        stopAppearAnim();
        stopCircleAnim();
        if (animator_disappear == null) {
            animator_disappear = ValueAnimator.ofFloat(1000, 0);
            animator_disappear.setDuration(200);
            animator_disappear.setInterpolator(new BounceInterpolator());
            animator_disappear.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float distance = (float) animation.getAnimatedValue();
                    float percent = distance / 1000;
                    y_circle = y_start_circle + distance_move_y * percent;
                    alphaForAll = (int) (alphaForAll * percent);
                    if (percent < 0.15f) {
                        alphaForAll = 0;
                    }
                    radius = radius_circle_max * percent;
                    if (alphaForAll < 100) {
                        alpha_lock = 0;
                    } else {
                        alpha_lock = alphaForAll;
                    }
                    if (lockedRecord) {
                        y_lock = (int) (y_start_circle - radius - h_lock - margin_c_l_vertical);
                    } else {
                        y_lock = (int) (y_start_lock - (y_start_lock - y_end_lock) * percent);
                    }


                    percent_position = percent;
                    if (percent_position < 0.1) {
                        percent_position = 0;
                    }
                    alpha_all = (int) (255 * percent_position);
                    alpha_redPoint = (int) (255 * percent_position);
                    calculateAllPosition2();

                    postInvalidate();
                }
            });
            animator_disappear.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    lockedRecord = false;

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
        if (!animator_disappear.isRunning()) {
            animator_disappear.start();
        }
    }

    /**
     * when locked
     */
    private synchronized void startLockAnim() {
        h_lock = w_lock;
        if (animator_lock == null) {
            animator_lock = ValueAnimator.ofFloat(1000, 0);
            animator_lock.setDuration(400);
            animator_lock.setInterpolator(new BounceInterpolator());
            animator_lock.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float distance = (float) animation.getAnimatedValue();
                    if (distance < 100) {
                        distance = 0;
                    }
                    float percent = distance / 1000;
                    distance_move_y *= percent;
                    y_circle = y_start_circle + distance_move_y;
                    y_lock = (int) (y_circle - radius_circle_max - h_lock - margin_c_l_vertical);


                    if (isRecording) {
                        x_cancelTouch = (int) (x_start_cancelTouch + distance_move_x * percent);
                        y_text1_cancelTouch = (int) (y_text1_cancelTouch_start + (y_end_cancelTouch - y_bitmap_cancelTouch_start) * (1 - percent));
                        S.s("y:"+y_text1_cancelTouch);
                        y_bitmap_cancelTouch = (int) (y_bitmap_cancelTouch_start + (y_end_cancelTouch - y_bitmap_cancelTouch_start) * (1 - percent));
                        y_text2_cancelTouch = (int) (y_text2_cancelTouch_start - (y_end_cancelTouch - y_text2_cancelTouch_start) * percent);
                        alpha_cancelTouch = (int) (255 * percent);
                        alpha_cancelTouch2 = (int) (255 * (1 - percent));
                    } else {
                        x_cancelTouch = (int) x_start_cancelTouch;
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

    private synchronized void startWaitSendAppearAnim() {
        showWaitSend = true;
        if (animator_appear_waitsend == null) {
            animator_appear_waitsend = ValueAnimator.ofFloat(0, 1000);
            animator_appear_waitsend.setDuration(400);
            animator_appear_waitsend.setInterpolator(new DecelerateInterpolator(2));
            animator_appear_waitsend.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float distance = (float) animation.getAnimatedValue();
                    if (distance > 900) {
                        distance = 1000;
                    }
                    float percent = distance / 1000;
                    y_rect_waitSend = (int) (h_view - h_rect_waitSend * percent);
                    postInvalidate();
                }
            });
        }
        if (!animator_appear_waitsend.isRunning()) {
            animator_appear_waitsend.start();
        }
    }

    private synchronized void startWaitSendDisappearAnim() {
        if (animator_disappear_waitsend == null) {
            animator_disappear_waitsend = ValueAnimator.ofFloat(1000, 0);
            animator_disappear_waitsend.setDuration(400);
            animator_disappear_waitsend.setInterpolator(new AccelerateInterpolator(2));
            animator_disappear_waitsend.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float distance = (float) animation.getAnimatedValue();
                    if (distance < 100) {
                        distance = 0;
                    }
                    float percent = distance / 1000;
                    y_rect_waitSend = (int) (h_view - h_rect_waitSend * percent);
                    postInvalidate();
                }
            });
        }
        animator_disappear_waitsend.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                showWaitSend = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        if (!animator_disappear_waitsend.isRunning()) {
            animator_disappear_waitsend.start();
        }
    }

    private void stopAppearAnim() {
        if (animator_appear != null && animator_appear.isRunning()) {
            animator_appear.cancel();
            animator_appear.end();
        }
    }

    private void stopDisappearAnim() {
        if (animator_disappear != null && animator_disappear.isRunning()) {
            animator_disappear.cancel();
            animator_disappear.end();
        }
    }

    private void stopLockAnim() {
        if (animator_lock != null && animator_lock.isRunning()) {
            animator_lock.cancel();
            animator_lock.end();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        stopDisappearAnim();
        stopAppearAnim();
        stopLockAnim();
        //-------
        stopCircleAnim();
        super.onDetachedFromWindow();
    }

    private void resetAllState() {
        alpha_lock = 0;
        alphaForAll = 0;
        showWaitSend = false;
        lockedRecord = false;

        //----------
        timeString = timeStringDefault;
        percent_position = 0;
        calculateAllPosition();
        postInvalidate();
    }

    private void appear() {
        isRecording = true;
        canceledByHand = false;
        distance_move_y = 0;
        x_circle = x_start_circle;
        y_circle = y_start_circle;
        h_lock = h_lock_max;
        alphaForAll = 255;

        VibratorUtil.vibrate(getContext());


        //------
        x_rect = 0;
        distance_move_x = 0;
        alpha_all = 255;
        alpha_cancelTouch = 255;
        alpha_cancelTouch2 = 0;
        startCircleAnim();

        waitSend = false;

        if (callBack != null) {
            callBack.whenStartRecord();
        }


        startAppearAnim();
        postInvalidate();
    }

    protected void stopRecord(boolean callEventBack, boolean needSend) {
        S.sd("stop record");
        startDisappearAnim();
        if (callEventBack) {
            stopRecord(false);
            postInvalidate();
        }
        if (needSend) {
            waitSend = false;
        }
        if (this.callBack != null) {
            this.callBack.whenStopRecord(needSend);
        }
    }

    private void cancelRecord() {
        waitSend = false;
        canceledByHand = true;
        lockedRecord = false;
        distance_move_x = 0;

        //--------
        startDisappearAnim();

        if (callBack != null) {
            callBack.whenCancelRecord();
        }
    }

    private void lockRecord() {
        lockedRecord = true;
        startLockAnim();
    }

    @Override
    public void whenActionDown() {
        if (callBack != null) {
            callBack.whenActionDown();
        }
    }

    @Override
    public void whenActionUp() {
        if (callBack != null) {
            callBack.whenActionUp();
        }
    }

    @Override
    public void whenLongClickDown(float x, float y) {
        if (lockedRecord) {
            //S.e("LONG:locked,long down not work");
        } else {
            //S.s("LONG:start record");
            x_press = x;
            y_press = y;
            appear();
        }
    }

    @Override
    public void whenLongClickUp(MotionEvent event) {
        if (canceledByHand) {
            canceledByHand = false;
            //S.e("UP:canceledByHand , up not work");
        } else {
            if (lockedRecord) {
                //S.e("UP:is locked ,up not work");
            } else {
                //S.s("UP:stop record and send");
                stopRecord(true, true);
            }
        }
    }


    @Override
    public void whenPressAndMove(MotionEvent event) {
        if (canceledByHand || !isRecording) {
            return;
        }
        alphaForAll = 255;
        if (!lockedRecord) {
            //calculate relative distance of move on y
            float distance_move_y_tmp = event.getY() - y_press;
            //forbid move down,only allow move up
            if (distance_move_y_tmp < 0) {
                //if distance over line,lockedRecord
                if (Math.abs(distance_move_y_tmp) >= distance_move_y_max) {
                    lockRecord();

                } else {//if still not over line ,just move up
                    distance_move_y = distance_move_y_tmp;
                    //circle
                    y_circle = y_start_circle + distance_move_y;
                    //lock position
                    y_lock = (int) (y_end_lock + distance_move_y);

                    float percent = 1 - Math.abs(distance_move_y / distance_move_y_max);
                    //lock size
                    h_lock = (int) ((h_lock_max - h_lock_min) * percent) + h_lock_min;
                }
            }

            //calculate relative distance of move on x
            float distance_move_x_tmp = event.getX() - x_press;
            //forbid move right,only allow move left
            if (distance_move_x_tmp < 0) {
                if (Math.abs(distance_move_x_tmp) < distance_move_x_max) {
                    distance_move_x = (int) distance_move_x_tmp;
                    //cancel touch position
                    x_cancelTouch = (int) (x_start_cancelTouch + distance_move_x);
                    float percent = 1 - Math.abs(distance_move_x_tmp) / distance_move_x_max;
                    alpha_cancelTouch = (int) (255 * percent);
                } else {
                    cancelRecord();
                }
            }
        }
        postInvalidate();
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public boolean isRecording() {
        return isRecording;
    }


    ///---------------------------------------------------- inputActionView
    private String timeString;
    private String timeStringDefault;
    private String cancelString1;
    private String cancelString2;

    private int w_rect_back, h_rect_back;
    private int w_tail;
    private int x_rect, y_rect;
    private int x_start;

    private int x_cancelTouch, y_cancelTouch;
    private float y_text1_cancelTouch;
    private float y_text2_cancelTouch;
    private float y_bitmap_cancelTouch;
    private float y_text1_cancelTouch_start, y_text2_cancelTouch_start, y_bitmap_cancelTouch_start;
    private int w_cancelTouch, h_cancelTouch;
    private int w_cancelTouch_icon, h_bitmap_cancelTouch;

    private int x_during, y_during;
    private int w_during, h_during;

    //start when start record
    private ValueAnimator animator_circle;

    private int distance_move_x_max;
    private int distance_move_x;

    private int marginX_start_during;
    private float x_start_cancelTouch;
    private float y_end_cancelTouch;

    //red point
    private int x_redPoint, y_redPoint;
    private float radius_redPoint;

    private float textSize_during;
    private float textSize_cancel1;
    private float textSize_cancel2;

    private int alpha_cancelTouch;
    private int alpha_cancelTouch2;
    private int alpha_redPoint;
    private int alpha_all;

    private long time_startTimer;

    private ZLoopThread loopThread;
    private float w_text_during, h_text_during;
    private float w_text1_cancel, h_text1_cancel;
    private float w_text2_cancel, h_text2_cancel;

    private int color_redPoint;
    private int color_back;
    private int color_back_during;
    private int color_text_during;
    private int color_text_cancel1;
    private int color_text_cancel2;
    private int color_back_cancel;

    private Bitmap bitmap_cancelTouch;
    private float percent_position;

    private String duration_formation;

    private boolean autoUpdateTime;

    //press time
    private long time_down;

    private int margin_circle_cancelTouch;
    private int margin_icon_cancelTouch;

    private void init2() {
        setWillNotDraw(false);

        timeStringDefault = getContext().getResources().getString(R.string.string_fastrecord_duration_default);
        timeString = timeStringDefault;
        cancelString1 = getContext().getResources().getString(R.string.string_fastrecord_cancel1);
        cancelString2 = getContext().getResources().getString(R.string.string_fastrecord_cancel2);
        duration_formation = getContext().getResources().getString(R.string.string_fastrecord_duration_formation);

        alpha_cancelTouch = 255;
        alpha_all = 255;
        alpha_redPoint = 0;

        distance_move_x_max = BitmapUtils.dip2px(getContext(), 80);
        h_rect_back = BitmapUtils.dip2px(getContext(), 48);
        w_tail = (int) (h_rect_back * 0.8f);

        //margin of during
        marginX_start_during = BitmapUtils.dip2px(getContext(), 40);

        //size of touch cancel
        w_cancelTouch = BitmapUtils.dip2px(getContext(), 130);
        h_cancelTouch = BitmapUtils.dip2px(getContext(), 40);

        radius_redPoint = BitmapUtils.dip2px(getContext(), 5);

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

        color_redPoint = Color.RED;
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

        h_bitmap_cancelTouch = (int) (h_cancelTouch * 0.5f);

        int w_bitmap = bitmap_cancelTouch.getWidth();
        int h_bitmap = bitmap_cancelTouch.getHeight();

        float propertion_bitmap_cancelTouch = (float) w_bitmap / h_bitmap;
        w_cancelTouch_icon = (int) (h_bitmap_cancelTouch * propertion_bitmap_cancelTouch);

        margin_circle_cancelTouch = BitmapUtils.dip2px(getContext(), 36);
        margin_icon_cancelTouch = BitmapUtils.dip2px(getContext(), 5);
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
                    alpha_redPoint = (int) (255 * percent);
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

    private void stopRecord(boolean callBack) {
        lockedRecord = false;
        distance_move_x = 0;
        startDisappearAnim();
        if (callBack) {
            stopRecord(false, false);
        }
        postInvalidate();
    }

    private void setTimeString(String timeString) {
        this.timeString = timeString;
        if (!waitSend) {
            this.textOfDuration = timeString;
        }
        postInvalidate();
    }

    public void setAutoUpdateTime(boolean autoUpdateTime) {
        this.autoUpdateTime = autoUpdateTime;
    }

    public interface CallBack {
        void whenStartRecord();

        void whenStopRecord(boolean needSend);

        void whenCancelRecord();

        void whenAbandonedVoice();

        void whenSendClick();

        void whenActionDown();

        void whenActionUp();
    }
}
