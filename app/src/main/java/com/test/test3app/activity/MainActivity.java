package com.test.test3app.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;

import com.test.test3app.BaseActivity;
import com.test.test3app.R;
import com.zhaoyuntao.androidutils.tools.S;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        S.s("onCreate");
        setContentView(R.layout.activity_main);

        registerListener(R.id.m1);
        registerListener(R.id.m2);
        registerListener(R.id.m3);
        registerListener(R.id.m4);
        registerListener(R.id.m5);
        registerListener(R.id.m6);
        registerListener(R.id.m7);
        registerListener(R.id.m8);
        registerListener(R.id.m9);
        registerListener(R.id.m91);
        registerListener(R.id.m92);
        registerListener(R.id.m93);
        registerListener(R.id.m94);
        registerListener(R.id.m95);
        registerListener(R.id.m96);
        registerListener(R.id.m97);
        registerListener(R.id.m98);
        registerListener(R.id.m99);
        registerListener(R.id.m990);
        registerListener(R.id.m991);
        registerListener(R.id.m992);

        onClick(findViewById(R.id.m94));
    }

    private void registerListener(@IdRes int viewId) {
        View view = findViewById(viewId);
        if (view != null) {
            view.setVisibility(View.VISIBLE);
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.m1) {
            goToActivity(MainActivity1_unknown.class);
        } else if (v.getId() == R.id.m2) {
            goToActivity(MainActivity2_record.class);
        } else if (v.getId() == R.id.m3) {
            goToActivity(MainActivity3_sms.class);
        } else if (v.getId() == R.id.m4) {
            goToActivity(MainActivity4_grouptips.class);
        } else if (v.getId() == R.id.m5) {
            goToActivity(MainActivity5_takephoto.class);
        } else if (v.getId() == R.id.m6) {
            goToActivity(MainActivity6_pay.class);
        } else if (v.getId() == R.id.m7) {
            goToActivity(MainActivity7_recyclerView.class);
        } else if (v.getId() == R.id.m8) {
            goToActivity(MainActivity8_permission.class);
        } else if (v.getId() == R.id.m9) {
            goToActivity(MainActivity9_openGL.class);
        } else if (v.getId() == R.id.m91) {
            goToActivity(MainActivity91_Qmoji.class);
        } else if (v.getId() == R.id.m92) {
            goToActivity(MainActivity92_huawei_meeting.class);
        } else if (v.getId() == R.id.m93) {
            goToActivity(MainActivity93_threadPool.class);
        } else if (v.getId() == R.id.m94) {
            goToActivity(MainActivity_94_wallpaper.class);
        } else if (v.getId() == R.id.m95) {
            goToActivity(MainActivity_95_textview.class);
        } else if (v.getId() == R.id.m96) {
            goToActivity(MainActivity_96_faceview.class);
        } else if (v.getId() == R.id.m97) {
            goToActivity(MainActivity_97_textview_spannable.class);
        } else if (v.getId() == R.id.m98) {
            goToActivity(MainActivity_98_sticker_reply.class);
        } else if (v.getId() == R.id.m99) {
            goToActivity(MainActivity_99_dark_mode.class);
        } else if (v.getId() == R.id.m990) {
            goToActivity(MainActivity_990_cell.class);
        } else if (v.getId() == R.id.m991) {
            goToActivity(MainActivity_991_calculate.class);
        } else if (v.getId() == R.id.m992) {
            goToActivity(MainActivity_992_drawerLayout.class);
        }
    }
}