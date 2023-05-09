package com.test.test3app.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.test.test3app.R;

import base.ui.BaseActivity;
import im.turbo.basetools.pattern.PatternSpannableBuilder;
import im.turbo.basetools.pattern.PatternUtils;
import im.turbo.basetools.span.ItemProcessor;

public class Activity_99995_webURL extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_99995_weburl);
        TextView textView1 = findViewById(R.id.textview_web_url1);

        String[] textArray = {
                "www.baidu.com",
                "a.www.baidu.com",
                "a.http://www.baidu.com",
                "1.http://www.baidu.com",
                "a.http://www.a.baidu.com",
        };
        for (String text : textArray) {
            textView1.append(PatternSpannableBuilder.newBuilder(text)
                    .addPattern(new ItemProcessor(PatternUtils.WEB_URL) {
                        @Override
                        public Integer getColor() {
                            return Color.rgb(155, 155, 255);
                        }
                    }).build());
            textView1.append("\n");
        }
    }
}
