package com.test.test3app.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.test.test3app.R;

import base.ui.BaseActivity;
import im.turbo.basetools.pattern.LinkifyPort;
import im.turbo.basetools.pattern.PatternSpannableBuilder;
import im.turbo.basetools.span.ItemProcessor;
import im.turbo.utils.log.S;

public class Activity_99995_webURL extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_99995_weburl);
        TextView textView = findViewById(R.id.textview_web_url1);

        String[] textArray = {
                "a.b.c.d.com",
                "a.-b.-.d.com",
                "www.baidu.com",
                "a.www.baidu.com",
                "aa.www.baidu.com",
                "1.www.baidu.com",
                "111.www.baidu.com",
                "111aaa.www.baidu.com",
                "a,www.baidu.com",
                "aa,www.baidu.com",
                "www.baidu.com你好",
                "你好www.baidu.comjsbdhj",
                "你好:www.baidu.com",
                "你好www.baidu.com jsbdhj",
                "你好www.baidu.com 你好",
                "你好www.baidu.com你好",
                "a.www.baidu.com",
                "the link is www.baidu.com/a=a,if you like",
                "https://3im.totok.ai/shares/invites?id=3645a15c8b26c8200016ac078&sid=_____46jhJ0",
                "a.http://www.baidu.com",
                "a.https://www.baidu.com",
                "1.http://www.baidu.com",
                "a.http://www.a.baidu.com",
                "://www.a.baidu.com",
                "//www.a.baidu.com",
                "//www.a.baidu.com/a=(a)",
                "//www.a.baidu.com/a=你好,abc",
                "Dubai’s desert climate means it’s a sure bet for sultry weather all year round. See our month-by-mon https://article.botim.me/a/K2dFRtHrnSdK - Best time to visit Dubai",
                "你家啊说不定就会把时间的哈baidu.com",
                "https://a.baidu.a-ba.com:5000/sjbjbsa/a-asda/-asd-/-/ad?a=a-a",
        };
        for (String text : textArray) {
            S.i(text);
            textView.append(PatternSpannableBuilder.newBuilder(text)
                    .addPattern(new ItemProcessor(LinkifyPort.WEB_URL_PATTERN) {
                        @Override
                        public Integer getColor() {
                            return Color.rgb(155, 155, 255);
                        }
                    }).build());
            textView.append("\n");
        }

//        textView.append("\n");
//        textView.append("\n");
//        textView.append(PatternSpannableBuilder.newBuilder("好")
//                .addPattern(new ItemProcessor(LinkifyPort.get()) {
//                    @Override
//                    public Integer getColor() {
//                        return Color.rgb(255, 80, 80);
//                    }
//                }).build());
//        textView.append("\n");
//        textView.append(PatternSpannableBuilder.newBuilder("0")
//                .addPattern(new ItemProcessor(LinkifyPort.get()) {
//                    @Override
//                    public Integer getColor() {
//                        return Color.rgb(255, 80, 80);
//                    }
//                }).build());
//        textView.append("\n");
//        textView.append(PatternSpannableBuilder.newBuilder("a")
//                .addPattern(new ItemProcessor(LinkifyPort.get()) {
//                    @Override
//                    public Integer getColor() {
//                        return Color.rgb(255, 80, 80);
//                    }
//                }).build());
//        textView.append("\n");
    }
}
