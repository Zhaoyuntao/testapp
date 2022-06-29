package com.test.test3app.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import base.ui.BaseActivity;
import com.test.test3app.R;
import com.test.test3app.textview.ZTestTextView;
import com.zhaoyuntao.androidutils.component.ZDialog;
import com.zhaoyuntao.androidutils.tools.T;

public class Activity_95_textview extends BaseActivity {

    ZTestTextView zTextView;
    EditText editText;
    LinearLayout linearLayout;
    TextWatcher textWatcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_95_textview);
        findViewById(R.id.imageview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                T.t(activity(),"clicked image view");
            }
        });
        editText = findViewById(R.id.edit);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("abcdefg ");
        spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.RED), 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        editText.setText(spannableStringBuilder);
        linearLayout = findViewById(R.id.abc);
        editText.addTextChangedListener(textWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                zTextView.setText(s, TextView.BufferType.SPANNABLE);
//                editText.removeTextChangedListener(textWatcher);
//                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(s);
//                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.RED), 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                editText.setText(spannableStringBuilder);
//                editText.setSelection(s.length());
//                editText.addTextChangedListener(textWatcher);
            }
        });
        String text = "\uD83D\uDC4D\uD83C\uDFFF";
        zTextView = findViewById(R.id.text);
        zTextView.setText(text, TextView.BufferType.SPANNABLE);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("abcdefg ");
//                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.RED), 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                editText.removeTextChangedListener(textWatcher);
//                editText.append(spannableStringBuilder);
//                T.t(activity(), editText.isEnabled());
//                editText.addTextChangedListener(textWatcher);
//                AlertDialog.Builder builder = new AlertDialog.Builder(activity());
//                builder.setTitle("hello");
//                builder.setMessage("hello");
//                builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        dialog.dismiss();
//                    }
//                });
//                builder.setCancelable(true);
//                builder.create();
//                builder.show();

                ZDialog dialog=new ZDialog(activity());
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                dialog.setTitle("hello");
                dialog.setGravity(Gravity.CENTER);
                dialog.show();

            }
        });
//        zTextView.setText(text);
//        TP.init();
//        TP.runOnUiDelayedSafely(new ZRunnable(this) {
//            @Override
//            protected void todo() {
//                S.s("wc:"+linearLayout.getWidth()+" w:"+zTextView.getWidth());
//            }
//        },1000);
    }


}
