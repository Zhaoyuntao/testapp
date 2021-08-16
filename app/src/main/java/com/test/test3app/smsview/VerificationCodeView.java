/*
 * Copyright (C) 2013 UCWeb Inc. All rights reserved
 * 本代码版权归UC优视科技所有。
 * UC游戏交易平台为优视科技（UC）旗下的手机游戏交易平台产品
 *
 *
 */

package com.test.test3app.smsview;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.test.test3app.R;
import com.test.test3app.utils.BitmapUtils;
import com.test.test3app.utils.InputMethodUtils;

import java.util.regex.Pattern;


public class VerificationCodeView extends LinearLayout {
    private final static String TYPE_NUMBER = "number";
    private final static String TYPE_TEXT = "text";
    private final static String TYPE_PASSWORD = "password";
    private final static String TYPE_PHONE = "phone";

    private int childCount;
    private int textWidth = 120;
    private int textHeight = 120;
    private String inputType = TYPE_NUMBER;
    private Drawable colorBorderChoose;
    private Drawable colorBorderNormal;

    private OnCompletedListener onCompletedListener;

    private int margin_child_top;
    private int margin_child_bottom;
    private int margin_child_start;
    private int margin_child_end;

    private float textsize;

    private Paint paint;

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
        textsize = 20;
        childCount = 4;
        inputType = TYPE_NUMBER;
        textWidth = BitmapUtils.dip2px(getContext(), 40);
        textHeight = BitmapUtils.dip2px(getContext(), 50);
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.VerificationCodeView);
            childCount = typedArray.getInt(R.styleable.VerificationCodeView_verify_count, 4);
            colorBorderChoose = typedArray.getDrawable(R.styleable.VerificationCodeView_verify_color_border_focus);
            colorBorderNormal = typedArray.getDrawable(R.styleable.VerificationCodeView_verify_color_border_normal);
            inputType = typedArray.getString(R.styleable.VerificationCodeView_verify_inputType);
            if (TextUtils.isEmpty(inputType)) {
                inputType = TYPE_NUMBER;
            }
            textWidth = (int) typedArray.getDimension(R.styleable.VerificationCodeView_verify_child_width, textWidth);
            textHeight = (int) typedArray.getDimension(R.styleable.VerificationCodeView_verify_child_height, textHeight);
            textsize = BitmapUtils.px2sp(getContext(), typedArray.getDimension(R.styleable.VerificationCodeView_verify_textsize, BitmapUtils.sp2px(getContext(), textsize)));
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
            editText.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                        if (TextUtils.isEmpty(editText.getText())) {
                            backForward((Integer) editText.getTag());
                        } else {
                            editText.setText(null);
                        }
                        return true;
                    }
                    return false;
                }
            });
            editText.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ClipboardManager clip = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    if (clip != null && !TextUtils.isEmpty(clip.getText())) {
                        String content = clip.getText().toString().trim();
                        if (inputType == TYPE_NUMBER) {
                            if (Pattern.matches("[0-9]{" + childCount + "}", content)) {
                                VerificationCodeView.this.setVerificationCode(content);
                            }
                        } else {
                            if (Pattern.matches("[0-9a-zA-Z]{" + childCount + "}", content)) {
                                VerificationCodeView.this.setVerificationCode(content);
                            }
                        }
                    }
                    return true;
                }
            });
            editText.setClickable(TextUtils.isEmpty(editText.getText()));
            LayoutParams layoutParams = new LayoutParams(textWidth, textHeight);
            layoutParams.setMarginStart(margin_child_start);
            layoutParams.setMarginEnd(margin_child_end);
            layoutParams.setMargins(margin_child_start, margin_child_top, margin_child_end, margin_child_bottom);
            layoutParams.gravity = Gravity.CENTER;
            editText.setLayoutParams(layoutParams);
            editText.setTextSize(textsize);
            editText.setTag(i);
            editText.setTextColor(Color.BLACK);
            editText.setCursorVisible(false);
            editText.setGravity(Gravity.CENTER);
            editText.setPadding(0, 0, 0, 0);
            setBackgroundStyle(editText, false);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});

            if (TYPE_NUMBER.equals(inputType)) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (TYPE_PASSWORD.equals(inputType)) {
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else if (TYPE_TEXT.equals(inputType)) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
            } else if (TYPE_PHONE.equals(inputType)) {
                editText.setInputType(InputType.TYPE_CLASS_PHONE);

            }
            editText.setId(i);
            editText.setEms(1);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (TextUtils.isEmpty(s)) {
                    } else {
                        next((Integer) editText.getTag());
                    }
                    checkAndCommit();
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    private void backForward(int index) {
        if (index > 0) {
            index--;
        } else {
            index = 0;
        }
        EditText editText = (EditText) getChildAt(index);
        EditText editTextLastEmpty = editText;
        editText.setText(null);
        while (TextUtils.isEmpty(editText.getText())) {
            editTextLastEmpty = editText;
            if (index > 0) {
                index--;
                editText = (EditText) getChildAt(index);
            } else {
                editTextLastEmpty = (EditText) getChildAt(0);
                break;
            }
        }
        editTextLastEmpty.requestFocus();
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
        if (index < (childCount - 1)) {
            index++;
        } else {
            index = childCount - 1;
        }

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

