package im.thebot.common.ui.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.os.SystemClock;
import android.text.Layout;
import android.text.SpannedString;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.emoji.widget.EmojiTextView;

import im.turbo.basetools.span.TClickableSpan;
import im.turbo.basetools.vibrate.VibratorUtil;

/**
 * created by zhaoyuntao
 * on 2020/8/11
 * description:
 */
public class CellTextView extends EmojiTextView {
    private long downTime;
    private PaintFlagsDrawFilter paintFlagsDrawFilter;
    private static final long LONG_PRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();

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
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(paintFlagsDrawFilter);
        super.onDraw(canvas);
    }

    boolean isLongClick = false;
    private TClickableSpan clickableSpans;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        CharSequence text = getText();
        if (text == null) {
            return super.onTouchEvent(event);
        }

        if (text instanceof SpannedString) {
            SpannedString spannable = (SpannedString) text;
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                isLongClick = false;
                clickableSpans = checkClickKSpan(event, spannable);
                downTime = SystemClock.elapsedRealtime();
                if (clickableSpans != null) {
                    if (clickableSpans.canClick(this)) {
                        return true;
                    } else {
                        clickableSpans = null;
                    }
                }
            } else if (clickableSpans != null) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (isLongClick) {
                        isLongClick = false;
                    } else {
                        clickableSpans.onClick(this);
                    }
                    clickableSpans = null;
                } else {
                    if (SystemClock.elapsedRealtime() - downTime >= LONG_PRESS_TIMEOUT) {
                        if (!isLongClick) {
                            if (clickableSpans.onLongClick(this)) {
                                isLongClick = true;
                                VibratorUtil.vibrate(getContext());
                            }
                        }
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
    public TClickableSpan checkClickKSpan(MotionEvent event, SpannedString spannable) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
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
                ClickableSpan clickableSpan = links[0];
                if (clickableSpan instanceof TClickableSpan) {
                    return (TClickableSpan) clickableSpan;
                }
            }
        }
        return null;
    }
}
