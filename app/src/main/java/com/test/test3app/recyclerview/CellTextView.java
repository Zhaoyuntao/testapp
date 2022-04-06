package com.test.test3app.recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.text.Layout;
import android.text.SpannedString;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.emoji.widget.EmojiTextView;

import com.test.test3app.R;
import com.zhaoyuntao.androidutils.tools.S;


/**
 * created by zhaoyuntao
 * on 2020/8/11
 * description:
 */
public class CellTextView extends EmojiTextView {
    private long downTime;
    private boolean ellipsizeSupport;
    private PaintFlagsDrawFilter paintFlagsDrawFilter;
    private View backgroundShareClickMotionView;

    public CellTextView(Context context) {
        super(context);
    }

    public CellTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public CellTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    protected void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
        }
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (ellipsizeSupport) {
            super.setText(text, type);
        } else {
            if (text == null || text.length() == 0) {
                super.setText(text, type);
                post(this::requestLayout);
                return;
            }

            setAutoLinkMask(0);
            super.setText(text, type);
            setHighlightColor(Color.TRANSPARENT);
        }
        post(this::requestLayout);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(paintFlagsDrawFilter);
        super.onDraw(canvas);
    }

    boolean isLongClickEventShow = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        CharSequence text = getText();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (backgroundShareClickMotionView != null) {
                backgroundShareClickMotionView.setPressed(true);
                backgroundShareClickMotionView.postInvalidate();
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            if (backgroundShareClickMotionView != null) {
                backgroundShareClickMotionView.setPressed(false);
                backgroundShareClickMotionView.postInvalidate();
            }
        }
        if (text == null) {
            return super.onTouchEvent(event);
        }

        if (text instanceof SpannedString) {
            SpannedString spannable = (SpannedString) text;
            ClickableSpan clickableSpans = checkClickKSpan(event, spannable);

            if (clickableSpans == null) {
                return super.onTouchEvent(event);
            } else {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    downTime = System.currentTimeMillis();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (System.currentTimeMillis() - downTime >= ViewConfiguration.getLongPressTimeout()) {
                        return true;
                    }
                    if (!isLongClickEventShow) {
                        clickableSpans.onClick(this);
                    }
                    isLongClickEventShow = false;
                } else {
                    if (System.currentTimeMillis() - downTime >= ViewConfiguration.getLongPressTimeout()) {
                        isLongClickEventShow = true;
                        return false;
                    }
                }
            }
        }

        return super.onTouchEvent(event);
    }

    /**
     * Check the existence of clickablespan in the event
     *
     * @param event
     * @param spannable
     * @return
     */
    public ClickableSpan checkClickKSpan(MotionEvent event, SpannedString spannable) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= getTotalPaddingLeft();
            y -= getTotalPaddingTop();

            x += getScrollX();
            y += getScrollY();

            Layout layout = getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            ClickableSpan[] links = spannable.getSpans(off, off, ClickableSpan.class);
            if (links != null && links.length > 0) {
                return links[0];
            } else {
                return null;
            }
        }
        return null;
    }

    public void setBackgroundShareClickMotionView(View backgroundShareClickMotionView) {
        this.backgroundShareClickMotionView = backgroundShareClickMotionView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
