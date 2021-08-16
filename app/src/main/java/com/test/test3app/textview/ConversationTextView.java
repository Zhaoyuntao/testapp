package com.test.test3app.textview;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zhaoyuntao.androidutils.tools.B;

import java.util.Locale;


public class ConversationTextView extends FrameLayout {

    private View contentView;
    private View tailView;
    private FrameLayout container;

    private int padding_start;
    private int padding_end;
    private int padding_top;
    private int padding_bottom;

    private int w_space;

    public ConversationTextView(@NonNull Context context) {
        super(context);
        init();
    }


    public ConversationTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ConversationTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        w_space = B.dip2px(getContext(), 10);

        container = new FrameLayout(getContext());
        container.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        container.setBackgroundColor(Color.YELLOW);
        addView(container);
    }

    @Override
    protected void onLayout(boolean changed, int left1, int top1, int right1, int bottom1) {
//        S.s("l:" + left1 + " t:" + top1 + " r:" + right1 + " b:" + bottom1);

        int left = padding_start;
        int right = getMeasuredWidth() - padding_end;
        int top = padding_top;
        int bottom = getMeasuredHeight() - padding_bottom;

        if (isRTL()) {
            if (contentView != null) {
                contentView.layout(right - contentView.getMeasuredWidth(), top, right, top + contentView.getMeasuredHeight());
            }
            container.layout(left, bottom - container.getMeasuredHeight(), left + container.getMeasuredWidth(), bottom);
        } else {
            if (contentView != null) {
                contentView.layout(left, top, left + contentView.getMeasuredWidth(), top + contentView.getMeasuredHeight());
            }
            container.layout(right - container.getMeasuredWidth(), bottom - container.getMeasuredHeight(), right, bottom);
        }
    }

    /**
     * get system layout direction
     *
     * @return
     */
    public boolean isRTL() {
        return getLayoutDirection() == LAYOUT_DIRECTION_RTL || TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_RTL;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int w_view = MeasureSpec.getSize(widthMeasureSpec);

        measureChild(container, widthMeasureSpec, heightMeasureSpec);
        int w_container = container.getMeasuredWidth();
        int h_container = container.getMeasuredHeight();

        padding_start = getPaddingLeft();
        padding_end = getPaddingRight();
        padding_top = getPaddingTop();
        padding_bottom = getPaddingBottom();

        int w_view_measure;
        int h_view_measure;

        if (contentView != null) {
            measureChild(contentView, widthMeasureSpec, heightMeasureSpec);
            if (contentView instanceof TextView) {
                TextView textView = (TextView) contentView;

                int w_textview = textView.getMeasuredWidth();
                int h_textview = textView.getMeasuredHeight();

                Layout layout = textView.getLayout();

                int lines = textView.getLineCount();

                int w_lastLine = (int) layout.getLineWidth(lines - 1);
                float left_lastLine = layout.getLineLeft(lines - 1);
                float right_lastLine = layout.getLineRight(lines - 1);

                if (isRTL()) {
                    if (right_lastLine == w_textview) {
                        if (left_lastLine - w_space - w_container >= 0) {
                            w_view_measure = padding_start + w_textview + padding_end;
                            h_view_measure = padding_top + h_textview + padding_bottom;
                        } else {
                            if (padding_start + w_container + w_space + w_lastLine + padding_end > w_view) {
                                w_view_measure = padding_start + w_textview + padding_end;
                                h_view_measure = padding_top + h_textview + h_container + padding_bottom;
                            } else {
                                w_view_measure = padding_start + w_container + w_space + w_lastLine + padding_end;
                                h_view_measure = padding_top + h_textview + padding_bottom;
                            }
                        }
                    } else {
                        if (padding_start + w_container + w_space + w_textview + padding_end > w_view) {
                            w_view_measure = padding_start + w_textview + padding_end;
                            h_view_measure = padding_top + h_textview + h_container + padding_bottom;
                        } else {
                            w_view_measure = padding_start + w_container + w_space + w_textview + padding_end;
                            h_view_measure = padding_top + h_textview + padding_bottom;
                        }
                    }
                } else {
                    if (left_lastLine == 0) {
                        if (right_lastLine + w_space + w_container <= w_textview) {
                            w_view_measure = padding_start + w_textview + padding_end;
                            h_view_measure = padding_top + h_textview + padding_bottom;
                        } else {
                            if (padding_start + w_lastLine + w_space + w_container + padding_end > w_view) {
                                w_view_measure = padding_start + w_textview + padding_end;
                                h_view_measure = padding_top + h_textview + h_container + padding_bottom;
                            } else {
                                w_view_measure = padding_start + w_lastLine + w_space + w_container + padding_end;
                                h_view_measure = padding_top + h_textview + padding_bottom;
                            }
                        }
                    } else {
                        if (padding_start + w_textview + w_space + w_container + padding_end > w_view) {
                            w_view_measure = padding_start + w_textview + padding_end;
                            h_view_measure = padding_top + h_textview + h_container + padding_bottom;
                        } else {
                            w_view_measure = padding_start + w_textview + w_space + w_container + padding_end;
                            h_view_measure = padding_top + h_textview + padding_bottom;
                        }
                    }
                }
            } else {
                w_view_measure = contentView.getMeasuredWidth();
                h_view_measure = contentView.getMeasuredHeight();
            }
        } else {
            w_view_measure = 0;
            h_view_measure = 0;
        }

        setMeasuredDimension(w_view_measure, h_view_measure);
    }

    @Override
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        super.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
    }

    public void setContentView(View view) {
        this.contentView = view;
        if (view.getParent() == null) {
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(layoutParams);
            addView(view);
        }
    }

    public View getContentView() {
        return contentView;
    }

    public void setTailView(View view) {
        this.tailView = view;
        if (view.getParent() == null) {
            container.addView(view);
        }
    }

    public View getTialView() {
        return tailView;
    }


}
