package im.thebot.common.ui.chat;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import im.thebot.chat.ui.view.ChatViewGroup;
import im.turbo.basetools.preconditions.Preconditions;
import im.turbo.baseui.utils.UiUtils;
import im.turbo.utils.log.S;


public class TextCellContainer extends ChatViewGroup {

    private View contentView;
    private View tailView;
    private int tailMarginStart, tailMarginTop;

    public TextCellContainer(@NonNull Context context) {
        super(context);
        init();
    }

    public TextCellContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextCellContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        tailMarginStart = UiUtils.dipToPx(15);
        tailMarginTop = UiUtils.dipToPx(5);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        Preconditions.checkArgument(childCount == 2, "Please put two children in this view group!");
        contentView = getChildAt(0);
        tailView = getChildAt(1);
        Preconditions.checkArgument(childCount == 2, "Found null child, please check your code!");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w_max = MeasureSpec.getSize(widthMeasureSpec);
        S.ve(true, "ChatCellContainer.onMeasure ------------>", widthMeasureSpec, heightMeasureSpec, this);

        int paddingStart = getPaddingStart();
        int paddingEnd = getPaddingEnd();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int width;
        int height;

        MarginLayoutParams contentLP = (MarginLayoutParams) contentView.getLayoutParams();
        contentLP.width = LayoutParams.WRAP_CONTENT;
        measureChildWithMargins(contentView, widthMeasureSpec, heightMeasureSpec, contentLP);
        MarginLayoutParams tailLP = (MarginLayoutParams) tailView.getLayoutParams();
        tailLP.width = LayoutParams.WRAP_CONTENT;
        measureChildWithMargins(tailView, widthMeasureSpec, heightMeasureSpec, tailLP);
        S.s("measure tail: w:" + tailView.getMeasuredWidth() + "  ms:" + tailLP.getMarginStart());

        int wTail = tailView.getMeasuredWidth();
        int hTail = tailView.getMeasuredHeight();
        int wTextview = contentView.getMeasuredWidth();
        int hTextview = contentView.getMeasuredHeight();
        boolean ltr = getLayoutDirection() != LAYOUT_DIRECTION_RTL;
        int wSpace = contentLP.getMarginEnd() + tailLP.getMarginStart();
        int hSpace = tailLP.topMargin;
        if (wSpace <= 0) {
            wSpace = tailMarginStart;
        }
        if (hSpace <= 0) {
            hSpace = tailMarginTop;
        }

        if (contentView instanceof TextView) {
            TextView textView = (TextView) contentView;

            Layout layout = textView.getLayout();
            int lines = textView.getLineCount();

            int topLine = lines == 0 ? 0 : layout.getLineTop(lines - 1);
            int bottomLine = lines == 0 ? 0 : layout.getLineBottom(lines - 1);
            int hLastLine = bottomLine - topLine;
            int leftLastLine = lines == 0 ? 0 : (int) layout.getLineLeft(lines - 1);
            int rightLastLine = lines == 0 ? 0 : (int) layout.getLineRight(lines - 1);
            int wLastLineAndTail;
            if (ltr) {
                wLastLineAndTail = paddingStart + rightLastLine + contentView.getPaddingStart() + contentView.getPaddingEnd() + wSpace + wTail + paddingEnd;
            } else {
                wLastLineAndTail = paddingStart + (wTextview - leftLastLine - contentView.getPaddingStart() - contentView.getPaddingEnd()) + wSpace + wTail + paddingEnd;
            }
//            S.s(true, "w_max:" + w_max + "  wLastLineAndTail:" + wLastLineAndTail + "  w_textview:" + wTextview + " right_lastLine:" + rightLastLine + " left_lastLine:" + leftLastLine + " padding_start:" + paddingStart + " padding_end:" + paddingEnd);
            if (wLastLineAndTail <= w_max) {
                width = Math.max(wLastLineAndTail, paddingStart + wTextview + paddingEnd);
                height = paddingTop + (hTextview - hLastLine + Math.max(hLastLine, hTail + hSpace)) + paddingBottom;
            } else {
                width = paddingStart + wTextview + paddingEnd;
                height = paddingTop + hTextview + hSpace + hTail + paddingBottom;
            }
        } else {
            width = contentView.getMeasuredWidth();
            height = contentView.getMeasuredHeight();
        }
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            width = w_max;
        }
        S.s("result: w:" + width + " h:" + height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        S.e("ChatCellContainer.onLayout: ------------>");
        MarginLayoutParams contentLP = (MarginLayoutParams) contentView.getLayoutParams();
        int leftContent;
        int topContent = getPaddingTop() + contentLP.topMargin;
        int rightContent;
        int bottomContent = topContent + contentView.getMeasuredHeight();

        MarginLayoutParams tailLP = (MarginLayoutParams) tailView.getLayoutParams();
        int leftTail;
        int rightTail;
        int bottomTail = getMeasuredHeight() - getPaddingBottom() - tailLP.bottomMargin;
        int topTail = bottomTail - tailView.getMeasuredHeight();

        if (getLayoutDirection() == LAYOUT_DIRECTION_RTL) {
            rightContent = getMeasuredWidth() - getPaddingEnd() - contentLP.getMarginEnd();
            leftContent = rightContent - contentView.getMeasuredWidth();
            leftTail = getPaddingEnd() + tailLP.getMarginStart();
            rightTail = leftTail + tailView.getMeasuredWidth();
        } else {
            leftContent = getPaddingStart() + contentLP.getMarginStart();
            rightContent = leftContent + contentView.getMeasuredWidth();

            rightTail = getMeasuredWidth() - getPaddingEnd() - tailLP.getMarginEnd();
            leftTail = rightTail - tailView.getMeasuredWidth();
        }
        contentView.layout(leftContent, topContent, rightContent, bottomContent);
        S.s("layout tail: l:" + leftTail + " r:" + rightTail + " w:" + tailView.getMeasuredWidth() + "  ms:" + tailLP.getMarginStart());
        tailView.layout(leftTail, topTail, rightTail, bottomTail);
    }
}
