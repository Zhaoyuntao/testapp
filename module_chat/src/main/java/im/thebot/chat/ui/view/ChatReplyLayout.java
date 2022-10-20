package im.thebot.chat.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.ColorInt;

import com.example.module_chat.R;

/**
 * created by zhaoyuntao
 * on 27/04/2022
 * description: All children will be layout as match parent.
 */
public class ChatReplyLayout extends ChatMatchParentLayout {
    private Integer headColor;
    private int headWidth;
    private Rect headRect;
    private Paint paint;

    public ChatReplyLayout(Context context) {
        super(context);
        init(null);
    }

    public ChatReplyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ChatReplyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        int defaultHeadColor = Color.parseColor("#00a983");
        paint = new Paint();
        headRect = new Rect();
        float radius;
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ChatHeadLayout);
            radius = typedArray.getDimensionPixelSize(R.styleable.ChatHeadLayout_ChatHeadLayout_radius, 0);
            headWidth = typedArray.getDimensionPixelSize(R.styleable.ChatHeadLayout_ChatHeadLayout_headWidth, 0);
            headColor = typedArray.getColor(R.styleable.ChatHeadLayout_ChatHeadLayout_headColor, defaultHeadColor);
            typedArray.recycle();
        } else {
            radius = 0;
            headColor = defaultHeadColor;
            headWidth = 0;
        }
        setCornerRadius(radius);
        setWillNotDraw(false);
    }

    public void setCornerRadius(float cornerRadius) {
        setClipToOutline(true);
        setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), cornerRadius);
            }
        });
    }

    @Override
    public int getPaddingStart() {
        return super.getPaddingStart() + headWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (headColor != null) {
            if (getLayoutDirection() == LAYOUT_DIRECTION_RTL) {
                headRect.set(getWidth() - headWidth, 0, getWidth(), getHeight());
            } else {
                headRect.set(0, 0, headWidth, getHeight());
            }
            paint.setColor(headColor);
            canvas.drawRect(headRect, paint);
        }
    }

    public void setHeadWidth(int headWidth) {
        this.headWidth = headWidth;
    }

    final public void setHeadColor(@ColorInt int headColor) {
        this.headColor = headColor;
        postInvalidate();
    }
}
