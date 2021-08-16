package com.test.test3app.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.test3app.BaseActivity;
import com.test.test3app.R;
import com.test.test3app.textview.ZTestTextView;
import com.zhaoyuntao.androidutils.tools.T;

public class MainActivity_95_textview extends BaseActivity {

    ZTestTextView zTextView;
    EditText editText;
    LinearLayout linearLayout;
    TextWatcher textWatcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_95_textview);
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
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("abcdefg ");
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.RED), 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                editText.removeTextChangedListener(textWatcher);
                editText.append(spannableStringBuilder);
                T.t(activity(), editText.isEnabled());
                editText.addTextChangedListener(textWatcher);
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
