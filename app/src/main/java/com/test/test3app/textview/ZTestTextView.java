package com.test.test3app.textview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.zhaoyuntao.androidutils.tools.S;

import java.text.BreakIterator;

/**
 * created by zhaoyuntao
 * on 2020-04-07
 * description:
 */
public class ZTestTextView extends AppCompatTextView {

    private CharSequence text;
    public ZTestTextView(Context context) {
        super(context);
        init();
    }

    public ZTestTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZTestTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setEllipsize(null);
        setSingleLine();
    }


    @Override
    public void setText(CharSequence text, BufferType type) {
        this.text=text;
        if (TextUtils.isEmpty(text)) {
            super.setText(text, type);
        }else{
            setText2(text, type);
            requestLayout();
        }

    }

    //
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        S.lll();
        S.s("isInlayout:"+isInLayout());
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
        S.s("-------- measure:w:" + getMeasuredWidth() + " h:" + getMeasuredHeight());
        setText2(text, BufferType.SPANNABLE);
    }

    public void setText2(CharSequence text, BufferType type) {
        S.s("getMeasuredWidth:" + getMeasuredWidth());
        MyCharIterator myCharIterator = new MyCharIterator(getTextSize(), getMeasuredWidth());
        getCharCount(text, myCharIterator);
        String finalString = myCharIterator.getLastString();
        if (myCharIterator.isNeedEmplisize()) {
            S.e("not equals:" + finalString + " " + text);
            finalString += "\u2026";
        }
        super.setText(finalString, type);
    }

    /**
     * version:2020-02-26
     *
     * @param source
     * @return
     */
    public static int getCharCount(CharSequence source, CharIterator charIterator) {
        if (TextUtils.isEmpty(source)) {
            return 0;
        }
        BreakIterator breakIterator = BreakIterator.getCharacterInstance();
        breakIterator.setText(source.toString());
        int start = breakIterator.first();
        int count = 0;
        for (int end = breakIterator.next(); end != BreakIterator.DONE; start = end, end = breakIterator.next()) {
            count++;
            if (charIterator != null) {
                CharSequence oneChar = source.subSequence(start, end);
                if (TextUtils.isEmpty(oneChar)) {
                    continue;
                }
                if (!charIterator.getCharString(source.subSequence(start, end), Character.codePointAt(source, start))) {
                    break;
                }
            }
        }
        return count;
    }

    public interface CharIterator {
        boolean getCharString(CharSequence item, int codePoint);
    }
}
