package com.test.test3app.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.test.test3app.R;
import com.test.test3app.textview.CustomTypefaceSpan;

import base.ui.BaseActivity;

public class Activity_95_textview extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_95_textview);

        TextView textView1 = findViewById(R.id.text1);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("啦啦啦");
        spannableStringBuilder.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView1.setText(spannableStringBuilder);

        TextView textView2 = findViewById(R.id.text2);
        Typeface font = Typeface.createFromAsset(getAssets(), "RobotoMono-SemiBoldItalic.ttf");
        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder("啦啦啦");
        spannableStringBuilder2.setSpan(new CustomTypefaceSpan("", font), 0, spannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView2.setText(spannableStringBuilder2);
    }
}
