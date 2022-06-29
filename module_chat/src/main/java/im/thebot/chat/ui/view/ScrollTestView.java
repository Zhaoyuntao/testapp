package im.thebot.chat.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.Scroller;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.module_chat.R;

import im.turbo.baseui.utils.UiUtils;

/**
 * created by zhaoyuntao
 * on 17/06/2022
 * description:
 */
public class ScrollTestView extends FrameLayout {
    private Scroller scroller;
    private float xDown, xLast;
    private float yDown, yLast;
    private Drawable drawable;
    private Paint paint;

    public ScrollTestView(Context context) {
        super(context);
        init();
    }

    public ScrollTestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScrollTestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ScrollTestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        setClipChildren(false);
        drawable = ContextCompat.getDrawable(getContext(), R.drawable.image0);
        scroller = new Scroller(getContext());
        paint = new Paint();
        setClipToOutline(true);
        setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), UiUtils.dipToPx(10));
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getX();
                yDown = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                xLast = event.getX();
                yLast = event.getY();
                scrollTo(-(int) (xLast - xDown), -(int) (yLast - yDown));
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                int scrollX = getScrollX();
                int scrollY = getScrollY();
                scroller.startScroll(scrollX, scrollY, -scrollX, -scrollY);
                break;
        }
        invalidate();
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.parseColor("#33ff0000"));
        super.draw(canvas);
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        float xScale = (float) (Math.sqrt(Math.pow(getScrollX(),2)+Math.pow(getScrollY(),2)) /(float) getMeasuredWidth());

        getChildAt(0).setScaleX(1-xScale);
        getChildAt(0).setScaleY(1-xScale);
    }
}
