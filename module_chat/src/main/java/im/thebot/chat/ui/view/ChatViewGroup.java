package im.thebot.chat.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * created by zhaoyuntao
 * on 10/06/2022
 * description:
 */
public abstract class ChatViewGroup extends ViewGroup {
    public ChatViewGroup(Context context) {
        super(context);
    }

    public ChatViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ChatViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected MarginLayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public MarginLayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec, MarginLayoutParams lp) {
        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, getPaddingStart() + getPaddingEnd() + lp.getMarginStart() + lp.getMarginEnd(), lp.width);
        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec, getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin, lp.height);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }
}
