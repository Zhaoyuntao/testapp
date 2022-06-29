package com.test.test3app.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;

import base.ui.BaseActivity;
import com.test.test3app.R;
import com.zhaoyuntao.androidutils.tools.T;

public class Activity_99_dark_mode extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dark_mode);
        findViewById(R.id.theme_switch).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                    T.t(activity(), "Light");
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    T.t(activity(), "Night");
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
            }
        });
    }
}