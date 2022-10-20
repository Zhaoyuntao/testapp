package im.thebot.chat.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import im.turbo.utils.log.S;

/**
 * created by zhaoyuntao
 * on 27/04/2022
 * description: All children will be layout as match parent.
 */
public class ChatMatchParentLayout extends ChatViewGroup {

    public ChatMatchParentLayout(Context context) {
        super(context);
    }

    public ChatMatchParentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatMatchParentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
//        Preconditions.checkArgument(childCount <= 1, "You can only put one child in this view group");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        int width = 0;
        int height = 0;
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        S.s("chatMatchParent");
        if (childCount > 0) {
            View child = getChildAt(0);
            if (child != null && child.getVisibility() != GONE) {
                final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                if (modeWidth == MeasureSpec.EXACTLY) {
                    lp.width = LayoutParams.MATCH_PARENT;
                }
                measureChildWithMargins(child, widthMeasureSpec, heightMeasureSpec, lp);
                width = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
                height = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            }
        }
        width += (getPaddingStart() + getPaddingEnd());
        height += (getPaddingTop() + getPaddingBottom());
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int paddingStart = getPaddingStart();
        int paddingTop = getPaddingTop();
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
            child.layout(left, top, right, bottom);
        }
    }
}
