package com.test.test3app.recyclerview;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.test.test3app.fastrecordviewnew.UiUtils;
import com.test.test3app.utils.RTLUtils;
import com.zhaoyuntao.androidutils.tools.S;

/**
 * created by zhaoyuntao
 * on 18/05/2020
 * description:
 */
public class ChatCellContainer extends ViewGroup {

    private TextView contentView;
    private View tailContainer;

    private int padding_start;
    private int padding_end;
    private int padding_top;
    private int padding_bottom;

    private int w_space;

    public ChatCellContainer(@NonNull Context context) {
        super(context);
        init();
    }

    public ChatCellContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChatCellContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    private void init() {
        w_space = UiUtils.dipToPx(15);
        S.s("getChildCount1:" + getChildCount());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = (TextView) getChildAt(0);
        tailContainer = getChildAt(1);
    }

    @Override
    protected void onLayout(boolean changed, int left1, int top1, int right1, int bottom1) {

        MarginLayoutParams cParams = (MarginLayoutParams) contentView.getLayoutParams();
        int left = cParams.leftMargin;
        int top = cParams.topMargin;

        int right = getMeasuredWidth() - padding_end;
        int bottom = getMeasuredHeight() - padding_bottom;

        if (isRTL()) {
            contentView.layout(right - contentView.getMeasuredWidth(), top, right, top + contentView.getMeasuredHeight());
            tailContainer.layout(left, bottom - tailContainer.getMeasuredHeight(), left + tailContainer.getMeasuredWidth(), bottom);
        } else {
            contentView.layout(left, top, left + contentView.getMeasuredWidth(), top + contentView.getMeasuredHeight());
            tailContainer.layout(right - tailContainer.getMeasuredWidth(), bottom - tailContainer.getMeasuredHeight(), right, bottom);
        }
    }

    /**
     * get system layout direction
     */
    public boolean isRTL() {
        return getLayoutDirection() == LAYOUT_DIRECTION_RTL || RTLUtils.isRTL(getContext());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        S.s("onMeasure[" + contentView.getText() + "]--------------------------------------------------------------------------");

        int w_view = MeasureSpec.getSize(widthMeasureSpec);

        measureChild(tailContainer, widthMeasureSpec, heightMeasureSpec);
        measureChild(contentView, widthMeasureSpec, heightMeasureSpec);

        int w_tail = tailContainer.getMeasuredWidth();
        int h_tail = tailContainer.getMeasuredHeight();

        padding_start = getPaddingLeft();
        padding_end = getPaddingRight();
        padding_top = getPaddingTop();
        padding_bottom = getPaddingBottom();

        int w_view_measure;
        int h_view_measure;

        TextView textView = contentView;

        int w_textview = textView.getMeasuredWidth();
        int h_textview = textView.getMeasuredHeight();

        Layout layout = textView.getLayout();

        int lines = textView.getLineCount();

        int w_lastLine = (int) layout.getLineWidth(lines - 1);
        float left_lastLine = layout.getLineLeft(lines - 1);
        float right_lastLine = layout.getLineRight(lines - 1);

        if (isRTL()) {
            S.s("1");
            if (right_lastLine == w_textview) {
                S.s("11");
                if (left_lastLine - w_space - w_tail >= 0) {
                    S.s("111");
                    w_view_measure = padding_start + w_textview + padding_end;
                    h_view_measure = padding_top + h_textview + padding_bottom;
                } else {
                    if (padding_start + w_tail + w_space + w_lastLine + padding_end > w_view) {
                        S.s("11333");
                        w_view_measure = padding_start + w_textview + padding_end;
                        h_view_measure = padding_top + h_textview + h_tail + padding_bottom;
                    } else {
                        S.s("11444");
                        w_view_measure = padding_start + w_tail + w_space + w_lastLine + padding_end;
                        h_view_measure = padding_top + h_textview + padding_bottom;
                    }
                }
            } else {
                if (padding_start + w_tail + w_space + w_textview + padding_end > w_view) {
                    S.s("22");
                    w_view_measure = padding_start + w_textview + padding_end;
                    h_view_measure = padding_top + h_textview + h_tail + padding_bottom;
                } else {
                    S.s("33");
                    w_view_measure = padding_start + w_tail + w_space + w_textview + padding_end;
                    h_view_measure = padding_top + h_textview + padding_bottom;
                }
            }
        } else {
            S.s("a");
            if (left_lastLine == 0) {
                S.s("b");
                if (right_lastLine + w_space + w_tail <= w_textview) {
                    S.s("c");
                    w_view_measure = padding_start + w_textview + padding_end;
                    h_view_measure = padding_top + h_textview + padding_bottom;
                } else {
                    if (padding_start + w_lastLine + w_space + w_tail + padding_end > w_view) {
                        S.s("d");
                        w_view_measure = padding_start + w_textview + padding_end;
                        h_view_measure = padding_top + h_textview + h_tail + padding_bottom;
                    } else {
                        S.s("e");
                        w_view_measure = padding_start + w_lastLine + w_space + w_tail + padding_end;
                        h_view_measure = padding_top + h_textview + padding_bottom;
                    }
                }
            } else {
                S.s("aa");
                if (padding_start + w_textview + w_space + w_tail + padding_end > w_view) {
                    S.s("aaa");
                    w_view_measure = padding_start + w_textview + padding_end;
                    h_view_measure = padding_top + h_textview + h_tail + padding_bottom;
                } else {
                    S.s("ccc");
                    w_view_measure = padding_start + w_textview + w_space + w_tail + padding_end;
                    h_view_measure = padding_top + h_textview + padding_bottom;
                }
            }
        }
        S.s("w:" + w_view_measure + " h:" + h_view_measure + "  wText:" + w_textview + " hText:" + h_textview + " wTail:" + w_tail + " hTail:" + h_tail);
        setMeasuredDimension(w_view_measure, h_view_measure);
    }

    public void setText(String text) {
        contentView.setText(text);
        contentView.requestLayout();
        tailContainer.requestLayout();
        requestLayout();
    }
}
