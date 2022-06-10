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

import im.turbo.basetools.preconditions.Preconditions;

/**
 * created by zhaoyuntao
 * on 27/04/2022
 * description:
 */
public class ChatHeadLayout extends ChatViewGroup {
    private Integer headColor;
    private int headWidth;
    private Rect headRect;
    private Paint paint;

    public ChatHeadLayout(Context context) {
        super(context);
        init(null);
    }

    public ChatHeadLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ChatHeadLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        int defaultHeadColor = Color.parseColor("#00a983");
        paint = new Paint();
        headRect = new Rect();
        float radius;
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ExactlyFrameLayout);
            radius = typedArray.getDimensionPixelSize(R.styleable.ExactlyFrameLayout_ExactlyFrameLayout_radius, 0);
            headWidth = typedArray.getDimensionPixelSize(R.styleable.ExactlyFrameLayout_ExactlyFrameLayout_headWidth, 0);
            headColor = typedArray.getColor(R.styleable.ExactlyFrameLayout_ExactlyFrameLayout_headColor, defaultHeadColor);
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
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        Preconditions.checkArgument(childCount <= 1, "You can only put one child in this view group");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        int width = 0;
        int height = 0;
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        if (childCount > 0) {
            View child = getChildAt(0);
            if (child != null && child.getVisibility() != GONE) {
                final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                if (modeWidth == MeasureSpec.EXACTLY) {
                    lp.width = LayoutParams.MATCH_PARENT;
                } else {
                    lp.width = LayoutParams.WRAP_CONTENT;
                }
                measureChildWithMargins(child, widthMeasureSpec, heightMeasureSpec, lp);
                width = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
                height = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
//                S.e(true, "[" + child.getClass().getSimpleName() + "].measure result w:" + childWidth + " h:" + childHeight + "  leftMargin:" + marginStart + " rightMargin:" + marginEnd);
            }
        }
        width += (getPaddingStart() + getPaddingEnd());
        height += (getPaddingTop() + getPaddingBottom());
        setMeasuredDimension(width, height);
//        S.ve(true, "ExactlyFrameLayout.onMeasure: ----------------------------->[w:" + width + "][h:" + height + "] ", widthMeasureSpec, heightMeasureSpec, this);
    }

    @Override
    public int getPaddingStart() {
        return super.getPaddingStart() + headWidth;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int paddingStart = getPaddingStart();
        int paddingTop = getPaddingTop();
//        S.e("ExactlyFrameLayout.layout: -----------------------------> l:" + l + "  r:" + r);
        if (childCount > 0) {
            View child = getChildAt(0);
            if (child == null || child.getVisibility() == GONE) {
                return;
            }
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int left = paddingStart + lp.leftMargin;
            int right = left + child.getMeasuredWidth();
            int top = paddingTop + lp.topMargin;
            int bottom = top + child.getMeasuredHeight();
//                S.e(true, "[" + child.getClass().getSimpleName() + "].layout: left:" + left + "  right:" + right);
            child.layout(left, top, right, bottom);
        }
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

    final public void setHeadColor(@ColorInt int headColor) {
        this.headColor = headColor;
        postInvalidate();
    }
}
