package com.test.test3app.activity;

import android.os.Bundle;
import android.view.View;

import com.test.test3app.R;
import im.turbo.utils.log.S;

import base.ui.BaseActivity;
import im.turbo.baseui.chat.SmoothLayoutFrameLayout;
import im.turbo.baseui.chat.SmoothScaleFrameLayout;


public class Activity_8_smoothSwitch extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity8_smooth);
        SmoothScaleFrameLayout smoothScaleFrameLayout1 = findViewById(R.id.smooth);
        SmoothLayoutFrameLayout smoothLayoutFrameLayout2 = findViewById(R.id.smooth2);
        SmoothLayoutFrameLayout smoothLayoutFrameLayout3 = findViewById(R.id.smooth3);
        SmoothLayoutFrameLayout smoothLayoutFrameLayout4 = findViewById(R.id.smooth_red);
        SmoothScaleFrameLayout smoothScaleFrameLayout3 = findViewById(R.id.smooth_switch3);

        findViewById(R.id.switch_button0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothScaleFrameLayout1.nextIndex();
            }
        });
        findViewById(R.id.switch_button).setOnClickListener(new View.OnClickListener() {
            int ok;

            @Override
            public void onClick(View v) {
                if (ok > smoothLayoutFrameLayout2.getChildCount() - 1) {
                    ok = 0;
                }
                for (int i = 0; i < smoothLayoutFrameLayout2.getChildCount(); i++) {
                    smoothLayoutFrameLayout2.getChildAt(i).setVisibility(i == ok ? View.VISIBLE : View.GONE);
                }
                ok++;
            }
        });
        findViewById(R.id.switch_button2).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                smoothScaleFrameLayout3.nextIndex();
            }
        });

        View singleView=findViewById(R.id.single_image_view);
        findViewById(R.id.button_red).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                S.s("visible:" + (singleView.getVisibility() == View.VISIBLE));
                singleView.setVisibility(singleView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });
    }
}
