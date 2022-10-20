package com.test.test3app.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;

import com.test.test3app.R;

import base.ui.BaseActivity;
import im.turbo.basetools.utils.InputMethodUtils;
import im.turbo.baseui.utils.UiUtils;
import im.turbo.utils.log.S;

public class Activity_99992_keyboard extends BaseActivity {
    View bottom;
    int heightOfKeyboard= UiUtils.dipToPx(300);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_99992_keyboard);

        bottom = findViewById(R.id.bottom);
        EditText editText = findViewById(R.id.edit);
        findViewById(R.id.emoji).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottom.getLayoutParams().height > 0) {
                    InputMethodUtils.forceOpenInputKeyboard(editText);
                    editText.post(new Runnable() {
                        @Override
                        public void run() {
                            editText.requestFocus();
                        }
                    });
                    ViewGroup.LayoutParams layoutParams = bottom.getLayoutParams();
                    layoutParams.height = 0;
                    bottom.setLayoutParams(layoutParams);
                } else {
                    ViewGroup.LayoutParams layoutParams = bottom.getLayoutParams();
                    layoutParams.height = heightOfKeyboard;
                    bottom.setLayoutParams(layoutParams);
                    InputMethodUtils.closeInputMethod(editText);
                }
            }
        });

        findViewById(R.id.root).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                InputMethodUtils.checkKeyBoardOpen(findViewById(R.id.root), new InputMethodUtils.OnKeyBoardOpenListener() {
                    @Override
                    public void getKeyBoardHeight(int heightOfKeyBoard) {
                        S.s("heightOfKeyBoard:" + heightOfKeyBoard);
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (bottom.getLayoutParams().height > 0) {
            ViewGroup.LayoutParams layoutParams = bottom.getLayoutParams();
            layoutParams.height = 0;
            bottom.setLayoutParams(layoutParams);
            return;
        }
        super.onBackPressed();
    }
}
