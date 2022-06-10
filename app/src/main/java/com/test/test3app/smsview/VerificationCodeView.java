package com.test.test3app.smsview;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.test.test3app.R;
import im.turbo.basetools.utils.BitmapUtils;
import im.turbo.basetools.utils.InputMethodUtils;
import com.zhaoyuntao.androidutils.tools.S;

import java.util.regex.Pattern;

/**
 * created by zhaoyuntao
 * on 2019-10-25
 * description:
 */
public class VerificationCodeView extends LinearLayout {

    private int childCount;
    private int textWidth = 120;
    private int textHeight = 120;
    private Drawable colorBorderChoose;
    private Drawable colorBorderNormal;

    private OnCompletedListener onCompletedListener;

    private int margin_child_top;
    private int margin_child_bottom;
    private int margin_child_start;
    private int margin_child_end;

    private float textSize;

    public VerificationCodeView(Context context) {
        super(context);
        init(null);
    }

    public VerificationCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public VerificationCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setGravity(Gravity.CENTER);
        setLayoutDirection(LAYOUT_DIRECTION_LTR);
        textSize = 20;
        childCount = 4;
        textWidth = BitmapUtils.dip2px(getContext(), 40);
        textHeight = BitmapUtils.dip2px(getContext(), 50);
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.VerificationCodeView);
            childCount = typedArray.getInt(R.styleable.VerificationCodeView_verify_count, 4);
            colorBorderChoose = typedArray.getDrawable(R.styleable.VerificationCodeView_verify_color_border_focus);
            colorBorderNormal = typedArray.getDrawable(R.styleable.VerificationCodeView_verify_color_border_normal);
            textWidth = (int) typedArray.getDimension(R.styleable.VerificationCodeView_verify_child_width, textWidth);
            textHeight = (int) typedArray.getDimension(R.styleable.VerificationCodeView_verify_child_height, textHeight);
            textSize = BitmapUtils.px2sp(getContext(), typedArray.getDimension(R.styleable.VerificationCodeView_verify_textsize, BitmapUtils.sp2px(getContext(), textSize)));
            typedArray.recycle();
        }

        margin_child_end = BitmapUtils.dip2px(getContext(), 4);
        margin_child_start = BitmapUtils.dip2px(getContext(), 4);
        margin_child_top = BitmapUtils.dip2px(getContext(), 4);
        margin_child_bottom = BitmapUtils.dip2px(getContext(), 4);

        initViews();
    }

    private void initViews() {
        for (int i = 0; i < childCount; i++) {
            final EditText editText = new EditText(getContext());
            if (i == 0) {
                editText.setClickable(true);
            } else {
                editText.setClickable(false);
            }
            editText.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ClipboardManager clip = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    if (clip != null && !TextUtils.isEmpty(clip.getText())) {
                        String content = clip.getText().toString().trim();
                        if (Pattern.matches("[0-9]{" + childCount + "}", content)) {
                            VerificationCodeView.this.setVerificationCode(content);
                        }
                    }
                    return true;
                }
            });
            editText.setClickable(TextUtils.isEmpty(editText.getText()));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(textWidth, textHeight);
            layoutParams.setMarginStart(margin_child_start);
            layoutParams.setMarginEnd(margin_child_end);
            layoutParams.setMargins(margin_child_start, margin_child_top, margin_child_end, margin_child_bottom);
            layoutParams.gravity = Gravity.CENTER;
            editText.setLayoutParams(layoutParams);
            editText.setTextSize(textSize);
            editText.setTag(i);
            editText.setTextColor(ContextCompat.getColor(getContext(), R.color.color_grey_10));
            editText.setCursorVisible(false);
            editText.setGravity(Gravity.CENTER);
            editText.setPadding(0, 0, 0, 0);
            setBackgroundStyle(editText, false);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});

            editText.setId(i);
            editText.setEms(1);
            final int index = i;
            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!TextUtils.isEmpty(s)) {
                        next(index);
                    }
                    checkAndCommit();
                }
            };
            editText.addTextChangedListener(textWatcher);
            editText.setTag(textWatcher);
            editText.setKeyListener(new KeyListener() {
                @Override
                public int getInputType() {
                    return InputType.TYPE_CLASS_NUMBER;
                }

                @Override
                public boolean onKeyDown(View view, Editable text, int keyCode, KeyEvent event) {
                    return false;
                }

                @Override
                public boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event) {
                    S.s("key:" + keyCode);
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        if (index == childCount - 1) {
                            checkAndCommit();
                        } else {
                            next(index);
                        }
                        return true;
                    } else if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if (TextUtils.isEmpty(editText.getText())) {
                            clearPrevious(index);
                        } else {
                            editText.setText("");
                        }
                        return true;
                    }
                    return false;
                }

                @Override
                public boolean onKeyOther(View view, Editable text, KeyEvent event) {
                    return false;
                }

                @Override
                public void clearMetaKeyState(View view, Editable content, int states) {

                }
            });
            editText.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    setBackgroundStyle((EditText) v, hasFocus);
                }
            });
            addView(editText, i);
        }
    }

    private void clearPrevious(int index) {
        if (index > 0) {
            index--;
        } else {
            index = 0;
        }

        EditText editText = (EditText) getChildAt(index);
        editText.removeTextChangedListener((TextWatcher) editText.getTag());
        editText.requestFocus();
        editText.setText("");
        editText.addTextChangedListener((TextWatcher) editText.getTag());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public void requestInputFocus() {
        if (getChildCount() > 0) {
            int index = 0;
            EditText editText = (EditText) getChildAt(index);
            while (!TextUtils.isEmpty(editText.getText())) {
                if (index < childCount - 1) {
                    index++;
                    editText = (EditText) getChildAt(index);
                } else {
                    editText = (EditText) getChildAt(childCount - 1);
                    break;
                }
            }
            editText.requestFocus();
            InputMethodUtils.openInputMethod(editText);
        }
    }

    @Override
    public void clearFocus() {
        for (int i = 0; i < childCount; i++) {
            EditText editText = (EditText) getChildAt(i);
            if (editText.hasFocus()) {
                editText.clearFocus();
            }
        }
        super.clearFocus();
    }

    private void next(int index) {
//        S.sd("next:" + index);
        if (index < (childCount - 1)) {
            index++;
        } else {
            index = childCount - 1;
        }
        EditText editText = (EditText) getChildAt(index);
        while (!TextUtils.isEmpty(editText.getText())) {
            if (index > (childCount - 1)) {
                break;
            }
            editText = (EditText) getChildAt(index);
            index++;
        }

        editText.requestFocus();
    }

    private void previous(int index) {
        S.s("previous:" + index);
        if (index > 0) {
            index--;
        } else {
            index = 0;
        }

        EditText editText = (EditText) getChildAt(index);
        editText.requestFocus();
    }

    private void setBackgroundStyle(EditText editText, boolean focus) {
        if (colorBorderNormal != null && !focus) {
            editText.setBackground(colorBorderNormal);
        } else if (colorBorderChoose != null && focus) {
            editText.setBackground(colorBorderChoose);
        }
    }

    private void checkAndCommit() {
        StringBuilder stringBuilder = new StringBuilder();
        boolean full = true;
        for (int i = 0; i < childCount; i++) {
            EditText editText = (EditText) getChildAt(i);
            String content = editText.getText().toString();
            if (content.length() == 0) {
                full = false;
                break;
            } else {
                stringBuilder.append(content);
            }

        }
        if (full) {
            if (onCompletedListener != null) {
                onCompletedListener.onComplete(stringBuilder.toString());
            }
        } else {
            if (onCompletedListener != null) {
                onCompletedListener.onInput();
            }
        }
    }

    public String getVerificationCode() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < childCount; i++) {
            EditText editText = (EditText) getChildAt(i);
            if (!TextUtils.isEmpty(editText.getText())) {
                stringBuilder.append(editText.getText().toString());
            }

        }
        return stringBuilder.toString();
    }

    @Override
    public void setEnabled(boolean enabled) {
        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.setEnabled(enabled);
        }
    }

    public void setVerificationCode(String veriCode) {
        clearFocus();
        closeIntputMethod();
        for (int i = 0; i < childCount && i < veriCode.length(); i++) {
            EditText editText = (EditText) getChildAt(i);
            if (editText != null) {
                editText.setText(String.valueOf(veriCode.charAt(i)));
            }
        }
    }

    public void clearVerificationCode() {
        for (int i = 0; i < childCount; i++) {
            EditText editText = (EditText) getChildAt(i);
            if (editText != null) {
                editText.setText(null);
            }
        }
    }

    public interface OnCompletedListener {
        void onComplete(String content);

        void onInput();
    }

    public void setOnCompletedListener(OnCompletedListener listener) {
        this.onCompletedListener = listener;
    }

    public void closeIntputMethod() {
        for (int i = 0; i < childCount; i++) {
            EditText editText = (EditText) getChildAt(i);
            editText.clearFocus();
            InputMethodUtils.closeInputMethod(editText);
        }
    }
}

